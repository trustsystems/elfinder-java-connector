package br.com.trustsystems.elfinder.core.impl;

import br.com.trustsystems.elfinder.core.VolumeSecurity;

public class DefaultVolumeSecurity implements VolumeSecurity {

    private String volumePattern;
    private SecurityConstraint securityConstraint;

    @Override
    public String getVolumePattern() {
        return volumePattern;
    }

    public void setVolumePattern(String volumePattern) {
        this.volumePattern = volumePattern;
    }

    @Override
    public SecurityConstraint getSecurityConstraint() {
        return securityConstraint;
    }

    public void setSecurityConstraint(SecurityConstraint securityConstraint) {
        this.securityConstraint = securityConstraint;
    }
}
