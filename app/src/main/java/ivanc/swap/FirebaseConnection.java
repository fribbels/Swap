package ivanc.swap;

import android.app.Activity;
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

/**
 * Created by ivanc on 12/1/2016.
 */

public class FirebaseConnection extends AppCompatActivity {
    private String mUsername;
    private Firebase ref; // experimental
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private HomeActivity activity;
//
    private DatabaseReference mFirebaseDatabaseReference;

    public static final String MESSAGES_CHILD = "Posts";

    private List<Post> localPosts;


    public FirebaseConnection (HomeActivity activity) {
        this.activity = activity;
        Firebase.setAndroidContext(activity);
        this.ref = new Firebase("https://swap-aa672.firebaseio.com/");
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
            }
        };
        ref.authAnonymously(authResultHandler);
        configureFirebase();
    }

    private void configureFirebase () {
        localPosts = new ArrayList<>();

//         Firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        // Anonymous sign in
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("*****************", "signInAnonymouslyFailed", task.getException());
                        } else {
                            Log.v("*****************", "DONE");
                        }
                    }
                });

        // Add message listener
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                localPosts = new ArrayList<Post>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> data = (Map)snapshot.getValue();
                    String text = data.get("title");

                    Log.v("***********", "New post: " + data);

                    Post post = new Post(text);
                    localPosts.add(post);
                }
                activity.updatePosts(localPosts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void makePost (Post post) {
        String key = mFirebaseDatabaseReference.push().getKey();
        mFirebaseDatabaseReference.child(key).setValue(post);
    }

    public List<Post> getPosts () {
        return localPosts;
    }

    public void onMainStart() {
//        if (mFirebaseAuth == null)
//            mFirebaseAuth = FirebaseAuth.getInstance();
//
//        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void onMainStop() {
//        if (mAuthListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthListener);
//        }
    }
}
