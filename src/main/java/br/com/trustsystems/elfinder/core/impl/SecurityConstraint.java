package br.com.trustsystems.elfinder.core.impl;

public class SecurityConstraint {

    private boolean locked = false;
    private boolean readable = true;
    private boolean writable = true;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }
}
