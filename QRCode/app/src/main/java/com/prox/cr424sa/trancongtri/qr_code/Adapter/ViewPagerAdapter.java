package com.prox.cr424sa.trancongtri.qr_code.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prox.cr424sa.trancongtri.qr_code.CreatedFragment;
import com.prox.cr424sa.trancongtri.qr_code.R;
import com.prox.cr424sa.trancongtri.qr_code.ScannedFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 2;
    private Context mContext = null;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ScannedFragment();
            case 1:
                return new CreatedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.scanned_tab_title);
            case 1:
                return mContext.getString(R.string.created_tab_title);
            default:
                return null;
        }
    }
}
