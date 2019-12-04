package nc.prog1415;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nc.com.Feedback;

public class FeedbackViewAdapter extends RecyclerView.Adapter<FeedbackViewAdapter.ViewHolder> {
    TcpClient client;
    FeedbackListActivity mainActivity;

    public FeedbackViewAdapter(FeedbackListActivity feedbackListActivity)
    {
        mainActivity = feedbackListActivity;
        client = TcpClient.getClient();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeback_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Feedback item = client.feedback.get(position);
        holder.tvDate.setText(item.date.toString());
        holder.tvComment.setText(item.comment);
        holder.rb.setRating(item.rating);
    }

    @Override
    public int getItemCount() {
        return client.feedback.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvDate;
        TextView tvComment;
        RatingBar rb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.feedbackDate);
            tvComment = itemView.findViewById(R.id.feedbackComment);
            rb = itemView.findViewById(R.id.feedbackRating);
        }
    }
}
