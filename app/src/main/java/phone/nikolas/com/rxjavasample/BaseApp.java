package phone.nikolas.com.rxjavasample;

import android.app.Application;

import java.io.File;

import phone.nikolas.com.rxjavasample.depend.component.AppComponent;

/**
 * Created by Pleret on 3/9/2017.
 */

public class BaseApp extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        File cacheFile = new File(getCacheDir(),"response");
        //setAppComponent(DaggerAppComponent);
    }


    public AppComponent getmAppComponent() {
        return mAppComponent;
    }

    public void setAppComponent(AppComponent mAppComponent) {
        this.mAppComponent = mAppComponent;
    }
}
