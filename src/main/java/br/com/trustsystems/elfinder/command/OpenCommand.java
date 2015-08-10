package br.com.trustsystems.elfinder.command;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class OpenCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json)
            throws Exception {

        boolean init = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_INIT) != null;
        boolean tree = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TREE) != null;
        String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files = new LinkedHashMap<>();
        if (init) {
            json.put(ElFinderConstants.ELFINDER_PARAMETER_API, ElFinderConstants.ELFINDER_VERSION_API);
            json.put(ElFinderConstants.ELFINDER_PARAMETER_NETDRIVERS, new Object[0]);
        }

        if (tree) {
            for (Volume volume : elfinderStorage.getVolumes()) {
                VolumeHandler root = new VolumeHandler(volume.getRoot(), elfinderStorage);
                files.put(root.getHash(), root);
                addSubFolders(files, root);
            }
        }

        VolumeHandler cwd = findCwd(elfinderStorage, target);
        files.put(cwd.getHash(), cwd);
        addChildren(files, cwd);

        json.put(ElFinderConstants.ELFINDER_PARAMETER_FILES, buildJsonFilesArray(request, files.values()));
        json.put(ElFinderConstants.ELFINDER_PARAMETER_CWD, getTargetInfo(request, cwd));
        json.put(ElFinderConstants.ELFINDER_PARAMETER_OPTIONS, getOptions(cwd));
    }
}
