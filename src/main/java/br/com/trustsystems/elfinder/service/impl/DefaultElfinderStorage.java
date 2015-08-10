package br.com.trustsystems.elfinder.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.core.VolumeSecurity;
import br.com.trustsystems.elfinder.core.impl.SecurityConstraint;
import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.ThumbnailWidth;

public class DefaultElfinderStorage implements ElfinderStorage {

    private static final String[][] ESCAPES = {{"+", "_P"}, {"-", "_M"}, {"/", "_S"}, {".", "_D"}, {"=", "_E"}};

    private List<Volume> volumes;
    private List<VolumeSecurity> volumeSecurities;
    private ThumbnailWidth thumbnailWidth;
    private Map<Volume, String> volumeIds;
    private Map<Volume, Locale> volumeLocales;

    @Override
    public Target fromHash(String hash) {
        for (Volume v : volumes) {
            String prefix = getVolumeId(v) + "_";

            if (hash.equals(prefix)) {
                return v.getRoot();
            }

            if (hash.startsWith(prefix)) {
                String localHash = hash.substring(prefix.length());

                for (String[] pair : ESCAPES) {
                    localHash = localHash.replace(pair[1], pair[0]);
                }

                String relativePath = new String(Base64.decodeBase64(localHash));
                return v.fromPath(relativePath);
            }
        }

        return null;
    }

    @Override
    public String getHash(Target target) throws IOException {
        String relativePath = target.getVolume().getPath(target);
        String base = new String(Base64.encodeBase64(relativePath.getBytes()));

        for (String[] pair : ESCAPES) {
            base = base.replace(pair[0], pair[1]);
        }

        return getVolumeId(target.getVolume()) + "_" + base;
    }

    @Override
    public String getVolumeId(Volume volume) {
        return volumeIds.get(volume);
    }

    @Override
    public Locale getVolumeLocale(Volume volume) {
        return volumeLocales.get(volume);
    }

    @Override
    public VolumeSecurity getVolumeSecurity(Target target) {
        try {
            final String targetHash = getHash(target);
            final String targetHashFirstChar = Character.toString(targetHash.charAt(0));
            final List<VolumeSecurity> volumeSecurities = getVolumeSecurities();

            for (VolumeSecurity volumeSecurity : volumeSecurities) {
                String volumePattern = volumeSecurity.getVolumePattern();

                // checks if volume pattern is equals to targethash first character, if so,
                // this volume pattern doesn't have any regex, applying the default regex to volume pattern
                if (volumePattern.trim().equalsIgnoreCase(targetHashFirstChar)) {
                    volumePattern = volumePattern + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX;
                }

                if (Pattern.compile(volumePattern, Pattern.CASE_INSENSITIVE).matcher(targetHash).matches()) {
                    return volumeSecurity;
                }
            }
            // return a default volume security
            return new VolumeSecurity() {
                @Override
                public String getVolumePattern() {
                    return targetHashFirstChar + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX;
                }
                @Override
                public SecurityConstraint getSecurityConstraint() {
                    return new SecurityConstraint();
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Unable to get target hash from elfinderStorage");
        }
    }

    // getters and setters

    public List<VolumeSecurity> getVolumeSecurities() {
        return volumeSecurities;
    }

    public void setVolumeSecurities(List<VolumeSecurity> volumeSecurities) {
        this.volumeSecurities = volumeSecurities;
    }

    public ThumbnailWidth getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(ThumbnailWidth thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public Map<Volume, String> getVolumeIds() {
        return volumeIds;
    }

    public void setVolumeIds(Map<Volume, String> volumeIds) {
        this.volumeIds = volumeIds;
    }

    public Map<Volume, Locale> getVolumeLocales() {
        return volumeLocales;
    }

    public void setVolumeLocales(Map<Volume, Locale> volumeLocales) {
        this.volumeLocales = volumeLocales;
    }

}
