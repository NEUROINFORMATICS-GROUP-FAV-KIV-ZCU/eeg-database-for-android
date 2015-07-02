package cz.zcu.kiv.eeg.mobile.base.localdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.DiseaseList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeConf;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocationList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeSystem;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.HardwareList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Owner;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Pharmaceutical;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.PharmaceuticalList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ScenarioSimple;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Software;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.SoftwareList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Subject;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.TimeContainer;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.utils.Keys;

/**
 * Created by Isuru on 6/10/2015.
 */
public class FetchExperimentsDB {

    private Database database;
    private Context ctx;
    private CBDatabase db;

    public FetchExperimentsDB(Database database, Context ctx){
        this.database = database;
        this.ctx = ctx;
    }

    public void FetchMyExperiments(String viewName, final String type, ExperimentAdapter experimentAdapter){

        SharedPreferences tempDataChk = ctx.getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
        String loggeduserDocID    = tempDataChk.getString("loggedUserDocID", null);

        View myExperimentsView = database.getView(viewName);

        myExperimentsView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals(type)) {
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    value.put("research_group_id", (String) document.get("research_group_id"));
                    emitter.emit(document.get("owner_id"), value);
                }else{
//                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
                }
            }
        }, "1");

        db = new CBDatabase(Keys.DB_NAME, ctx);
        Query query = database.getView(viewName).createQuery();
        List<Experiment> fetchedExperimentList = new ArrayList<Experiment>();

        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                //Fetch scenarios where logged_user_id == scenario owner_id
                if(row.getDocument().getProperties().get("owner_id").toString().equals(loggeduserDocID)){

                    Experiment experiment = new Experiment();
                    experiment.setExperimentId(row.getDocumentId().toString());

                    //Pass rg_id to the method to retrieve rg object
                    ResearchGroup rg = db.fetchRgById(row.getDocument().getProperties().get("research_group_id").toString());
                    experiment.setResearchGroup(rg);

                    ScenarioSimple scenarioSimple = db.fetchSimpleScenarioById(row.getDocument().getProperties().get("scenario_id").toString());
                    experiment.setScenario(scenarioSimple);

                    Artifact artifact = db.fetchArtifactById(row.getDocument().getProperties().get("artifact_id").toString());
                    experiment.setArtifact(artifact);

                    Digitization digitization = db.fetchDigitizationById(row.getDocument().getProperties().get("digitization_id").toString());
                    experiment.setDigitization(digitization);

                    experiment.setEnvironmentNote(row.getDocument().getProperties().get("environment_note").toString());
                    experiment.setTemperature(Integer.parseInt(row.getDocument().getProperties().get("temperature").toString()));

                    String start_time = row.getDocument().getProperties().get("start_time").toString();
                    TimeContainer startTime = new TimeContainer();
                    //need to convert start_time string into the TimeContainer object
                    //To- Do
                    experiment.setStartTime(startTime);

                    String end_time = row.getDocument().getProperties().get("end_time").toString();
                    TimeContainer endTime = new TimeContainer();
                    //need to convert end_time string into the TimeContainer object
                    //To- Do
                    experiment.setEndTime(endTime);

                    Subject subject = db.fetchSubjectById(row.getDocument().getProperties().get("subject_id").toString());
                    experiment.setSubject(subject);

                    Owner owner = db.fetchOwnerById(row.getDocument().getProperties().get("owner_id").toString());
                    experiment.setOwner(owner);

                    Weather weather = db.fetchWeatherById(row.getDocument().getProperties().get("weather_id").toString());
                    experiment.setWeather(weather);

                    //Fetching hardware
                    List<String> hardware_id_list = (List<String>) row.getDocument().getProperties().get("hardware_list");
                    List<Hardware> hardware_list = new ArrayList<Hardware>();
                    for (int i = 0; i <hardware_id_list.size() ; i++) {
                        Hardware hardware = db.fetchHardwareById(hardware_id_list.get(i));
                        hardware_list.add(hardware);
                    }
                    HardwareList hardwareList = new HardwareList(hardware_list);
                    experiment.setHardwareList(hardwareList);

                    //Fetching software
                    List<String> software_id_list = (List<String>) row.getDocument().getProperties().get("software_list");
                    List<Software> software_list = new ArrayList<Software>();
                    for (int i = 0; i <software_id_list.size() ; i++) {
                        Software software = db.fetchSoftwareById(software_id_list.get(i));
                        software_list.add(software);
                    }
                    SoftwareList softwareList = new SoftwareList(software_list);
                    experiment.setSoftwareList(softwareList);

                    //Fetching diseases
                    List<String> diseases_id_list = (List<String>) row.getDocument().getProperties().get("disease_list");
                    List<Disease> diseases_list = new ArrayList<Disease>();
                    for (int i = 0; i <diseases_id_list.size() ; i++) {
                        Disease disease = db.fetchDiseaseById(diseases_id_list.get(i));
                        diseases_list.add(disease);
                    }
                    DiseaseList diseasesList = new DiseaseList(diseases_list);
                    experiment.setDiseases(diseasesList);

                    //Fetching pharmaceuticals
                    List<String> pharmaceuticals_id_list = (List<String>) row.getDocument().getProperties().get("pharmaceutical_list");
                    List<Pharmaceutical> pharmaceuticals_list = new ArrayList<Pharmaceutical>();
                    for (int i = 0; i <pharmaceuticals_id_list.size() ; i++) {
                        Pharmaceutical pharmaceutical = db.fetchPharmaceuticalById(pharmaceuticals_id_list.get(i));
                        pharmaceuticals_list.add(pharmaceutical);
                    }
                    PharmaceuticalList pharmaceuticalsList = new PharmaceuticalList(pharmaceuticals_list);
                    experiment.setPharmaceuticals(pharmaceuticalsList);

                    //Fetching electrodeConf data
                    Map<String, Object> electrodeConfDocContent = db.retrieve(row.getDocument().getProperties().get("electrode_conf_id").toString());
                    String impedance = electrodeConfDocContent.get("impedance").toString();
                    String electrode_sys_id = electrodeConfDocContent.get("electrode_system_id").toString();
                    ElectrodeSystem electrode_sys = db.fetchElectrodeSystemById(electrode_sys_id);
                    List<String> electrode_location_ids_list = (List<String>) electrodeConfDocContent.get("electrode_locations_list");

                    List<ElectrodeLocation> electrode_location_list = new ArrayList<ElectrodeLocation>();
                    for (int i = 0; i <electrode_location_ids_list.size() ; i++) {
                        ElectrodeLocation electrodLocation = db.fetchElectrodeLocationById(electrode_location_ids_list.get(i));
                        electrode_location_list.add(electrodLocation);
                    }

                    ElectrodeLocationList electrodELocationList = new ElectrodeLocationList(electrode_location_list);

                    ElectrodeConf eConf = new ElectrodeConf();
                    eConf.setId(row.getDocument().getProperties().get("electrode_conf_id").toString());
                    eConf.setImpedance(Integer.parseInt(impedance));
                    eConf.setElectrodeSystem(electrode_sys);
                    eConf.setElectrodeLocations(electrodELocationList);

                    experiment.setElectrodeConf(eConf);

                    fetchedExperimentList.add(experiment);
                }
            }

            experimentAdapter.clear();
            if (fetchedExperimentList != null && !fetchedExperimentList.isEmpty()){
                for (Experiment experiment : fetchedExperimentList) {
                    experimentAdapter.add(experiment);
                }
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

//    public void FetchAllExperiments(String viewName, final String type,ExperimentAdapter experimentAdapter){
//
//        View allScenariosView = database.getView(viewName);
//
//        SharedPreferences tempDataChk = ctx.getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
//        String loggeduserDocID    = tempDataChk.getString("loggedUserDocID", null);
//
//        allScenariosView.setMap(new Mapper() {
//            @Override
//            public void map(Map<String, Object> document, Emitter emitter) {
//                if (document.get("type").equals(type)) {
//                    HashMap<String, Object> value = new HashMap<String, Object>();
//                    value.put("scenarioName", (String) document.get("scenarioName"));
//                    emitter.emit(document.get("scenarioName"), value);
//                }else{
////                    Toast.makeText(ctx, "No such document(s) in the database", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, "1");
//
//        Query query = database.getView(viewName).createQuery();
//        List<Scenario> fetchedScenarioList = new ArrayList<Scenario>();
//
//        try {
//            QueryEnumerator result = query.run();
//            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
//                QueryRow row = it.next();
//
//                //fetch all owner's public scenarios only + other scenarios which belongs to the research group where the logged user is a member
//                //What about logged user's private scenarios
//                //what about other scenarios where logged user is a member ?
//
//                if(row.getDocument().getProperties().get("ownerId").toString().equals(loggeduserDocID)&&(!(Boolean) row.getDocument().getProperties().get("isPrivate"))){
//                    //Retrieve data if Scenario is Public
//                    Scenario scenario = new Scenario();
//                    scenario.setScenarioId(row.getDocumentId().toString());
//                    scenario.setScenarioName(row.getDocument().getProperties().get("scenarioName").toString());
//                    scenario.setResearchGroupId((String) row.getDocument().getProperties().get("researchGroupId"));
//                    scenario.setDescription(row.getDocument().getProperties().get("description").toString());
//                    scenario.setMimeType(row.getDocument().getProperties().get("mimeType").toString());
//                    scenario.setFileName(row.getDocument().getProperties().get("fileName").toString());
//                    scenario.setPrivate((Boolean) row.getDocument().getProperties().get("isPrivate"));
//                    scenario.setFilePath(row.getDocument().getProperties().get("filePath").toString());
//                    scenario.setFileLength(row.getDocument().getProperties().get("fileLength").toString());
//                    scenario.setOwnerId(row.getDocument().getProperties().get("ownerId").toString());
//                    fetchedScenarioList.add(scenario);
//                }
//            }
//
//            scenarioAdapter.clear();
//            if (fetchedScenarioList != null && !fetchedScenarioList.isEmpty()){
//                for (Scenario scenario : fetchedScenarioList) {
//                    scenarioAdapter.add(scenario);
//                }
//            }
//
//        } catch (CouchbaseLiteException e) {
//            e.printStackTrace();
//        }
//    }

}
