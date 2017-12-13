package org.smartregister.deviceinterface;


import android.content.Context;

import org.smartregister.deviceinterface.repository.BpmRepository;
import org.smartregister.repository.Repository;

/**
 * Created by sid-tech on 12/13/17.
 */

public class DeviceInterfaceLibrary {
    private static DeviceInterfaceLibrary instance;
    private Context context;
    private BpmRepository bpmRepository;

    public DeviceInterfaceLibrary(Context context, Repository repository) {
        this.repository = repository;
        this.context = context;
    }

    public static DeviceInterfaceLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Repository does not exist!!! Did you forget to init? ");
        }
        return instance;
    }

    private final Repository repository;

    public Repository getRepository() {
        return repository;
    }

    public Context context() {
        return context;
    }

    public BpmRepository weightRepository() {
        if (bpmRepository == null) {
            bpmRepository = new BpmRepository(getRepository());
        }
        return bpmRepository;
    }

    public BpmRepository bpmRepository() {
        return null;
    }
}
