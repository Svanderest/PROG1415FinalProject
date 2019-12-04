package nc.prog1415;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import nc.com.Business;
import nc.com.Feedback;

public class ReviewActivity  extends AppCompatActivity {
    Business business;
    TcpClient client;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        client = TcpClient.getClient();
        business = (Business)getIntent().getSerializableExtra("Business");
        ((TextView)findViewById(R.id.reviewBusinessName)).setText(business.name);
        ((Button)findViewById(R.id.btnSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback review = new Feedback();
                review.businessId = business.id;
                review.date = new Date();
                review.comment = ((TextView)findViewById(R.id.reviewComment)).getText().toString();
                review.rating = ((RatingBar)findViewById(R.id.reviewRating)).getRating();
                client.send(review);
                Intent i = new Intent(ReviewActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
