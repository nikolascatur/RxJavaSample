package phone.nikolas.com.rxjavasample;

import java.util.List;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Pleret on 2/23/2017.
 */

public interface SampleService {
    @GET("emails")
    Observable<List<String>> getEmails();

}
