package br.com.trustsystems.elfinder.support.archiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;

public abstract class AbstractArchiver implements Archiver {

    public static final String DEFAULT_ARCHIVE_NAME = "Archive";

    protected Path compressFile;

    public abstract ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream);

    public abstract ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) throws IOException;

    public abstract ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetBytes);

    @Override
    public String getArchiveName() {
        return DEFAULT_ARCHIVE_NAME;
    }

    @Override
    public Target compress(List<Target> targets) throws IOException {

        compressFile = null;

        Target compressTarget = null;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ArchiveOutputStream archiveOutputStream = null;

        try {

            for (Target target : targets) {

                // gets the target infos
                final Volume targetVolume = target.getVolume();
                final String targetName = targetVolume.getName(target);
                final String targetDir = targetVolume.getParent(target).toString();
                final boolean isTargetFolder = targetVolume.isFolder(target);

                if (compressFile == null) {
                    // create compress file
                    String compressFileName = (targets.size() == 1) ? targetName : getArchiveName();
                    compressFile = Paths.get(targetDir, compressFileName + System.currentTimeMillis() + getExtension());
                    compressTarget = targetVolume.fromPath(compressFile.toString());

                    // open streams to write the compress contents to
                    outputStream = Files.newOutputStream(compressFile);
                    bufferedOutputStream = new BufferedOutputStream(outputStream);
                    archiveOutputStream = createArchiveOutputStream(bufferedOutputStream);
                }

                if (isTargetFolder) {
                    // compress target directory
                    compressDirectory(target, archiveOutputStream);
                } else {
                    // compress target file
                    compressFile(target, archiveOutputStream);
                }
            }

        } finally {
            // close all streams
            if (archiveOutputStream != null) {
                archiveOutputStream.finish();
                archiveOutputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return compressTarget;
    }

    @Override
    public Target decompress(Target target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("operation not supported yet");
    }

    private void compressFile(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        addTargetToArchiveOutputStream(target, archiveOutputStream);
    }

    private void compressDirectory(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        Volume targetVolume = target.getVolume();
        Target[] targetChildrens = targetVolume.listChildren(target);

        for (Target targetChildren : targetChildrens) {

            if (targetVolume.isFolder(targetChildren)) {
                // go down the directory tree recursively
                compressDirectory(targetChildren, archiveOutputStream);
            } else {
                addTargetToArchiveOutputStream(targetChildren, archiveOutputStream);
            }
        }
    }

    private void addTargetToArchiveOutputStream(Target target, ArchiveOutputStream archiveOutputStream) throws IOException {
        Volume targetVolume = target.getVolume();

        try (InputStream targetInputStream = targetVolume.openInputStream(target)) {

            final long targetSize = targetVolume.getSize(target);
            final byte[] targetBytes = ArchiverUtils.toByteArray(targetInputStream);
            final String targetPath = targetVolume.getPath(target); // relative path

            // creates the entry and writes in the archive output stream
            ArchiveEntry entry = createArchiveEntry(targetPath, targetSize, targetBytes);
            archiveOutputStream.putArchiveEntry(entry);
            archiveOutputStream.write(targetBytes);
            archiveOutputStream.closeArchiveEntry();
        }
    }
}
