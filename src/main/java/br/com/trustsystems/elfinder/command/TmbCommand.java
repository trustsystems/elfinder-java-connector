package br.com.trustsystems.elfinder.command;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;

public class TmbCommand extends AbstractCommand implements ElfinderCommand {
    @Override
    public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String target = request.getParameter("target");
        VolumeHandler fsi = super.findTarget(elfinderStorage, target);

        final DateTime dateTime = new DateTime();
        final String pattern = "d MMM yyyy HH:mm:ss 'GMT'";
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);

        try (InputStream is = fsi.openInputStream()) {
            BufferedImage image = ImageIO.read(is);
            int width = 80;
            ResampleOp rop = new ResampleOp(DimensionConstrain.createMaxDimension(width, -1));
            rop.setNumberOfThreads(4);
            BufferedImage b = rop.filter(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(b, "png", baos);

            response.setHeader("Last-Modified", dateTimeFormatter.print(dateTime));
            response.setHeader("Expires", dateTimeFormatter.print(dateTime.plusYears(2)));

            ImageIO.write(b, "png", response.getOutputStream());
        }
    }
}
