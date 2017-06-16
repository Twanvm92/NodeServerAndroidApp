package twanvm.movieapp.domain;

import java.io.Serializable;

/**
 * Created by twanv on 16-6-2017.
 */

public class RentedFilm extends Film implements Serializable {
    private int inventory_id;

    public RentedFilm(int id, String title, String description,
                      int release_year, double rental_rate, int length, String rating, int inventory_id) {
        super(id, title, description, release_year, rental_rate, length, rating);
        this.inventory_id = inventory_id;
    }

    public int getInventory_id() {
        return inventory_id;
    }
}
