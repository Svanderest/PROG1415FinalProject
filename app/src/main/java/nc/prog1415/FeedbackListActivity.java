package nc.prog1415;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nc.com.Business;

public class FeedbackListActivity extends AppCompatActivity {
    Business business;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_list_view);
        business = (Business)getIntent().getSerializableExtra("Business");
        ((TextView)findViewById(R.id.tvName)).setText(business.name);
        ((TextView)findViewById(R.id.tvAddress)).setText(business.address);
        final TextView tvWebsite = (TextView)findViewById(R.id.tvWebsite);
        tvWebsite.setText(business.website);
        tvWebsite.setOnLongClickListener(new LinkListener());

        ((Button)findViewById(R.id.btnReview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FeedbackListActivity.this,ReviewActivity.class);
                i.putExtra("Business",business);
                startActivity(i);
            }
        });

        FeedbackViewAdapter adapter = new FeedbackViewAdapter(this);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rvFeedback);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
