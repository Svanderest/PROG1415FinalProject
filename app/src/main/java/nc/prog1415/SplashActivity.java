package nc.prog1415;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    TcpClient client;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INITIALIZE", "Creating main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView iv = (ImageView)findViewById( R.id.imageView);
        //iv.setOnSystemUiVisibilityChangeListener();
        client = TcpClient.getClient();
    }

    protected void onResume()
    {
        super.onResume();
        mp = MediaPlayer.create(this, R.raw.startup);
        mp.start();

        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public synchronized void run() {
                while (!client.connected || mp.isPlaying())
                {
                    try {
                        this.wait();
                    } catch (InterruptedException e){ Log.d("DEBUG", e.getMessage());}
                }
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
