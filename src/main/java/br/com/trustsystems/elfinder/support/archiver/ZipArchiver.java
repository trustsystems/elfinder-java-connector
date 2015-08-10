package br.com.trustsystems.elfinder.support.archiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ZipArchiver extends AbstractArchiver implements Archiver {

    @Override
    public String getMimeType() {
        return "application/zip";
    }

    @Override
    public String getExtension() {
        return ".zip";
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetBytes) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(targetPath);
        zipEntry.setSize(targetSize);
        zipEntry.setMethod(ZipEntry.STORED);
        if (targetBytes != null) {
            zipEntry.setCrc(ArchiverUtils.crc32Checksum(targetBytes));
        }
        return zipEntry;
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException {
        // for some internal optimizations should use the constructor that accepts a File argument
        return new ZipArchiveOutputStream(compressFile.toFile());
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) {
        return new ZipArchiveInputStream(bufferedInputStream);
    }
}
