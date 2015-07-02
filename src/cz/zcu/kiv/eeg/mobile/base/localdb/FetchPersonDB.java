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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.PersonAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Owner;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Subject;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchPersonDB {

    private Database database;
    private Context ctx;

    public FetchPersonDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Owner FetchOwnerById(String docId ){

        Owner owner = new Owner();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Owner();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        owner.setId(doc.getId());
        if(docContent.get("name")!=null){
            owner.setName(docContent.get("name").toString());
        }else{
            owner.setName("");
        }
        if(docContent.get("surname")!=null){
            owner.setSurname(docContent.get("surname").toString());
        }else{
            owner.setSurname("");
        }

        // return the object
        return owner;
    }

    public Subject FetchSubjectById(String docId ){

        Subject subject = new Subject();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Subject();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        subject.setPersonId(doc.getId());
        if(docContent.get("name")!=null){
            subject.setName(docContent.get("name").toString());
        }else{
            subject.setName("");
        }
        if(docContent.get("surname")!=null){
            subject.setSurname(docContent.get("surname").toString());
        }else{
            subject.setSurname("");
        }
        if(docContent.get("gender")!=null){
            subject.setGender(docContent.get("gender").toString());
        }else{
            subject.setGender("");
        }
        if(docContent.get("lefthanded")!=null){
            subject.setLeftHanded(Boolean.parseBoolean(docContent.get("lefthanded").toString()));
        }else{
            subject.setLeftHanded(false);
        }
        if(docContent.get("birthday")!=null){



        }else{
            subject.setAge(0);
        }


        // return the object
        return subject;
    }

    public void FetchPerson(String viewName, final String type, PersonAdapter personAdapter){

        SharedPreferences tempDataChk = ctx.getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
        String loggeduserDocID    = tempDataChk.getString("loggedUserDocID", null);

        View personView = database.getView(viewName);

        personView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("name", (String) document.get("name"));
                    emitter.emit(document.get("name"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        Query query = database.getView(viewName).createQuery();
        List<Person> fetchedPersonList = new ArrayList<Person>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //Fetch all person records
                Person person = new Person();
                person.setId(row.getDocumentId().toString());
                if(row.getDocument().getProperties().get("name")!=null){
                    person.setName(row.getDocument().getProperties().get("name").toString());
                }else{
                    person.setName("");
                }
                if(row.getDocument().getProperties().get("surname")!=null){
                    person.setSurname(row.getDocument().getProperties().get("surname").toString());
                }else{
                    person.setSurname("");
                }
                if(row.getDocument().getProperties().get("birthday")!=null){
                    person.setBirthday(row.getDocument().getProperties().get("birthday").toString());
                }else{
                    person.setBirthday("");
                }
                if(row.getDocument().getProperties().get("gender")!=null){
                    person.setGender(row.getDocument().getProperties().get("gender").toString());
                }else{
                    person.setGender("");
                }
                if(row.getDocument().getProperties().get("email")!=null){
                    person.setEmail(row.getDocument().getProperties().get("email").toString());
                }else{
                    person.setEmail("");
                }
                if(row.getDocument().getProperties().get("lefthanded")!=null){
                    person.setLeftHanded(row.getDocument().getProperties().get("lefthanded").toString());
                }else{
                    person.setLeftHanded("");
                }
                if(row.getDocument().getProperties().get("notes")!=null){
                    person.setNotes(row.getDocument().getProperties().get("notes").toString());
                }else{
                    person.setNotes("");
                }
                if(row.getDocument().getProperties().get("phone")!=null){
                    person.setPhone(row.getDocument().getProperties().get("phone").toString());
                }else{
                    person.setPhone("");
                }
                if(row.getDocument().getProperties().get("group_id")!=null){
                    person.setDef_group_id(row.getDocument().getProperties().get("group_id").toString());
                }else{
                    person.setDef_group_id("");
                }

                fetchedPersonList.add(person);

            }

            personAdapter.clear();
            if (fetchedPersonList != null && !fetchedPersonList.isEmpty()){
                for (Person person : fetchedPersonList) {
                    personAdapter.add(person);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }


}
