package br.com.trustsystems.elfinder.support.archiver;

import java.io.IOException;
import java.util.List;

import br.com.trustsystems.elfinder.core.Target;

public interface Archiver {

    String getArchiveName();

    String getMimeType();

    String getExtension();

    Target compress(List<Target> targets) throws IOException;

    Target decompress(Target target);
}
