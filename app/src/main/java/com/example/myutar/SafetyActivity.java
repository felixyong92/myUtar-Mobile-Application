package com.example.myutar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class SafetyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private List<Safety> safetyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SafetyAdapter mAdapter;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        result = (TextView)findViewById(R.id.no_result);

        new AsyncTaskFetch().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
                return true;
            case R.id.action_sorted:
                getSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        s = s.toLowerCase();
        List<Safety> newList = new ArrayList<>();
        for (Safety safety : safetyList) {
            String title = safety.getTitle().toLowerCase();
            String departmentName = safety.getDepartmentID().toLowerCase();
            if (title.contains(s) || departmentName.contains(s)) {
                newList.add(safety);
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

            progressBar = ProgressDialog.show(SafetyActivity.this, "Fetching Data","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://myutar.000webhostapp.com/MyUTAR/getSafety.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String json;
                while((json = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json);
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
                JSONArray jsonArray = object.getJSONArray("safety");

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

                    safetyList.add(new Safety(jsonObject.getString("dId"), jsonObject.getString("title"),
                            jsonObject.getString("description"), images, attachments, jsonObject.getString("link"),
                            jsonObject.getString("publishDate").substring(0,11)));
                }

                if (safetyList.size() == 0) {
                    result.setVisibility(View.VISIBLE);
                } else {
                    result.setVisibility(View.GONE);
                }

                Collections.reverse(safetyList);

                recyclerView = (RecyclerView) findViewById(R.id.list);

                mAdapter = new SafetyAdapter(safetyList, SafetyActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(SafetyActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                Toast.makeText(SafetyActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getSort(){
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
                        Collections.sort(safetyList, mAdapter.DateDescComparator);
                        mAdapter.getFilter(safetyList);
                        Toast.makeText(SafetyActivity.this, "Sorting in Newest Date Order", Toast.LENGTH_LONG).show();
                        break;
                    case 1: // Oldest
                        Collections.sort(safetyList, mAdapter.DateAscComparator);
                        mAdapter.getFilter(safetyList);
                        Toast.makeText(SafetyActivity.this, "Sorting in Oldest Date Order", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
