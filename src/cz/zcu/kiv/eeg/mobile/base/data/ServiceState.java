/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
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
