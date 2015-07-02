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

import cz.zcu.kiv.eeg.mobile.base.data.adapter.DiseaseAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.SoftwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Pharmaceutical;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Software;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchDiseaseListDB {

    private Database database;
    private Context ctx;

    public FetchDiseaseListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Disease FetchDiseaseById(String docId ){

        Disease disease = new Disease();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Disease();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        disease.setDiseaseId(doc.getId());
        disease.setName(docContent.get("title").toString());
        disease.setDescription(docContent.get("description").toString());

        // return the object
        return disease;
    }

    public void FetchAllDiseases(String viewName, final String type, DiseaseAdapter diseaseAdapter){

        View allDiseasesView = database.getView(viewName);

        allDiseasesView.setMap(new Mapper() {
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
        List<Disease> fetchedDiseaseList = new ArrayList<Disease>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //fetch all Disease records
                Disease disease = new Disease();
                disease.setDiseaseId(row.getDocumentId().toString());
                disease.setName(row.getDocument().getProperties().get("title").toString());
                disease.setDescription(row.getDocument().getProperties().get("description").toString());
                fetchedDiseaseList.add(disease);
            }

            diseaseAdapter.clear();
            if (fetchedDiseaseList != null && !fetchedDiseaseList.isEmpty()){
                for (Disease disease : fetchedDiseaseList) {
                    diseaseAdapter.add(disease);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
