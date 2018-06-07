package com.example.myutar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FeedbackFormActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICKFILE_RESULT_CODE = 1;
    private static final String FILE_UPLOAD_HTTP_URL = "https://myutar.000webhostapp.com/MyUTAR/uploadImg.php";
    private static final String FEEDBACK_SUBMIT_URL = "https://myutar.000webhostapp.com/MyUTAR/submitFeedback.php";

    private String userID = "1403537";
    private String departmentID = "DSA-SL";

    private String file_path_1, file_path_2, file_path_3, final_file_name_1, final_file_name_2, final_file_name_3;

    EditText comment;

    TextView file_name_1, file_name_2, file_name_3;
    ImageButton delete_1, delete_2, delete_3;

    Spinner spinner;

    Button openFile, submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);
        spinner = (Spinner) findViewById(R.id.feedback_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.feedback_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        comment = (EditText)findViewById(R.id.et_Description);

        file_name_1 = (TextView) findViewById(R.id.file_name1);
        file_name_2 = (TextView) findViewById(R.id.file_name2);
        file_name_3 = (TextView) findViewById(R.id.file_name3);
        delete_1 = (ImageButton) findViewById(R.id.attachment1);
        delete_2 = (ImageButton) findViewById(R.id.attachment2);
        delete_3 = (ImageButton) findViewById(R.id.attachment3);

        openFile = (Button)findViewById(R.id.attachment);
        submitButton = (Button)findViewById(R.id.feedback_submit);

        delete_1.setOnClickListener(this);
        delete_2.setOnClickListener(this);
        delete_3.setOnClickListener(this);
        openFile.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == delete_1) {
            file_path_1 = null;
            final_file_name_1 = null;
            file_name_1.setText("");
            file_name_1.setVisibility(View.GONE);
            delete_1.setVisibility(View.GONE);
        }

        if (view == delete_2) {
            file_path_2 = null;
            final_file_name_2 = null;
            file_name_2.setText("");
            file_name_2.setVisibility(View.GONE);
            delete_2.setVisibility(View.GONE);
        }

        if (view == delete_3) {
            file_path_3 = null;
            final_file_name_3 = null;
            file_name_3.setText("");
            file_name_3.setVisibility(View.GONE);
            delete_3.setVisibility(View.GONE);
        }

        if (view == openFile) {
            if (ContextCompat.checkSelfPermission(FeedbackFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Request the permission
                ActivityCompat.requestPermissions(FeedbackFormActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICKFILE_RESULT_CODE);

            } else {
                Log.d("DEBUG", "Permission has already been granted");
                // Permission has already been granted
                Intent intent = new Intent();
                intent.setType("*/*");
                String[] extraMimeTypes = {"image/*", "application/pdf"};

                    /*
                    * extraMimeTypes  --  doc, ppt, xls, etc
                    * , "text/plain",
                            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                            "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xls & .xlsx
                    */

                intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            }
        }

        if (view == submitButton) {
            String comments = "";
            String attachments = "";

            int type = spinner.getSelectedItemPosition();
            comments = comment.getText().toString();

            if (final_file_name_1 != null) {
                attachments += final_file_name_1;
            }
            if (final_file_name_2 != null) {
                if (final_file_name_1 != null){
                    attachments += ",";
                }
                attachments += final_file_name_2;
            }
            if (final_file_name_3 != null) {
                if (final_file_name_1 != null || final_file_name_2 != null){
                    attachments += ",";
                }
                attachments += final_file_name_3;
            }

            Log.d("Feedback Type", Integer.toString(type));
            Log.d("Comments / Suggestion", comments);

            if (type == 0) {
                Toast.makeText(FeedbackFormActivity.this, "Please choose your feedback type before submitting the feedback", Toast.LENGTH_LONG).show();
            } else if (comments.equals("") && attachments.equals("")){
                Toast.makeText(FeedbackFormActivity.this, "Please either fill in the comments / suggestion / Grievances OR upload related file(s)", Toast.LENGTH_LONG).show();
            } else {
                Log.d("result", attachments);
                SubmitFeedback(type, comments, attachments);
            }
        }
    }

    public void SubmitFeedback(final int type, final String comments, final String attachments){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            ProgressDialog progressBar;
            @Override
            protected  void onPreExecute() {
                super.onPreExecute();
                // Display a progress dialog

                progressBar = ProgressDialog.show(FeedbackFormActivity.this, "Submitting Data","Please Wait...",true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                if(file_path_1 != null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            UploadFile(final_file_name_1, file_path_1);
                        }
                    }).start();
                }
                if(file_path_2 != null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            UploadFile(final_file_name_2, file_path_2);
                        }
                    }).start();
                }
                if(file_path_3 != null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            UploadFile(final_file_name_3, file_path_3);
                        }
                    }).start();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("fType", Integer.toString(type-1)));
                nameValuePairs.add(new BasicNameValuePair("fContent", comments));
                if (!attachments.equals("")) {
                    nameValuePairs.add(new BasicNameValuePair("fAttachment", attachments));
                }
                nameValuePairs.add(new BasicNameValuePair("dId", departmentID));
                nameValuePairs.add(new BasicNameValuePair("uId", userID));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(FEEDBACK_SUBMIT_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                    Log.d("DEBUG HttpResponse", EntityUtils.toString(httpEntity));

                    return "Successfully Submitted! Thank you for your feedback.";
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

                spinner.setSelection(0);

                comment.setText("");

                file_path_1 = null;
                file_path_2 = null;
                file_path_3 = null;
                final_file_name_1 = null;
                final_file_name_2 = null;
                final_file_name_3 = null;

                file_name_1.setText("");
                file_name_2.setText("");
                file_name_3.setText("");

                file_name_1.setVisibility(View.GONE);
                file_name_2.setVisibility(View.GONE);
                file_name_3.setVisibility(View.GONE);

                delete_1.setVisibility(View.GONE);
                delete_2.setVisibility(View.GONE);
                delete_3.setVisibility(View.GONE);

                Toast.makeText(FeedbackFormActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }

        // Execute SendPostReqAsyncTack to send data to server
        new SendPostReqAsyncTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String realPath = "";

        // TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    if (data == null) {
                        return;
                    }

                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        // to make sure the max. file number is not exists
                        if (clipData.getItemCount() > 3 || clipData.getItemCount() > EmptyFileSpace()) {
                            Toast.makeText(FeedbackFormActivity.this, "The max. file number is only 3", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                realPath = GetRealPath(item.getUri());

                                SetTextView(realPath);
                            }
                        }
                    } else {
                        if (EmptyFileSpace() == 0) {
                            Toast.makeText(FeedbackFormActivity.this, "The max. file number is only 3", Toast.LENGTH_LONG).show();
                        } else {
                            realPath = GetRealPath(data.getData());
                            SetTextView(realPath);
                        }
                    }
                }
                break;
        }
    }

    // display selected file name
    public void SetTextView(String realPath) {

        String name = realPath.substring(realPath.lastIndexOf("/")+1);
        String type = realPath.substring(realPath.lastIndexOf(".")+1);
        Log.d("File Name", name);
        String file_name = name.replace(name, userID + GetDateTime());
        Log.d("Changed File Name", file_name);

        if (file_name_1.getText().toString().equals("")) {
            file_path_1 = realPath;
            file_name_1.setText(name);
            final_file_name_1 = file_name + "_1." + type;
            file_name_1.setVisibility(View.VISIBLE);
            delete_1.setVisibility(View.VISIBLE);
        } else if (file_name_2.getText().toString().equals("")) {
            file_path_2 = realPath;
            file_name_2.setText(name);
            final_file_name_2 = file_name + "_2." + type;
            file_name_2.setVisibility(View.VISIBLE);
            delete_2.setVisibility(View.VISIBLE);
        } else {
            file_path_3 = realPath;
            file_name_3.setText(name);
            final_file_name_3 = file_name + "_3." + type;
            file_name_3.setVisibility(View.VISIBLE);
            delete_3.setVisibility(View.VISIBLE);
        }
    }

    // calculate the remaining upload file space
    public int EmptyFileSpace() {
        int emptyFileSpace = 0;

        if (file_path_1 == null)
            emptyFileSpace++;
        if (file_path_2 == null)
            emptyFileSpace++;
        if (file_path_3 == null)
            emptyFileSpace++;

        return emptyFileSpace;
    }

    // get real path of file to be uploaded
    public String GetRealPath(Uri uri) {
        String realPath = "";

        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            realPath = RealPathUtil.getRealPathFromURI_API11to18(this, uri);
        // SDK > 19 (Android 4.4)
        else
            realPath = RealPathUtil.getRealPathFromURI_API19(this, uri);

        return realPath;
    }

    // get current date time
    public String GetDateTime() {
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("_yyyyMMdd_HH_mm_ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        String formattedDate = df.format(currentTime);

        Log.d("Current Time",formattedDate);

        return formattedDate;
    }

    // upload file to server
    public int UploadFile(final String selectedFileName, final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");

        if (!selectedFile.isFile()){
            //dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FeedbackFormActivity.this, "Source File Doesn't Exist: " + selectedFilePath, Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(FILE_UPLOAD_HTTP_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"img_name\"" + lineEnd + lineEnd); // param name
                dataOutputStream.writeBytes(selectedFileName + lineEnd + twoHyphens + boundary + lineEnd); // param value
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.d("TAG", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
//                if(serverResponseCode == 200){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            filePath1.setText("File Upload completed");
//                        }
//                    });
//                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FeedbackFormActivity.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(FeedbackFormActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(FeedbackFormActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            return serverResponseCode;
        }

    }
}