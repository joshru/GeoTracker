package com.tcss450.moneyteam.geotracker;

import android.app.ActionBar;
        import android.app.ActionBar.Tab;
        import android.app.Fragment;
        import android.app.FragmentTransaction;

/**
 * Actionbar listener for all tabs on inside MainActivity.
 * @author Alexander, Brandon, Josh
 */
public class PipTabListener implements ActionBar.TabListener {

    /** The fragment context.*/
    Fragment mFragment;

    /**
     *
     * @param fragment
     */
    public PipTabListener(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, mFragment);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}