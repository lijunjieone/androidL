package com.y.t.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.y.t.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class AlertFragment extends Fragment {

    TextView mAlert;
    TextView mAlert2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.alert_fragment, container, false);
        initView(view);
        return view;
    }


    void initView(View v) {
        mAlert = v.findViewById(R.id.alert1);
        mAlert2 = v.findViewById(R.id.alert2);
        ((View) mAlert2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("message").setTitle("title").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"ok",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("cacnel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"cancel",Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });


    }
}
