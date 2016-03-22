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
package br.com.trustsystems.elfinder.service;

import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.core.impl.NIO2FileSystemTarget;
import br.com.trustsystems.elfinder.core.impl.NIO2FileSystemVolume;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * NIO FileSystem Volume Test.
 *
 * @author Thiago Gutenberg Carvalho da Costa
 */
public class NIO2FileSystemVolumeTest {

    // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! MELHORAR ESSA CLASSE DE TESTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private static final String pattern = "d MMM yyyy HH:mm:ss 'GMT'";
    private static final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);

    private static final Path rootPath = Paths.get(System.getProperty("user.home"), "RootFolderTest");
    private static final Path parentPath = Paths.get(rootPath.toString(), "ParentFolderTest");
    private static final Path targetPath = Paths.get(parentPath.toString(), "FolderTest");

    private static Volume volume;
    private static Target target;

    @BeforeClass
    public static void setUp() {
//        volume = new NIO2FileSystemVolume.Builder("myhome", rootPath).build();
        volume = NIO2FileSystemVolume.builder("myhome", rootPath).build();
        target = new NIO2FileSystemTarget((NIO2FileSystemVolume) volume, targetPath);

        // creates target folders tree
        try {
            volume.createFolder(target);
        } catch (IOException e) {
            try {
                Files.createDirectories(targetPath);
            } catch (IOException e1) {
                throw new RuntimeException("Unable to create test folders: " + targetPath, e1);
            }
        }

        System.out.println("-------------------------------------");
        System.out.println("Volume Alias: " + volume.getAlias());
        System.out.println("Volume: " + toPath(volume.getRoot()));
        System.out.println("Target: " + toPath(target));
        System.out.println("-------------------------------------");
    }

    @AfterClass
    public static void tearDown() {
        // deletes target folders tree
        try {
            volume.deleteFolder(toTarget(targetPath));
            volume.deleteFolder(toTarget(parentPath));
            volume.deleteFolder(toTarget(rootPath));
        } catch (IOException e) {
            try {
                Files.deleteIfExists(targetPath);
                Files.deleteIfExists(parentPath);
                Files.deleteIfExists(rootPath);
            } catch (IOException e1) {
                throw new RuntimeException("Unable to delete test folders", e1);
            }
        }
    }

    private static Target toTarget(Path path) {
        return new NIO2FileSystemTarget((NIO2FileSystemVolume) volume, path);
    }

    private static Path toPath(Target target) {
        return ((NIO2FileSystemTarget) target).getPath();
    }

    @Test(enabled = false)
    public void isFolderTest() {
        boolean folder = volume.isFolder(target);
        System.out.println("Volume#isFolder(Target target): " + folder);
        Assert.assertTrue(folder);
    }

    @Test(enabled = false)
    public void listChildrenTest() throws IOException {
        int length = volume.listChildren(target).length;
        Assert.assertEquals(length, 0);
        System.out.println("Volume#listChildren(Target target): " + length);
//        Assert.assertEquals(volume.listChildren(toTarget(parentPath)).length, 1);
    }

    @Test(enabled = false)
    public void hasChildFolderTest() throws IOException {
        boolean hasChildFolder = volume.hasChildFolder(target);
        System.out.println("Volume#hasChildFolder(Target target): " + hasChildFolder);
        Assert.assertFalse(hasChildFolder);
//        Assert.assertTrue(volume.hasChildFolder(toTarget(parentPath)));
    }

    @Test(enabled = false)
    public void getLastModifiedTest() throws IOException {
        long lastModifiedMillis = volume.getLastModified(target);
        Assert.assertNotNull(lastModifiedMillis);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(lastModifiedMillis);

        System.out.println("Volume#getLastModified(Target target): " + sdf.format(calendar.getTime()));
    }

    @Test(enabled = false)
    public void getNameTest() {
        String name = volume.getName(target);
        System.out.println("Volume#getName(Target target): " + name);
        Assert.assertEquals(name, targetPath.getFileName().toString());
    }

    @Test(enabled = false)
    public void getMimeTypeTest() throws IOException {
        String mimeType = volume.getMimeType(target);
        System.out.println("Volume#getMimeType(Target target): " + mimeType);
        Assert.assertEquals("directory", mimeType);
    }

    @Test(enabled = false)
    public void getRootTest() {
        Path path = toPath(volume.getRoot());
        System.out.println("Volume#getRoot(): " + path);
        Assert.assertEquals(path, rootPath);
    }

    @Test(enabled = false)
    public void getParentTest() {
        Path path = toPath(volume.getParent(target));
        System.out.println("Volume#getParent(Target target): " + path);
        Assert.assertEquals(path, parentPath);
    }

    @Test(enabled = false)
    public void getPathTest() throws IOException {
        String path = volume.getPath(target);
        System.out.println("Volume#getPath(Target target): " + path);
        Assert.assertEquals(path, targetPath.subpath(rootPath.getNameCount(), targetPath.getNameCount()).toString());
    }

    @Test(enabled = false)
    public void getSizeTest() throws IOException {
        long size = volume.getSize(target);
        System.out.println("Volume#getSize(Target target) in bytes: " + size);
        Assert.assertNotNull(size);
    }

    @Test(enabled = false)
    public void isRootTest() throws IOException {
        boolean root = volume.isRoot(target);
        Assert.assertFalse(root);
        System.out.println("Volume#isRoot(Target target): " + root);
//        Assert.assertTrue(volume.isRoot(toTarget(rootPath)));
    }

}
