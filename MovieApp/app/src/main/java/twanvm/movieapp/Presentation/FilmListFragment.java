package twanvm.movieapp.Presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.io.Serializable;
import java.util.ArrayList;

import twanvm.movieapp.API.FilmAPIRequest;
import twanvm.movieapp.R;
import twanvm.movieapp.adapter.FilmAdapter;
import twanvm.movieapp.domain.Film;
import twanvm.movieapp.domain.RentedFilm;

public class FilmListFragment extends Fragment implements FilmAPIRequest.FilmAPIListener {

    public final String TAG = this.getClass().getSimpleName();
    private ListView listViewFilms;
    private ArrayAdapter filmAdapter;
    private ArrayList<Film> films = new ArrayList<>();
    private ArrayList<Integer> filmCountList;
    private ArrayAdapter<Integer> filmCountAdapter;
    private int count, offset = 0;
    private EditText offsetEditText;
    private InputMethodManager imm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);
        listViewFilms = (ListView) view.findViewById(R.id.fragment_film_ListView);
        filmAdapter = new FilmAdapter(getContext(), films);
        listViewFilms.setAdapter(filmAdapter);
        listViewFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), FilmDetailActivity.class);
                Film film = films.get(position);
                i.putExtra("Film", (Serializable) film);
                startActivity(i);
            }
        });

        filmCountList = new ArrayList<Integer>();
        filmCountList.add(1);
        filmCountList.add(5);
        filmCountList.add(10);
        filmCountList.add(20);
        filmCountList.add(50);
        Spinner filmCountSpinner = (Spinner) view.findViewById(R.id.fragment_film_spinner_count);
        filmCountAdapter = new ArrayAdapter<Integer>(getContext(),
                android.R.layout.simple_spinner_item, filmCountList) {
        };
        filmCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filmCountSpinner.setAdapter(filmCountAdapter);
        filmCountSpinner.setSelection(2);
        filmCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                count = Integer.parseInt(parent.getSelectedItem().toString());
                if (!offsetEditText.getText().toString().equals("")) {
                    try {
                        offset = Integer.parseInt(offsetEditText.getText().toString());
                    } catch (NumberFormatException e){
                            Log.e(TAG, e.toString());
                        offset = 0;
                        }
                } else {
                    offset = 0;
                }
                getFilms(count, offset);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        offsetEditText = (EditText) view.findViewById(R.id.fragment_film_et_offset);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        offsetEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    if (!offsetEditText.getText().toString().equals("")) {
                        try {
                            if (offset != Integer.parseInt(offsetEditText.getText().toString())) {
                                offset = Integer.parseInt(offsetEditText.getText().toString());
                            } else {
                                return true;
                            }
                        } catch (NumberFormatException e){
                            Log.e(TAG, e.toString());
                        }
                    } else {
                        offset = 0;
                    }
                    getFilms(count, offset);
                    return true;
                }
                return false;
            }
        });
        return view;


    }

    @Override
    public void onFilmsAvailable(ArrayList<Film> filmArrayList) {

        Log.i(TAG, "We have " + filmArrayList.size() + "  films in our list");

        films.clear();
        for(int i = 0; i < filmArrayList.size(); i++) {
            films.add(filmArrayList.get(i));
        }
        filmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRentedFilmsAvailable(ArrayList<RentedFilm> rentedFilms) {

    }

    @Override
    public void handleResponseError(VolleyError error) {
        Log.e(TAG, error.toString());
    }

    @Override
    public void handleLoginNeeded(boolean loginNeeded) {

    }

    @Override
    public void isFilmReturned(boolean filmReturned) {

    }

    /**
     * Get films with amount + index start
     */
    private void getFilms(int count, int offset){
        FilmAPIRequest request = new FilmAPIRequest(getContext(), this);
        request.handleGetFilms(count, offset);
    }

}

