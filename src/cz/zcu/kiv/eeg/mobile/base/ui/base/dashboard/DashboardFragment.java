package cz.zcu.kiv.eeg.mobile.base.ui.base.dashboard;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.ui.base.person.PersonAddActivity;

public class DashboardFragment extends Fragment {

    public static final String TAG = DashboardFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_dashboard, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dash_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person_add:
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonAddActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
