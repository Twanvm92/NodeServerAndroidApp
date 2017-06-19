package twanvm.movieapp.Presentation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilities {

    public static void displayMessage(Context context, String toastString){
        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
    }

    public static String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

}
