package ivanc.swap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ServerConnection serverConnection;

    private EditText newPostTitleEditText;
    private EditText newPostDescEditText;
    private Button newPostSubmitButton;
    private Button getImageButton;

    private String currentImage = "";

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private HomeActivity homeActivity;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance() {
        NewPostFragment fragment = new NewPostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        newPostTitleEditText = (EditText)getView().findViewById(R.id.new_post_title);
        newPostDescEditText = (EditText)getView().findViewById(R.id.new_post_description);
        newPostSubmitButton = (Button)getView().findViewById(R.id.new_post_submit);
        getImageButton = (Button)getView().findViewById(R.id.get_image_button);

        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(newPostTitleEditText.getWindowToken(), 0);

        newPostTitleEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(newPostTitleEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return handled;
            }
        });


        newPostDescEditText.setHorizontallyScrolling(false);
        newPostDescEditText.setMaxLines(Integer.MAX_VALUE);
        newPostDescEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(newPostTitleEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return handled;
            }
        });

        newPostSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            String title = newPostTitleEditText.getText().toString();
            String desc = newPostDescEditText.getText().toString();

            String timestamp = DateUtils.getTimestamp();

            serverConnection.makePost(new Post(title, desc, currentImage, serverConnection.getUserid(), homeActivity.getUsername(), timestamp));
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(newPostTitleEditText.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(newPostDescEditText.getWindowToken(), 0);


            new AlertDialog.Builder(homeActivity)
                .setTitle("Post complete")
                .setMessage("Your post has been submitted!")
                .setPositiveButton("Back to main screen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        homeActivity.goBackToHomeScreen();
                    }
                })
                .show();

            }

        });

        getImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , REQUEST_IMAGE_GALLERY);//one can be replaced with any action code
            }
        });
    }

    public void initialize (HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageView = (ImageView)getView().findViewById(R.id.new_post_image);
        switch(requestCode) {
            case REQUEST_IMAGE_GALLERY:
                if(resultCode == RESULT_OK){
                    final Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);

                    final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                            "cloud_name", "dg9xrbtal",
                            "api_key", "741985358596817",
                            "api_secret", "VltjoXVriCpOEdNJurs-NPD8XsU"));

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                InputStream in = homeActivity.getContentResolver().openInputStream(selectedImage);
                                Map uploadResult = cloudinary.uploader().upload(in, ObjectUtils.emptyMap());
                                currentImage = (String)uploadResult.get("url");
                            } catch (IOException e) {

                            }
                        }
                    };

                    new Thread(runnable).start();
                    currentImage = "";
                }
                break;
        }
    }


    public void setupFirebaseConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
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
