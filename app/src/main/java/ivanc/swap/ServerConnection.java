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

/**
 * Created by ivanc on 12/1/2016.
 */

public class ServerConnection extends AppCompatActivity {
    private HomeActivity activity;

    private List<Post> localPosts;
    private Context context;
    private NewsFeedFragment newsFeedFragment;


    public ServerConnection(HomeActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        localPosts = new ArrayList<>();
    }

    public void makePost (Post post) {
        // Server
        JsonObject json = new JsonObject();
        json.addProperty("title", post.getTitle());
        json.addProperty("desc", post.getDescription());
        json.addProperty("image", post.getImage());

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
}
