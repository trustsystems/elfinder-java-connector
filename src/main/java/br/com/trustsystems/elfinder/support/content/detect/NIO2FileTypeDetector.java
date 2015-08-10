package br.com.trustsystems.elfinder.support.content.detect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;

public class NIO2FileTypeDetector extends java.nio.file.spi.FileTypeDetector implements Detector {

    private final Tika tika = new Tika();

    @Override
    public String detect(InputStream inputStream) {
        try {
            return tika.detect(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to detect inputstream type", e);
        }
    }

    @Override
    public String detect(File file) {
        try {
            // is this good?
            if (file.isDirectory()) {
                return MIME_DIRECTORY;
            }
            return tika.detect(file);
        } catch (IOException e) {
            throw new RuntimeException("Unable to detect file type", e);
        }
    }

    @Override
    public String probeContentType(java.nio.file.Path path) throws IOException {
        // is this good?
        if (java.nio.file.Files.isDirectory(path)) {
            return MIME_DIRECTORY;
        }
        return tika.detect(path.toFile());
    }

}
