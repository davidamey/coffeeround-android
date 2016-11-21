package uk.org.amey.android.coffeeround.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import uk.org.amey.android.coffeeround.data.model.User;

public interface CoffeeRoundApi {

    @GET("user")
    Call<List<User>> getUsers();

    @GET("user/{username}")
    Call<User> getUserDetails(@Path("username") String username);

}
