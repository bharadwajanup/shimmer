package org.openmhealth.shim;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created as part of the class project for Mobile Computing
 */
public class DataSyncGroup {
    @Id
    private String shim;
//    private List<DataSyncRecord> records;
    private List<String> user;

    public String getShim() {
        return shim;
    }

    public void setShim(String shim) {
        this.shim = shim;
    }

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }

//    public List<DataSyncRecord> getRecords() {
//        return records;
//    }

//    public void setRecords(List<DataSyncRecord> records) {
//        this.records = records;
//    }
}
