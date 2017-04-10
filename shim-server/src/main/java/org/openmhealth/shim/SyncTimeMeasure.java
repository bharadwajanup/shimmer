package org.openmhealth.shim;

import java.util.Date;

/**
 * Created by Bharadwaj on 4/6/2017.
 */
public class SyncTimeMeasure {
    private Date lastSyncTime;
    private Date lastRetrieveTime;

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public Date getLastRetrieveTime() {
        return lastRetrieveTime;
    }

    public void setLastRetrieveTime(Date lastRetrieveTime) {
        this.lastRetrieveTime = lastRetrieveTime;
    }
}
