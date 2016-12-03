package uk.org.amey.android.coffeeround.data;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class CoffeeRoundClientGenerator {
//    private static final String API_BASE_URL = "https://coffeeround.herokuapp.com";
    private static final String API_BASE_URL = "http://10.241.28.90:5000";

    private static CoffeeRoundApi client;
    private static JWTInterceptor jwtInterceptor = new JWTInterceptor();

    private CoffeeRoundClientGenerator() {
        // Prevent instantiation
    }

    public static CoffeeRoundApi getClient() {
        if (client == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(jwtInterceptor);
            httpClient.addInterceptor(new MockingInterceptor());

//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .addHeader("Accept", "application/json")
//                            .header("Authorization", "Bearer " + idToken)
//                            .method(original.method(), original.body());
//
//                    return chain.proceed(requestBuilder.build());
//                }
//            });

            Retrofit retrofit = builder.client(httpClient.build()).build();
            client = retrofit.create(CoffeeRoundApi.class);
        }

        return client;
    }

    public static void setJWT(String jwt) {
        jwtInterceptor.setJWT(jwt);
    }
}