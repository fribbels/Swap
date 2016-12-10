package ivanc.swap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private PostListAdapter postListAdapter;
    private Context context;
    private ListView listView;
    private List<Post> posts;
    private ServerConnection serverConnection;
    private HomeActivity homeActivity;

    private static final int REQUEST_SENDBIRD_MESSAGING_ACTIVITY = 200;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;


//        this.posts = posts;
    }

    public void updatePosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);

        listView.setAdapter(new PostListAdapter(this.context, this.posts));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged(); // Uh?
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<>();
        postListAdapter = new PostListAdapter(this.context, this.posts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        listView = (ListView) rootView.findViewById(R.id.postListView);
        listView.setAdapter(this.postListAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Log.v("***************** pos", "" + position);
//                Log.v("***************** ID", "" + posts.get(position).getUserid());
//
//                String otherId = posts.get(position).getUserid();
//                String appId = "6A2C5712-870F-4487-AB31-3EF97B040807";
//                String userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//                Intent intent = new Intent(homeActivity, SendBirdMessagingActivity.class);
//                Bundle args = SendBirdMessagingActivity.makeMessagingStartArgs(appId, userId, userId, new String[]{otherId});
//                intent.putExtras(args);
//
//                startActivityForResult(intent, REQUEST_SENDBIRD_MESSAGING_ACTIVITY);
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//
                AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
                helpBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                            }
                        });
                Display display = homeActivity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                // Remember, create doesn't show the dialog
                ViewDialog alert = new ViewDialog();
                alert.showDialog(getActivity(), posts.get(position));
                //alert.getWindow().setLayout((int)(width-width*0.05), (int)(height-height*0.05));
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void onResume() {
        super.onResume();
        this.posts = serverConnection.getPosts(this);
        postListAdapter = new PostListAdapter(this.context, this.posts);
        listView.setAdapter(postListAdapter);
        listView.requestLayout();
        postListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    public void setupFirebaseConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.posts = serverConnection.getPosts(this);
    }

    public void initialize (HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public void postsArrivedCallback (JsonObject json) {

        JsonArray posts = json.getAsJsonArray("posts");
        List<Post> tempList = new ArrayList<>();

        for (int i = 0; i < posts.size(); i++) {
            JsonObject post = posts.get(i).getAsJsonObject();

            String title = post.get("title").getAsString();
            String desc = post.get("desc").getAsString();
            String userid = post.get("userid").getAsString();
            String image = post.get("image").getAsString();
            String username = post.get("username").getAsString();
            tempList.add(new Post(title, desc, image, userid, username));
        }

        this.posts.clear();
        this.posts.addAll(tempList);
        postListAdapter.notifyDataSetChanged();
    }
    /**
     * 
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
