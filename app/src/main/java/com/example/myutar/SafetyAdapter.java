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

public class SafetyAdapter extends RecyclerView.Adapter<SafetyAdapter.ViewHolder> {

    private List<Safety> safetyList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sender, publishDate, subject;
        List<Safety> safetyList = new ArrayList<>();
        Context context;

        public ViewHolder(View view, Context context, List<Safety> safetyList) {
            super(view);
            this.safetyList = safetyList;
            publishDate = (TextView) view.findViewById(R.id.publishDate);
            sender = (TextView) view.findViewById(R.id.sender);
            subject = (TextView) view.findViewById(R.id.subject);
        }
    }

    public SafetyAdapter(List<Safety> safetyList, Context context) {
        this.safetyList = safetyList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
        return new ViewHolder(itemView, this.context, safetyList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Safety safety = safetyList.get(position);
        holder.publishDate.setText(safety.getPublishDate());
        holder.sender.setText(safety.getDepartmentID());
        holder.subject.setText(safety.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (safety.getDescription().equals("") && safety.getImage() == null && safety.getAttachment() == null && !safety.getLink().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(safety.getLink()));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, SafetyDetailActivity.class);
                    intent.putExtra("title", safety.getTitle());
                    intent.putExtra("description", safety.getDescription());
                    intent.putExtra("image", safety.getImage());
                    intent.putExtra("attachment", safety.getAttachment());
                    intent.putExtra("link", safety.getLink());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return safetyList.size();
    }

    public void getFilter(List<Safety> newSafetyList) {
        safetyList = new ArrayList<>();
        safetyList.addAll(newSafetyList);
        notifyDataSetChanged();
    }

    // Comparator for Ascending Order
    public static Comparator<Safety> DateAscComparator = new Comparator<Safety>() {

        public int compare(Safety app1, Safety app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();
            return date1.compareToIgnoreCase(date2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<Safety> DateDescComparator = new Comparator<Safety>() {

        public int compare(Safety app1, Safety app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();

            return date2.compareToIgnoreCase(date1);
        }
    };
}
