package br.com.trustsystems.elfinder.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class FileCommand extends AbstractCommand implements ElfinderCommand {

    public static final String STREAM = "1";

    @Override
    public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean download = STREAM.equals(request.getParameter("download"));
        VolumeHandler fsi = super.findTarget(elfinderStorage, target);
        String mime = fsi.getMimeType();

        response.setCharacterEncoding("utf-8");
        response.setContentType(mime);
        String fileName = fsi.getName();
        if (download) {
            response.setHeader("Content-Disposition",
                    "attachments; " + getAttachementFileName(fileName, request.getHeader("USER-AGENT")));
            response.setHeader("Content-Transfer-Encoding", "binary");
        }

        OutputStream out = response.getOutputStream();
        response.setContentLength((int) fsi.getSize());

        try (InputStream is = fsi.openInputStream()) {
            IOUtils.copy(is, out);
            out.flush();
            out.close();
        }
    }

    private String getAttachementFileName(String fileName, String userAgent) throws UnsupportedEncodingException {
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();

            if (userAgent.contains("msie")) {
                return "filename=\"" + URLEncoder.encode(fileName, "UTF8") + "\"";
            }

            if (userAgent.contains("opera")) {
                return "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF8");
            }
            if (userAgent.contains("safari")) {
                return "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
            }
            if (userAgent.contains("mozilla")) {
                return "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF8");
            }
        }

        return "filename=\"" + URLEncoder.encode(fileName, "UTF8") + "\"";
    }
}
