package phone.nikolas.com.rxjavasample.service;

import java.util.List;

import phone.nikolas.com.rxjavasample.model.EmailListResponse;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Pleret on 3/7/2017.
 */

public interface SampleService {
    @GET("emails")
    Observable<EmailListResponse> getEmails();
}
