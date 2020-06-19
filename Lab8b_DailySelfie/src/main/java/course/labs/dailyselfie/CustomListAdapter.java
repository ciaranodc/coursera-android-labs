package course.labs.dailyselfie;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter {
    private Activity context;
    private List<PhotoItem> photoItems;

    public CustomListAdapter(Activity context, List<PhotoItem> photoItems) {
        super(context, R.layout.listview_row, photoItems);

        this.context = context;
        this.photoItems = photoItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null,true);

        TextView photoView = rowView.findViewById(R.id.photo_view);

        photoView.setCompoundDrawables(photoItems.get(position).getPhoto(), null, null, null);
        photoView.setText(photoItems.get(position).getTimestamp());

        return rowView;
    }
}
