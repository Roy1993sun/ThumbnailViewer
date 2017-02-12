package roy.me.thumbnailviewer;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Roy Sun on 2017/2/10.
 */

public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder> {

    private       Cursor                   mMediaStoreCursor;
    private final Activity                 mActivity;
    private       OnClickThumbnailListener mOnClickThumbnailListener;

    public interface OnClickThumbnailListener {
        void onClickImage(Uri imageUri);
    }

    public MediaStoreAdapter(Activity activity) {
        mActivity = activity;
        this.mOnClickThumbnailListener = (OnClickThumbnailListener) activity;
    }

    @Override
    public MediaStoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.media_image_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*Bitmap bitmap = getBitmapFromMediaStore(position);
        if (bitmap != null) {
            holder.getImageView().setImageBitmap(bitmap);
        }*/
        Glide.with(mActivity)
             .load(getUriFromMediaStore(position))
             .centerCrop()
             .override(96, 96)
             .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return (mMediaStoreCursor == null) ?
                0 :
                mMediaStoreCursor.getCount();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.media_store_imageView);
            mImageView.setOnClickListener(this);
        }

        public ImageView getImageView() {
            return mImageView;
        }


        @Override
        public void onClick(View view) {
            getOnClickUri(getAdapterPosition());
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (cursor == mMediaStoreCursor) {
            return null;
        }
        Cursor oldCursor = mMediaStoreCursor;
        this.mMediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private Bitmap getBitmapFromMediaStore(int position) {
        int idIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(
                MediaStore.Files.FileColumns.MEDIA_TYPE);

        mMediaStoreCursor.moveToPosition(position);
        switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                return MediaStore.Images.Thumbnails.getThumbnail(mActivity.getContentResolver(),
                                                                 mMediaStoreCursor.getLong(idIndex),
                                                                 MediaStore.Images.Thumbnails.MICRO_KIND,
                                                                 null);
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                return MediaStore.Video.Thumbnails.getThumbnail(mActivity.getContentResolver(),
                                                                mMediaStoreCursor.getLong(idIndex),
                                                                MediaStore.Video.Thumbnails.MICRO_KIND,
                                                                null);

            default:
                return null;
        }

    }

    private Uri getUriFromMediaStore(int position) {
        int dataIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
        mMediaStoreCursor.moveToPosition(position);

        String dataString = mMediaStoreCursor.getString(dataIndex);

        return Uri.parse("file://" + dataString);
    }

    private void getOnClickUri(int position) {
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(
                MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mMediaStoreCursor.moveToPosition(position);
        switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                String dataString = mMediaStoreCursor.getString(dataIndex);
                Uri imageUri = Uri.parse("file://" + dataString);
                mOnClickThumbnailListener.onClickImage(imageUri);
                break;

            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:

                break;
            default:
                break;
        }

    }
}
