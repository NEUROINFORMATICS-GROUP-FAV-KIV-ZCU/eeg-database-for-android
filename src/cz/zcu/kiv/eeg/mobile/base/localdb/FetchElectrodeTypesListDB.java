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
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeTypeAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeType;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchElectrodeTypesListDB {

    private Database database;
    private Context ctx;

    public FetchElectrodeTypesListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public void FetchAllElectrodeTypeRecords(String viewName, final String type, ElectrodeTypeAdapter electrodeTypeAdapter){

        View electrodeTypeRecordsView = database.getView(viewName);

        electrodeTypeRecordsView.setMap(new Mapper() {
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
        List<ElectrodeType> fetchedElectrodeTypesList = new ArrayList<ElectrodeType>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //fetch all electrode fix records
                ElectrodeType electrodeType = new ElectrodeType();
                electrodeType.setId(row.getDocumentId().toString());
                electrodeType.setTitle(row.getDocument().getProperties().get("title").toString());
                electrodeType.setDescription(row.getDocument().getProperties().get("description").toString());
                electrodeType.setDefaultNumber((Integer) row.getDocument().getProperties().get("default_number"));
                fetchedElectrodeTypesList.add(electrodeType);
            }

            electrodeTypeAdapter.clear();
            if (fetchedElectrodeTypesList != null && !fetchedElectrodeTypesList.isEmpty()){
                for (ElectrodeType electrodeTyp : fetchedElectrodeTypesList) {
                    electrodeTypeAdapter.add(electrodeTyp);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
