package fe.uni.lj.si.eventify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> events;
    private Context context;

    public EventsAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.performerName.setText(event.getPerformerName());
        holder.venueName.setText(event.getVenueName());

        // Set images if available
        if (event.getPerformerImage() != null) {
            holder.imagePerformer.setImageBitmap(event.getPerformerImage());
        }
        if (event.getVenueImage() != null) {
            holder.imageVenue.setImageBitmap(event.getVenueImage());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("performerName", event.getPerformerName());
            intent.putExtra("performerDescription", event.getPerformerDescription());
            intent.putExtra("performerLink", event.getPerformerLink());
            intent.putExtra("venueName", event.getVenueName());
            intent.putExtra("datesAndTimes", event.getDatesAndTimes());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView performerName;
        ImageView imagePerformer;
        TextView venueName;
        ImageView imageVenue;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            performerName = itemView.findViewById(R.id.performerName);
            imagePerformer = itemView.findViewById(R.id.imagePerformer);
            venueName = itemView.findViewById(R.id.venueName);
            imageVenue = itemView.findViewById(R.id.imageVenue);
        }
    }
}
