package nc.prog1415;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nc.com.Business;

public class FeedbackListActivity extends AppCompatActivity {
    int id;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_list_view);
        Business business = (Business)getIntent().getSerializableExtra("Business");
        id = business.id;
        ((TextView)findViewById(R.id.tvName)).setText(business.name);
        ((TextView)findViewById(R.id.tvAddress)).setText(business.address);
        ((TextView)findViewById(R.id.tvWebsite)).setText(business.website);

        FeedbackViewAdapter adapter = new FeedbackViewAdapter(this);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rvFeedback);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
