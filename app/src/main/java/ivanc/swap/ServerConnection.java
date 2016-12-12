package ivanc.swap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import android.provider.Settings.Secure;

import static java.security.AccessController.getContext;
import com.cloudinary.*;

/**
 * Created by ivanc on 12/1/2016.
 */

public class ServerConnection extends AppCompatActivity {
    private HomeActivity activity;

    private List<Post> localPosts;
    private Context context;
    private NewsFeedFragment newsFeedFragment;

    private String SENDBIRD_APP_ID = "6A2C5712-870F-4487-AB31-3EF97B040807";
    private String android_id;
    private Cloudinary cloudinary;

    public ServerConnection(HomeActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        localPosts = new ArrayList<>();
//        cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "dg9xrbtal",
//                "api_key", "741985358596817",
//                "api_secret", "VltjoXVriCpOEdNJurs-NPD8XsU"));
//
        android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

//        initSendbird();
    }
//
//    private void initSendbird() {
//        SendBird.init(SENDBIRD_APP_ID, context);
//        SendBird.connect(android_id, new SendBird.ConnectHandler() {
//            @Override
//            public void onConnected(User user, SendBirdException e) {
//            if (e != null) {
//                // Error.
//                return;
//            }
//            SendBird.updateCurrentUserInfo("nickname", "profileurl", new SendBird.UserInfoUpdateHandler() {
//                @Override
//                public void onUpdated(SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    return;
//                }
//                }
//            });
//            }
//        });
//    }

    public void newChat(String otherid) {
        List<String> userids = new ArrayList<>();
        userids.add(otherid);
        userids.add(getUserid());

//        GroupChannel.createChannelWithUserIds(userids, false, new GroupChannel.GroupChannelCreateHandler() {
//            @Override
//            public void onResult(GroupChannel groupChannel, SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    return;
//                }
//            }
//        });
    }

    public void makePost (Post post) {
        // Server
        final JsonObject json = new JsonObject();
        json.addProperty("title", post.getTitle());
        json.addProperty("desc", post.getDescription());
        json.addProperty("image", post.getImage());
        json.addProperty("userid", post.getUserid());
        json.addProperty("username", post.getUsername());
        json.addProperty("timestamp", post.getTimestamp());

        Log.v("************make", json.get("timestamp").getAsString());
        new Thread() {
            public void run() {
                Ion.with(context)
                        .load("https://infinite-stream-97401.herokuapp.com/post")
                        .setHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                            }
                        });
            }
        }.start();


    }

    private void postReceivedListener(JsonObject json) {
        newsFeedFragment.postsArrivedCallback(json);
    }

    public List<Post> getPosts (NewsFeedFragment newsFeedFragment) {
        // Check if server available otherwise return local
        this.newsFeedFragment = newsFeedFragment;
        final JsonObject json = new JsonObject();

        new Thread() {
            public void run() {
                Ion.with(context)
                        .load("https://infinite-stream-97401.herokuapp.com/get")
                        .setHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                postReceivedListener(result);
                            }
                        });
            }
        }.start();

        return localPosts;
    }

    public String getUserid () {
        return android_id;
    }
}
