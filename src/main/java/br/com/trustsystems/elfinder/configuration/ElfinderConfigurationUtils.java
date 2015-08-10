package br.com.trustsystems.elfinder.configuration;

import java.net.URI;

public final class ElfinderConfigurationUtils {

    private ElfinderConfigurationUtils() {
        // suppress default constructor
        // for noninstantiability
        throw new AssertionError();
    }

    public static String treatPath(String path) {
        if (path != null && !path.trim().isEmpty()) {

            final String uriSuffix = "file:";
            final String slash = "/";

            path = path.trim();
            path = path.replaceAll("//", slash);
            path = path.replaceAll("\\\\", slash);
            path = path.replaceAll("\\s+", " ");
            path = path.replaceAll("[\\p{Z}]", "%20");

            StringBuilder sb = new StringBuilder();
            if (!path.startsWith(uriSuffix)) {
                if (path.startsWith(slash)) {
                    sb.append(uriSuffix).append(path);
                } else {
                    sb.append(uriSuffix).append(slash).append(path);
                }
            }
            return sb.toString();
        }
        return path;
    }

    public static URI toURI(String path) {
        return URI.create(treatPath(path));
    }
}
