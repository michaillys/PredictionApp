package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.SecureStorage;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClientWithAuth {

    private static final String BASE_URL = "http://localhost:9090";

    public static Retrofit getClient() {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String token = SecureStorage.getToken();

                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer "+token)
                                .build();

                        return chain.proceed(request);
                    }
                }).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}
