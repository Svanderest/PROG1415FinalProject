package nc.prog1415;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NotFoundActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_found);
        ((Button)findViewById(R.id.btnTryAgain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: reload businesses
            }
        });
    }
}
