package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.DigitizationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching digitizations from eeg base.
 *
 * @author Petr Miko
 */
public class FetchDigitizations extends CommonService<Void, Void, List<Digitization>> {

    private static final String TAG = FetchDigitizations.class.getSimpleName();
    private final DigitizationAdapter digitizationAdapter;

    /**
     * Constructor.
     *
     * @param activity            parent activity
     * @param digitizationAdapter adapter for holding collection of digitizations
     */
    public FetchDigitizations(CommonActivity activity, DigitizationAdapter digitizationAdapter) {
        super(activity);
        this.digitizationAdapter = digitizationAdapter;
    }

    /**
     * Method, where all digitizations are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched digitizations
     */
    @Override
    protected List<Digitization> doInBackground(Void... params) {

        List<?> result = new ArrayList();

        setState(RUNNING, R.string.working_ws_digitization);
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.DIGITISATION.toString());
            result = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Digitization>) result;
    }

    /**
     * Fetched records are put into Digitization adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Digitization> resultList) {
        digitizationAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Digitization>() {
                @Override
                public int compare(Digitization lhs, Digitization rhs) {
                    float sub = lhs.getSamplingRate() - rhs.getSamplingRate();

                    if (sub > 0) return 1;
                    else return sub < 0 ? -1 : 0;
                }
            });

            for (Digitization artifact : resultList) {
                digitizationAdapter.add(artifact);
            }
        }
    }

}
