package course.labs.dailyselfie.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import course.labs.dailyselfie.R;
import course.labs.dailyselfie.model.PhotoItem;

public class PhotoItemsRecyclerAdapter extends RecyclerView.Adapter<PhotoItemsRecyclerAdapter.CustomViewHolder> {
    private static final String TAG = "PhotoItemRecyclAdapter";

    private Context mActivityContext;
    private List<PhotoItem> mPhotoItems;
    private OnPhotoItemListener mOnPhotoItemListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhotoItemsRecyclerAdapter(Context activityContext, List<PhotoItem> photoItems, OnPhotoItemListener onPhotoItemListener) {
        this.mActivityContext = activityContext;
        this.mPhotoItems = photoItems;
        this.mOnPhotoItemListener = onPhotoItemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View listViewRow = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_row, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(listViewRow, mOnPhotoItemListener);
        return customViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Drawable photo = mPhotoItems.get(position).getPhoto();
        photo.setBounds(new Rect(0, 0, 195, 260));

        holder.textView.setCompoundDrawables(photo, null, null, null);
        holder.textView.setText(mPhotoItems.get(position).getTimestamp());
    }

    // Return the size of your photo items list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPhotoItems.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView textView;
        OnPhotoItemListener onPhotoItemListener;

        CustomViewHolder(View itemView, OnPhotoItemListener onPhotoItemListener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.photo_view);
            this.itemView.setOnClickListener(this);
            this.onPhotoItemListener = onPhotoItemListener;
        }

        @Override
        public void onClick(View v) {
            onPhotoItemListener.onPhotoItemClick(getAdapterPosition());
        }
    }

    public interface OnPhotoItemListener {
        void onPhotoItemClick(int position);
    }
}
