package br.com.trustsystems.elfinder.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.core.impl.NIO2FileSystemTarget;
import br.com.trustsystems.elfinder.core.impl.NIO2FileSystemVolume;
import br.com.trustsystems.elfinder.support.content.detect.Detector;

@Test(enabled = false)
public class NIO2FileSystemVolumeTest {

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

    public void isFolder() {
        boolean folder = volume.isFolder(target);
        System.out.println("Volume#isFolder(Target target): " + folder);
        Assert.assertTrue(folder);
    }

    public void listChildren() throws IOException {
        int length = volume.listChildren(target).length;
        Assert.assertEquals(length, 0);
        System.out.println("Volume#listChildren(Target target): " + length);
//        Assert.assertEquals(volume.listChildren(toTarget(parentPath)).length, 1);
    }

    public void hasChildFolder() throws IOException {
        boolean hasChildFolder = volume.hasChildFolder(target);
        System.out.println("Volume#hasChildFolder(Target target): " + hasChildFolder);
        Assert.assertFalse(hasChildFolder);
//        Assert.assertTrue(volume.hasChildFolder(toTarget(parentPath)));
    }

    public void getLastModified() throws IOException {
        long lastModifiedMillis = volume.getLastModified(target);
        Assert.assertNotNull(lastModifiedMillis);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(lastModifiedMillis);

        System.out.println("Volume#getLastModified(Target target): " + sdf.format(calendar.getTime()));
    }

    public void getName() {
        String name = volume.getName(target);
        System.out.println("Volume#getName(Target target): " + name);
        Assert.assertEquals(name, targetPath.getFileName().toString());
    }

    public void getMimeType() throws IOException {
        String mimeType = volume.getMimeType(target);
        System.out.println("Volume#getMimeType(Target target): " + mimeType);
        Assert.assertEquals(Detector.MIME_DIRECTORY, mimeType);
    }

    public void getRoot() {
        Path path = toPath(volume.getRoot());
        System.out.println("Volume#getRoot(): " + path);
        Assert.assertEquals(path, rootPath);
    }

    public void getParent() {
        Path path = toPath(volume.getParent(target));
        System.out.println("Volume#getParent(Target target): " + path);
        Assert.assertEquals(path, parentPath);
    }

    public void getPath() throws IOException {
        String path = volume.getPath(target);
        System.out.println("Volume#getPath(Target target): " + path);
        Assert.assertEquals(path, targetPath.subpath(rootPath.getNameCount(), targetPath.getNameCount()).toString());
    }

    public void getSize() throws IOException {
        long size = volume.getSize(target);
        System.out.println("Volume#getSize(Target target) in bytes: " + size);
        Assert.assertNotNull(size);
    }

    public void isRoot() throws IOException {
        boolean root = volume.isRoot(target);
        Assert.assertFalse(root);
        System.out.println("Volume#isRoot(Target target): " + root);
//        Assert.assertTrue(volume.isRoot(toTarget(rootPath)));
    }

}
