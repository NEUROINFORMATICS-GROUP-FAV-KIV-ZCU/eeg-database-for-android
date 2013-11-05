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
package cz.zcu.kiv.eeg.mobile.base.archetypes;

import cz.zcu.kiv.eeg.mobile.base.R;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * Archetype for activities with only two ways out of it - accepting displayed data, or rejecting them.
 * Requires implementing accept/reject methods in child classes.
 *
 * Based on Roman Nurik's Done+Discard design pattern.
 * @see <a href="http://www.androiduipatterns.com/2012/08/roman-nurik-on-done-discard.html">Done+Discard design pattern</a>
 *
 * @author Petr Miko
 */
public abstract class SaveDiscardActivity extends CommonActivity implements OnClickListener {

    /**
     * Creates Common activity with Save/Discard buttons in actionbar.
     * @param savedInstanceState information bundle from previously destroyed instance
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_save_cancel, null);

        //set buttons' click listeners
        customActionBarView.findViewById(R.id.actionbar_save).setOnClickListener(this);
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(this);

        //change actionbar behaviour
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
	}

    /**
     * Handling onClick events.
     * @param v source of onClick event
     */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.actionbar_save:
			save();
			break;
		case R.id.actionbar_cancel:
			discard();
			break;
		}
	}

    /**
     * Actions performed when save option is chosen.
     * Data should be gathered and stored.
     */
	protected abstract void save();

    /**
     * Actions performed when discard option is chosed.
     * Data should not be gathered, but cleaned.
     */
	protected abstract void discard();
}
