package br.com.trustsystems.elfinder.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.trustsystems.elfinder.core.Target;
import br.com.trustsystems.elfinder.core.Volume;
import br.com.trustsystems.elfinder.core.VolumeBuilder;
import br.com.trustsystems.elfinder.support.content.detect.Detector;
import br.com.trustsystems.elfinder.support.content.detect.NIO2FileTypeDetector;

public class NIO2FileSystemVolume implements Volume {

    private final String alias;
    private final Path rootDir;

    private NIO2FileSystemVolume(Builder builder) {
        this.alias = builder.alias;
        this.rootDir = builder.rootDir;
        try {
            Target target = fromPath(this.rootDir);
            if (!exists(target))
                createFolder(target);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create root dir folder", e);
        }
    }

    private Path fromTarget(Target target) {
        return ((NIO2FileSystemTarget) target).getPath();
    }

    private NIO2FileSystemTarget fromPath(Path path) {
        return new NIO2FileSystemTarget(this, path);
    }

    public Path getRootDir() {
        return rootDir;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void createFile(Target target) throws IOException {
        Files.createFile(fromTarget(target));
    }

    @Override
    public void createFolder(Target target) throws IOException {
        Files.createDirectories(fromTarget(target));
    }

    @Override
    public void deleteFile(Target target) throws IOException {
        if (!isFolder(target)) {
            Files.deleteIfExists(fromTarget(target));
        }
    }

    @Override
    public void deleteFolder(Target target) throws IOException {
        if (isFolder(target)) {
            Files.deleteIfExists(fromTarget(target));
        }
    }

    @Override
    public boolean exists(Target target) {
        return Files.exists(fromTarget(target));
    }

    @Override
    public Target fromPath(String relativePath) {
        String rootDir = getRootDir().toString();

        Path path;
        if (relativePath.startsWith(rootDir)) {
            path = Paths.get(relativePath);
        } else {
            path = Paths.get(rootDir, relativePath);
        }
        return fromPath(path);
    }

    @Override
    public long getLastModified(Target target) throws IOException {
        return Files.getLastModifiedTime(fromTarget(target)).toMillis();
    }

    @Override
    public String getMimeType(Target target) {
        // An implementation using "Java Service Provider Interface (SPI)" is
        // registered in /META-INF/services/java.nio.file.spi.FileTypeDetector,
        // improving the standard default NIO implementation with the API Apache
        // Tika.
//        return Files.probeContentType(fromTarget(target));

        Detector detector = new NIO2FileTypeDetector();
        Path path = fromTarget(target);
        return detector.detect(path.toFile());
    }

    @Override
    public String getName(Target target) {
        return fromTarget(target).getFileName().toString();
    }

    @Override
    public Target getParent(Target target) {
        Path path = fromTarget(target);
        return fromPath(path.getParent());
    }

    @Override
    public String getPath(Target target) throws IOException {
        String relativePath = "";
        if (!isRoot(target)) {
            Path path = fromTarget(target);
            relativePath = path.subpath(getRootDir().getNameCount(), path.getNameCount()).toString();
        }
        return relativePath;
    }

    @Override
    public Target getRoot() {
        return fromPath(getRootDir());
    }

    @Override
    public long getSize(Target target) throws IOException {
        if (isFolder(target)) {
            FileTreeSize fileTreeSize = new NIO2FileSystemVolume.FileTreeSize();
            Files.walkFileTree(fromTarget(target), fileTreeSize);
            return fileTreeSize.getTotalSize();
        }
        return Files.size(fromTarget(target));
    }

    @Override
    public boolean isFolder(Target target) {
        return Files.isDirectory(fromTarget(target));
    }

    @Override
    public boolean isRoot(Target target) throws IOException {
        return isFolder(target) && Files.isSameFile(getRootDir(), fromTarget(target));
    }

    @Override
    public boolean hasChildFolder(Target target) throws IOException {
    	if (isFolder(target)) {
    		Path dir = fromTarget(target);
    		
    		// directory filter
    		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
                @Override
                public boolean accept(Path path) throws IOException {
                    return Files.isDirectory(path);
                }
            };
    		
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir, filter)) {
            	return directoryStream.iterator().hasNext();
            }
    	}
    	return false;
    }

    @Override
    public Target[] listChildren(Target target) throws IOException {
        if (isFolder(target)) {
            Path directory = fromTarget(target);

            // filter to exclude hidden files
            DirectoryStream.Filter<Path> notHiddenFilter = new DirectoryStream.Filter<Path>() {
                public boolean accept(Path path) throws IOException {
                    return (!Files.isHidden(path));
                }
            };

            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory, notHiddenFilter)) {
            	List<Target> list = new ArrayList<>();
                for (Path path : directoryStream) {
                    list.add(fromPath(path));
                }
                return Collections.unmodifiableList(list).toArray(new Target[list.size()]);
            }
        }
        return new Target[0];
    }

    @Override
    public InputStream openInputStream(Target target) throws IOException {
        return Files.newInputStream(fromTarget(target));
    }

    @Override
    public OutputStream openOutputStream(Target target) throws IOException {
        return Files.newOutputStream(fromTarget(target));
    }

    @Override
    public void rename(Target origin, Target destination) throws IOException {
//        fromTarget(origin).toFile().renameTo(fromTarget(destination).toFile());
        Files.move(fromTarget(origin), fromTarget(destination), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public List<Target> search(String target) throws IOException {
        Path rootPath = getRootDir();

        FileTreeSearch fileTreeSearch = new NIO2FileSystemVolume.FileTreeSearch();
        fileTreeSearch.setQuery(target);
        Files.walkFileTree(rootPath, fileTreeSearch);

        List<Path> paths = fileTreeSearch.getFoundPaths();
        List<Target> targets = new ArrayList<>(paths.size());
        for (Path path : paths) {
            targets.add(fromPath(path));
        }
        return Collections.unmodifiableList(targets);
    }

    /**
     * Gets a Builder for creating a new NIO2FileSystemVolume instance.
     * 
     * @return a new Builder for NIO2FileSystemVolume.
     */
    public static Builder builder(String alias, Path rootDir) {
        return new NIO2FileSystemVolume.Builder(alias, rootDir);
    }
    
    /**
     * Builder NIO2FileSystemVolume Inner Class
     */
    public static class Builder implements VolumeBuilder<NIO2FileSystemVolume> {

        private final String alias;
        private final Path rootDir;

        public Builder(String alias, Path rootDir) {
            this.alias = alias;
            this.rootDir = rootDir;
        }

        @Override
        public NIO2FileSystemVolume build() {
            return new NIO2FileSystemVolume(this);
        }
    }

    /**
     * File Tree Size Visitor Inner Class
     */
    public static class FileTreeSize extends SimpleFileVisitor<Path> {

        private long totalSize;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (attrs.isOther() || attrs.isRegularFile()) {
                totalSize += attrs.size();
            }
            return FileVisitResult.CONTINUE;
        }

        public long getTotalSize() {
            return totalSize;
        }
    }

    /**
     * File Tree Search Visitor Inner Class
     */
    public static class FileTreeSearch extends SimpleFileVisitor<Path> {

        private final List<Path> foundPaths = new ArrayList<>();

        private String query;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            String fileName = file.getFileName().toString();
            if (fileName.toLowerCase().contains(query.toLowerCase())) {
                foundPaths.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public List<Path> getFoundPaths() {
            return Collections.unmodifiableList(foundPaths);
        }
    }
}