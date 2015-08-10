package br.com.trustsystems.elfinder.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.trustsystems.elfinder.service.ElfinderStorageFactory;

public interface ElfinderContext {

    ElfinderStorageFactory getVolumeSourceFactory();

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

}
