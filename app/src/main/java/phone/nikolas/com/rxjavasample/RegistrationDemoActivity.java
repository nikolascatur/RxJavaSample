package phone.nikolas.com.rxjavasample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jakewharton.rxbinding.widget.RxTextView;


import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.schedulers.Schedulers;

/**
 * Created by Pleret on 2/22/2017.
 */

public class RegistrationDemoActivity extends AppCompatActivity {
    EditText etEmail;
    TextView textEmailAlert; //text_email_alert
    EditText etPassword; //et_password
    TextView textPasswordAlert; //text_password_alert
    EditText etPasswordConfirmation; //et_password_confirmation
    TextView textPasswordConfirmationAlert; //text_password_confirmation_alert
    Button btnSubmit; //btn_submit

    Retrofit retrofit;

    SampleService service;

    //============================================================
    // Observer
    //============================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_demo);

        //initialize form
        etEmail = (EditText) findViewById(R.id.et_email);
        textEmailAlert = (TextView) findViewById(R.id.text_email_alert);
        etPassword = (EditText) findViewById(R.id.et_password);
        textPasswordAlert = (TextView) findViewById(R.id.text_password_alert);
        etPasswordConfirmation = (EditText) findViewById(R.id.et_password_confirmation);
        textPasswordConfirmationAlert = (TextView) findViewById(R.id.text_password_confirmation_alert);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        //inisialisasi default form
        textEmailAlert.setVisibility(View.GONE);
        textPasswordAlert.setVisibility(View.GONE);
        textPasswordConfirmationAlert.setVisibility(View.GONE);
        btnSubmit.setEnabled(false);

