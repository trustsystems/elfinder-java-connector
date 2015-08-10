package br.com.trustsystems.elfinder.command;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class PutCommand extends AbstractJsonCommand implements ElfinderCommand{

    public static final String ENCODING = "utf-8";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        VolumeHandler file = findTarget(elfinderStorage, target);
        OutputStream os = file.openOutputStream();
        IOUtils.write(request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_CONTENT), os, ENCODING);
        os.close();
        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_CHANGED, new Object[]{getTargetInfo(request, file)});
    }
}
