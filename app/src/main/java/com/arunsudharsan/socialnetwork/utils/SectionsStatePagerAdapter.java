package com.arunsudharsan.socialnetwork.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 13/12/17.
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private final HashMap<String, Integer> mFragmentNumbers = new HashMap<>();
    private final HashMap<Integer, String> mFragmentNames = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String fragname) {
        Integer i = mFragmentList.size() - 1;

        mFragmentList.add(fragment);
        mFragments.put(fragment, i);
        mFragmentNumbers.put(fragname, i);
        mFragmentNames.put(i, fragname);
    }

    public Integer getFragmentNumber(String fragname) {
        if (mFragmentNumbers.containsKey(fragname))
            return mFragmentNumbers.get(fragname);
        else return null;
    }
    public Integer getFragmentNumber(Fragment frag) {
        if (mFragments.containsKey(frag))
            return mFragments.get(frag);
        else return null;
    }
    public String getFragmentName(Integer fragmentno) {
        if (mFragmentNames.containsKey(fragmentno))
            return mFragmentNames.get(fragmentno);
        else return null;
    }
}
