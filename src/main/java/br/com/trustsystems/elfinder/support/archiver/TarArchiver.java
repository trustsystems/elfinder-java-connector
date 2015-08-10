package br.com.trustsystems.elfinder.support.archiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiver extends AbstractArchiver implements Archiver {

    @Override
    public String getMimeType() {
        return "application/x-tar";
    }

    @Override
    public String getExtension() {
        return ".tar";
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetBytes) {
        TarArchiveEntry tarEntry = new TarArchiveEntry(targetPath);
        tarEntry.setSize(targetSize);
        return tarEntry;
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException {
        return new TarArchiveOutputStream(bufferedOutputStream);
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) {
        return new TarArchiveInputStream(bufferedInputStream);
    }
}
