package br.com.trustsystems.elfinder.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.service.ElfinderStorage;

public class SizeCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstants.ELFINDER_PARAMETER_TARGETS);

        List<Target> targetList = findTargets(elfinderStorage, targets);

        long size = 0;
        for (Target target : targetList) {
            Volume volume = target.getVolume();
            size += volume.getSize(target);
        }

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_SIZE, size);
    }
}
