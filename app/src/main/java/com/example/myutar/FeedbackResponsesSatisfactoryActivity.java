package com.example.myutar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackResponsesSatisfactoryActivity extends AppCompatActivity {

    String ServerURL = "https://myutar.000webhostapp.com/MyUTAR/submitSatisfactory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String feedbackID = getIntent().getStringExtra("fId");
        final String departmentID = getIntent().getStringExtra("dId");
        final String answer = getIntent().getStringExtra("answer");

        if (answer.equals("Yes")) {
            setContentView(R.layout.activity_satisfied);
        } else {
            setContentView(R.layout.activity_not_satisfied);
        }

        Button submitButton = (Button)findViewById(R.id.satisfactory_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rating = 0;
                String reasons = "";

                if (answer.equals("Yes")) {
                    RatingBar ratingBar = findViewById(R.id.ratingBar);
                    rating = Math.round(ratingBar.getRating());
                } else {
                    EditText reason = (EditText)findViewById(R.id.satisfactory_reason);
                    reasons = reason.getText().toString();
                }

                Log.d("Debug", Integer.toString(rating));
                Log.d("Debug", reasons);
                if (answer.equals("Yes") && rating == 0) {
                    Toast.makeText(FeedbackResponsesSatisfactoryActivity.this, "Please rate the responses before submitting", Toast.LENGTH_LONG).show();
                } else if (answer.equals("No") && reasons.equals("")) {
                    Toast.makeText(FeedbackResponsesSatisfactoryActivity.this, "Please provide the reason(s) before submitting", Toast.LENGTH_LONG).show();
                } else {
                    InsertData(feedbackID, departmentID, rating, reasons, 1403537);
                }
            }
        });
    }

    public void InsertData(final String feedbackID, final String departmentID, final int rating, final String reasons, final int uID){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            ProgressDialog progressBar;
            @Override
            protected  void onPreExecute() {
                super.onPreExecute();
                // Display a progress dialog

                progressBar = ProgressDialog.show(FeedbackResponsesSatisfactoryActivity.this, "Submitting Data","Please Wait...",true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("fId", feedbackID));
                nameValuePairs.add(new BasicNameValuePair("dId", departmentID));
                nameValuePairs.add(new BasicNameValuePair("rating", Integer.toString(rating)));
                if (!reasons.equals("")) { nameValuePairs.add(new BasicNameValuePair("reason", reasons)); }
                nameValuePairs.add(new BasicNameValuePair("uId", Integer.toString(uID)));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                    Log.d("DEBUG RESPONSE", EntityUtils.toString(httpEntity));

                    return "Successfully Sent";
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    return e.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String result) {

                // Dismiss the progress bar after receiving data from API
                progressBar.dismiss();

                super.onPostExecute(result);

                Toast.makeText(FeedbackResponsesSatisfactoryActivity.this, result, Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                if (getIntent().getStringExtra("answer").equals("Yes")) {
                    intent.putExtra("status", 4);
                } else {
                    intent.putExtra("status", 3);
                }
                intent.putExtra("fId", getIntent().getStringExtra("fId"));
                intent.putExtra("dId", getIntent().getStringExtra("dId"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(getApplicationContext(), FeedbackResponsesDetailActivity.class);
                startActivity(intent);
            }
        }

        // Execute SendPostReqAsyncTack to send data to server
        new SendPostReqAsyncTask().execute();
    }
}