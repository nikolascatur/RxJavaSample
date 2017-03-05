package phone.nikolas.com.rxjavasample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;

import rx.Observer;
import rx.functions.Func1;

/**
 * Created by Pleret on 3/3/2017.
 */

public class TextDemoActivity extends AppCompatActivity {
    EditText etInputText;
    TextView textShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_demo);

        etInputText = (EditText) findViewById(R.id.et_input_text);
        textShow = (TextView) findViewById(R.id.text_show);

        Observable<String> etInputTextObservable = RxTextView.textChanges(etInputText)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return  charSequence.toString();
                    }
                });
                /*.debounce(1000, TimeUnit.MILLISECONDS)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length()>3;
                    }
                });*/

        Observer<String> etInputTextObserver =new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                showAnalyticString(s);
            }
        };

        etInputTextObservable.subscribe(etInputTextObserver);

    }

    public void showAnalyticString(String value){
        int totalCharacter = checkTotalCharacter(value);
        int countPolindrom = checkPolindrom(value);

        textShow.setText("Total Character : "+totalCharacter+" == Total Polindrom : "+countPolindrom);
    }

    public int checkPolindrom(String s){
        String tmp = s.replace("\n"," ").replace("\r"," ");
        String[] spilt = tmp.split(" ");
        int retVal = 0;

        Log.d("TAG","Input : "+tmp);
        for(String text : spilt){
            char[] poliArray = text.toCharArray();
            String puter = "";
            for(int i = poliArray.length-1;i>=0;i--)
                puter +=""+poliArray[i];

            Log.d("rx","  "+puter);
            if(!"".equals(puter) && text.equals(puter)){
                retVal++;
            }
        }
        return retVal;
    }

    public int checkTotalCharacter(String val) {
        return val.length();
    }
}
