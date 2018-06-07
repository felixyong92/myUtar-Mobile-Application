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

public class FeedbackResponseAdapter extends RecyclerView.Adapter<FeedbackResponseAdapter.ViewHolder>{

    private List<FeedbackResponse> feedbackResponseList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, transaction, department;
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        Context context;
        public ViewHolder(View view, Context context, List<FeedbackResponse> feedbackResponseList) {
            super(view);
            this.feedbackResponseList = feedbackResponseList;
            this.context = context;
            date = (TextView) view.findViewById(R.id.date);
            transaction = (TextView) view.findViewById(R.id.transaction);
            department = (TextView) view.findViewById(R.id.department);
        }
    }

    public FeedbackResponseAdapter(List<FeedbackResponse> feedbackResponseList, Context context) {
        this.feedbackResponseList = feedbackResponseList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_feedback_responses, parent, false);
        return new ViewHolder(itemView, this.context, feedbackResponseList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FeedbackResponse feedbackResponse = feedbackResponseList.get(position);
        holder.date.setText(feedbackResponse.getDate());
        holder.transaction.setText(feedbackResponse.getResponse());
        holder.department.setText(feedbackResponse.getDepartmentID());
    }

    @Override
    public int getItemCount() {
        return feedbackResponseList.size();
    }
}
