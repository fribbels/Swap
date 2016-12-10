package ivanc.swap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by ivanc on 12/1/2016.
 */


public class PostListAdapter extends BaseAdapter {

    private Context context;
    private List<Post> posts;
    private LayoutInflater layoutInflater;

    public PostListAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.post_row, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Post currentPost = getItem(position);
        viewHolder.textViewPostText.setText(currentPost.getTitle());
        viewHolder.textViewPostDesc.setText(currentPost.getDescription());
        viewHolder.textViewPostTimestamp.setText(DateUtils.convertISO8601ToTimeAgo(currentPost.getTimestamp()));
        Bitmap bitmap = ImageStringConverter.getBitmapFromString(currentPost.getImage());
        viewHolder.imageViewPostImage.setImageBitmap(bitmap);
        return convertView;
    }

    /*
    * This Function converts the String back to Bitmap
    * */

    private class ViewHolder {
        TextView textViewPostText;
        TextView textViewPostDesc;
        TextView textViewPostTimestamp;
        ImageView imageViewPostImage;
        public ViewHolder(View view) {
            textViewPostText = (TextView) view.findViewById(R.id.post_title);
            textViewPostDesc = (TextView) view.findViewById(R.id.post_desc);
            textViewPostTimestamp = (TextView) view.findViewById(R.id.post_timestamp);
            imageViewPostImage = (ImageView) view.findViewById(R.id.post_image);
        }
    }
}