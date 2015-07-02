package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.data.adapter.DiseaseAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeLocationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeSystem;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeType;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;
import cz.zcu.kiv.eeg.mobile.base.utils.Keys;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchElectrodeLocationsListDB {

    private Database database;
    private Context ctx;
    private CBDatabase db;

    public FetchElectrodeLocationsListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
        db = new CBDatabase(Keys.DB_NAME, this.ctx);
    }

    public ElectrodeLocation FetchElectrodeLocationById(String docId ){

        ElectrodeLocation electrodeLocation = new ElectrodeLocation();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new ElectrodeLocation();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        electrodeLocation.setId(doc.getId());
        electrodeLocation.setTitle(docContent.get("title").toString());
        electrodeLocation.setAbbr(docContent.get("abbr").toString());
        electrodeLocation.setDescription(docContent.get("description").toString());
        electrodeLocation.setDefaultNumber(Integer.parseInt(docContent.get("default_number").toString()));

        Map<String, Object> efContent = db.retrieve(docContent.get("electrode_fix_id").toString());
        ElectrodeFix ef = new ElectrodeFix();
        ef.setId(docContent.get("electrode_fix_id").toString());
        ef.setTitle(efContent.get("title").toString());
        ef.setDescription(efContent.get("description").toString());
        ef.setDefaultNumber((Integer) efContent.get("default_number"));
        electrodeLocation.setElectrodeFix(ef);

        Map<String, Object> etContent = db.retrieve(docContent.get("electrode_type_id").toString());
        ElectrodeType et = new ElectrodeType();
        et.setId(docContent.get("electrode_type_id").toString());
        et.setTitle(etContent.get("title").toString());
        et.setDescription(etContent.get("description").toString());
        et.setDefaultNumber((Integer) etContent.get("default_number"));
        electrodeLocation.setElectrodeType(et);

        // return the object
        return electrodeLocation;
    }

    public void FetchAllElectrodeLocations(String viewName, final String type, ElectrodeLocationAdapter electrodeLocationAdapter){

        View allElectrodeLocationsView = database.getView(viewName);

        allElectrodeLocationsView.setMap(new Mapper() {
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
        List<ElectrodeLocation> fetchedElectrodeLocationsList = new ArrayList<ElectrodeLocation>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //fetch all ElectrodeLocation records
                ElectrodeLocation electrodeLocation = new ElectrodeLocation();
                electrodeLocation.setId(row.getDocumentId().toString());
                electrodeLocation.setTitle(row.getDocument().getProperties().get("title").toString());
                electrodeLocation.setAbbr(row.getDocument().getProperties().get("abbr").toString());
                electrodeLocation.setDescription(row.getDocument().getProperties().get("description").toString());
                electrodeLocation.setDefaultNumber((Integer) row.getDocument().getProperties().get("default_number"));

//                Object electrodeFix = row.getDocument().getProperties().get("electrode_fix");
//                ElectrodeFix ef = new ElectrodeFix();
//                LinkedHashMap hm = new LinkedHashMap();
//                hm = (LinkedHashMap) electrodeFix;
                Map<String, Object> efContent = db.retrieve(row.getDocument().getProperties().get("electrode_fix_id").toString());
                ElectrodeFix ef = new ElectrodeFix();
                ef.setId(row.getDocument().getProperties().get("electrode_fix_id").toString());
                ef.setTitle(efContent.get("title").toString());
                ef.setDescription(efContent.get("description").toString());
                ef.setDefaultNumber((Integer) efContent.get("default_number"));
                electrodeLocation.setElectrodeFix(ef);

                Map<String, Object> etContent = db.retrieve(row.getDocument().getProperties().get("electrode_type_id").toString());
                ElectrodeType et = new ElectrodeType();
                et.setId(row.getDocument().getProperties().get("electrode_type_id").toString());
                et.setTitle(etContent.get("title").toString());
                et.setDescription(etContent.get("description").toString());
                et.setDefaultNumber((Integer) etContent.get("default_number"));
                electrodeLocation.setElectrodeType(et);

                fetchedElectrodeLocationsList.add(electrodeLocation);
            }

            electrodeLocationAdapter.clear();
            if (fetchedElectrodeLocationsList != null && !fetchedElectrodeLocationsList.isEmpty()){
                for (ElectrodeLocation electrodeLocation : fetchedElectrodeLocationsList) {
                    electrodeLocationAdapter.add(electrodeLocation);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
