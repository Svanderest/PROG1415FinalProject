package nc.prog1415;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INITIALIZE", "Creating main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        client = TcpClient.getClient();
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                while (!client.connected);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
