package org.smartregister.deviceinterface.listener;

import org.smartregister.deviceinterface.domain.BpmWrapper;

/**
 * Created by sid-tech on 12/13/17.
 */

public interface BpmActionListener {

    public void onBpmTaken(BpmWrapper tag);
    public void onBpmTaken();

}
