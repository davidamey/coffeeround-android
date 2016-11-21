package uk.org.amey.android.coffeeround.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class CoffeeRoundClientGenerator {
    private static final String API_BASE_URL = "https://coffeeround.herokuapp.com";

    private static CoffeeRoundApi client;

    private CoffeeRoundClientGenerator() {
        // Prevent instantiation
    }

    public static CoffeeRoundApi getClient() {
        if (client == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient()).build();

            client = retrofit.create(CoffeeRoundApi.class);
        }

        return client;
    }
}
