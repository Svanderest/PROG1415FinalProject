package nc.prog1415;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INITIALIZE", "Creating main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
