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
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.data.PersonData;
import cz.zcu.kiv.eeg.mobile.base.ws.eegbase.CreatePerson;

/**
 * @author Petr Miko
 *         Date: 23.2.13
 */
public class PersonAddActivity extends SaveDiscardActivity {

    private final static String DATE_PATTERN = "dd/MM/yyyy";

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
                    notesCountText.setText(getString(R.string.app_characters_left) + (getResources().getInteger(R.integer.limit_description_chars) - s.length()));
                } else {
                    notesCountText.setVisibility(View.INVISIBLE);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        EditText descriptionText = (EditText) findViewById(R.id.person_notes_value);
        descriptionText.addTextChangedListener(datafileDescriptionWatcher);

        TextView birthday = (TextView) findViewById(R.id.person_birthday_label);
        birthday.setText(birthday.getText().toString() + " (" + DATE_PATTERN + ")");
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

        validateAndRun(person);
    }

    private void validateAndRun(PersonData person) {

        if (!ConnectionUtils.isOnline(this)) {
            showAlert(getString(R.string.error_offline));
            return;
        }

        StringBuilder error = new StringBuilder();

        if (ValidationUtils.isEmpty(person.getName()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.person_name)).append(")").append('\n');
        if (ValidationUtils.isEmpty(person.getSurname()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.person_surname)).append(")").append('\n');
        if (!ValidationUtils.isEmailValid(person.getEmail()))
            error.append(getString(R.string.error_invalid_mail_format)).append('\n');
        if (!ValidationUtils.isDateValid(person.getBirthday(), DATE_PATTERN))
            error.append(getString(R.string.error_invalid_date_format)).append(" (").append(DATE_PATTERN).append(")").append('\n');

        if (error.toString().isEmpty()) {
            service = (CommonService) new CreatePerson(this).execute(person);
        } else {
            showAlert(error.toString());
        }
    }

    @Override
    protected void discard() {
        finish();
    }
}
