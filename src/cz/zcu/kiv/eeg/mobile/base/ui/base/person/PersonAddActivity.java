package cz.zcu.kiv.eeg.mobile.base.ui.base.person;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.ws.data.PersonData;
import cz.zcu.kiv.eeg.mobile.base.ws.eegbase.CreatePerson;

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
        EditText name = (EditText) findViewById(R.id.person_name_value);
        EditText surname = (EditText) findViewById(R.id.person_surname_value);
        EditText mail = (EditText) findViewById(R.id.person_mail_value);
        Spinner gender = (Spinner) findViewById(R.id.person_gender_value);
        EditText birthday = (EditText) findViewById(R.id.person_birthday_value);
        CompoundButton leftHanded = (CompoundButton) findViewById(R.id.person_lefthand_value);
        EditText notes = (EditText) findViewById(R.id.person_notes_value);
        EditText phone = (EditText) findViewById(R.id.person_phone_value);

        PersonData person = new PersonData();

        person.setName(name.getText().toString());
        person.setSurname(surname.getText().toString());
        person.setEmail(mail.getText().toString());
        person.setGender(gender.getSelectedItemPosition() == 0 ? "M" : "Y");
        person.setBirthday(birthday.getText().toString());
        person.setNotes(notes.getText().toString());
        person.setLeftHanded(leftHanded.isChecked() ? "L" : "R");
        person.setPhone(phone.getText().toString());

        service = (CommonService) new CreatePerson(this).execute(person);
    }

    @Override
    protected void discard() {
        finish();
    }
}
