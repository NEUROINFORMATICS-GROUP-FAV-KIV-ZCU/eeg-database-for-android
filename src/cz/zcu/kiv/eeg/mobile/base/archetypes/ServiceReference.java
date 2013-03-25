package cz.zcu.kiv.eeg.mobile.base.archetypes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Container for holding information about service and its working message.
 *
 * @author Petr Miko
 */
public class ServiceReference {

    /**
     * Assigned common service (AsyncTask actually) and its description.
     * Handled as a FIFO of references, only first is used for creating progress dialog and removing after it is done.
     */
    private static List<ServiceReference> references = Collections.synchronizedList(new LinkedList<ServiceReference>());
    /**
     * Service's message to be displayed.
     */
    String message;
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

    /**
     * Adds new reference between activity and service.
     *
     * @param activity activity on screen
     * @param service  common service
     */
    public static void push(CommonActivity activity, CommonService service) {
        ServiceReference ref = new ServiceReference(activity, service);
        references.add(ref);
    }

    /**
     * Returns currently active (first) reference.
     *
     * @return reference to service
     */
    public static ServiceReference peek() {
        if (references.isEmpty())
            return null;
        else
            return references.get(0);
    }

    /**
     * Removes and returns active (first) reference.
     *
     * @return reference to service
     */
    public static ServiceReference pop() {
        if (references.isEmpty())
            return null;
        else
            return references.remove(0);
    }

    /**
     * Resets activity to all registered services.
     * Necessary when recreating activity, so asynctasks keep reference on displayed activity.
     *
     * @param activity on screen activity
     */
    public static void refreshReferences(CommonActivity activity) {
        for (ServiceReference ref : references) {
            ref.service.setActivity(activity);
        }
    }

    /**
     * Count of registered (currently working) references.
     *
     * @return registered references
     */
    public static int size() {
        return references.size();
    }

    /**
     * Checks, if there are any references.
     *
     * @return true if no reference found
     */
    public static boolean isEmpty() {
        return references.isEmpty();
    }
}