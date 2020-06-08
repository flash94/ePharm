package com.example.epharm.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.epharm.fragments.PatientFragment;
import com.example.epharm.fragments.PharmacistFragment;

public class TabViewAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabViewAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PatientFragment patientFragment = new PatientFragment();
                return patientFragment;
            case 1:
                PharmacistFragment pharmacistFragment = new PharmacistFragment();
                return pharmacistFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
