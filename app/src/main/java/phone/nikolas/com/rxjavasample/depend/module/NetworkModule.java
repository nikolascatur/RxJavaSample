package phone.nikolas.com.rxjavasample.depend.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import phone.nikolas.com.rxjavasample.BuildConfig;
import phone.nikolas.com.rxjavasample.R;
import phone.nikolas.com.rxjavasample.service.SampleService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pleret on 3/8/2017.
 */

@Module
public class NetworkModule {

    public NetworkModule(){

    }

    //create interceptor to debug callback return from server
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    @Named("loggingInterceptor")
    public HttpLoggingInterceptor providesLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    @Named("baseOkHttp3")
    public OkHttpClient provideHttpClient(@Named("loggingInterceptor") HttpLoggingInterceptor interceptor){
        int timeout = 3;
        return new okhttp3.OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout,TimeUnit.SECONDS)
                .connectTimeout(timeout,TimeUnit.SECONDS)
                .build();
    }

    /*@Provides
    @Singleton
    @SuppressWarnings("unused")
    @Named("gson")
    public Gson provideGson(){
        return new GsonBuilder().create();
    }*/

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    @Named("baseRetrofit")
    public Retrofit provideBaseRetrovit(@Named("baseOkHttp3") OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    @Named("service")
    public SampleService service (@Named("baseRetrofit") Retrofit retrofit){
        return retrofit.create(SampleService.class);
    }

}
