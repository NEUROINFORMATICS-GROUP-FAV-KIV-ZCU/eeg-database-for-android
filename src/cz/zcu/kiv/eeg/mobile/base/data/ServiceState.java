package cz.zcu.kiv.eeg.mobile.base.data;

/**
 * Possible states of CommonService.
 */
public enum ServiceState {

    /**
     * Service is inactive.
     */
    INACTIVE,

    /**
     * Service is currently running.
     */
    RUNNING,

    /**
     * Service has finished its job.
     */
    DONE,

    /**
     * Service's job resulted in an error.
     */
    ERROR;

    /**
     * Checker, whether is service inactive.
     *
     * @return is service inactive
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }

    /**
     * Checker, whether is service running.
     *
     * @return is service running
     */
    public boolean isRunning() {
        return this == RUNNING;
    }

    /**
     * Checker, whether is service done with its job.
     *
     * @return is service done with its job
     */
    public boolean isDone() {
        return this == DONE;
    }

    /**
     * Checker, whether service resulted in an error.
     *
     * @return resulted service in an error
     */
    public boolean isError() {
        return this == ERROR;
    }
}
