package br.com.trustsystems.elfinder;

public final class ElFinderConstants {

    //global constants
    public static final int ELFINDER_TRUE_RESPONSE = 1;
    public static final int ELFINDER_FALSE_RESPONSE = 0;

    //options
    public static final String ELFINDER_VERSION_API = "2.1";

    //security
    // regex that matches any character that
    // occurs zero or more times (finds any character sequence)
    public static final String ELFINDER_VOLUME_SERCURITY_REGEX = "_.*";

    //api commands parameters
    public static final String ELFINDER_PARAMETER_INIT = "init";
    public static final String ELFINDER_PARAMETER_TREE = "tree";
    public static final String ELFINDER_PARAMETER_TARGET = "target";
    public static final String ELFINDER_PARAMETER_API = "api";
    public static final String ELFINDER_PARAMETER_NETDRIVERS = "netDrivers";
    public static final String ELFINDER_PARAMETER_FILES = "files";
    public static final String ELFINDER_PARAMETER_CWD = "cwd";
    public static final String ELFINDER_PARAMETER_OPTIONS = "options";
    public static final String ELFINDER_PARAMETER_HASH = "hash";
    public static final String ELFINDER_PARAMETER_MIME = "mime";
    public static final String ELFINDER_PARAMETER_TIMESTAMP = "ts";
    public static final String ELFINDER_PARAMETER_SIZE = "size";
    public static final String ELFINDER_PARAMETER_READ = "read";
    public static final String ELFINDER_PARAMETER_WRITE = "write";
    public static final String ELFINDER_PARAMETER_LOCKED = "locked";
    public static final String ELFINDER_PARAMETER_THUMBNAIL = "tmb";
    public static final String ELFINDER_PARAMETER_PARENTHASH = "phash";
    public static final String ELFINDER_PARAMETER_DIRECTORY_FILE_NAME = "name";
    public static final String ELFINDER_PARAMETER_VOLUME_ID = "volumeid";
    public static final String ELFINDER_PARAMETER_HAS_DIR = "dirs";
    public static final String ELFINDER_PARAMETER_PATH = "path";
    public static final String ELFINDER_PARAMETER_COMMAND_DISABLED = "disabled";
    public static final String ELFINDER_PARAMETER_FILE_SEPARATOR = "/";
    public static final String ELFINDER_PARAMETER_OVERWRITE_FILE = "copyOverwrite";
    public static final String ELFINDER_PARAMETER_ARCHIVERS = "archivers";
    public static final String ELFINDER_PARAMETER_COMMAND = "cmd";
    public static final String ELFINDER_PARAMETER_TARGETS = "targets[]";
    public static final String ELFINDER_PARAMETER_SEARCH_QUERY = "q";
    public static final String ELFINDER_PARAMETER_CONTENT = "content";
    public static final String ELFINDER_PARAMETER_LIST = "list";
    public static final String ELFINDER_PARAMETER_NAME = "name";
    public static final String ELFINDER_PARAMETER_FILE_DESTINATION = "dst";
    public static final String ELFINDER_PARAMETER_CUT = "cut";
    public static final String ELFINDER_PARAMETER_TYPE = "type";

    //api commands json header
    public static final String ELFINDER_JSON_RESPONSE_ADDED = "added";
    public static final String ELFINDER_JSON_RESPONSE_REMOVED = "removed";
    public static final String ELFINDER_JSON_RESPONSE_CHANGED = "changed";
    public static final String ELFINDER_JSON_RESPONSE_DIM = "dim";
    public static final String ELFINDER_JSON_RESPONSE_ERROR = "error";
    public static final String ELFINDER_JSON_RESPONSE_SIZE = "size";
}
