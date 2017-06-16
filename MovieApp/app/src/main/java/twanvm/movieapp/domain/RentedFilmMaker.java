package twanvm.movieapp.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RentedFilmMaker {

    public static final String FILM_ID = "film_id";
    public static final String FILM_TITLE = "title";
    public static final String FILM_DESCRIPTION = "description";
    public static final String FILM_RELEASE_YEAR = "release_year";
    public static final String FILM_RENTAL_RATE = "rental_rate";
    public static final String FILM_LENGTH = "length";
    public static final String FILM_RATING = "rating";
    public static final String RENTED_FILM_INVENTORY_ID = "inventory_id";


    public static ArrayList<RentedFilm> makeRentedFilmList(JSONArray response){

        ArrayList<RentedFilm> rentedFilmList = new ArrayList<>();

        try{

            for(int i = 0; i < response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);

                RentedFilm rentedFilm = new RentedFilm(
                        jsonObject.getInt(FILM_ID),
                        jsonObject.getString(FILM_TITLE),
                        jsonObject.getString(FILM_DESCRIPTION),
                        jsonObject.getInt(FILM_RELEASE_YEAR),
                        jsonObject.getDouble(FILM_RENTAL_RATE),
                        jsonObject.getInt(FILM_LENGTH),
                        jsonObject.getString(FILM_RATING),
                        jsonObject.getInt(RENTED_FILM_INVENTORY_ID)
                );
                rentedFilmList.add(rentedFilm);
            }
        } catch( JSONException ex) {
            Log.e("RentedFilmMaker", "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
        return rentedFilmList;
    }
}
