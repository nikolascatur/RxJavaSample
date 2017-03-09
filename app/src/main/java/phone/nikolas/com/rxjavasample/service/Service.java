package phone.nikolas.com.rxjavasample.service;

import java.util.List;

import phone.nikolas.com.rxjavasample.model.EmailListResponse;
import rx.Observable;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Pleret on 3/7/2017.
 */

public class Service {
    private final SampleService sampleService;

    public Service(SampleService sampleService){
        this.sampleService = sampleService;
    }


    public Subscription checkEmailExist(final getEmailsListCallBack callBack){
        return sampleService.getEmails()
               .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends EmailListResponse>>() {
                    @Override
                    public Observable<? extends EmailListResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<EmailListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed();
                    }

                    @Override
                    public void onNext(EmailListResponse emailListResponse) {
                        callBack.onSuccess(emailListResponse);
                    }

                });
    }



    public interface getEmailsListCallBack {
        void onSuccess(EmailListResponse response);
        void onFailed();
    }

}
