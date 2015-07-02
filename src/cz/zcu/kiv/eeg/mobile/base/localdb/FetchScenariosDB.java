package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ScenarioSimple;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchScenariosDB {

    private Database database;
    private Context ctx;

    public FetchScenariosDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public ScenarioSimple FetchSimpleScenarioById(String docId ){

        ScenarioSimple scenarioSimple = new ScenarioSimple();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new ScenarioSimple();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        scenarioSimple.setScenarioId(doc.getId());
        scenarioSimple.setScenarioName(docContent.get("scenarioName").toString());

        // return the object
        return scenarioSimple;
    }

    public void FetchMyScenarios(String viewName, final String type,ScenarioAdapter scenarioAdapter){

        SharedPreferences tempDataChk = ctx.getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
        String loggeduserDocID    = tempDataChk.getString("loggedUserDocID", null);

        View myScenariosView = database.getView(viewName);

        myScenariosView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("scenarioName", (String) document.get("scenarioName"));
                    emitter.emit(document.get("scenarioName"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "2");

        Query query = database.getView(viewName).createQuery();
        List<Scenario> fetchedScenarioList = new ArrayList<Scenario>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //Fetch scenarios where logged_user_id == scenario owner_id
                if(row.getDocument().getProperties().get("ownerId").toString().equals(loggeduserDocID)){
                    Scenario scenario = new Scenario();
                    scenario.setScenarioId(row.getDocumentId().toString());
                    scenario.setScenarioName(row.getDocument().getProperties().get("scenarioName").toString());
                    scenario.setResearchGroupId((String) row.getDocument().getProperties().get("researchGroupId"));
                    scenario.setDescription(row.getDocument().getProperties().get("description").toString());
                    scenario.setMimeType(row.getDocument().getProperties().get("mimeType").toString());
                    scenario.setFileName(row.getDocument().getProperties().get("fileName").toString());
                    scenario.setPrivate((Boolean) row.getDocument().getProperties().get("isPrivate"));
                    scenario.setFilePath(row.getDocument().getProperties().get("filePath").toString());
                    scenario.setFileLength(row.getDocument().getProperties().get("fileLength").toString());
                    scenario.setOwnerId(row.getDocument().getProperties().get("ownerId").toString());
                    if(row.getDocument().getProperties().get("ownerUserName")!=null){
                        scenario.setOwnerUserName(row.getDocument().getProperties().get("ownerUserName").toString());
                    }else{
                        scenario.setOwnerUserName("");
                    }

                    fetchedScenarioList.add(scenario);
                }
            }

            scenarioAdapter.clear();
            if (fetchedScenarioList != null && !fetchedScenarioList.isEmpty()){
                for (Scenario scenario : fetchedScenarioList) {
                    scenarioAdapter.add(scenario);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void FetchAllScenarios(String viewName, final String type,ScenarioAdapter scenarioAdapter){

        View allScenariosView = database.getView(viewName);

        SharedPreferences tempDataChk = ctx.getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
        String loggeduserDocID    = tempDataChk.getString("loggedUserDocID", null);

        allScenariosView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("scenarioName", (String) document.get("scenarioName"));
                    emitter.emit(document.get("scenarioName"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query query = database.getView(viewName).createQuery();
        List<Scenario> fetchedScenarioList = new ArrayList<Scenario>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();

                //getting logged users private+public scenarios+ other public scenarios + other members private scenarios of same group

//                if(row.getDocument().getProperties().get("ownerId").toString().equals(loggeduserDocID)){
                    Scenario scenario = new Scenario();
                    scenario.setScenarioId(row.getDocumentId().toString());
                    scenario.setScenarioName(row.getDocument().getProperties().get("scenarioName").toString());
                    scenario.setResearchGroupId((String) row.getDocument().getProperties().get("researchGroupId"));
                    scenario.setDescription(row.getDocument().getProperties().get("description").toString());
                    scenario.setMimeType(row.getDocument().getProperties().get("mimeType").toString());
                    scenario.setFileName(row.getDocument().getProperties().get("fileName").toString());
                    scenario.setPrivate((Boolean) row.getDocument().getProperties().get("isPrivate"));
                    scenario.setFilePath(row.getDocument().getProperties().get("filePath").toString());
                    scenario.setFileLength(row.getDocument().getProperties().get("fileLength").toString());
                    scenario.setOwnerId(row.getDocument().getProperties().get("ownerId").toString());
                    if(row.getDocument().getProperties().get("ownerUserName")!=null){
                        scenario.setOwnerUserName(row.getDocument().getProperties().get("ownerUserName").toString());
                    }else{
                        scenario.setOwnerUserName("");
                    }
                    fetchedScenarioList.add(scenario);
//                }
            }

            scenarioAdapter.clear();
            if (fetchedScenarioList != null && !fetchedScenarioList.isEmpty()){
                for (Scenario scenario : fetchedScenarioList) {
                    scenarioAdapter.add(scenario);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
