package uk.org.amey.android.coffeeround.data;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

final class JWTInterceptor implements Interceptor {

    private volatile String jwt;

    public void setJWT(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (jwt != null && jwt.length() > 0) {
            Request.Builder requestBuilder = request.newBuilder()
                    .addHeader("Accept", "application/json")
                    .header("Authorization", "Bearer " + jwt)
                    .method(request.method(), request.body());

            request = requestBuilder.build();
        }

        return chain.proceed(request);
    }

}