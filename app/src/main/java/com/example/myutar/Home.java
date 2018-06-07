package com.example.myutar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton btn_bus_service = (ImageButton)view.findViewById(R.id.button_bus_service);
        btn_bus_service.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),BusServiceActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_notification = (ImageButton)view.findViewById(R.id.button_notification);
        btn_notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),NotificationActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_event = (ImageButton)view.findViewById(R.id.button_event);
        btn_event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),EventActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_health_care = (ImageButton)view.findViewById(R.id.button_healthcare);
        btn_health_care.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),HealthCareActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_safety = (ImageButton)view.findViewById(R.id.button_safety);
        btn_safety.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),SafetyActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_feedback_form = (ImageButton)view.findViewById(R.id.button_feedback_form);
        btn_feedback_form.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),FeedbackFormActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_feedback_status = (ImageButton)view.findViewById(R.id.button_feedback_status);
        btn_feedback_status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),FeedbackStatusActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
