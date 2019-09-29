package com.y.t.ui.main;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.y.t.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SpanFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.span_fragment, container, false);
        initView(view);
        return view;
    }

    void initView(View root) {
        SpannableString spannableString = new SpannableString("test");
        TextView textView = root.findViewById(R.id.message);
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);


    }
}
