package phone.nikolas.com.rxjavasample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Pleret on 3/3/2017.
 */

public class MultipeTapDemoActivity extends AppCompatActivity {

    final static String TAG = "multiple tap stream";
    TextView textTotal;
    TextView textCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_tap_demo);

        getSupportActionBar().setTitle("Multiple click demo");

        final Button btnTap = (Button) findViewById(R.id.btn_tap);
        textTotal = (TextView) findViewById(R.id.text_total);
        textCount = (TextView) findViewById(R.id.text_count);

        Observable<Void> TapStream = RxView.clicks(btnTap).share();

        Observable<Integer> multipleCountStream = TapStream.buffer(TapStream
                .debounce(1, TimeUnit.SECONDS))
                .map(new Func1<List<Void>, Integer>() {
            @Override
            public Integer call(List<Void> voids) {
                Log.d(TAG,String.valueOf(voids.size()));
                return voids.size();
            }
        }).filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        Log.d(TAG,"Multiple filter "+String.valueOf(integer));
                        return integer > 2;
                    }
                });
        Observer<Integer> multipleTapObserver =  new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.d("TAG","Multiple Observer Complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"multiple observer error "+e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                showMultipleTap(integer);
            }
        };

        multipleCountStream.observeOn(AndroidSchedulers.mainThread()).subscribe(multipleTapObserver);
    }

    public void showMultipleTap(int integer){
        textCount.setText(String.valueOf(integer)+" x tap");
        textCount.setVisibility(View.VISIBLE);
        textCount.setScaleX(1f);
        textCount.setScaleY(1f);
        ViewCompat.animate(textCount)
                .scaleXBy(-1f)
                .scaleYBy(-1f)
                .setDuration(800)
                .setStartDelay(100);
    }
}
