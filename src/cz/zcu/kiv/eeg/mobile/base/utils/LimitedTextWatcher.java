package cz.zcu.kiv.eeg.mobile.base.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;

/**
 * Common text watcher for EditTexts with limited char count.
 *
 * @author Petr Miko
 */
public class LimitedTextWatcher implements TextWatcher {

    private int charLimit;
    private TextView availableCharsView;
    private Context context;

    public LimitedTextWatcher(int charLimit, TextView availableCharsView) {
        this.charLimit = charLimit;
        this.availableCharsView = availableCharsView;
        this.context = availableCharsView.getContext();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            availableCharsView.setVisibility(View.VISIBLE);
            availableCharsView.setText(context.getString(R.string.app_characters_left) + (charLimit - s.length()))
            ;
        } else {
            availableCharsView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
