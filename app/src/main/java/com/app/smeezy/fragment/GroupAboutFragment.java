package com.app.smeezy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.Event;
import com.app.smeezy.responsemodels.Group;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupAboutFragment extends BaseFragment {

    @BindView(R.id.tv_group_detail_name)
    TextView tv_name;

    @BindView(R.id.tv_group_detail_desc)
    TextView tv_desc;

    private Group group;

    public GroupAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_about, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null){
            group = (Group) getArguments().getSerializable("group");
        }

        setData(group);

        return view;
    }

    public void setData(Group group){
        this.group = group;

        tv_name.setText(group.getTitle());
        tv_desc.setText(group.getDescription());

    }

}
