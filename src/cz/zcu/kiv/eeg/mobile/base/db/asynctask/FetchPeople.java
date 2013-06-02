package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.PersonAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.RUNNING;

/**
 * Common service (Asynctask) for fetching people from eeg base.
 *
 * @author Petr Miko
 */
public class FetchPeople extends CommonService<Void, Void, List<Person>> {

    private static final String TAG = FetchPeople.class.getSimpleName();
    private final PersonAdapter personAdapter;

    /**
     * Constructor.
     *
     * @param activity      parent activity
     * @param PersonAdapter adapter for holding collection of people
     */
    public FetchPeople(CommonActivity activity, PersonAdapter PersonAdapter) {
        super(activity);
        this.personAdapter = PersonAdapter;
    }

    /**
     * Method, where all people are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched people
     */
    @Override
    protected List<Person> doInBackground(Void... params) {

        setState(RUNNING, R.string.working_ws_people);
        try {

            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash res = dbSupport.getOrCreateHash(HashConstants.PEOPLES.toString());
            List<?> persons = res.getAllValues();


          return (List<Person>) persons;

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return Collections.emptyList();
    }

    /**
     * Fetched people are put into Person adapter and sorted.
     *
     * @param resultList fetched people
     */
    @Override
    protected void onPostExecute(List<Person> resultList) {
        personAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Person>() {
                @Override
                public int compare(Person lhs, Person rhs) {
                    return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }
            });

            for (Person person : resultList) {
                personAdapter.add(person);
            }
        }
    }

}
