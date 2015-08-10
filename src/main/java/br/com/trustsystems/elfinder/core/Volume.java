package br.com.trustsystems.elfinder.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Volume {

    void createFile(Target target) throws IOException;

    void createFolder(Target target) throws IOException;

    void deleteFile(Target target) throws IOException;

    void deleteFolder(Target target) throws IOException;

    boolean exists(Target target);

    Target fromPath(String relativePath);

    long getLastModified(Target target) throws IOException;

    String getMimeType(Target target);

    String getAlias();

    String getName(Target target);

    Target getParent(Target target);

    String getPath(Target target) throws IOException;

    Target getRoot();

    long getSize(Target target) throws IOException;

    boolean hasChildFolder(Target target) throws IOException;

    boolean isFolder(Target target);

    boolean isRoot(Target target) throws IOException;

    Target[] listChildren(Target target) throws IOException;

    InputStream openInputStream(Target target) throws IOException;

    OutputStream openOutputStream(Target target) throws IOException;

    void rename(Target origin, Target destination) throws IOException;

    List<Target> search(String target) throws IOException;
}
