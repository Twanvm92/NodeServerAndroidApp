package twanvm.movieapp.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by twanv on 16-6-2017.
 */

public class InventoryFilmMaker {
    public static final String FILM_ID = "film_id";
    public static final String FILM_TITLE = "title";
    public static final String FILM_DESCRIPTION = "description";
    public static final String FILM_RELEASE_YEAR = "release_year";
    public static final String FILM_RENTAL_RATE = "rental_rate";
    public static final String FILM_LENGTH = "length";
    public static final String FILM_RATING = "rating";
    public static final String FILM_INVENTORY_ID = "inventory_id";
    public static final String FILM_RENTAL_DATE = "rental_date";
    public static final String FILM_RETURN_DATE = "return_date";

    public static ArrayList<Film> makeInventoryFilmList(JSONArray response){

        ArrayList<Film> filmList = new ArrayList<>();

        try{

            for(int i = 0; i < response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);

                String returnDate = jsonObject.getString(FILM_RETURN_DATE);
                String rentalDate = jsonObject.getString(FILM_RENTAL_DATE);
                boolean rented = false;

                if(returnDate.equals("null") && !rentalDate.equals("null")) {
                    rented = true;
                }
                if(jsonObject.optInt(FILM_INVENTORY_ID) != 0) {
                    InventoryFilm film = new InventoryFilm(
                            jsonObject.getInt(FILM_ID),
                            jsonObject.getString(FILM_TITLE),
                            jsonObject.getString(FILM_DESCRIPTION),
                            jsonObject.getInt(FILM_RELEASE_YEAR),
                            jsonObject.getDouble(FILM_RENTAL_RATE),
                            jsonObject.getInt(FILM_LENGTH),
                            jsonObject.getString(FILM_RATING),
                            jsonObject.optInt(FILM_INVENTORY_ID),
                            rented);

                    filmList.add(film);
                }



            }
        } catch( JSONException ex) {
            Log.e("InventoryFilmMaker", "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
        return filmList;
    }
}
