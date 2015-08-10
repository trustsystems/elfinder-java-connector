package br.com.trustsystems.elfinder.core;

import br.com.trustsystems.elfinder.core.impl.SecurityConstraint;

public interface VolumeSecurity {

    String getVolumePattern();

    SecurityConstraint getSecurityConstraint();

}