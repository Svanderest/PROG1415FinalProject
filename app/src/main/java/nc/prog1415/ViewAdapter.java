package nc.prog1415;

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

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Business item = TcpClient.getClient().businesses.get(position);
        holder.tvName.setText(item.name);
        holder.tvAddress.setText(item.address);
        holder.tvWebsite.setText(item.website);
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
