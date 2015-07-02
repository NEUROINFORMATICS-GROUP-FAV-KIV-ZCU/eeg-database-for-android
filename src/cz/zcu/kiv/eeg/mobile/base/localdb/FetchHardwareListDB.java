package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.data.adapter.DigitizationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.HardwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchHardwareListDB {

    private Database database;
    private Context ctx;

    public FetchHardwareListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Hardware FetchHardwareById(String docId ){

        Hardware hardware = new Hardware();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Hardware();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        hardware.setHardwareId(doc.getId());
        hardware.setTitle(docContent.get("title").toString());
        hardware.setDescription(docContent.get("description").toString());
        hardware.setDefaultNumber(Integer.parseInt(docContent.get("default_number").toString()));
        hardware.setType(docContent.get("hardware_type").toString());

        // return the object
        return hardware;
    }

    public void FetchAllHardware(String viewName, final String type, HardwareAdapter hardwareAdapter){

        View allHardwareView = database.getView(viewName);

        allHardwareView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("hardware_type", (String) document.get("hardware_type"));
                    emitter.emit(document.get("title"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query query = database.getView(viewName).createQuery();
        List<Hardware> fetchedHardwareList = new ArrayList<Hardware>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();

                //fetch all Hardware records
                Hardware hardware = new Hardware();
                hardware.setHardwareId(row.getDocumentId().toString());
                hardware.setTitle(row.getDocument().getProperties().get("title").toString());
                hardware.setType(row.getDocument().getProperties().get("hardware_type").toString());
                hardware.setDescription(row.getDocument().getProperties().get("description").toString());
                hardware.setDefaultNumber((Integer) row.getDocument().getProperties().get("default_number"));
                fetchedHardwareList.add(hardware);
            }

            hardwareAdapter.clear();
            if (fetchedHardwareList != null && !fetchedHardwareList.isEmpty()){
                for (Hardware hardware : fetchedHardwareList) {
                    hardwareAdapter.add(hardware);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
