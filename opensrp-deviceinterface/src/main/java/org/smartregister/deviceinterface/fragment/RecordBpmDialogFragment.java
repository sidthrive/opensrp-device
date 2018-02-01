package org.smartregister.deviceinterface.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import org.smartregister.deviceinterface.domain.BpmWrapper;

import java.io.Serializable;

/**
 * Created by sid-tech on 12/13/17.
 */

@SuppressLint("ValidFragment")
public class RecordBpmDialogFragment extends DialogFragment {
    private final Context context;
    private final BpmWrapper tag;
    public static final String WRAPPER_TAG = "tag";

    public RecordBpmDialogFragment(Context context, BpmWrapper tag) {
        this.context = context;
        if (tag == null) this.tag = new BpmWrapper();
        else this.tag = tag;
    }

    public RecordBpmDialogFragment(BpmWrapper tag) {
        this.tag = tag;

        context = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    public static RecordBpmDialogFragment newInstance(
            BpmWrapper tag) {

        BpmWrapper tagToSend;
        if (tag == null) {
            tagToSend = new BpmWrapper();
        } else {
            tagToSend = tag;
        }

        RecordBpmDialogFragment recordWeightDialogFragment = new RecordBpmDialogFragment(tag);

        Bundle args = new Bundle();
        args.putSerializable(WRAPPER_TAG, (Serializable) tagToSend);
        recordWeightDialogFragment.setArguments(args);

        return recordWeightDialogFragment;
    }

    public static RecordBpmDialogFragment newInstance(
            Context context,
            BpmWrapper tag) {
        return new RecordBpmDialogFragment(context, tag);
    }


}
