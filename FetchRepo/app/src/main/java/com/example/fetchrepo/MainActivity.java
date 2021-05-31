package com.example.fetchrepo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button nextBtn;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("api", MODE_PRIVATE);
        String url = pref.getString("url", null);

        if (url == null) {
            showRepo("https://api.bitbucket.org/2.0/repositories");
        } else {
            showRepo(url);
        }

        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(MainActivity.this, MainActivity.class);
                startActivity(data);
            }
        });

    }

    private void showRepo(String api) {
        adapter = new CustomAdapter(this, 0);
        // Pulling items from the array
        Thread bkgdThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(api);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.connect();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    rd.close();
                    conn.disconnect();

                    String result = sb.toString();
                    JSONObject jResult = new JSONObject(result);
                    JSONArray jValues = jResult.getJSONArray("values");

                    if (jResult.getString("next").isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nextBtn = (Button) findViewById(R.id.nextBtn);
                                nextBtn.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        SharedPreferences pref =
                                getSharedPreferences("api", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("url", jResult.getString("next"));
                        editor.commit();

                    }

                    Repository[] repos = new Repository[jValues.length()];
                    for (int i = 0; i < jValues.length(); i++) {
                        try {
                            JSONObject jRepo = jValues.getJSONObject(i);

                            JSONObject jOwner = jRepo.getJSONObject("owner");
                            String displayName = jOwner.getString("display_name");
                            String type = jOwner.getString("type");
                            String dateOfCreation = jRepo.getString("created_on")
                                    .substring(0, 10);

                            JSONObject jLinks = jOwner.getJSONObject("links");
                            JSONObject jAvatar = jLinks.getJSONObject("avatar");
                            String avatar = jAvatar.getString("href");

                            // Pulling items from the array
                            repos[i] = new Repository(displayName, type, dateOfCreation, avatar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setData(repos);
                            ListView listView = findViewById(R.id.listView);
                            if (listView != null) {
                                listView.setAdapter(adapter);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bkgdThread.start();
    }

}