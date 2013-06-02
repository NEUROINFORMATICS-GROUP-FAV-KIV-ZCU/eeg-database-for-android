package cz.zcu.kiv.eeg.mobile.base.db;

import android.os.Environment;
import android.util.Log;
import net.rehacktive.wasp.WaspDb;
import net.rehacktive.wasp.WaspFactory;
import net.rehacktive.wasp.WaspHash;
import net.rehacktive.wasp.exceptions.WaspFatalException;

/**
 * Created with IntelliJ IDEA.
 * User: Petr
 * Date: 20.5.13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class WaspDbSupport {

    protected WaspDb db;
    private static final String TAG = WaspDbSupport.class.getSimpleName();

    public WaspDbSupport() throws WaspFatalException{
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dbname = "EEG_ERP_Database";



        if (!WaspFactory.existsDatabase(path, dbname)) {
            Log.d(TAG, "Creating db: " + dbname);
            db = WaspFactory.createDatabase(path, dbname);

        } else {
            Log.d(TAG, "Getting db: " + dbname);
            db = WaspFactory.loadDatabase(path, dbname);
        }

    }

    public WaspDb getDb() throws Exception {
        return db;
    }

    public WaspHash getOrCreateHash(String hashName) throws WaspFatalException{
        WaspHash hash = null;
        if(db.existsHash(hashName)) {
            Log.d(TAG, "Getting Hash: " + hashName);
             hash = db.getHash(hashName);
        } else {
            Log.d(TAG, "Creating Hash: " + hashName);
            hash = db.createHash(hashName);
        }
        return hash;
    }
}
