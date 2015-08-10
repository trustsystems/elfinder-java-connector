package br.com.trustsystems.elfinder.support.content.detect;

import java.io.File;
import java.io.InputStream;

public interface Detector {

    String MIME_DIRECTORY = "directory";
    String MIME_IMAGE = "image";

    String detect(InputStream inputStream);

    String detect(File file);
}
