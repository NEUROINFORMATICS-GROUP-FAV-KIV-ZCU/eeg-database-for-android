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

import cz.zcu.kiv.eeg.mobile.base.data.adapter.HardwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.SoftwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Software;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchSoftwareListDB {

    private Database database;
    private Context ctx;

    public FetchSoftwareListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Software FetchSoftwareById(String docId ){

        Software software = new Software();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Software();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        software.setId(doc.getId());
        software.setTitle(docContent.get("title").toString());
        software.setDescription(docContent.get("description").toString());
//        software.setDefaultNumber((Integer) docContent.get("default_number"));

        // return the object
        return software;
    }

    public void FetchAllSoftware(String viewName, final String type, SoftwareAdapter softwareAdapter){

        View allSoftwareView = database.getView(viewName);

        allSoftwareView.setMap(new Mapper() {
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
        List<Software> fetchedSoftwareList = new ArrayList<Software>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();

                //fetch all Software records
                Software software = new Software();
                software.setId(row.getDocumentId().toString());
                software.setTitle(row.getDocument().getProperties().get("title").toString());
                software.setDescription(row.getDocument().getProperties().get("description").toString());
                software.setDefaultNumber((Integer) row.getDocument().getProperties().get("default_number"));
                fetchedSoftwareList.add(software);
            }

            softwareAdapter.clear();
            if (fetchedSoftwareList != null && !fetchedSoftwareList.isEmpty()){
                for (Software software : fetchedSoftwareList) {
                    softwareAdapter.add(software);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
