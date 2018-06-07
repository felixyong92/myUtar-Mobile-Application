package com.example.myutar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BusServiceActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<BusService> busServiceList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BusServiceAdapter mAdapter;
    private String type = "All";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String date = df.format(Calendar.getInstance().getTime());

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        result = (TextView)findViewById(R.id.no_result);

        new AsyncTaskFetch().execute();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        s = s.toLowerCase();
        List<BusService> newList = new ArrayList<>();
        for (BusService busService : busServiceList) {
            String title = busService.getTitle().toLowerCase();
            String departmentName = busService.getDepartmentID().toLowerCase();
            if (title.contains(s) || departmentName.contains(s)) {
                newList.add(busService);
            }
        }

        if (newList.size() == 0) {
            result.setVisibility(View.VISIBLE);
        } else {
            result.setVisibility(View.GONE);
        }

        mAdapter.getFilter(newList);

        return false;
    }

    private class AsyncTaskFetch extends AsyncTask<String, String, String> {

        ProgressDialog progressBar;
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
            // Display a progress dialog
            progressBar = ProgressDialog.show(BusServiceActivity.this, "Fetching Data","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://myutar.000webhostapp.com/MyUTAR/getBus.php");
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
                JSONArray jsonArray = object.getJSONArray("bus");

                busServiceList = new ArrayList<>();

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

                    if (type.equals("All") || (type.equals("Today") && jsonObject.getString("publishDate").substring(0,11).compareTo(date) == 0)
                            || (type.equals("Upcoming") && jsonObject.getString("publishDate").substring(0,11).compareTo(date) > 0)
                            || (type.equals("Archived") && jsonObject.getString("publishDate").substring(0,11).compareTo(date) < 0)) {
                        busServiceList.add(new BusService(jsonObject.getString("dId"), jsonObject.getString("title"),
                                jsonObject.getString("description"), images, attachments, jsonObject.getString("link"),
                                jsonObject.getString("expiryDate"), jsonObject.getString("publishDate").substring(0, 11)));
                    }
                }

                if (busServiceList.size() == 0) {
                    result.setVisibility(View.VISIBLE);
                } else {
                    result.setVisibility(View.GONE);
                }

                recyclerView = (RecyclerView) findViewById(R.id.list);

                mAdapter = new BusServiceAdapter(busServiceList, BusServiceActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(BusServiceActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                Log.d("DEBUG", e.toString());
                Toast.makeText(BusServiceActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
                return true;
            case R.id.action_sorted:
                getBusServiceSort();
                return true;
            case R.id.action_filter:
                getBusServiceFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBusServiceSort(){
        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sorted By (Date)");

        // Add a list_content
        String[] sort = {"Newest", "Oldest"};
        builder.setItems(sort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                switch (selection) {
                    case 0: // Newest
                        Collections.sort(busServiceList, mAdapter.DateDescComparator);
                        mAdapter.getFilter(busServiceList);
                        Toast.makeText(BusServiceActivity.this, "Sorting in Newest Date Order", Toast.LENGTH_LONG).show();
                        break;
                    case 1: // Oldest
                        Collections.sort(busServiceList, mAdapter.DateAscComparator);
                        mAdapter.getFilter(busServiceList);
                        Toast.makeText(BusServiceActivity.this, "Sorting in Oldest Date Order", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getBusServiceFilter(){
        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtered By");

        // Add a list_content
        final String[] sort = {"All", "Today", "Upcoming", "Archived"};
        builder.setItems(sort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                //getTypeFilter(sort[selection].toString());
                type = sort[selection].toString();
                new AsyncTaskFetch().execute();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
