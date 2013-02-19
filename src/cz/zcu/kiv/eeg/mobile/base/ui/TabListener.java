package cz.zcu.kiv.eeg.mobile.base.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final Class<T> classType;

    /** Constructor used each time a new tab is created.
     * @param activity  The host Activity, used to instantiate the fragment
     * @param tag  The identifier tag for the fragment
     * @param classType  The fragment's Class, used to instantiate the fragment
     */
    public TabListener(Activity activity, String tag, Class<T> classType) {
        this.activity = activity;
        this.tag = tag;
        this.classType = classType;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        if(fragment != null){
            ft.attach(fragment);
        }

        fragment = activity.getFragmentManager().findFragmentByTag(tag);
        if(fragment != null){
            ft.attach(fragment);
        }else{


            // If not, instantiate and add it to the activity
            fragment = Fragment.instantiate(activity, classType.getName());
            ft.add(android.R.id.content, fragment, tag);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(fragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}