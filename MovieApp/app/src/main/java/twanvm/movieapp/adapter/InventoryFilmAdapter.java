package twanvm.movieapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.InventoryFilm;
import twanvm.movieapp.domain.RentedFilm;

/**
 * Created by twanv on 16-6-2017.
 */

public class InventoryFilmAdapter extends ArrayAdapter<Film> implements FilmAPIRequest.FilmAPIListener {
    private Context context;
    private InventoryFilm film;
    private InvenFilmAdapterListener adapterListener;

    public InventoryFilmAdapter(Context context, ArrayList<Film> invenFilms,
                                InvenFilmAdapterListener adapterListener) {
        super(context, 0, invenFilms);
        this.context = context;
        this.adapterListener = adapterListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        film = (InventoryFilm) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_detailed_film_row, parent, false);
        }

        TextView inventoryidTV = (TextView) convertView.findViewById(R.id.activityDetailedMovie_listview_tv_itemText);
        String filmID = Integer.toString(film.getInventory_id());
        inventoryidTV.setText(filmID);

        int filmIdint = film.getInventory_id();
        Button rentBtn = (Button) convertView.findViewById(R.id.activityDetailedMovie_lv_btn);
        rentBtn.setTag(filmIdint);
        Log.i("Inventory_id: ", ""+ film.getInventory_id());
        if(film.isRented()) {
            rentBtn.setText(convertView.getResources().getString(R.string.title_film_btn_unavailable));
            rentBtn.setEnabled(false);
        } else {
            rentBtn.setText(convertView.getResources().getString(R.string.title_film_btn_available));
//            rentBtn.setEnabled(true);
        }

//        if (rentBtn.isEnabled()) {
            rentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int filmID = (int) v.getTag();
                    rentFilm(filmID);
                    Log.i("Invent_id click:  ", ""+ film.getInventory_id());
                }
            });
//        }
        return convertView;
    }

    private void rentFilm(int inventoryID){
        FilmAPIRequest request = new FilmAPIRequest(context, this);
        request.handleRentFilm(inventoryID);
    }

    @Override
    public void handleLoginNeeded(boolean loginNeeded) {
        if (loginNeeded) {
            adapterListener.handleLoginNeededAdapter(true);
        }
    }

    @Override
    public void isFilmReturned(boolean filmReturned) {

    }

    @Override
    public void onFilmsAvailable(ArrayList<Film> films) {

    }

    @Override
    public void onRentedFilmsAvailable(ArrayList<RentedFilm> rentedFilms) {

    }

    @Override
    public void handleResponseError(VolleyError error) {

    }

    @Override
    public void isFilmRented(boolean filmRented) {
        if (filmRented) {
            adapterListener.isFilmRentedAdapter(filmRented);
        }
    }

    public interface InvenFilmAdapterListener {
        void isFilmRentedAdapter(boolean filmReturned);
        void handleLoginNeededAdapter(boolean loginNeeded);
    }
}
