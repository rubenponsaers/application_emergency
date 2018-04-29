package com.example.android.emergency.data;

import android.os.AsyncTask;

import com.example.android.emergency.MainActivity;
import com.example.android.emergency.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Ruben on 29/04/2018.
 */

public class fetchData extends AsyncTask<URL,Void,String> {

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl = params[0];
        String githubSearchResults = null;
        String singleOutput = null;
        String allOutput = "";
        try {
            githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            JSONArray jsonArray = new JSONArray(githubSearchResults);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(jsonObject.get("city").toString().equals("Tongeren")) {
                    singleOutput = "City: " + jsonObject.get("city") + "\n" +
                             "Adres: " + jsonObject.get("adres") + "\n" +
                             "Medical practice: " + jsonObject.get("medical practice") + "\n";
                    allOutput = allOutput + singleOutput;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(allOutput == ""){
            allOutput = "No doctor on duty";
        }

        return allOutput;
    }

    @Override
    protected void onPostExecute(String githubSearchResults) {
        if (githubSearchResults != null && !githubSearchResults.equals("")) {
            MainActivity.doctorResult.setText(githubSearchResults);
        }
    }
}
