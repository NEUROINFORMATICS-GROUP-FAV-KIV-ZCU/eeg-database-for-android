package cz.zcu.kiv.eeg.mobile.base.ui.base.person;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;

/**
 * @author Petr Miko
 *         Date: 23.2.13
 */
public class PersonAddActivity extends SaveDiscardActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_person_add);
        initView();
    }

    private void initView() {

        final TextView notesCountText = (TextView) findViewById(R.id.person_notes_count);

        final TextWatcher datafileDescriptionWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    notesCountText.setVisibility(View.VISIBLE);
                    notesCountText.setText(getString(R.string.data_file_left) + (getResources().getInteger(R.integer.limit_datafile_description_chars) - s.length()));
                } else {
                    notesCountText.setVisibility(View.INVISIBLE);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        EditText descriptionText = (EditText) findViewById(R.id.person_notes_value);
        descriptionText.addTextChangedListener(datafileDescriptionWatcher);
    }

    @Override
    protected void save() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void discard() {
        finish();
    }
}
