package app.tfkproject.onfish.util.sekitaran;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by taufik on 18/04/18.
 */

public interface RetrofitMapsSekitaran {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk") //api key tidak tau punya siapa, tapi yang jelas jangan diubah, ini udah working
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
