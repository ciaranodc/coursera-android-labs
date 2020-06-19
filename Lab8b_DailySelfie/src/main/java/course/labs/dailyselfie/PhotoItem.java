package course.labs.dailyselfie;

import android.graphics.drawable.Drawable;

class PhotoItem {

    private Drawable photo;
    private String timestamp;

    PhotoItem(Drawable photo, String timestamp) {
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
