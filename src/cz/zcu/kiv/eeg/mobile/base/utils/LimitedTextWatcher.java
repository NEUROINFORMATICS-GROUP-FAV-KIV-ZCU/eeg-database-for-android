/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
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

    /**
     * Constructor setting basic text watcher characteristics.
     *
     * @param charLimit          max text length
     * @param availableCharsView view where available character count will be displayed in
     */
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
