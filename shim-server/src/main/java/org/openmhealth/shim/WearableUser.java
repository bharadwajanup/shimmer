package org.openmhealth.shim;

import java.util.Date;

/**
 * Created by Bharadwaj on 4/9/2017.
 */
public class WearableUser {

    private long _id;
    private String name;
    private int age;
    private String gender;
    private Date startDate;
    private String device;
    private String shim;

    public WearableUser(long _id, String name, int age, String gender, Date startDate, String device, String shim)
    {
        set_id(_id);
        setName(name);
        setAge(age);
        setGender(gender);
        setStartDate(startDate);
        setDevice(device);
        setShim(shim);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getShim() {
        return shim;
    }

    public void setShim(String shim) {
        this.shim = shim;
    }
}
