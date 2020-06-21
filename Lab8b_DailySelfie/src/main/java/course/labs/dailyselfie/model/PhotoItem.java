package course.labs.dailyselfie.model;

import android.graphics.drawable.Drawable;

public class PhotoItem {

    private Drawable photo;
    private String timestamp;

    public PhotoItem(Drawable photo, String timestamp) {
        this.photo = photo;
        this.timestamp = timestamp;
    }


    public Drawable getPhoto() {
        return photo;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
