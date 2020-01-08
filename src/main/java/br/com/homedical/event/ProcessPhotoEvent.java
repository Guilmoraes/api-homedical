package br.com.homedical.event;

import br.com.homedical.domain.Photo;

public class ProcessPhotoEvent {

    private Photo photo;

    private boolean update;

    public ProcessPhotoEvent(Photo photo, boolean update) {
        this.photo = photo;
        this.update = update;
    }

    public boolean isUpdate() {
        return this.update;
    }

    public Photo get() {
        return photo;
    }

}
