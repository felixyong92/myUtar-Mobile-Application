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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sender, publishDate, subject;
        List<Notification> notificationList = new ArrayList<>();
        Context context;

        public ViewHolder(View view, Context context, List<Notification> notificationList) {
            super(view);
            this.notificationList = notificationList;
            publishDate = (TextView) view.findViewById(R.id.publishDate);
            sender = (TextView) view.findViewById(R.id.sender);
            subject = (TextView) view.findViewById(R.id.subject);
        }
    }

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
        return new ViewHolder(itemView, this.context, notificationList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notification notification = notificationList.get(position);
        holder.publishDate.setText(notification.getPublishDate());
        holder.sender.setText(notification.getDepartmentID());
        holder.subject.setText(notification.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.getDescription().equals("") && notification.getImage() == null && notification.getAttachment() == null && !notification.getLink().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(notification.getLink()));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, NotificationDetailActivity.class);
                    intent.putExtra("title", notification.getTitle());
                    intent.putExtra("description", notification.getDescription());
                    intent.putExtra("image", notification.getImage());
                    intent.putExtra("attachment", notification.getAttachment());
                    intent.putExtra("link", notification.getLink());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void getFilter(List<Notification> newNotificationList) {
        notificationList = new ArrayList<>();
        notificationList.addAll(newNotificationList);
        notifyDataSetChanged();
    }

    // Comparator for Ascending Order
    public static Comparator<Notification> DateAscComparator = new Comparator<Notification>() {

        public int compare(Notification app1, Notification app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();
            return date1.compareToIgnoreCase(date2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<Notification> DateDescComparator = new Comparator<Notification>() {

        public int compare(Notification app1, Notification app2) {

            String date1 = app1.getPublishDate();
            String date2 = app2.getPublishDate();

            return date2.compareToIgnoreCase(date1);
        }
    };
}