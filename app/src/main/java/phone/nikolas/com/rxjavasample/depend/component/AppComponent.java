package phone.nikolas.com.rxjavasample.depend.component;


import phone.nikolas.com.rxjavasample.activity.registrationdemo.RegistrationDemoActivity;
import phone.nikolas.com.rxjavasample.depend.module.NetworkModule;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Pleret on 3/9/2017.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {
    void inject(RegistrationDemoActivity registrationDemoActivity);
}
