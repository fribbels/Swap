package ivanc.swap;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import java.util.List;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
                   AboutFragment.OnFragmentInteractionListener,
                   NewsFeedFragment.OnFragmentInteractionListener,
                   ChatFragment.OnFragmentInteractionListener,
                   NewPostFragment.OnFragmentInteractionListener,
                   ProfileFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private NewsFeedFragment newsFeedFragment;
    private ChatFragment chatFragment;
    private AboutFragment aboutFragment;
    private ProfileFragment profileFragment;
    private NewPostFragment newPostFragment;

    private ServerConnection serverConnection;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverConnection = new ServerConnection(this, getApplicationContext());
        setContentView(R.layout.activity_home);
//        initializeFragments();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.gradientbar);
        BitmapDrawable actionBarBackground = new BitmapDrawable(getResources(), bMap);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(actionBarBackground);
    }
//
//    public void initializeFragments() {
//        newsFeedFragment = new NewsFeedFragment();
//        chatFragment = new ChatFragment();
//        aboutFragment = new AboutFragment();
//        profileFragment = new ProfileFragment();
//        newPostFragment = new NewPostFragment();
//    }
    public void updatePosts(List<Post> posts) {
        newsFeedFragment.updatePosts(posts);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment oldFragment = currentFragment;
        switch (position) {
            case 0:
                fragment = new NewsFeedFragment(); //new NewsFeedFragment();
                newsFeedFragment = (NewsFeedFragment)fragment;
                newsFeedFragment.setupFirebaseConnection(serverConnection);
                break;
            case 1:
                fragment = new ProfileFragment(); //new ProfileFragment();
                profileFragment = (ProfileFragment)fragment;
                break;
            case 2:
                fragment = new NewPostFragment();
                newPostFragment = (NewPostFragment) fragment;
                newPostFragment.setupFirebaseConnection(serverConnection);
                break;
            case 3:
                fragment = new ChatFragment(); //new ChatFragment();
                chatFragment = (ChatFragment)fragment;
                break;
            case 4:
                fragment = new AboutFragment();
                aboutFragment = (AboutFragment)fragment;
                break;
            default:
        }

        currentFragment = fragment;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(null);
        if (oldFragment != null)
            ft.hide(oldFragment);
        ft.add(R.id.container, fragment);
        ft.commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "SEC1";//getString(R.string.title_section1);
                break;
            case 2:
                mTitle = "SEC2";//getString(R.string.title_section2);
                break;
            case 3:
                mTitle = "SEC3";//getString(R.string.title_section3);
                break;
            case 4:
                mTitle = "SEC4";//getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
