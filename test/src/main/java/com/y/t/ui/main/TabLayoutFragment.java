package com.y.t.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.y.b.tools.Log;
import com.y.t.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TabLayoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tablayout_fragment, container, false);
        initView(view);
        return view;
    }

    int mCount = 4;

    void initView(View root) {
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        //添加标签
        for(int i=0;i<mCount;i++){
            tabLayout.addTab(tabLayout.newTab().setText("tab"+i));
        }
        final ViewPager viewPager = (ViewPager)root.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PageAdapter(this.getFragmentManager(),tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //绑定tab点击事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class PageAdapter extends FragmentPagerAdapter {

        private int num;

        public PageAdapter(FragmentManager fm, int num) {
            super(fm);
            this.num = num;
        }

        @Override
        public Fragment getItem(int position) {
            return createFragment(position);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        private Fragment createFragment(int pos) {
            return new TestFragment();
        }
    }

}
