package twanvm.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import twanvm.movieapp.R;
import twanvm.movieapp.domain.Film;

/**
 * Created by Maikel on 14-6-2017.
 */

public class FilmAdapter extends ArrayAdapter<Film> {
    public FilmAdapter(Context context, ArrayList<Film> meldingen){
        super(context, 0, meldingen);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        Film film = getItem(position);

        if (convertview == null){
            convertview = LayoutInflater.from(getContext()).inflate(R.layout.listview_fragment_film_list, parent, false);
        }

        TextView name = (TextView) convertview.findViewById(R.id.fragFilm_lwRow);
        name.setText(film.getTitle());

        return convertview;


    }
}