package ivanc.swap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public ServerConnection(HomeActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        localPosts = new ArrayList<>();
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
        JsonObject json = new JsonObject();
        json.addProperty("title", post.getTitle());
        json.addProperty("desc", post.getDescription());
        json.addProperty("image", post.getImage());
        json.addProperty("userid", post.getUserid());

        Ion.with(context)
                .load("https://damp-tundra-41875.herokuapp.com/post")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                    }
                });
    }

    private void postReceivedListener(JsonObject json) {
        newsFeedFragment.postsArrivedCallback(json);
    }

    public List<Post> getPosts (NewsFeedFragment newsFeedFragment) {
        // Check if server available otherwise return local
        this.newsFeedFragment = newsFeedFragment;
        JsonObject json = new JsonObject();

        Ion.with(context)
                .load("https://damp-tundra-41875.herokuapp.com/get")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        postReceivedListener(result);
                    }
                });
        return localPosts;
    }

    public String getUserid () {
        return android_id;
    }
}
