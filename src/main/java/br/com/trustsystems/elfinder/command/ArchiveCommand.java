package br.com.trustsystems.elfinder.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;
import br.com.trustsystems.elfinder.support.archiver.Archiver;
import br.com.trustsystems.elfinder.support.archiver.ArchiverType;

public class ArchiveCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstants.ELFINDER_PARAMETER_TARGETS);
        final String type = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TYPE);

        List<Target> targetList = findTargets(elfinderStorage, targets);

        try {
            Archiver archiver = ArchiverType.of(type).getStrategy();
            Target targetArchive = archiver.compress(targetList);

            Object[] archiveInfos = {getTargetInfo(request, new VolumeHandler(targetArchive, elfinderStorage))};
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, archiveInfos);

        } catch (Exception e) {
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ERROR, "Unable to create the archive! Error: " + e);
        }

    }
}
