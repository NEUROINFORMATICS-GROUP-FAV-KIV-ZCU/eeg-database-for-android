package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
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

import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeFixAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeSystemAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeSystem;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchElectrodeFixesListDB {

    private Database database;
    private Context ctx;

    public FetchElectrodeFixesListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public void FetchAllElectrodeFixRecords(String viewName, final String type, ElectrodeFixAdapter electrodeFixAdapter){

        View electrodeFixRecordsView = database.getView(viewName);

        electrodeFixRecordsView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("description", (String) document.get("description"));
                    emitter.emit(document.get("title"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query query = database.getView(viewName).createQuery();
        List<ElectrodeFix> fetchedElectrodeFixesList = new ArrayList<ElectrodeFix>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //fetch all electrode fix records
                ElectrodeFix electrodeFix = new ElectrodeFix();
                electrodeFix.setId(row.getDocumentId().toString());
                electrodeFix.setTitle(row.getDocument().getProperties().get("title").toString());
                electrodeFix.setDescription(row.getDocument().getProperties().get("description").toString());
                electrodeFix.setDefaultNumber((Integer) row.getDocument().getProperties().get("default_number"));
                fetchedElectrodeFixesList.add(electrodeFix);
            }

            electrodeFixAdapter.clear();
            if (fetchedElectrodeFixesList != null && !fetchedElectrodeFixesList.isEmpty()){
                for (ElectrodeFix electrodeFix : fetchedElectrodeFixesList) {
                    electrodeFixAdapter.add(electrodeFix);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
