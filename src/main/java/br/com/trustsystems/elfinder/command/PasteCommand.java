package br.com.trustsystems.elfinder.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;

public class PasteCommand extends AbstractJsonCommand implements ElfinderCommand{
    public static final String INT_CUT = "1";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstants.ELFINDER_PARAMETER_TARGETS);
        final String destination = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_FILE_DESTINATION);
        final boolean cut = INT_CUT.equals(request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_CUT));

        List<VolumeHandler> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();

        VolumeHandler vhDst = findTarget(elfinderStorage, destination);

        for (String target : targets) {
            VolumeHandler vhTarget = findTarget(elfinderStorage, target);
            final String name = vhTarget.getName();
            VolumeHandler newFile = new VolumeHandler(vhDst, name);
            createAndCopy(vhTarget, newFile);
            added.add(newFile);

            if(cut){
                vhTarget.delete();
                removed.add(vhTarget.getHash());
            }
        }

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, buildJsonFilesArray(request, added));
        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_REMOVED, removed.toArray());
    }
}
