package fe.uni.lj.si.eventify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private TextView mRegisterLink;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterLink = findViewById(R.id.register_link);

//        mRegisterLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        mGson = new Gson();

        mLoginButton.setOnClickListener(view -> {
            String email = mEmailField.getText().toString().trim();
            String password = mPasswordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> data = new HashMap<>();
            data.put("Email", email); // Ensure the key matches what the backend expects
            data.put("Password", password); // Ensure the key matches what the backend expects

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    new JSONObject(data).toString()
            );

            Request request = new Request.Builder()
                    .url("http://192.168.1.222:4000/login/Spectator") // IP address and port of the remote server
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build();

            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("MainActivity", "Error sending request", e);
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        ApiResponse apiResponse = mGson.fromJson(responseBody, ApiResponse.class);
                        if (apiResponse.isSuccess()) {
                            String token = apiResponse.getToken(); // Changed to get token from response body

                            if (token != null) {
                                // Save the authentication token in the session
                                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                sharedPreferences.edit().putString("authToken", token).apply();
                                Log.i("Token", token);

                                runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    final Intent intent = new Intent(MainActivity.this, EventsActivity.class);
                                    startActivity(intent);
                                });
                            } else {
                                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Token is missing in response.", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        });
    }

    static class ApiResponse {
        private String status;
        private String token;

        public boolean isSuccess() {
            return "success".equals(status);
        }

        public String getToken() {
            return token;
        }
    }
}
