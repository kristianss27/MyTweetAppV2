package com.cristiansanchez.mytweetapp.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.RestApplication;
import com.cristiansanchez.mytweetapp.RestClient;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kristianss27 on 10/30/16.
 */
public class ComposeTweetFragment extends DialogFragment {

    private RestClient client;
    private EditText etTweet;
    private TextView tvCharacterCounter;
    private Tweet tweet;
    private Button btnComposeTweet;


    public ComposeTweetFragment(){

    }

    public static ComposeTweetFragment newInstance() {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        /*Bundle args = new Bundle();
        args.putString("tweet", tweet);
        frag.setArguments(args);*/
        return frag;
    }




    public interface ComposeTweeDialogListener {
        void onFinishComposeDialog(Tweet tweet);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        client = RestApplication.getRestClient(); //singleton client
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return inflater.inflate(R.layout.fragment_compose_tweet, container);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        tvCharacterCounter = (TextView) view.findViewById(R.id.tvCharacterCounter);
        btnComposeTweet = (Button) view.findViewById(R.id.btSendTweet);
        btnComposeTweet.setEnabled(false);

        btnComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.composeTweet(etTweet.getText().toString(), null,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        ComposeTweeDialogListener listener = (ComposeTweeDialogListener) getActivity();

                        Log.d("COMPOSE TWEET","Compose tweet response: "+response);
                        try {
                            Log.d("COMPOSE TWEET","ITS HERE");
                            tweet = new Tweet(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listener.onFinishComposeDialog(tweet);
                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("ComposeTweetFragment","Tweet failed");
                    }

                });
            }
        });

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this will show characters remaining
                int counter = 140 - s.toString().length();
                tvCharacterCounter.setText(Integer.toString(counter));
                if (counter < 0) {
                    tvCharacterCounter.setTextColor(Color.RED);
                    btnComposeTweet.setEnabled(false);
                }
                else {
                    btnComposeTweet.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //getDialog().setTitle(R.string.compose_tweet_fragment);
        // Show soft keyboard automatically and request focus to field

        //etTweet.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 1), (int) (size.y * 1));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }


}
