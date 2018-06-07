package com.example.myutar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by limrusin on 19/1/2018.
 */

public class EventAdapter extends BaseAdapter {

    private List<Event> eventList;
    Context context;

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            String imageUrl = "https://myutar.000webhostapp.com/MyUTAR/basic/web/defaultUTAR.jpg";
            if (eventList.get(i).getImage() != null) {
                imageUrl = "https://myutar.000webhostapp.com/MyUTAR/basic/web/uploads/images/" + eventList.get(i).getImage()[0];
            }
            grid = new View(context);
            grid = inflater.inflate(R.layout.grid_content, null);
            TextView eventTitle = (TextView) grid.findViewById(R.id.event_title);
            TextView eventDate = (TextView) grid.findViewById(R.id.event_date);
            ImageView imageView = (ImageView)grid.findViewById(R.id.event_image);
            eventTitle.setText(eventList.get(i).getTitle());
            eventDate.setText(eventList.get(i).getExpiryDate());
            Picasso.with(context).load(imageUrl).into(imageView);


            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra("title", eventList.get(i).getTitle());
                        intent.putExtra("description", eventList.get(i).getDescription());
                        intent.putExtra("image", eventList.get(i).getImage());
                        intent.putExtra("attachment", eventList.get(i).getAttachment());
                        context.startActivity(intent);
                }
            });
        } else {
            grid = (View) view;
        }

        return grid;
    }
}
