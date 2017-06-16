package twanvm.movieapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.RentedFilm;

public class FilmRentedAdapter extends ArrayAdapter<RentedFilm> implements FilmAPIRequest.FilmAPIListener{
    public FilmRentedAdapter(Context context, ArrayList<RentedFilm> rentedFilms){
        super(context, 0, rentedFilms);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        final RentedFilm film = getItem(position);

        if (convertview == null){
            convertview = LayoutInflater.from(getContext()).inflate(R.layout.listview_fragment_rented_film_list, parent, false);
        }

        TextView name = (TextView) convertview.findViewById(R.id.fragRentedFilm_lv_name);
        name.setText(film.getTitle());

        TextView inventory_id = (TextView) convertview.findViewById(R.id.fragRentedFilm_lv_inventory_id);
        inventory_id.setText(String.valueOf(film.getInventory_id()));

        Button returnBtn = (Button) convertview.findViewById(R.id.fragRentedFilm_lv_returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            //return film
                returnRentedFilms(film.getInventory_id());
            }
        });

        return convertview;
    }

    private void returnRentedFilms(int inventoryID){
        FilmAPIRequest request = new FilmAPIRequest(getContext(), this);
        request.handleReturnRentedFilms(inventoryID);
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
    public void isFilmReturned(boolean filmReturned) {

    }
}