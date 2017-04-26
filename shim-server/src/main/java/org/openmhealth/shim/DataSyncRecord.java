package org.openmhealth.shim;

import java.io.Serializable;
import java.util.Map;

/**
 * Created as part of the class project for Mobile Computing
 */
public class DataSyncRecord implements Serializable{
    private String user;
    private Map<String,SyncTimeMeasure> measure;

    public Map<String, SyncTimeMeasure> getMeasure() {
        return measure;
    }

    public void setMeasure(Map<String, SyncTimeMeasure> measure) {
        this.measure = measure;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
