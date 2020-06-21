package course.labs.dailyselfie.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import course.labs.dailyselfie.R;
import course.labs.dailyselfie.model.PhotoItem;

public class PhotoViewerFragment extends DialogFragment {
    private PhotoItem mPhotoItem;

    public PhotoViewerFragment(PhotoItem photoItem) {
        this.mPhotoItem = photoItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imageFragmentView = inflater.inflate(R.layout.image_dialog, container, false);
        ImageView imageView = imageFragmentView.findViewById(R.id.image_preview);
        imageView.setImageDrawable(mPhotoItem.getPhoto());

        return imageFragmentView;
    }
}
