package br.com.trustsystems.elfinder.service.impl;

import br.com.trustsystems.elfinder.service.ThumbnailWidth;

public class DefaultThumbnailWidth implements ThumbnailWidth {

    private int thumbnailWidth;

    @Override
    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }
}
