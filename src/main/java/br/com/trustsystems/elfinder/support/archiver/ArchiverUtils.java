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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;

public final class ArchiverUtils {

    private ArchiverUtils() {
        // suppress default constructor
        // for noninstantiability
        throw new AssertionError();
    }

    /**
     * The default buffer size
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * The CRC32 checksum engine
     */
    private static final CRC32 crc32 = new CRC32();

    /**
     * Read input from input stream and write it to output stream
     * until there is no more input from input stream.
     *
     * @param is  input stream the input stream to read from.
     * @param os  output stream the output stream to write to.
     * @param buf the byte array to use as a buffer
     */
    public static void flow(InputStream is, OutputStream os, byte[] buf) throws IOException {
        int numRead;
        while (-1 != (numRead = is.read(buf))) {
            os.write(buf, 0, numRead);
        }
    }

    /**
     * Read input from input stream and write it to output stream
     * until there is no more input from input stream.
     *
     * @param is input stream the input stream to read from.
     * @param os output stream the output stream to write to.
     */
    public static void flow(InputStream is, OutputStream os) throws IOException {
        flow(is, os, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * <p/>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param is the <code>InputStream</code> to read from
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws java.io.IOException  if an I/O error occurs
     */
    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        flow(is, byteArrayOutputStream, new byte[DEFAULT_BUFFER_SIZE]);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Get CRC32 checksum of the uncompressed data
     *
     * @param bytes the bytes to read from
     * @return checksum for bytes
     */
    public static long crc32Checksum(byte[] bytes) {
        crc32.update(bytes);
        long checksum = crc32.getValue();
        crc32.reset();
        return checksum;
    }
}
