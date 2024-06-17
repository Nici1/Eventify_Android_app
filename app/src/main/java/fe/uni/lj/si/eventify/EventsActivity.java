package fe.uni.lj.si.eventify;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Event> events;
    private OkHttpClient client;
    private boolean isLoading = false;
    private int currentPage = 1;
    private int totalPages = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        events = new ArrayList<>();
        adapter = new EventsAdapter(events, this);
        recyclerView.setAdapter(adapter);

        client = new OkHttpClient();

        loadEvents(currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && firstVisibleItemPosition + visibleItemCount >= totalItemCount && currentPage < totalPages) {
                    currentPage++;
                    loadEvents(currentPage);
                }
            }
        });
    }

    private void loadEvents(int page) {
        isLoading = true;
        String url = "http://192.168.1.222:4000/events";

        JSONObject requestBodyJson = new JSONObject();
        try {
            requestBodyJson.put("pageNumber", page);
            requestBodyJson.put("pageSize", 2);
            requestBodyJson.put("token", getTokenFromLocalStorage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                requestBodyJson.toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(EventsActivity.this, "Failed to fetch events. Please try again.", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("EventsActivity", "Response JSON: " + responseBody); // Log the response JSON for debugging

                        JSONObject jsonObject = new JSONObject(responseBody);

                        // Parse venue images array (assuming base64 encoded strings)
                        JSONArray venueImagesArray = jsonObject.getJSONArray("venue_images");
                        List<String> venueImages = new ArrayList<>();
                        for (int i = 0; i < venueImagesArray.length(); i++) {
                            String base64Image = venueImagesArray.getString(i);
                            venueImages.add(base64Image);
                        }


                        // Parse performer images array (assuming base64 encoded strings)
                        JSONArray performerImagesArray = jsonObject.getJSONArray("performer_images");
                        List<String> performerImages = new ArrayList<>();
                        for (int i = 0; i < performerImagesArray.length(); i++) {
                            String base64Image = performerImagesArray.getString(i);
                            performerImages.add(base64Image);
                        }

                        // Parse events array
                        List<Event> newEvents = new ArrayList<>();
                        JSONArray result = jsonObject.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject eventJson = result.getJSONObject(i);

                            // Example of handling missing or optional fields
                            Bitmap venueImage = null;
                            if (i < venueImages.size()) {
                                venueImage = decodeBase64ToBitmap(venueImages.get(i));
                            }

                            Bitmap performerImage = null;
                            if (i < performerImages.size()) {
                                performerImage = decodeBase64ToBitmap(performerImages.get(i));
                            }

                            Event event = new Event(
                                    eventJson.getInt("VenueavailabilityID"),
                                    eventJson.getInt("PerformerID"),
                                    eventJson.getString("PerformerName"),
                                    eventJson.getString("PerformerDescription"),
                                    eventJson.getString("Link"), // Correctly pass the performer link
                                    eventJson.getString("VenueID"),
                                    eventJson.getString("VenueName"),
                                    eventJson.getString("VenueDescription"),
                                    venueImage,
                                    eventJson.getString("DatesAndTimes"),
                                    performerImage // Correctly pass the performer image
                            );

                            newEvents.add(event);
                        }

                        runOnUiThread(() -> {
                            events.addAll(newEvents);
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                        });
                    } catch (JSONException e) {

                    }
                } else {

                }
            }


        });
    }

    private String getTokenFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("authToken", ""); // Retrieves the token or empty string if not found
    }



    public Bitmap decodeBase64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

}
