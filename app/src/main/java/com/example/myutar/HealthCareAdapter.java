package com.example.myutar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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

public class HealthCareAdapter extends RecyclerView.Adapter<HealthCareAdapter.ViewHolder> {

    private List<HealthCare> healthCareList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sender, publishDate, subject;
        List<HealthCare> healthCareList = new ArrayList<>();
        Context context;

        public ViewHolder(View view, Context context, List<HealthCare> healthCareList) {
            super(view);
            this.healthCareList = healthCareList;
            publishDate = (TextView) view.findViewById(R.id.publishDate);
            sender = (TextView) view.findViewById(R.id.sender);
            subject = (TextView) view.findViewById(R.id.subject);
        }
    }

    public HealthCareAdapter(List<HealthCare> healthCareList, Context context) {
        this.healthCareList = healthCareList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
        return new ViewHolder(itemView, this.context, healthCareList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HealthCare healthCare = healthCareList.get(position);
        holder.publishDate.setText(healthCare.getPublishDate());
        holder.sender.setText(healthCare.getDepartmentID());
        holder.subject.setText(healthCare.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (healthCare.getDescription().equals("") && healthCare.getImage() == null && healthCare.getAttachment() == null && !healthCare.getLink().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(healthCare.getLink()));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, HealthCareDetailActivity.class);
                    intent.putExtra("title", healthCare.getTitle());
                    intent.putExtra("description", healthCare.getDescription());
                    intent.putExtra("image", healthCare.getImage());
                    intent.putExtra("attachment", healthCare.getAttachment());
                    intent.putExtra("link", healthCare.getLink());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return healthCareList.size();
    }

    public void getFilter(List<HealthCare> newHealthCareList) {
        healthCareList = new ArrayList<>();
        healthCareList.addAll(newHealthCareList);
        notifyDataSetChanged();
    }

    // Comparator for Ascending Order
    public static Comparator<HealthCare> DateAscComparator = new Comparator<HealthCare>() {

        public int compare(HealthCare app1, HealthCare app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();
            return date1.compareToIgnoreCase(date2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<HealthCare> DateDescComparator = new Comparator<HealthCare>() {

        public int compare(HealthCare app1, HealthCare app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();

            return date2.compareToIgnoreCase(date1);
        }
    };
}
