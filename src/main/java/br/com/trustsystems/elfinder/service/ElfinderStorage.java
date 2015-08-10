package br.com.trustsystems.elfinder.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.core.VolumeSecurity;

public interface ElfinderStorage {

    Target fromHash(String hash);

    String getHash(Target target) throws IOException;

    String getVolumeId(Volume volume);

    Locale getVolumeLocale(Volume volume);

    VolumeSecurity getVolumeSecurity(Target target);

    List<Volume> getVolumes();

    List<VolumeSecurity> getVolumeSecurities();

    ThumbnailWidth getThumbnailWidth();
}