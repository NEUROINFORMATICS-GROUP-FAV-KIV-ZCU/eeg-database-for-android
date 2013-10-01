package cz.zcu.kiv.eeg.mobile.base.data.ui.dbsynchonization;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.R.layout;
import cz.zcu.kiv.eeg.mobile.base.R.menu;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
/**
 * 
 * Activity class DBLogin allows connection to remoted EEG Database and download data 
 * 
 * @author Jan Hrbek
 *
 */
public class DbLogin extends CommonActivity {

	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_login);
		
		button = (Button)findViewById(R.id.buttonSynchronize);
	
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				synchronize();
			}
		});
	
	}
	
    
    
    
    public void synchronize(){
    	
    	
    }
    
}
