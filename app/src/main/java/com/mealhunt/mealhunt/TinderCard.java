package com.mealhunt.mealhunt;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

@Layout(R.layout.tinder_card_view)
@NonReusable // bc we do not plan to reuse a view when rejected. memory gets released.
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameTxt)
    private TextView nameTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Profile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved() {
        if (mProfile == null) {
            Log.i("edittext", "profile itself came back null");
        }
        if (mProfile.getPhotos() == null) {
            Log.i("edittext", "photos array came back null");
        }
        if (mProfile.getPhotos().get(0) == null) {
            Log.i("edittext", "photos array sub 0 came back null");
        }
        if (mProfile.getPhotos().get(0).getPhotoReference() == null) {
            Log.i("edittext", "photo reference string came back null");
        }
        Glide.with(mContext).load(mProfile.getPhotos().get(0).getPhotoReference()).into(profileImageView);
        nameTxt.setText(mProfile.getName());// + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getVicinity());

    }

    @SwipeOut
    private void onSwipedOut() { // left swipe/reject
        Log.d("EVENT", "onSwipedOut");
        Log.i("edittext: resolved #: ",  Integer.toString(mSwipeView.getAllResolvers().size()));
        //mSwipeView.addView(this);
        if (mSwipeView.getAllResolvers().size() == 1) { // this user has finished swiping.
            TinderCard.MyAsynTask task5 = new TinderCard.MyAsynTask();
            String[] params = new String[2];
            params[0] = "https://web.njit.edu/~rs637/download/finished.php";
            params[1] = "don't need this.";
            task5.execute(params);
        }

    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() { // right swipe/accept
        Log.d("EVENT", "onSwipedIn");
        String restaurantName = mProfile.getName();
        String placeId = mProfile.getPlaceId();
        Log.i("EDITTEXT.. yes on ", restaurantName);

        TinderCard.MyAsynTask task = new TinderCard.MyAsynTask();
        String[] params = new String[2];
        params[0] = "https://web.njit.edu/~rs637/download/updateDb.php";
        params[1] = restaurantName + ":::" + placeId;
        task.execute(params);
        Log.i("edittext: resolved #: ",  Integer.toString(mSwipeView.getAllResolvers().size()));
        // for each yes, send to a PHP file that will store into a table
        // send name of restaurant to PHP to store in DB
        // php will check counts.. when it is >=2, retrieve the winning place's name, and delete the rows in SQL

        if (mSwipeView.getAllResolvers().size() == 1) { // this user has finished swiping.
            TinderCard.MyAsynTask task7 = new TinderCard.MyAsynTask();
            String[] params7 = new String[2];
            params[0] = "https://web.njit.edu/~rs637/download/finished.php";
            params[1] = "don't need this.";
            task7.execute(params7);
        }
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");

    }

    private class MyAsynTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            BufferedReader reader = null;
            HttpURLConnection conn = null;
            try {
                URL urlObj = new URL(params[0]);

                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(params[1]);
                wr.flush();

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    Log.i("edittext", "received HTTP OK");
                } else {
                    Log.i("editext", "did NOT receive HTTP OK");
                }

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                response = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    reader.close();
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception ex) {
                }
            }
            return response;
        }

    }
}