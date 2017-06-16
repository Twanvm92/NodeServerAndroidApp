package twanvm.movieapp;

public class Constants {
    // API
//    private static final String BASIC_URL = "https://programmeren14dag1.herokuapp.com/api/v1";
    // for testing on localhost
    private static final String BASIC_URL = "http://192.168.1.92:8000/api/v1";
    public static final String URL_LOGIN = BASIC_URL + "/login";
    public static final String URL_REGISTER = BASIC_URL + "/register";
    public static final String URL_FILMS =  BASIC_URL + "/films";
    public static final String URL_RENTED_FILMS =  BASIC_URL + "/rentals/";
    public static final String URL_INVENTORY_FILMS =  BASIC_URL + "/films/";

}
