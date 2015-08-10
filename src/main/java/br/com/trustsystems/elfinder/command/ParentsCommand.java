package br.com.trustsystems.elfinder.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class ParentsCommand extends AbstractJsonCommand implements ElfinderCommand{
    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files = new HashMap<>();
        VolumeHandler volumeHandler = findTarget(elfinderStorage, target);

        while(!volumeHandler.isRoot()){
            addSubFolders(files, volumeHandler);
            volumeHandler = volumeHandler.getParent();
        }

        json.put(ElFinderConstants.ELFINDER_PARAMETER_TREE, buildJsonFilesArray(request, files.values()));
    }
}
