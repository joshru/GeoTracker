package com.tcss450.moneyteam.geotracker;

import android.app.ActionBar;
        import android.app.ActionBar.Tab;
        import android.app.Fragment;
        import android.app.FragmentTransaction;

import com.tcss450.moneyteam.geotracker.fragments.MapFragment;

/**
 * Actionbar listener for all tabs on inside MainActivity.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class PipTabListener implements ActionBar.TabListener {

    /** The fragment context.*/
    Fragment mFragment;

    /**
     * Listener for the tab view
     * @param fragment the fragment selected by user
     */
    public PipTabListener(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * Called when a tab enters the selected state
     * @param tab the tab selected
     * @param ft the fragment transaction
     */
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, mFragment);
    }

    /**
     * Called when a tab exits the selected state
     * @param tab the tab selected
     * @param ft the fragment transaction
     */
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    /**
     * (Unused) Called when a tab that is already is selected is chosen again by the user
     * @param tab
     * @param ft
     */
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}