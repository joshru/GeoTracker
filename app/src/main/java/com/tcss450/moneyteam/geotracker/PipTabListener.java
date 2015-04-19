package com.tcss450.moneyteam.geotracker;

import android.app.ActionBar;
        import android.app.ActionBar.Tab;
        import android.app.Fragment;
        import android.app.FragmentTransaction;

import com.tcss450.moneyteam.geotracker.R;

public class PipTabListener implements ActionBar.TabListener {

    Fragment mFragment;

    public PipTabListener(Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.mFragment = fragment;
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        ft.replace(R.id.fragment_container, mFragment);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        ft.remove(mFragment);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
}