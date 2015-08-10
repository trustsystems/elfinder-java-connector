package br.com.trustsystems.elfinder.command;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class DimCommand extends AbstractJsonCommand implements ElfinderCommand{
    public static final String SEPARATOR = "x";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        BufferedImage image;
        VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
        image = ImageIO.read(volumeHandler.openInputStream());

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_DIM,image.getWidth() + SEPARATOR + image.getHeight());
    }
}
