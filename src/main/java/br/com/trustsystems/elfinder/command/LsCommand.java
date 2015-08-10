package br.com.trustsystems.elfinder.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class LsCommand extends AbstractJsonCommand implements ElfinderCommand {
    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files = new HashMap<>();
        VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
        addChildren(files, volumeHandler);

        json.put(ElFinderConstants.ELFINDER_PARAMETER_LIST, files.values());
    }
}
