package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.app.Activity;
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
import com.couchbase.lite.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.RUNNING;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchResearchGroupsDB {

    private Database database;
    private Context ctx;

    public FetchResearchGroupsDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public ResearchGroup FetchResearchGroupById(String docId ){

        ResearchGroup rg = new ResearchGroup();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new ResearchGroup();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        rg.setGroupId(doc.getId());
        rg.setGroupName(docContent.get("group_name").toString());
        rg.setOwnerId(docContent.get("owner_id").toString());
        if(docContent.get("description")!=null){
            rg.setDescription(docContent.get("description").toString());
        }else{
            rg.setDescription("");
        }


        // return the object
        return rg;
    }

    public void FetchAllResearchGroups(String viewName, final String type, ResearchGroupAdapter researchGroupAdapter){

        List<ResearchGroup> fetchedResearchGroupList = new ArrayList<ResearchGroup>();

        SharedPreferences getLoggedUserId= ctx.getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
        String loggeduserDocID          = getLoggedUserId.getString("loggedUserDocID", null);

        View membershipView = database.getView("MembershipView");

        membershipView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals("Membership")) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("group_id", (String) document.get("group_id"));
                    emitter.emit(document.get("member_id"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query queryMembership = database.getView("MembershipView").createQuery();
        List<String> groupIds = new ArrayList<String>();

        try{
            QueryEnumerator membershipResult = queryMembership.run();
            for (Iterator<QueryRow> it = membershipResult; it.hasNext(); ) {
                QueryRow row = it.next();

                if(row.getDocument().getProperties().get("member_id").toString().equals(loggeduserDocID)){
                    groupIds.add(row.getDocument().getProperties().get("group_id").toString());
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }

        //Got the group id list, then for each id in that list, run a get Group by id method to get the content of RG

        for (int i = 0; i <groupIds.size() ; i++) {
            ResearchGroup rg = this.FetchResearchGroupById(groupIds.get(i));
            fetchedResearchGroupList.add(rg);
        }

        researchGroupAdapter.clear();
        if (fetchedResearchGroupList != null && !fetchedResearchGroupList.isEmpty()){
            for (ResearchGroup res : fetchedResearchGroupList) {
                researchGroupAdapter.add(res);
            }
        }

    }

    public String FetchMyDefaultResearchGroupId(String viewName, final String type, String loggedUserId){

        View researchGroupView = database.getView(viewName);
        String def_resgrp_id = null;

        researchGroupView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("description", (String) document.get("description"));
                    emitter.emit(document.get("group_name"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "2");

        Query query = database.getView(viewName).createQuery();
//        List<ResearchGroup> fetchedResearchGroupList = new ArrayList<ResearchGroup>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //get research groups if only owner_id == logged_user_id
                if(row.getDocument().getProperties().get("owner_id").toString().equals(loggedUserId)){
                    def_resgrp_id = row.getDocumentId().toString();
//                    ResearchGroup researchGroup = new ResearchGroup();
//                    researchGroup.setGroupId(row.getDocumentId().toString());
//                    researchGroup.setGroupName(row.getDocument().getProperties().get("group_name").toString());
//                    researchGroup.setDescription(row.getDocument().getProperties().get("description").toString());
//                    researchGroup.setOwnerId(row.getDocument().getProperties().get("owner_id").toString());
//                    fetchedResearchGroupList.add(researchGroup);
                }
            }


        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return def_resgrp_id;
    }

}
