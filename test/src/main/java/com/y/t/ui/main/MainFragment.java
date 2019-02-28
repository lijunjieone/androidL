package com.y.t.ui.main;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.y.b.tools.reflect.Reflect;
import com.y.t.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

//    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_recycleview, container, false);
        init(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    void initData() {
        mList.add(new Item("test1","com.y.t.ui.main.TestFragment"));
        mList.add(new Item("home","com.y.t.ui.main.home.HomeFragment"));
        mList.add(new Item("home2","com.y.t.ui.main.home.HomeFragment2"));
        mList.add(new Item("actvity1","com.y.t.SettingsActivity"));
        mList.add(new Item("alert","com.y.t.ui.main.AlertFragment"));
        mList.add(new Item("livedata","com.y.t.ui.main.LiveDataFragment"));
    }

    void init(View view) {
        initData();
        mTabsList = (RecyclerView)view.findViewById(R.id.test_layout);
        mTabsList.setBackgroundColor(Color.WHITE);
        mTabsList.addItemDecoration(new MarginDecoration(getContext()));
        mTabsList.setAdapter(new ItemAdapter(getActivity(),mList));
//        mTabsList.setHasFixedSize(true);
        mTabsList.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    private List<Item> mList = new ArrayList<>();
    RecyclerView mTabsList;


    public static class MarginDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public MarginDecoration(Context context) {
//            margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
            margin = 8;
        }

        @Override
        public void getItemOffsets(
                Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(margin, margin, margin, margin);
        }
    }

    public static class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        FragmentActivity mContext;
        List<Item> mList;


        public Context getContext() {
            return mContext;
        }

        public ItemAdapter(FragmentActivity context, List<Item> list) {
            mContext = context;
            mList = list;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View v = View.inflate(mContext,R.layout.tabs_item,null);
            View v=LayoutInflater.from(getContext()).inflate(R.layout.item, parent,false);

//            v.setBackgroundColor(Color.RED);
            ItemHolder holder = new ItemHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
            if(mList!=null&&mList.size()>position) {
                final Item historyEntity = mList.get(position);
                if(holder!=null) {
                    holder.title.setText(historyEntity.title);
                    holder.itemView.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
//                            openNewUrl(historyEntity.getUrl());
                            String className = historyEntity.className;
                            if(className == null) {
                                Toast.makeText(getContext(),"no class",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(className.endsWith("Activity")) {
                                Intent intent = new Intent();
                                intent.setClassName(mContext,className);
                                mContext.startActivity(intent);

                            }else {
                                Fragment fragment = (Fragment) Reflect.on(className,getContext().getClassLoader()).create().get();
                                mContext.getSupportFragmentManager().beginTransaction()
                                        .add(R.id.container, fragment).addToBackStack("")
                                        .commit();
                            }

                        }
                    });
                }
            }


        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


    public static class ItemHolder extends  RecyclerView.ViewHolder {
        TextView title;
        TextView url;
        ImageView icon;
        ImageView delete;
        View root;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            title = (TextView)itemView.findViewById(R.id.item_title);
        }
    }


    public static class Item {
        String title;
        String className;

        public Item(String title,String className) {
            this.title = title;
            this.className = className;
        }
    }

}
