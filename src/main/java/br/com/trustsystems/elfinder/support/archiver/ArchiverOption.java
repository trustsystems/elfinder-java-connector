package br.com.trustsystems.elfinder.support.archiver;

import org.json.JSONObject;

public enum ArchiverOption {

    INSTANCE;

    public static final JSONObject JSON_INSTANCE = new JSONObject(INSTANCE);

    public String[] getCreate() {
        return ArchiverType.supportedMimeTypes;
    }

    public String[] getExtract() {
        return ArchiverType.supportedMimeTypes;
    }

}