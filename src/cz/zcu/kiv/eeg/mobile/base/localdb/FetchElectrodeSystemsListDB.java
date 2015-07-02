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

import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeSystemAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.WeatherAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeSystem;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchElectrodeSystemsListDB {

    private Database database;
    private Context ctx;

    public FetchElectrodeSystemsListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public ElectrodeSystem FetchElectrodeSystemById(String docId ){

        ElectrodeSystem electrodeSystem = new ElectrodeSystem();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new ElectrodeSystem();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        electrodeSystem.setId(doc.getId());
        electrodeSystem.setTitle(docContent.get("title").toString());
        electrodeSystem.setDescription(docContent.get("description").toString());
        electrodeSystem.setDefaultNumber(Integer.parseInt(docContent.get("default_number").toString()));

        // return the object
        return electrodeSystem;
    }

    public void FetchAllElectrodeSystemRecords(String viewName, final String type, ElectrodeSystemAdapter electrodeSystemAdapter){

        View electrodeSystemRecordsView = database.getView(viewName);

        electrodeSystemRecordsView.setMap(new Mapper() {
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
        List<ElectrodeSystem> fetchedElectrodeSystemList = new ArrayList<ElectrodeSystem>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //fetch all electrode system records
                ElectrodeSystem electrodeSystem = new ElectrodeSystem();
                electrodeSystem.setId(row.getDocumentId().toString());
                if(row.getDocument().getProperties().get("title")!=null){
                    electrodeSystem.setTitle(row.getDocument().getProperties().get("title").toString());
                }else{
                    electrodeSystem.setTitle("");
                }
                if(row.getDocument().getProperties().get("description")!=null){
                    electrodeSystem.setDescription(row.getDocument().getProperties().get("description").toString());
                }else{
                    electrodeSystem.setDescription("");
                }
                if(row.getDocument().getProperties().get("default_number")!=null){
                    electrodeSystem.setDefaultNumber((Integer) row.getDocument().getProperties().get("default_number"));
                }else{
                    electrodeSystem.setDefaultNumber(0);
                }

                fetchedElectrodeSystemList.add(electrodeSystem);
            }

            electrodeSystemAdapter.clear();
            if (fetchedElectrodeSystemList != null && !fetchedElectrodeSystemList.isEmpty()){
                for (ElectrodeSystem electrodeSystem : fetchedElectrodeSystemList) {
                    electrodeSystemAdapter.add(electrodeSystem);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
