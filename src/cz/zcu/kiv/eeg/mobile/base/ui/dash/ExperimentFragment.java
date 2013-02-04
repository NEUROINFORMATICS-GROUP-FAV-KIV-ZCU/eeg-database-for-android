package cz.zcu.kiv.eeg.mobile.base.ui.dash;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.ui.filechooser.FileChooserActivity;

public class ExperimentFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = ExperimentFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dash_exp, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button fChooserButton = (Button) getActivity().findViewById(R.id.fchooserButton);
        fChooserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fchooserButton:
                Log.d(TAG, "Choosing file");
                Intent intent = new Intent(getActivity(), FileChooserActivity.class);
                startActivityForResult(intent, Constants.SELECT_FILE_FLAG);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Constants.SELECT_FILE_FLAG): {
                if (resultCode == Activity.RESULT_OK) {
                    TextView fChooserPathView = (TextView) getActivity().findViewById(R.id.fchooserPathView);
                    String path = data.getExtras().getString(Constants.FILE_PATH);
                    fChooserPathView.setText(path);
                }
                break;
            }
        }
    }
}
