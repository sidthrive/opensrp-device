package org.smartregister.deviceinterface.repository;

import android.database.Cursor;
import android.util.Log;

import org.smartregister.deviceinterface.domain.Bpm;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sid-tech on 12/13/17.
 */

public class BpmRepository extends BaseRepository {

    private static final String TAG = BpmRepository.class.getCanonicalName();
    private static final String WEIGHT_SQL = "CREATE TABLE weights (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,base_entity_id VARCHAR NOT NULL,program_client_id VARCHAR NULL,kg REAL NOT NULL,date DATETIME NOT NULL,anmid VARCHAR NULL,location_id VARCHAR NULL,sync_status VARCHAR,updated_at INTEGER NULL)";
    public static final String BPM_TABLE_NAME = "weights";
    public static final String KG = "kg";
    public static final String DATE = "date";
    public static final String ANMID = "anmid";
    public static final String LOCATIONID = "location_id";
    public static final String SYNC_STATUS = "sync_status";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    private static final String Z_SCORE = "z_score";
    public static final double DEFAULT_Z_SCORE = 999999d;
    public static final String ID_COLUMN = "_id";
    public static final String BASE_ENTITY_ID = "base_entity_id";
    public static final String SISTOLE = "sistole";
    public static final String DIASTOLE = "diastole";
    public static final String EVENT_ID = "event_id";
    public static final String PROGRAM_CLIENT_ID = "program_client_id";// ID to be used to identify entity when base_entity_id is unavailable
    public static final String FORMSUBMISSION_ID = "formSubmissionId";
    public static final String OUT_OF_AREA = "out_of_area";

    public static final String[] BPM_TABLE_COLUMN = {
            ID_COLUMN, BASE_ENTITY_ID, PROGRAM_CLIENT_ID, KG, DATE, ANMID, LOCATIONID, SYNC_STATUS,
            UPDATED_AT_COLUMN, EVENT_ID, FORMSUBMISSION_ID, Z_SCORE, OUT_OF_AREA};

    public BpmRepository(Repository repository) {
        super(repository);
    }

    public List<Bpm> findByEntityId(String entityId) {
        return null;
    }

    public List<Bpm> findLast5(String entityId) {
        Cursor cursor = getRepository().getReadableDatabase().query(BPM_TABLE_NAME, BPM_TABLE_COLUMN,
                BASE_ENTITY_ID + " = ? " + COLLATE_NOCASE, new String[]{entityId}, null, null,
                UPDATED_AT_COLUMN + COLLATE_NOCASE + " DESC", null);

        List<Bpm> weights = readAllWeights(cursor);
//        if (!weights.isEmpty()) {
        return weights;    }

    private List<Bpm> readAllWeights(Cursor cursor) {
        List<Bpm> weights = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Double zScore = cursor.getDouble(cursor.getColumnIndex(Z_SCORE));
                    if (zScore != null && zScore.equals(new Double(DEFAULT_Z_SCORE))) {
                        zScore = null;
                    }

                    weights.add(
                            new Bpm(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                    cursor.getString(cursor.getColumnIndex(BASE_ENTITY_ID)),
                                    cursor.getFloat(cursor.getColumnIndex(SISTOLE)),
                                    cursor.getFloat(cursor.getColumnIndex(DIASTOLE)),
                                    new Date(cursor.getLong(cursor.getColumnIndex(DATE))),
                                    cursor.getString(cursor.getColumnIndex(ANMID)),
                                    cursor.getString(cursor.getColumnIndex(LOCATIONID)),
                                    cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                                    cursor.getLong(cursor.getColumnIndex(UPDATED_AT_COLUMN)),
                                    cursor.getString(cursor.getColumnIndex(EVENT_ID)),
                                    cursor.getString(cursor.getColumnIndex(FORMSUBMISSION_ID)),
                                    cursor.getInt(cursor.getColumnIndex(OUT_OF_AREA))

                            ));

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return weights;

    }

}
