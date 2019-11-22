package nc.prog1415;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    TcpClient client;
    private LocationManager lm;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INITIALIZE","Creating main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = TcpClient.getClient();
        final TextView tv = (TextView)findViewById(R.id.textView);
        final EditText et = (EditText)findViewById(R.id.editText2);
        final Button b = (Button)findViewById(R.id.button2);

        //event to send user's input to the server
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String text = et.getText().toString();
                client.send(text);
            }
        });

        client.receive(tv);
    }
}
