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
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;

public class ZipArchiver extends AbstractArchiver implements Archiver {

    private Path compressFile;

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
//    	return new ZipArchiveOutputStream(bufferedOutputStream);
        return new ZipArchiveOutputStream(compressFile.toFile());
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) throws IOException {
        return new ZipArchiveInputStream(bufferedInputStream);
    }

    @Override
    public Target compress(List<Target> targets) throws IOException {
        Target compressTarget = null;
        compressFile = null;

        for (Target target : targets) {
            // get target volume
            final Volume targetVolume = target.getVolume();

            // gets the target infos
            final String targetName = targetVolume.getName(target);
            final String targetDir = targetVolume.getParent(target).toString();
            final boolean isTargetFolder = targetVolume.isFolder(target);

            if (compressFile == null) {
                // create compress file
                String compressFileName = (targets.size() == 1) ? targetName : getArchiveName();
                compressFile = Paths.get(targetDir, compressFileName + System.currentTimeMillis() + getExtension());
                compressTarget = targetVolume.fromPath(compressFile.toString());
            }

            // open streams to write the compress target contents and auto close it
            try (ArchiveOutputStream archiveOutputStream = createArchiveOutputStream(
                    new BufferedOutputStream(
                            targetVolume.openOutputStream(compressTarget)))) {

                if (isTargetFolder) {
                    // compress target directory
                    compressDirectory(target, archiveOutputStream);
                } else {
                    // compress target file
                    compressFile(target, archiveOutputStream);
                }
            }
        }
        return compressTarget;
    }

    @Override
    public Target decompress(Target targetCompress) throws IOException {
        Target decompressTarget;

        final String src = targetCompress.toString();

        // create zipFile instance to read the compress target 
        // contents  and auto close it
        try (ZipFile zipFile = new ZipFile(src)) {
            // gets the compress target infos
            final Volume volume = targetCompress.getVolume();
            final String dest = removeExtension(src);

            // creates the decompress target infos
            decompressTarget = volume.fromPath(dest);

            // creates the dest folder if not exists
            volume.createFolder(decompressTarget);

            // get the compress target list entry
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entries.nextElement();

                if (zipFile.canReadEntryData(zipArchiveEntry)) {
                    // get the entry infos
                    final String entryName = zipArchiveEntry.getName();
                    final InputStream inputStream = zipFile.getInputStream(zipArchiveEntry);
                    final Target target = volume.fromPath(Paths.get(dest, entryName).toString());
                    final Target parent = volume.getParent(target);

                    // create parent folder if not exists
                    if (parent != null && !volume.exists(parent)) {
                        volume.createFolder(parent);
                    }

                    if (!zipArchiveEntry.isDirectory()) {
                        // open streams to write the decompress target contents and auto close it
                        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                                volume.openOutputStream(target))) {

                            // get entry content
                            byte[] content = new byte[(int) zipArchiveEntry.getSize()];
                            ArchiverUtils.flow(inputStream, bufferedOutputStream, content);
                        }
                    }
                }
            }
        }
        return decompressTarget;
    }
}