        //inisialisasi retrovit
        retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.base_url))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //initialisation service sample
        service = retrofit.create(SampleService.class);

        //====================================================================
        //OBSERVABLES
        //===================================================================

        //Return true jika email sudah dipakai
        Observable<Boolean> emailStream = RxTextView.textChanges(etEmail).
                map(new Func1<CharSequence, String>() {
                    public String call(CharSequence charSequence){
                        return  charSequence.toString();
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String input)
                    {
                        boolean val = (input.length() >3);
                        Log.d("rx","filter "+input+"  "+input.length()+" val "+val);
                        return (input.length() > 3);
                    }
                })
                .debounce(1000, TimeUnit.MILLISECONDS)
                .flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String input) {
                        Observable<Boolean> checkEmail = checkIfEmailExistFromApi(input);
                        Log.d("rx"," flatmap  "+checkEmail);
                        return checkEmail;
                    }
                });

        //return true jika panjang password kurang 6
        Observable<Boolean> passwordStream = RxTextView.textChanges(etPassword)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence) && charSequence.toString().length() < 6 ;
                    }
                });

        //check apakah password dan confirmation sama
        Observable<Boolean> passwordConfirmationStream = Observable.merge(
                RxTextView.textChanges(etPassword)
                        .map(new Func1<CharSequence, Boolean>() {
                            @Override
                            public Boolean call(CharSequence charSequence) {
                                return !charSequence.toString().equals(etPasswordConfirmation.getText().toString());
                            }
                        }),
                RxTextView.textChanges(etPasswordConfirmation)
                        .map(new Func1<CharSequence, Boolean>() {
                            @Override
                            public Boolean call(CharSequence charSequence) {
                                return !charSequence.toString().equals(etPassword.getText().toString());
                            }
                        })
        );

        Observable<Boolean> emptyCheckStream = Observable.combineLatest(
                RxTextView.textChanges(etEmail)
                        .map(new Func1<CharSequence, Boolean>() {
                            public Boolean call(CharSequence charSequence) {
                                return TextUtils.isEmpty(charSequence) && (charSequence.toString().indexOf('@') > -1) && (charSequence.toString().indexOf('.') > -1) ;
                            }
                        }),
                RxTextView.textChanges(etPassword)
                        .map(new Func1<CharSequence, Boolean>() {
                            @Override
                            public Boolean call(CharSequence charSequence) {
                                return TextUtils.isEmpty(charSequence);
                            }
                        }),
                RxTextView.textChanges(etPasswordConfirmation)
                        .map(new Func1<CharSequence, Boolean>() {
                            @Override
                            public Boolean call(CharSequence charSequence) {
                                return TextUtils.isEmpty(charSequence);
                            }
                        }),
                new Func3<Boolean, Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean emailEmpty, Boolean passwordEmpty, Boolean passwordConfirmationEmpty) {
                        return emailEmpty || passwordEmpty || passwordConfirmationEmpty;
                    }
                }
        );

        Observable<Boolean> invaliedFieldStream = Observable.combineLatest(
                emailStream,
                passwordStream,
                passwordConfirmationStream,
                emptyCheckStream,
                new Func4<Boolean, Boolean, Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean email, Boolean password, Boolean passwordConfirm, Boolean emptyCheck) {
                        return !email && !password && !passwordConfirm && !emptyCheck;
                    }
                }

        );

        //========================================================================
        // OBSERVER
        //========================================================================

        //inisialisasi observer untuk field email
        Observer<Boolean> emailObserver = new Observer<Boolean>(){

            @Override
            public void onCompleted() {
                Log.d("rx","email observer complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("rx","email observer error "+e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.d("rx","Email observer onNext  "+String.valueOf(aBoolean.booleanValue()));
                showEmailAlert(aBoolean.booleanValue());
            }
        };

        Observer<Boolean> passwordObserver = new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d("rx","password observer complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("rx","error password observer "+e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                showPasswordMinimalAlert(aBoolean.booleanValue());
            }
        };

        Observer<Boolean> passwordConfirmationObserver = new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d("rx"," password confirmation observer compelete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("rx","error password confimation observer "+e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                showPassordConfirmationAlert(aBoolean.booleanValue());
            }
        };

        Observer<Boolean> invalidFieldsObserver = new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d("rx","invalid confirmation comeplete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("rx","eror invalid conirmation alert "+e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.d("rx","next "+String.valueOf(aBoolean.booleanValue()));
                btnSubmit.setEnabled(aBoolean);
            }
        };

        //=====================================================================
        // Subscripting
        //=====================================================================
        //proses subscripting(menunggu) prosess emiting(mengirim) dari observer(penerima), melalu observable(stream/prosotan)
        emailStream.subscribe(emailObserver);
        passwordStream.subscribe(passwordObserver);
        passwordConfirmationStream.subscribe(passwordConfirmationObserver);
        invaliedFieldStream.subscribe(invalidFieldsObserver);
    }


    public void showEmailAlert(boolean value){
        if(value){
            textEmailAlert.setText(getString(R.string.email_exist_alert));
            textEmailAlert.setVisibility(View.VISIBLE);
        }
        else{
            textEmailAlert.setVisibility(View.GONE);
        }
    }

    public void showPasswordMinimalAlert(boolean value){
        if(value){
            textPasswordAlert.setText(getString(R.string.password_minimal_alert));
            textPasswordAlert.setVisibility(View.VISIBLE);
        }
        else {
            textPasswordAlert.setVisibility(View.GONE);
        }
    }

    public void showPassordConfirmationAlert(boolean value){
        if(value){
            textPasswordConfirmationAlert.setText(getString(R.string.password_confirmation_not_match));
            textPasswordConfirmationAlert.setVisibility(View.VISIBLE);
        }
        else{
            textPasswordConfirmationAlert.setVisibility(View.GONE);
        }
    }

    public void showEmailExistAlert(boolean value){
        if(value){
            textEmailAlert.setText(getString(R.string.email_exist_alert));
            textEmailAlert.setVisibility(View.VISIBLE);
        }
        else{
            textEmailAlert.setVisibility(View.GONE);
        }
    }

    //Ambil data email dari API dan cek apakah sudah terpakai
    public Observable<Boolean> checkIfEmailExistFromApi(final String input){
        return  service.getEmails()
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        //Log.d("rx","value "+strings.get(0));
                        return Observable.from(strings);
                    }
                }).contains(input)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
