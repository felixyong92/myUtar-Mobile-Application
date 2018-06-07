package com.example.myutar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limrusin on 19/1/2018.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{

    private List<Feedback> feedbackList;
    private String[] statusType = {"New", "Processing", "Closed (Pending)", "Reopened", "Closed"};
    private String[] feedbackTypes = {"Academic Matters - unit registration, assessment, timetable, etc","Admission, Enrolment and Credit Transfer Matters","Alumni and Graduates Matters - convocation, job zone, etc.","Examinations - procedures, timetable, venue, results, etc","Fees and Refunds","General Cleanliness and Upkeep","General Facilities - air-con, building maintenance, etc","IT Hardware - Computer labs, printer, network, etc","IT Software - Student portal, systems, etc","Labs, Teaching Facilities at Faculty/Centre","Library Facilities and Services","Loans and Scholarships - PTPTN, other loans, etc","Security and Car Parking - Safety, guards, parking, etc","Student Services- bus schedule, car stickers, sport facilities, etc.","UTAR Bus Service","UTAR Policy Matters, Rules and Regulations"};
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, status, subject;
        List<Feedback> feedbackList = new ArrayList<>();
        Context context;
        public ViewHolder(View view, Context context, List<Feedback> feedbackList) {
            super(view);
            this.feedbackList = feedbackList;
            this.context = context;
            status = (TextView) view.findViewById(R.id.publishDate);
            date = (TextView) view.findViewById(R.id.sender);
            subject = (TextView) view.findViewById(R.id.subject);
        }
    }

    public FeedbackAdapter(List<Feedback> feedbackList, Context context) {
        this.feedbackList = feedbackList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
        return new ViewHolder(itemView, this.context, feedbackList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Feedback feedback = feedbackList.get(position);
        holder.status.setText(statusType[feedback.getStatus()]);
        holder.date.setText(feedback.getDate().substring(0,11));
        holder.subject.setText(feedbackTypes[feedback.getType()]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FeedbackStatusDetailActivity.class);
                intent.putExtra("id", feedback.getId());
                intent.putExtra("type", feedbackTypes[feedback.getType()]);
                intent.putExtra("status", feedback.getStatus());
                intent.putExtra("contents", feedback.getContents());
                intent.putExtra("date", feedback.getDate());
                intent.putExtra("attachment", feedback.getAttachment());
                intent.putExtra("departmentID", feedback.getDepartmentID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
}
