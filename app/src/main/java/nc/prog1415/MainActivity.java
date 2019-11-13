package nc.prog1415;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // final Connection con = new Connection();
        //con.execute();

        final EditText et = (EditText)findViewById(R.id.editText2);
        final Button b = (Button)findViewById(R.id.button2);
        final TextView tv = (TextView)findViewById(R.id.textView);

        //event to send user's input to the server
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText().length() > 0) {
                    try {
                        con.out.writeObject(et.getText());
                        con.out.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Object obj = con.in.readObject();
                        if(obj instanceof String) {
                            String content = tv.getText().toString();
                            content += obj.toString() + "\n";
                            tv.setText(content);
                        }
                    } catch (Exception e) { }
                }
            }
        });
    }
}
