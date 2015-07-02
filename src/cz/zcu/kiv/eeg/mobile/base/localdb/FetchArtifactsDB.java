package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.content.Context;
import android.content.SharedPreferences;

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

import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ArtifactAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ScenarioSimple;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchArtifactsDB {

    private Database database;
    private Context ctx;

    public FetchArtifactsDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Artifact FetchArtifactById(String docId ){

        Artifact artifact = new Artifact();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Artifact();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        artifact.setArtifactId(doc.getId());
        artifact.setCompensation(docContent.get("compensation").toString());
        artifact.setRejectCondition(docContent.get("reject_condition").toString());

        // return the object
        return artifact;
    }

    public void FetchAllArtifacts(String viewName, final String type, ArtifactAdapter artifactAdapter){

        View allArtifactsView = database.getView(viewName);

        allArtifactsView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("reject_condition", (String) document.get("reject_condition"));
                    emitter.emit(document.get("compensation"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query query = database.getView(viewName).createQuery();
        List<Artifact> fetchedArtifactList = new ArrayList<Artifact>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();

                //fetch all Artifact records
                Artifact artifact = new Artifact();
                artifact.setArtifactId(row.getDocumentId().toString());
                artifact.setCompensation(row.getDocument().getProperties().get("compensation").toString());
                artifact.setRejectCondition(row.getDocument().getProperties().get("reject_condition").toString());
                fetchedArtifactList.add(artifact);
            }

            artifactAdapter.clear();
            if (fetchedArtifactList != null && !fetchedArtifactList.isEmpty()){
                for (Artifact artifact : fetchedArtifactList) {
                    artifactAdapter.add(artifact);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
