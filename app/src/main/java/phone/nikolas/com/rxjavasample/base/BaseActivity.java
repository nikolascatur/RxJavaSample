package phone.nikolas.com.rxjavasample.base;

/**
 * Created by Pleret on 3/8/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<B, V, P> extends AppCompatActivity {

    protected B binding;
    protected V viewModel;
    protected P presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initInjection();
        initView();
        initPresenter();
        onViewReady(savedInstanceState);
    }

    protected abstract void initInjection();
    protected abstract void initBinding();
    protected abstract void initView();
    protected abstract void initPresenter();
    protected abstract void onViewReady(Bundle savedinstance);

}
