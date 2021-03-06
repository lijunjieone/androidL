package com.y.t.ui.main.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.y.b.tools.reflect.Reflect;
import com.y.t.R;
import com.y.t.ui.main.MainFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {


    FrameLayout mBottomLayout;
    FrameLayout mTopLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_main_framelayout, container, false);
        initView(view);
        return view;
    }

    void initView(View view) {
        mBottomLayout = view.findViewById(R.id.home_bottom_container);
        mTopLayout = view.findViewById(R.id.home_header_contaner);

        init(view);
        initTop(view);
    }

    void initData() {
        for(int i=0;i<20;i++) {
            mList.add(new MainFragment.Item("test1"+i,"com.y.t.ui.main.TestFragment"));
        }
    }

    void init(View view) {
        initData();
        mTabsList = (RecyclerView)View.inflate(getContext(),R.layout.simple_recycleview,null);
        mTabsList.setBackgroundColor(Color.WHITE);
        mTabsList.addItemDecoration(new MainFragment.MarginDecoration(getContext()));
        mTabsList.setAdapter(new MainFragment.ItemAdapter(getActivity(),mList));
//        mTabsList.setHasFixedSize(true);
        mTabsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBottomLayout.addView(mTabsList);

    }

    void initTop(View view) {
        mTabsList2 = (RecyclerView)View.inflate(getContext(),R.layout.simple_recycleview,null);
        mTabsList2.setBackgroundColor(Color.WHITE);
        mTabsList2.addItemDecoration(new MainFragment.MarginDecoration(getContext()));
        mTabsList2.setAdapter(new MainFragment.ItemAdapter(getActivity(),mList));
//        mTabsList.setHasFixedSize(true);
        mTabsList2.setLayoutManager(new GridLayoutManager(getContext(),4));
        mTopLayout.addView(mTabsList2);

    }
    private List<MainFragment.Item> mList = new ArrayList<>();
    RecyclerView mTabsList;
    RecyclerView mTabsList2;







}
