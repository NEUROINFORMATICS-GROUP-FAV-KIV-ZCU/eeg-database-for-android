package cz.zcu.kiv.eeg.mobile.base.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class SwipeTabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {


    private final Context context;
    private final ActionBar actionBar;
    private final ViewPager viewPager;
    private final List<TabInfo> tabInfos = new ArrayList<TabInfo>();

    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    public SwipeTabsAdapter(FragmentActivity activity, ActionBar actionBar, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        context = activity;
        this.actionBar = actionBar;
        viewPager = pager;
        viewPager.setAdapter(this);
        viewPager.setOnPageChangeListener(this);
    }

    public void addTab(int tabName, Class<?> aClass, Bundle args) {
        TabInfo info = new TabInfo(aClass, args);
        ActionBar.Tab tab = actionBar.newTab().setText(context.getText(tabName));
        tab.setTag(info);
        tab.setTabListener(this);
        tabInfos.add(info);
        actionBar.addTab(tab);
        notifyDataSetChanged();
    }


    public int getCount() {
        return tabInfos.size();
    }

    public Fragment getItem(int position) {
        TabInfo info = tabInfos.get(position);
        return Fragment.instantiate(context, info.clss.getName(), info.args);
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }


    public void onPageScrollStateChanged(int state) {
    }


    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
        Object tag = tab.getTag();
        for (int i = 0; i < tabInfos.size(); i++) {
            if (tabInfos.get(i) == tag) {
                viewPager.setCurrentItem(i);
            }
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

}