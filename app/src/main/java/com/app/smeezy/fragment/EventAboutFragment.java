package com.app.smeezy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventAboutFragment extends BaseFragment {

    @BindView(R.id.tv_event_detail_name)
    TextView tv_name;

    @BindView(R.id.tv_event_detail_location)
    TextView tv_location;

    @BindView(R.id.tv_event_detail_date_time)
    TextView tv_date_time;

    @BindView(R.id.tv_event_detail_desc)
    TextView tv_desc;

    private Event event;

    public EventAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_about, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null){
            event = (Event) getArguments().getSerializable("event");
        }

        setData(event);

        return view;
    }

    public void setData(Event event){

        this.event = event;

        tv_name.setText(event.getTitle());
        tv_location.setText(event.getLocation());
        tv_desc.setText(event.getDescription());

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");

        Date inputDate;

        try {
            inputDate = inputDateFormat.parse(event.getStartDate());
            tv_date_time.setText(outputDateFormat.format(inputDate));
        }catch (ParseException e){
            e.printStackTrace();
        }


    }

}
