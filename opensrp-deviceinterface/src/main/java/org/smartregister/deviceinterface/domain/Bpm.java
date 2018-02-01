package org.smartregister.deviceinterface.domain;

import java.util.Date;

/**
 * Created by sid-tech on 12/13/17.
 */

public class Bpm {
    private Date date;
    private Long id;
    private String sistole;
    private String eventId;
    private String formSubmissionId;

    public Bpm(Long id, String baseEntityId, Float sistole, Float diastole, Date date, String anmId, String
            locationId, String syncStatus, Long updatedAt, String eventId, String
                       formSubmissionId, Integer outOfCatchment) {
    }

    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public String getSistole() {
        return sistole;
    }

    public String getEventId() {
        return eventId;
    }

    public String getFormSubmissionId() {
        return formSubmissionId;
    }
}
