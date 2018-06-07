package com.example.myutar;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FeedbackStatusActivity extends AppCompatActivity {

    private ArrayList<Feedback> feedbackList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedbackAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        new AsyncTaskFetch().execute();
    }

    private class AsyncTaskFetch extends AsyncTask<String, String, String> {

        ProgressDialog progressBar;
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
            // Display a progress dialog
            progressBar = ProgressDialog.show(FeedbackStatusActivity.this, "Fetching Data","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://myutar.000webhostapp.com/MyUTAR/getFeedbacksByUserID.php?user=1403537");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String json;
                while((json = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json+"\n");
                }
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String data) {
            // Dismiss the progress bar after receiving data from API
            progressBar.dismiss();
            try {
                // JSON Parsing of data
                JSONObject object = new JSONObject(data);
                JSONArray jsonArray = object.getJSONArray("feedback");

                feedbackList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String[] attachments = null;
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (!jsonObject.isNull("fAttachment")) {
                        attachments = jsonObject.getString("fAttachment").split(",");
                    }
                    feedbackList.add(new Feedback(jsonObject.getInt("fId"), jsonObject.getInt("fType"),
                            jsonObject.getString("fContent"), attachments, jsonObject.getString("fDateTime"),
                            jsonObject.getInt("fStatus"), jsonObject.getString("dId")));
                }

                TextView result = (TextView)findViewById(R.id.no_result);

                if (feedbackList.size() == 0) {
                    result.setVisibility(View.VISIBLE);
                } else {
                    result.setVisibility(View.GONE);
                }

                recyclerView = (RecyclerView) findViewById(R.id.list);

                mAdapter = new FeedbackAdapter(feedbackList, FeedbackStatusActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(FeedbackStatusActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                Log.d("DEBUG", e.toString());
                Toast.makeText(FeedbackStatusActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
