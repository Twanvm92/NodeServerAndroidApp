package twanvm.movieapp.domain;

import java.io.Serializable;

public class Film implements Serializable {
    protected int id;
    protected String title;
    protected String description;
    protected int release_year;
    protected double rental_rate;
    protected int length;
    protected String rating;

    public Film(int id, String title, String description, int release_year, double rental_rate, int length, String rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.release_year = release_year;
        this.rental_rate = rental_rate;
        this.length = length;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public double getRental_rate() {
        return rental_rate;
    }

    public void setRental_rate(double rental_rate) {
        this.rental_rate = rental_rate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}


