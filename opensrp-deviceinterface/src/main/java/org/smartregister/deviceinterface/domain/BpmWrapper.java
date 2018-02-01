package org.smartregister.deviceinterface.domain;

import org.joda.time.DateTime;
import org.smartregister.domain.Photo;

/**
 * Created by sid-tech on 12/13/17.
 */

public class BpmWrapper {
    private String bpm;
    private String id;
    private String sistole;
    private Long dbKey;
    private String gender;
    private String patientName;
    private String patientNumber;
    private String patientAge;
    private Photo photo;
    private String dateOfBirth;
    private String pmtctStatus;

    public String getBpm() {
        return bpm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSistole(String sistole) {
        this.sistole = sistole;
    }

    public void setUpdatedWeightDate(DateTime dateTime, boolean b) {

    }

    public void setDbKey(Long dbKey) {
        this.dbKey = dbKey;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPmtctStatus(String pmtctStatus) {
        this.pmtctStatus = pmtctStatus;
    }
}
