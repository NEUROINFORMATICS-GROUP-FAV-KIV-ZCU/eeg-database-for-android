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

import cz.zcu.kiv.eeg.mobile.base.data.adapter.ArtifactAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.DigitizationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchDigitizationsDB {

    private Database database;
    private Context ctx;

    public FetchDigitizationsDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Digitization FetchDigitizationById(String docId ){

        Digitization digitization = new Digitization();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Digitization();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        digitization.setDigitizationId(doc.getId());
        digitization.setFilter(docContent.get("filter").toString());
        digitization.setGain(docContent.get("gain").toString());
        digitization.setSamplingRate(docContent.get("sampling_rate").toString());

        // return the object
        return digitization;
    }

    public void FetchAllDigitizations(String viewName, final String type, DigitizationAdapter digitizationAdapter){

        View allArtifactsView = database.getView(viewName);

        allArtifactsView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("gain", document.get("gain"));
                    emitter.emit(document.get("filter"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query query = database.getView(viewName).createQuery();
        List<Digitization> fetchedDigitizationList = new ArrayList<Digitization>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();

                //fetch all Digitization records
                Digitization digitization = new Digitization();
                digitization.setDigitizationId(row.getDocumentId().toString());
                digitization.setFilter(row.getDocument().getProperties().get("filter").toString());
                digitization.setGain(row.getDocument().getProperties().get("gain").toString());
                digitization.setSamplingRate(row.getDocument().getProperties().get("sampling_rate").toString());
                fetchedDigitizationList.add(digitization);
            }

            digitizationAdapter.clear();
            if (fetchedDigitizationList != null && !fetchedDigitizationList.isEmpty()){
                for (Digitization artifact : fetchedDigitizationList) {
                    digitizationAdapter.add(artifact);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
