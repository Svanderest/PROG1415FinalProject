package nc.prog1415;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nc.com.Business;
import nc.com.BusinessMessage;

public class BusinessViewAdapter extends RecyclerView.Adapter<BusinessViewAdapter.ViewHolder>{

    MainActivity mainActivity;
    TcpClient client;

    public BusinessViewAdapter(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        client = TcpClient.getClient();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Business item = client.businesses.get(position);
        holder.tvName.setText(item.name);
        holder.tvAddress.setText(item.address);
        holder.tvWebsite.setText(item.website);
        holder.tvWebsite.setOnLongClickListener(new LinkListener());
        holder.rb.setNumStars(6);
        holder.rb.setStepSize(0.1f);
        holder.rb.setRating(item.averageRating);
        holder.rb.setIsIndicator(true);
        Log.d("ADRESS",item.address);
        Log.d("RATING", String.valueOf(item.averageRating));
        holder.leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Get feed back for business and navigate to feedback view
                client.receive(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(mainActivity, FeedbackListActivity.class);
                        i.putExtra("Business",item);
                        mainActivity.startActivity(i);
                    }
                });
                BusinessMessage msg = new BusinessMessage();
                msg.BusinessID = item.id;
                client.send(msg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return TcpClient.getClient().businesses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvName;
        public TextView tvAddress;
        public TextView tvWebsite;
        public RatingBar rb;
        public Button leaveReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.businessName);
            tvAddress = itemView.findViewById(R.id.businessAddress);
            tvWebsite = itemView.findViewById(R.id.buisnessWebsite);
            rb = itemView.findViewById(R.id.businessRating);
            leaveReview = itemView.findViewById(R.id.buttonFeedback);
        }
    }
}
