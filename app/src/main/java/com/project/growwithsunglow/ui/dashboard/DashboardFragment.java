package com.project.growwithsunglow.ui.dashboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;

import com.project.growwithsunglow.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private ViewPager2 viewPager;
    private PagerAdapter pagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewPager = view.findViewById(R.id.view_pager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Graph());
        fragments.add(new Graph2());
        fragments.add(new Graph3());
        fragments.add(new Graph4());
        fragments.add(new Graph5());

        pagerAdapter = new PagerAdapter(this, fragments);
        viewPager.setAdapter(pagerAdapter);



        return view;
    }



}
