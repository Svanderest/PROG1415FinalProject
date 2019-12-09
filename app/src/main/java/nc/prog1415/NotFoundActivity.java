package nc.prog1415;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotFoundActivity extends AppCompatActivity {
    boolean receiving = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_found);
        ((Button)findViewById(R.id.btnTryAgain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(receiving)
                    return;
                final TcpClient client = TcpClient.getClient();
                Location androidLocation = client.location;
                receiving = true;
                nc.com.Location l = new nc.com.Location(androidLocation.getLongitude(),androidLocation.getLatitude());
                client.send(l);
                client.receive(new Runnable() {
                    @Override
                    public void run() {
                        if(client.businesses.size() > 0)
                        {
                            Intent i = new Intent(NotFoundActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(NotFoundActivity.this, R.string.notFoundToast, Toast.LENGTH_LONG).show();
                        receiving = false;
                    }
                }, l.hashCode());
            }
        });

        //for reasons unkown this view does render the text views correctly
        ((TextView)findViewById(R.id.notFoundHeading)).setText(getString(R.string.notFoundTitle));
        ((TextView)findViewById(R.id.notFoundBody)).setText(getString(R.string.notFoundBodyText));
    }
}
