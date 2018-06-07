package com.example.myutar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by limrusin on 19/1/2018.
 */

public class BusServiceAdapter extends RecyclerView.Adapter<BusServiceAdapter.ViewHolder> {

    private List<BusService> busServiceList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sender, publishDate, subject;
        List<BusService> busServiceList = new ArrayList<>();
        Context context;
        public ViewHolder(View view, Context context, List<BusService> busServiceList) {
            super(view);
            this.busServiceList = busServiceList;
            this.context = context;
            publishDate = (TextView) view.findViewById(R.id.publishDate);
            sender = (TextView) view.findViewById(R.id.sender);
            subject = (TextView) view.findViewById(R.id.subject);
        }
    }

    public BusServiceAdapter(List<BusService> busServiceList, Context context) {
        this.busServiceList = busServiceList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
        return new ViewHolder(itemView, this.context, busServiceList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BusService busService = busServiceList.get(position);
        holder.publishDate.setText(busService.getPublishDate());
        holder.sender.setText(busService.getDepartmentID());
        holder.subject.setText(busService.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (busService.getDescription().equals("") && busService.getImage() == null && busService.getAttachment() == null && !busService.getLink().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(busService.getLink()));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, BusServiceDetailActivity.class);
                    intent.putExtra("title", busService.getTitle());
                    intent.putExtra("description", busService.getDescription());
                    intent.putExtra("image", busService.getImage());
                    intent.putExtra("attachment", busService.getAttachment());
                    intent.putExtra("link", busService.getLink());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return busServiceList.size();
    }

    public void getFilter(List<BusService> newBusServiceList) {
        busServiceList = new ArrayList<>();
        busServiceList.addAll(newBusServiceList);
        notifyDataSetChanged();
    }

    // Comparator for Ascending Order
    public static Comparator<BusService> DateAscComparator = new Comparator<BusService>() {

        public int compare(BusService app1, BusService app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();
            return date1.compareToIgnoreCase(date2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<BusService> DateDescComparator = new Comparator<BusService>() {

        public int compare(BusService app1, BusService app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();

            return date2.compareToIgnoreCase(date1);
        }
    };
}
