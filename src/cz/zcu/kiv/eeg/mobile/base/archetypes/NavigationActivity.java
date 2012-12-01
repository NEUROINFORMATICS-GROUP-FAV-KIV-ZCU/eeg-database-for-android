package cz.zcu.kiv.eeg.mobile.base.archetypes;

import cz.zcu.kiv.eeg.mobile.base.ui.dash.DashBoardActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.AgendaActivity;
import android.app.ActionBar;
import android.content.Intent;

public class NavigationActivity extends CommonActivity implements ActionBar.OnNavigationListener {

	
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		
		
		Intent intent = new Intent();
		
		switch(itemPosition){
		case 0:
			if(! (this instanceof DashBoardActivity))
				intent.setClass(this, DashBoardActivity.class);
			break;
		case 1:
			if(! (this instanceof AgendaActivity))
				intent.setClass(this, AgendaActivity.class);
			break;
		case 2:
			return false;
		}
		startActivity(intent);
		return true;
	}

}
