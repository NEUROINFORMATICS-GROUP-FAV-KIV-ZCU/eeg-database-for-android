package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new Person on eeg base.
 * Meant mainly for creating new subject.
 *
 * @author Petr Miko
 */
public class CreatePerson extends CommonService<Person, Void, Person> {

    private final static String TAG = CreatePerson.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreatePerson(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where person information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param persons only one Person object is accepted
     * @return information about created user
     */
    @Override
    protected Person doInBackground(Person... persons) {


        Person person = persons[0];

        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.PEOPLES.toString());
            hash.put("hash" + person.hashCode(), person);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return person;
    }

    /**
     * Informs user whether person creation was successful or not.
     *
     * @param person returned user info if any
     */
    @Override
    protected void onPostExecute(Person person) {
        if (person != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_PERSON_KEY, person);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
