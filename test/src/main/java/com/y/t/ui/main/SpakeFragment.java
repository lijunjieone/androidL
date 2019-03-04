package com.y.t.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;
import com.y.t.R;
import com.y.t.ui.main.view.SwipeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;

public class SpakeFragment extends Fragment {
    SwipeLayout swipe_layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.spak_fragment, container, false);

        initView(view);
        return view;
    }


    void initView(View v){
        SparkView sparkView = (SparkView)v.findViewById(R.id.sparkview);
        float[] data = {1.0f,2.0f,3.1f,4.5f,8.0f};
        sparkView.setAdapter(new MyAdapter(data));

    }

   class MyAdapter extends SparkAdapter {
        private float[] yData;

        public MyAdapter(float[] yData) {
            this.yData = yData;
        }

        @Override
        public int getCount() {
            return yData.length;
        }

        @Override
        public Object getItem(int index) {
            return yData[index];
        }

        @Override
        public float getY(int index) {
            return yData[index];
        }
    }
}
