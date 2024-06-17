package fe.uni.lj.si.eventify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        TextView performerName = findViewById(R.id.detailPerformerName);
        TextView performerDescription = findViewById(R.id.detailPerformerDescription);
        TextView performerLink = findViewById(R.id.detailPerformerLink);
        TextView venueName = findViewById(R.id.detailVenueName);
        TextView datesAndTimes = findViewById(R.id.detailDatesAndTimes);

        // Get the intent and extract the data
        Intent intent = getIntent();
        performerName.setText(intent.getStringExtra("performerName"));
        performerDescription.setText(intent.getStringExtra("performerDescription"));
        performerLink.setText(intent.getStringExtra("performerLink"));
        venueName.setText(intent.getStringExtra("venueName"));
        datesAndTimes.setText(intent.getStringExtra("datesAndTimes"));
    }
}
