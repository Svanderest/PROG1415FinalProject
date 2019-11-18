package nc.prog1415;

import androidx.appcompat.app.AppCompatActivity;
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

    final Connection con = new Connection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INITIALIZE","Creating main activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        con.execute();

        final TextView tv = (TextView)findViewById(R.id.textView);
        final EditText et = (EditText)findViewById(R.id.editText2);
        final Button b = (Button)findViewById(R.id.button2);


        //event to send user's input to the server
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Handler h = new Handler();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (!con.connected)
                                this.wait();
                        }
                        catch (InterruptedException e){}

                        if(et.getText().length() > 0) {
                            try {
                                con.out.writeObject(et.getText().toString());
                                con.out.flush();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

/*   @Override
    protected void onResume()
    {
        super.onResume();
        final TextView tv = (TextView)findViewById(R.id.textView);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(con.connected) {
                        try {
                            Object obj = con.in.readObject();
                            if (obj instanceof String) {
                                String content = tv.getText().toString();
                                content += obj.toString() + "\n";
                                tv.setText(content);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });
    }*/
}
