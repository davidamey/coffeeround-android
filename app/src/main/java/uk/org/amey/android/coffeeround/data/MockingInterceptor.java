package uk.org.amey.android.coffeeround.data;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String data = "";

        if (chain.request().url().encodedPath().contains("login")) {
            data = "{\"token\":\"abc\"}";
        } else {
            data = "[{\"name\": \"Bob\"}]";
        }

        String authHeader = chain.request().header("Authorization");

        Response response = new Response.Builder()
                .code(200)
                .message(data)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(
                        MediaType.parse("application/json"),
                        data.getBytes()
                ))
                .addHeader("content-type", "application/json")
                .build();

        return response;
    }
}
