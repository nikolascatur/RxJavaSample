package phone.nikolas.com.rxjavasample;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnMultipleTapsDemo; //btn_multiple_taps_demo
    Button btnRegistrationDemo; //btn_registration_demo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMultipleTapsDemo = (Button) findViewById(R.id.btn_multiple_taps_demo);

        btnRegistrationDemo = (Button) findViewById(R.id.btn_registration_demo);
        btnRegistrationDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationDemoActivity.class));
            }
        });
    }
}
