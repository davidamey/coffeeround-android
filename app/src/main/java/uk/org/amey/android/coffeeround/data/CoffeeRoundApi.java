package uk.org.amey.android.coffeeround.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uk.org.amey.android.coffeeround.data.model.TokenResponse;
import uk.org.amey.android.coffeeround.data.model.User;

public interface CoffeeRoundApi {

    @FormUrlEncoded
    @POST("login")
    Call<TokenResponse> login(@Field("idToken") String idToken);

    @GET("user")
    Call<List<User>> getUsers();

    @GET("user/{username}")
    Call<User> getUserDetails(@Path("username") String username);

}
