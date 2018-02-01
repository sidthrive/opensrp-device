package org.smartregister.deviceinterface.sample.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Pair;
import android.view.View;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.deviceinterface.DeviceInterfaceLibrary;
import org.smartregister.deviceinterface.domain.Bpm;
import org.smartregister.deviceinterface.domain.BpmWrapper;
import org.smartregister.deviceinterface.fragment.RecordBpmDialogFragment;
import org.smartregister.deviceinterface.repository.BpmRepository;
import org.smartregister.deviceinterface.sample.MainActivity;
import org.smartregister.deviceinterface.utils.ImageUtils;
import org.smartregister.domain.Photo;
import org.smartregister.util.DateUtil;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import static org.smartregister.util.Utils.getName;
import static org.smartregister.util.Utils.getValue;

/**
 * Created by sid-tech on 2/1/18.
 */

public class SampleUtils {

    // Dummpy values, Can be changed manually
    public static final String ENTITY_ID = "1";
    public static final String STR_DOB = "2000-01-01T00:00:00.000Z";
    public static final String GENDER = (new Random()).nextBoolean() ? "male" : "female";
    public static final Float SISTOLE = 125.5f;
    public static final Float DIASTOLE = 85.5f;
    public static String BIRTH_WEIGHT;

    public static CommonPersonObjectClient dummyDetails() {
        HashMap<String, String> columnMap = new HashMap<>();
        columnMap.put("first_name", "Foo");
        columnMap.put("last_name", "Bar");
        columnMap.put("dev_id", "1");
        columnMap.put("dob", STR_DOB);
        columnMap.put("gender", GENDER);

        CommonPersonObjectClient personDetails = new CommonPersonObjectClient(ENTITY_ID, columnMap, "Test");
        personDetails.setColumnmaps(columnMap);

        return personDetails;
    }

    public static FragmentTransaction initFragmentTransaction(Activity context, String tag) {
        FragmentTransaction ft = context.getFragmentManager().beginTransaction();
        Fragment prev = context.getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

    public static void showBpmDialog(Activity context, View view, String tag) {
        BpmWrapper bpmWrapper = (BpmWrapper) view.getTag();
        RecordBpmDialogFragment recordBpmDialogFragment = RecordBpmDialogFragment.newInstance(bpmWrapper);
        recordBpmDialogFragment.show(initFragmentTransaction(context, tag), tag);

    }

    public static void showEditBpmDialog(Activity context, int i, String tag) {
        CommonPersonObjectClient userDetails = dummyDetails();

        String firstName = Utils.getValue(userDetails.getColumnmaps(), "first_name", true);
        String lastName = Utils.getValue(userDetails.getColumnmaps(), "last_name", true);
        String childName = getName(firstName, lastName).trim();

        String gender = getValue(userDetails.getColumnmaps(), "gender", true);

        String zeirId = getValue(userDetails.getColumnmaps(), "dev_id", false);
        String duration = "";
        String dobString = getValue(userDetails.getColumnmaps(), "dob", false);
        if (StringUtils.isNotBlank(dobString)) {
            DateTime dateTime = new DateTime(getValue(userDetails.getColumnmaps(), "dob", false));
            duration = DateUtil.getDuration(dateTime);
        }

        Photo photo = ImageUtils.profilePhotoByClient(userDetails);

        BpmWrapper bpmWrapper = new BpmWrapper();
        bpmWrapper.setId(userDetails.entityId());
        BpmRepository wp = DeviceInterfaceLibrary.getInstance().bpmRepository();
        List<Bpm> bpmList = wp.findLast5(userDetails.entityId());
        if (!bpmList.isEmpty()) {
            bpmWrapper.setSistole(bpmList.get(i).getSistole());
            bpmWrapper.setUpdatedWeightDate(new DateTime(bpmList.get(i).getDate()), false);
            bpmWrapper.setDbKey(bpmList.get(i).getId());
        }

        bpmWrapper.setGender(gender);
        bpmWrapper.setPatientName(childName);
        bpmWrapper.setPatientNumber(zeirId);
        bpmWrapper.setPatientAge(duration);
        bpmWrapper.setPhoto(photo);
        bpmWrapper.setDateOfBirth(STR_DOB);
        bpmWrapper.setPmtctStatus(getValue(userDetails.getColumnmaps(), "pmtct_status", false));

        RecordBpmDialogFragment editWeightDialogFragment = RecordBpmDialogFragment.newInstance(context, bpmWrapper);
        editWeightDialogFragment.show(initFragmentTransaction(context, tag), tag);

    }

    public static void createWeightWidget(MainActivity mainActivity, View weightWidget, LinkedHashMap<Long, Pair<String, String>> weightmap, ArrayList<View.OnClickListener> listeners, ArrayList<Boolean> weighteditmode) {

    }
}
