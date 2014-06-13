package com.hyeok.m16_chat_pt.CustomView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by GwonHyeok on 2014. 6. 14..
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    String TITLES[] = {"현재채널", "친구목록", "클랜목록"};
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ChatViewpager.newInstance(position);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
