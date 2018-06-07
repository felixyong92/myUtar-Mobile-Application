package com.example.myutar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private GridView gridView;
    private EventAdapter mAdapter;
    private String type = "All";
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final Spinner year = (Spinner) findViewById(R.id.year_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list_content of choices appears
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        year.setAdapter(adapterYear);

        final Spinner month = (Spinner) findViewById(R.id.month_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list_content of choices appears
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        month.setAdapter(adapterMonth);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        String formattedDate = df.format(currentTime);
        date = formattedDate;

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (month.getSelectedItem().toString().equals("All")) {
                    date = year.getItemAtPosition(i).toString();
                } else {
                    date = year.getItemAtPosition(i).toString() + "-" + month.getSelectedItem().toString();
                }
                Log.d("Date", date);
                new AsyncTaskFetch().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (month.getSelectedItemPosition() == 0) {
                    date = year.getSelectedItem().toString();
                } else {
                    date = year.getSelectedItem().toString() + "-" + month.getItemAtPosition(i).toString();
                }
                Log.d("Date", date);
                new AsyncTaskFetch().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Log.d("Date", date);
        new AsyncTaskFetch().execute();
    }

    private class AsyncTaskFetch extends AsyncTask<String, String, String> {

        ProgressDialog progressBar;
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
            // Display a progress dialog
            progressBar = ProgressDialog.show(EventActivity.this, "Fetching Data","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://myutar.000webhostapp.com/MyUTAR/getEvents.php?date=" + date);
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
                JSONArray jsonArray = object.getJSONArray("event");

                eventList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String[] images = null;
                    String[] attachments = null;
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!jsonObject.isNull("image")) {
                        images = jsonObject.getString("image").split(",");
                    }

                    if (!jsonObject.isNull("attachment")) {
                        attachments = jsonObject.getString("attachment").split(",");
                    }

                    if (type.equals("All") || type.equals(jsonObject.getString("type"))) {
                        eventList.add(new Event(jsonObject.getString("dId"), jsonObject.getString("title"),
                                jsonObject.getString("description"), images, attachments, jsonObject.getString("type"),
                                jsonObject.getString("expiryDate")));
                    }
                }

                TextView result = (TextView)findViewById(R.id.no_result);

                if (eventList.size() == 0 ) {
                    result.setVisibility(View.VISIBLE);
                } else {
                    result.setVisibility(View.GONE);
                }

                gridView = (GridView) findViewById(R.id.gridview);

                mAdapter = new EventAdapter(eventList, EventActivity.this);
                gridView.setAdapter(new EventAdapter(eventList, EventActivity.this));

            } catch (JSONException e) {
                Log.d("DEBUG", e.toString());
                Toast.makeText(EventActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_filter:
                getFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getFilter(){
        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtered By");

        // Add a list_content
        final String[] sort = {"All", "Campaign/Festival", "Competition", "Seminar/Course/Workshop", "Talk", "Others"};
        builder.setItems(sort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                type = sort[selection].toString();
                new AsyncTaskFetch().execute();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}