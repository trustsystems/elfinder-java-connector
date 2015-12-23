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
package br.com.trustsystems.elfinder.support.content.detect;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
