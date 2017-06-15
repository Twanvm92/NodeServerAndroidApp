package twanvm.movieapp;

/**
 * Created by twanv on 13-6-2017.
 */

public class Constants {
    // API
    private static final String BASIC_URL = "http://192.168.43.198:3306";
    public static final String URL_LOGIN = BASIC_URL + "/api/v1/login";
    public static final String URL_REGISTER = BASIC_URL + "/api/v1/register";
    public static final String URL_FILMS =  BASIC_URL + "/api/v1/films";
    public static final String URL_RENTED_FILMS =  BASIC_URL + "/api/v1/rentals/";
    public static final int TOKEN_REQUIRED = 1;
    public static final int REGISTERED = 2;
    public static final int CANCELED = 3;

}
