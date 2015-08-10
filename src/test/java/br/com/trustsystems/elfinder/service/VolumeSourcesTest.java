package br.com.trustsystems.elfinder.service;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.trustsystems.elfinder.exception.VolumeSourceException;

public class VolumeSourcesTest {

    @Test
    public void normalizeSource() {
        String[] sources = {"FILESYSTEM", "filesystem", "FileSystem", " File System", "file_system", "file-        system"};
        Assert.assertNotNull(sources);
        for (int i = 0; i < sources.length; i++) {
            Assert.assertEquals(VolumeSources.of(sources[i]), VolumeSources.FILESYSTEM);
        }
    }

    @Test(expectedExceptions = VolumeSourceException.class)
    public void volumeSourceNotSupported() {
        String source = "blablablaCloud";
        for (VolumeSources volumeSource : VolumeSources.values()) {
            Assert.assertNotNull(source);
            Assert.assertNotEquals(source, volumeSource.toString());
        }
        VolumeSources.of(source);
    }

    @Test(expectedExceptions = VolumeSourceException.class)
    public void volumeSourceNotInformed() {
        String[] sources = {null, "", "   "};
        for (String source : sources) {
            Assert.assertNull(source);
            VolumeSources.of(source);
        }
    }

    @Test
    public void volumeBuilderTest() {
        String alias = "User Home";
        String path = System.getProperty("user.home");
        Assert.assertNotNull(VolumeSources.of("filesystem").newInstance(alias, path));
        Assert.assertNotNull(VolumeSources.FILESYSTEM.newInstance(alias, path));
    }

    @Test(expectedExceptions = VolumeSourceException.class)
    public void volumeBuilderFailTest() {
        String alias = "User Home";
        String path = null;
        Assert.assertNotNull(VolumeSources.of("filesystem").newInstance(alias, path));
        Assert.assertNotNull(VolumeSources.FILESYSTEM.newInstance(alias, path));
    }
}
