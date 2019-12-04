package nc.prog1415;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private LocationManager lm;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INITIALIZE","Creating main activity");
        Log.d("Items Received",String.valueOf(TcpClient.getClient().businesses.size()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusinessViewAdapter adapter = new BusinessViewAdapter(this);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rvBusiness);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
