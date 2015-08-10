package br.com.trustsystems.elfinder.command;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.VolumeHandler;
import br.com.trustsystems.elfinder.support.archiver.Archiver;
import br.com.trustsystems.elfinder.support.archiver.ArchiverType;

public class ExtractCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String targetHash = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        Target targetCompressed = elfinderStorage.fromHash(targetHash);
        Volume targetCompressedVolume = targetCompressed.getVolume();
        String mimeType = targetCompressedVolume.getMimeType(targetCompressed);

        try {
            Archiver archiver = ArchiverType.of(mimeType).getStrategy();
            Target decompressTarget = archiver.decompress(targetCompressed);

            Object[] archiveInfos = {getTargetInfo(request, new VolumeHandler(decompressTarget, elfinderStorage))};
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, archiveInfos);

        } catch (Exception e) {
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ERROR, "Unable to extract the archive! Error: " + e);
        }

    }
}
