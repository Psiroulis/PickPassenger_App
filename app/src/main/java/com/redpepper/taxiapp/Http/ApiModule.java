package com.redpepper.taxiapp.Http;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    //Mac Server Url
    //public final String BASE_URL = "http://192.168.1.6:8000/";

    //Laptop Server Url
    //public final String BASE_URL = "http://192.168.1.9:8000/";

    //Digital Ocean Url
    public final String BASE_URL = "http://pick-rides.eu/";


    @Provides
    public OkHttpClient provideHttpClient(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("Accept", "application/json")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

    }

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    public PassengerApiService provideLoginSignUpService(){
        return provideRetrofit(BASE_URL, provideHttpClient()).create(PassengerApiService.class);
    }
}
