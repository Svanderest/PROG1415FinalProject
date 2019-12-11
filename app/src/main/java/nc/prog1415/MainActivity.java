package nc.prog1415;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private Location location;
    private Boolean receiving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        receiving = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusinessViewAdapter adapter = new BusinessViewAdapter(this);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rvBusiness);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ((TextView)findViewById(R.id.updateLocation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(receiving)
                    return;
                final TcpClient client = TcpClient.getClient();
                Location androidLocation = client.location;
                receiving = true;
                final nc.com.Location l = new nc.com.Location(androidLocation.getLongitude(),androidLocation.getLatitude());
                client.send(l);
                client.receive(new Runnable() {
                    @Override
                    public void run() {
                        Intent i;
                        if(client.lastReceiptID == l.hashCode())
                            i = new Intent(MainActivity.this, MainActivity.class);
                        else
                            i = new Intent(MainActivity.this, NotFoundActivity.class);
                        receiving = false;
                        startActivity(i);
                    }
                }, l.hashCode());
            }
        });
    }
}
