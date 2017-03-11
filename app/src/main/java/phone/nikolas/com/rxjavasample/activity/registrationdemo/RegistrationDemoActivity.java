package phone.nikolas.com.rxjavasample.activity.registrationdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;


import phone.nikolas.com.rxjavasample.BaseApp;
import phone.nikolas.com.rxjavasample.MainActivity;
import phone.nikolas.com.rxjavasample.R;
import phone.nikolas.com.rxjavasample.base.BaseActivity;
import phone.nikolas.com.rxjavasample.service.Service;

/**
 * Created by Pleret on 3/8/2017.
 */

public class RegistrationDemoActivity extends BaseActivity<RegistrationDemoActivityBinding,RegistrationDemoViewModel,RegistrationDemoPresenter>
        implements RegistrationDemoView{

    @Inject
    Service service;

    @Override
    protected void initInjection() {
        ((BaseApp)getApplication()).getmAppComponent().inject(this);
    }

    @Override
    protected void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_demo);
    }

    @Override
    protected void initView() {
        viewModel = new RegistrationDemoView();

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onViewReady(Bundle savedinstance) {

    }
}
