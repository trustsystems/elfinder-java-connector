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
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

public class GzipArchiver extends AbstractArchiver implements Archiver {

    @Override
    public String getMimeType() {
        return "application/x-gzip";
    }

    @Override
    public String getExtension() {
        return ".tgz";
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetBytes) {
        TarArchiveEntry targzEntry = new TarArchiveEntry(targetPath);
        targzEntry.setSize(targetSize);
        return targzEntry;
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException {
        return new TarArchiveOutputStream(new GzipCompressorOutputStream(bufferedOutputStream));
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) {
        return new TarArchiveInputStream(bufferedInputStream);
    }
}
