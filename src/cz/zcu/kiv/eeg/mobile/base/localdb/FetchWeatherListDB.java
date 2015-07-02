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

import cz.zcu.kiv.eeg.mobile.base.data.adapter.PharmaceuticalAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.WeatherAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Pharmaceutical;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.utils.ErrorChecker;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchWeatherListDB {

    private Database database;
    private Context ctx;

    public FetchWeatherListDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public Weather FetchWeatherById(String docId ){

        Weather weather = new Weather();

        if( ! ErrorChecker.checkDb(ctx, database)){
            return new Weather();//empty
        }
        // retrieve the document from the database
        Document doc = database.getDocument(docId);

        Map<String, Object> docContent = doc.getProperties();

        weather.setWeatherId(doc.getId());
        if(docContent.get("title")!=null){
            weather.setTitle(docContent.get("title").toString());
        }else{
            weather.setTitle("");
        }
        if(docContent.get("description")!=null){
            weather.setDescription(docContent.get("description").toString());
        }else{
            weather.setDescription("");
        }

        // return the object
        return weather;
    }

    public void FetchWeatherRecords(String viewName, final String type, WeatherAdapter weatherAdapter, String resGroupId){

        View weatherRecordsView = database.getView(viewName);

        weatherRecordsView.setMap(new Mapper() {
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
        List<Weather> fetchedWeatherList = new ArrayList<Weather>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //fetch weather records belongs to selected research group
                if(row.getDocument().getProperties().get("def_group_id").toString().equals(resGroupId)){
                    Weather weather = new Weather();
                    weather.setWeatherId(row.getDocumentId().toString());
                    if(row.getDocument().getProperties().get("title")!=null){
                        weather.setTitle(row.getDocument().getProperties().get("title").toString());
                    }else{
                        weather.setTitle("");
                    }
                    if(row.getDocument().getProperties().get("description")!=null){
                        weather.setDescription(row.getDocument().getProperties().get("description").toString());
                    }else{
                        weather.setDescription("");
                    }
                    fetchedWeatherList.add(weather);
                }
            }

            weatherAdapter.clear();
            if (fetchedWeatherList != null && !fetchedWeatherList.isEmpty()){
                for (Weather weather : fetchedWeatherList) {
                    weatherAdapter.add(weather);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}
