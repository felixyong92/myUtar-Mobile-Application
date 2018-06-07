package com.example.myutar;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedbackStatusDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_status_detail);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detail_content);

        // Get the Intent that started this activity and extract the string
        // Capture the layout's TextView and set the string as its text
        TextView date = findViewById(R.id.date);
        TextView type = findViewById(R.id.feedback_type);
        TextView contents = findViewById(R.id.contents);
        TextView attachment = findViewById(R.id.attachment);

        date.setText(getIntent().getStringExtra("date"));
        type.setText(getIntent().getStringExtra("type"));

        if (getIntent().getStringExtra("contents") == null) {
            contents.setVisibility(View.GONE);
        } else {
            contents.setText(getIntent().getStringExtra("contents"));
        }

        if (getIntent().getStringArrayExtra("attachment") == null) {
            attachment.setVisibility(View.GONE);
        } else {
            String[] attachments = getIntent().getStringArrayExtra("attachment");

            if (attachments.length > 1) {
                attachment.setText("Attachments :");
            }

            for (int i = 0; i < attachments.length; i++) {
                // Initialize image's url
                final String attachmentUrl = "https://myutar.000webhostapp.com/MyUTAR/basic/web/uploads/feedback_Images/" + attachments[i].toString();
                TextView attachment_file = new TextView(getApplicationContext());
                attachment_file.setText(attachments[i].toString());
                attachment_file.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                attachment_file.setTextColor(getColor(R.color.colorAccent));
                attachment_file.setPaintFlags(attachment_file.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                attachment_file.setPadding(0,10,0,0);
                linearLayout.addView(attachment_file);

                attachment_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(attachmentUrl));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public  void viewResponses(View view) {
        Intent intent = new Intent(this, FeedbackResponsesDetailActivity.class);
        intent.putExtra("status", getIntent().getIntExtra("status", 0));
        intent.putExtra("fId", Integer.toString(getIntent().getIntExtra("id", 0)));
        intent.putExtra("dId", getIntent().getStringExtra("departmentID"));
        startActivity(intent);
    }
}
