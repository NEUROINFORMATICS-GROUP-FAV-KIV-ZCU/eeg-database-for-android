package cz.zcu.kiv.eeg.mobile.base.archetypes;

/**
 * Container for holding information about service and its working message.
 *
 * @author Petr Miko
 */
public class ServiceReference {
    /**
     * Reference onto working Common service
     */
    CommonService service;

    /**
     * Service's message to be displayed.
     */
    String message;
}