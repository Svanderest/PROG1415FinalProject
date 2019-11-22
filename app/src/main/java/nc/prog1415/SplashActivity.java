package nc.prog1415;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
        client = TcpClient.getClient((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
        mp = MediaPlayer.create(this, R.raw.startup);
        mp.start();

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(client.connected && !mp.isPlaying())
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else
                    h.postDelayed(this,1500);
            }
        }, 1500);
    }
}
