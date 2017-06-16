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

import java.util.ArrayList;
import java.util.List;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.InventoryFilm;

/**
 * Created by twanv on 16-6-2017.
 */

public class InventoryFilmAdapter extends ArrayAdapter<Film> {
    public InventoryFilmAdapter(@NonNull Context context, @NonNull ArrayList<Film> invenFilms) {
        super(context, 0, invenFilms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        InventoryFilm film = (InventoryFilm) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_detailed_film_row, parent, false);
        }

        TextView inventoryidTV = (TextView) convertView.findViewById(R.id.activityDetailedMovie_listview_tv_itemText);
        String filmID = Integer.toString(film.getInventory_id());
        inventoryidTV.setText(filmID);

        Button rentBtn = (Button) convertView.findViewById(R.id.activityDetailedMovie_lv_btn);
        Log.i("Inventory_id: ", ""+ film.getInventory_id());
        if(film.isRented()) {
            rentBtn.setText(convertView.getResources().getString(R.string.title_film_btn_unavailable));
            rentBtn.setEnabled(false);
        } else {
            rentBtn.setText(convertView.getResources().getString(R.string.title_film_btn_available));
            rentBtn.setEnabled(true);
        }

        if (rentBtn.isEnabled()) {
            rentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    FilmAPIRequest request = new FilmAPIRequest()
                }
            });
        }
        return convertView;
    }
}
