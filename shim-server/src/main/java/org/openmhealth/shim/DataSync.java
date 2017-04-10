package org.openmhealth.shim;

import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * Created by Bharadwaj on 4/6/2017.
 */
public class DataSync {
    @Id
    private String id;
    private String userId;
    private String shim;
    private Map<String,SyncTimeMeasure> measure;
    private Boolean updateAvailable;

    public Map<String, SyncTimeMeasure> getMeasure() {
        return measure;
    }

    public void setMeasure(Map<String, SyncTimeMeasure> measure) {
        this.measure = measure;
    }

    public String getShim() {
        return shim;
    }

    public void setShim(String shim) {
        this.shim = shim;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(Boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
    }
}
