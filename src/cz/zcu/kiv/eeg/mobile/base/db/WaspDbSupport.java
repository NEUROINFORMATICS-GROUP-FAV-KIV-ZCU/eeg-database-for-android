package cz.zcu.kiv.eeg.mobile.base.db;

import android.os.Environment;
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

    public WaspDbSupport() throws WaspFatalException{
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dbname = "myDatabase";


        if (!WaspFactory.existsDatabase(path, dbname)) {
            db = WaspFactory.createDatabase(path, dbname);

        } else {
            db = WaspFactory.loadDatabase(path, dbname);
        }

    }

    public WaspDb getDb() throws Exception {
        return db;
    }

    public WaspHash getHash(String hashName) throws WaspFatalException{
        WaspHash hash = null;
        if(db.existsHash(hashName)) {
             hash = db.getHash(hashName);
        } else {
            hash = db.createHash(hashName);
        }
        return hash;
    }
}
