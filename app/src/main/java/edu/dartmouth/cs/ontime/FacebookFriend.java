package edu.dartmouth.cs.ontime;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookFriend {
    String facebookId;
    String name;
    String pictureUrl;
    boolean invitable;
    boolean available;
    boolean isValid;
    public enum Type {AVAILABLE, INVITABLE};

    public FacebookFriend(JSONObject jsonObject) {
        //
        //Parse the Facebook Data from the JSON object.
        //
        try {
                //parse /me/invitable_friend
                this.facebookId =  jsonObject.getString("id");
                this.name = jsonObject.getString("name");

                //Handle the picture data.
                JSONObject pictureJsonObject = jsonObject.getJSONObject("picture").getJSONObject("data");
                boolean isSilhouette = pictureJsonObject.getBoolean("is_silhouette");
                if (!isSilhouette) {
                    this.pictureUrl = pictureJsonObject.getString("url");

                } else {
                    this.pictureUrl = "";
                }


            isValid = true;
        } catch (JSONException e) {
            Log.w("#", "Warnings - unable to process FB JSON: " + e.getLocalizedMessage());
        }
    }
}