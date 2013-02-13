package cz.zcu.kiv.eeg.mobile.base.ui.dash;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
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


        final TextView descriptionTextCount = (TextView) getActivity().findViewById(R.id.datafile_description_count);

        final TextWatcher datafileDescriptionWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    descriptionTextCount.setVisibility(View.VISIBLE);
                    descriptionTextCount.setText(getString(R.string.data_file_left) + (getResources().getInteger(R.integer.limit_datafile_description_chars) - s.length()));
                }
                else{
                    descriptionTextCount.setVisibility(View.INVISIBLE);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        EditText descriptionText = (EditText) getActivity().findViewById(R.id.datafile_description);
        descriptionText.addTextChangedListener(datafileDescriptionWatcher);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fchooserButton:
                Log.d(TAG, "Choosing file");
                Intent intent = new Intent(getActivity(), FileChooserActivity.class);
                startActivityForResult(intent, Values.SELECT_FILE_FLAG);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Values.SELECT_FILE_FLAG): {
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getExtras().getString(Values.FILE_PATH);
                    Toast.makeText(this.getActivity(), path, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
