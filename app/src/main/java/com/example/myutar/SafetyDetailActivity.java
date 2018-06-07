package com.example.myutar;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SafetyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_detail_view);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detaial_content);

        // Get the Intent that started this activity and extract the string
        // Capture the layout's TextView and set the string as its text
        TextView title = findViewById(R.id.subject);
        TextView description = findViewById(R.id.description);
        TextView link = findViewById(R.id.link);
        TextView url = findViewById(R.id.url);
        TextView attachment = findViewById(R.id.attachment);

        title.setText(getIntent().getStringExtra("title"));

        if (getIntent().getStringExtra("description").equals("")) {
            description.setVisibility(View.GONE);
        } else {
            description.setText(getIntent().getStringExtra("description"));
        }
        if (getIntent().getStringExtra("link").equals("")) {
            link.setVisibility(View.GONE);
            url.setVisibility(View.GONE);
        } else {
            url.setText(getIntent().getStringExtra("link"));
        }

        if (getIntent().getStringArrayExtra("attachment") == null) {
            attachment.setVisibility(View.GONE);
        } else {
            String[] attachments = getIntent().getStringArrayExtra("attachment");
            final String attachmentUrl = "https://myutar.000webhostapp.com/MyUTAR/basic/web/uploads/attachments/";

            if (attachments.length > 1) {
                attachment.setText("Attachments:");
            }

            for (int i = 0; i < attachments.length; i++) {
                final TextView attachment_file = new TextView(getApplicationContext());
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
                        intent.setData(Uri.parse(attachmentUrl + attachment_file.getText()));
                        startActivity(intent);
                    }
                });

            }
        }


        if (getIntent().getStringArrayExtra("image") != null) {
            String[] images = getIntent().getStringArrayExtra("image");

            for(int i = 0; i < images.length; i ++) {
                // Initialize image's url
                String imageUrl = "https://myutar.000webhostapp.com/MyUTAR/basic/web/uploads/images/" + images[i].toString();

                // Initialize a new ImageView widget
                ImageView iv = new ImageView(getApplicationContext());

                // Set an image for ImageView
                Picasso.with(this).load(imageUrl).into(iv);

                // Create layout parameters for ImageView
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Add layout parameters, padding & adjust view bounds to ImageView
                iv.setLayoutParams(lp);
                iv.setPadding(0,50,0,0);
                iv.setAdjustViewBounds(true);

                // Finally, add the ImageView to layout
                linearLayout.addView(iv);
            }
        }
    }
}
