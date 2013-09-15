package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.os.Handler;
import android.os.Looper;
import cz.zcu.kiv.eeg.mobile.base.R;
import org.holoeverywhere.app.ProgressDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Container for holding information about service and its working message.
 *
 * @author Petr Miko
 */
final class ServiceReference {

    private static List<ServiceReference> ready = new LinkedList<ServiceReference>();
    private static List<ServiceReference> running = new LinkedList<ServiceReference>();


    /**
     * Service's message to be displayed.
     */
     int message;
    /**
     * Reference onto working Common service
     */
    private CommonService service;

    /**
     * Constructor. Sets reference from activity to service and pushes it into queue of references.
     *
     * @param activity activity on screen
     * @param service  common service
     */
    private ServiceReference(CommonActivity activity, CommonService service) {
        this.service = service;
        service.setActivity(activity);
    }

    public static synchronized void add(CommonActivity activity, CommonService service, int message) {
        ServiceReference ref = new ServiceReference(activity, service);
        ref.message = message;
        ready.add(ref);
    }

    public synchronized static boolean start(int message) {

        ServiceReference start = findReference(ready, message);

        if(start != null){
            running.add(start);
            ready.remove(start);

            return true;
        }
        return false;
    }


    public synchronized static void done(int message){
        ServiceReference done = findReference(running, message);
        if(done != null){
            running.remove(done);
        }
    }

    public synchronized static ServiceReference peek(){
        if(!running.isEmpty()){
            return running.get(0);
        } else return null;
    }

    /**
     * Resets activity to all registered services.
     * Necessary when recreating activity, so asynctasks keep reference on displayed activity.
     *
     * @param activity on screen activity
     */
    public synchronized static void refreshReferences(CommonActivity activity) {
        for (ServiceReference ref : ready) {
            ref.service.setActivity(activity);
        }

        for (ServiceReference ref : running ){
            ref.service.setActivity(activity);
        }
    }


    private static ServiceReference findReference(List<ServiceReference> list, int messageId){
        if (list.isEmpty()) {
            return null;
        } else {
            int indexToRemove = 0;

            for(ServiceReference reference : list){
                if(reference.message == messageId){
                    break;
                }
                indexToRemove++;
            }

            return list.get(indexToRemove);
        }
    }

}