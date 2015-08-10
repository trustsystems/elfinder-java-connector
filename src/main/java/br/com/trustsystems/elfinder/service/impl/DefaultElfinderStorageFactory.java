package br.com.trustsystems.elfinder.service.impl;

import br.com.trustsystems.elfinder.service.ElfinderStorage;
import br.com.trustsystems.elfinder.service.ElfinderStorageFactory;

public class DefaultElfinderStorageFactory implements ElfinderStorageFactory {

    private ElfinderStorage elfinderStorage;

    @Override
    public ElfinderStorage getVolumeSource() {
        return elfinderStorage;
    }

    public void setElfinderStorage(ElfinderStorage elfinderStorage) {
        this.elfinderStorage = elfinderStorage;
    }
}
