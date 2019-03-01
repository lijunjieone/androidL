package com.y.t.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.y.t.R;
import com.y.t.ui.main.view.SwipeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;

public class SwipeFragment extends Fragment {
    SwipeLayout swipe_layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.swipe_layout, container, false);
        swipe_layout = (SwipeLayout) view.findViewById(R.id.swipe_layout);
        swipe_layout.setSwipeEdge(ViewDragHelper.EDGE_ALL);
        swipe_layout.setOnFinishScroll(new SwipeLayout.OnFinishScroll() {
            @Override
            public void complete() {
                SwipeFragment.this.getChildFragmentManager().popBackStack();
//                completefinish();
            }
        });
        return view;
    }
}
