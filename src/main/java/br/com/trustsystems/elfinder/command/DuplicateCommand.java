package br.com.trustsystems.elfinder.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class DuplicateCommand extends AbstractJsonCommand implements ElfinderCommand {
    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstants.ELFINDER_PARAMETER_TARGETS);

        List<VolumeHandler> added = new ArrayList<>();

        for (String target : targets) {
            final VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
            final String name = volumeHandler.getName();
            String baseName = FilenameUtils.getBaseName(name);
            final String extension = FilenameUtils.getExtension(name);

            int i = 1;
            VolumeHandler newFile;
            baseName = baseName.replaceAll("\\(\\d+\\)$", "");

            while (true){
                String newName = String.format("%s(%d)%s", baseName, i, (extension == null || extension.isEmpty() ? ""
                        : "." + extension));
                newFile = new VolumeHandler(volumeHandler.getParent(), newName);
                if (!newFile.exists())
                {
                    break;
                }
                i++;
            }

            createAndCopy(volumeHandler, newFile);
            added.add(newFile);
        }
        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, buildJsonFilesArray(request, added));
    }
}
