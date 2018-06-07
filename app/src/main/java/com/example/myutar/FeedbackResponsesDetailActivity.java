package com.example.myutar;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class FeedbackResponsesDetailActivity extends AppCompatActivity {

    private ArrayList<FeedbackResponse> feedbackResponseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedbackResponseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_responses_detail);

        TextView question = findViewById(R.id.question);
        TextView answerNo = findViewById(R.id.no);
        TextView answerYes = findViewById(R.id.yes);

        Log.d("DEBUG STATUS TYPE", Integer.toString(getIntent().getIntExtra("status", 0)));
        if (getIntent().getIntExtra("status", 0) != 2) {
            question.setVisibility(View.GONE);
            answerNo.setVisibility(View.GONE);
            answerYes.setVisibility(View.GONE);
        }

        new AsyncTaskFetch().execute();
    }

    private class AsyncTaskFetch extends AsyncTask<String, String, String> {

        ProgressDialog progressBar;
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
            // Display a progress dialog
            progressBar = ProgressDialog.show(FeedbackResponsesDetailActivity.this, "Fetching Data","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://myutar.000webhostapp.com/MyUTAR/getFeedbackResponseByFId.php?fId=" + getIntent().getStringExtra("fId"));

                Log.d("DEBUG:", url.toString());
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
                JSONArray jsonArray = object.getJSONArray("feedbackResponse");

                feedbackResponseList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    feedbackResponseList.add(new FeedbackResponse(jsonObject.getString("frDateTime"), jsonObject.getString("fResponse"), jsonObject.getString("dId")));
                }

                recyclerView = (RecyclerView) findViewById(R.id.list);

                mAdapter = new FeedbackResponseAdapter(feedbackResponseList, FeedbackResponsesDetailActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(FeedbackResponsesDetailActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                Log.d("DEBUG", e.toString());
                Toast.makeText(FeedbackResponsesDetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void answerYes(View view) {
        Intent intent = new Intent(this, FeedbackResponsesSatisfactoryActivity.class);
        intent.putExtra("answer", "Yes");
        intent.putExtra("fId", getIntent().getStringExtra("fId"));
        intent.putExtra("dId", getIntent().getStringExtra("dId"));
        startActivity(intent);
    }

    public void answerNo(View view) {
        Intent intent = new Intent(this, FeedbackResponsesSatisfactoryActivity.class);
        intent.putExtra("answer", "No");
        intent.putExtra("fId", getIntent().getStringExtra("fId"));
        intent.putExtra("dId", getIntent().getStringExtra("dId"));
        startActivity(intent);
    }
}
