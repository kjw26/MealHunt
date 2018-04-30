package com.mealhunt.mealhunt;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";

    public static List<Profile> loadProfiles(Context context, String jsonAsString){
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            JSONObject json = new JSONObject(jsonAsString);
            JSONArray array = json.getJSONArray("results");

            List<Profile> profileList = new ArrayList<>();

            for(int i=0;i<array.length();i++){
                //JSONObject json_data = array.getJSONObject(i);
                //String name = json_data.getString("name");
                Profile profile = gson.fromJson(array.getString(i), Profile.class);
                profileList.add(profile);
            }
            return profileList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}