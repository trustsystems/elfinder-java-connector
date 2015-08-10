package br.com.trustsystems.elfinder.command;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class MkdirCommand extends AbstractJsonCommand implements ElfinderCommand {
    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
        final String dirName = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_NAME);

        VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
        VolumeHandler directory = new VolumeHandler(volumeHandler, dirName);
        directory.createFolder();

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, new Object[]{getTargetInfo(request, directory)});
    }
}
