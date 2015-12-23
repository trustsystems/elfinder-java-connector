/*
 * #%L
 * %%
 * Copyright (C) 2015 Trustsystems Desenvolvimento de Sistemas, LTDA.
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Trustsystems Desenvolvimento de Sistemas, LTDA. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package br.com.trustsystems.elfinder.support.archiver;

import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
