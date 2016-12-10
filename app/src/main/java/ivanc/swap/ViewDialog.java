package ivanc.swap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ivanc on 12/9/2016.
 */

public class ViewDialog {

    public void showDialog(final Activity activity, Post post){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.post_dialog);

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView dialogDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        TextView dialogUsername = (TextView) dialog.findViewById(R.id.dialog_username);
        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialog_image);

        dialogTitle.setText(post.getTitle());
        dialogDesc.setText(post.getDescription());
        dialogUsername.setText(post.getUsername());
        dialogImage.setImageBitmap(ImageStringConverter.getBitmapFromString(post.getImage()));

        Button backButton = (Button) dialog.findViewById(R.id.dialog_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Post copyPost = post;
        Button chatButton = (Button) dialog.findViewById(R.id.dialog_chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otherId = copyPost.getUserid();
                String appId = "6A2C5712-870F-4487-AB31-3EF97B040807";
                String userId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

                Intent intent = new Intent(activity, SendBirdMessagingActivity.class);
                Bundle args = SendBirdMessagingActivity.makeMessagingStartArgs(appId, userId, copyPost.getUsername(), new String[]{otherId});
                intent.putExtras(args);

                activity.startActivityForResult(intent, 333);
            }
        });

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dialog.show();
        dialog.getWindow().setLayout((int)(width-width*0.05), (int)(height-height*0.05));
    }
}
