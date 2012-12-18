package cz.zcu.kiv.eeg.mobile.base.ui.dash;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cz.zcu.kiv.eeg.mobile.base.R;

public class ExperimentFragment extends Fragment {

    public final static String TAG = ExperimentFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dash_exp, container, false);
    }

}
