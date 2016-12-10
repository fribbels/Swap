package ivanc.swap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by ivanc on 12/9/2016.
 */

public class ImageStringConverter {
    public static Bitmap getBitmapFromString(String jsonString) {
        Bitmap decodedByte;
        try {
            byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            Log.v("****** BAD JSON??? " , jsonString);
            e.printStackTrace();
            decodedByte = BitmapFactory.decodeByteArray(new byte[]{}, 0, 0);
        }
        return decodedByte;
    }


    public static String getStringFromImageView(ImageView imageView) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

        Drawable drawable = imageView.getDrawable();
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmapPicture = bitmapDrawable.getBitmap();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}
