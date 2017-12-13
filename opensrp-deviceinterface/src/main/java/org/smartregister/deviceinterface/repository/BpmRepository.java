package org.smartregister.deviceinterface.repository;

import org.smartregister.deviceinterface.domain.Bpm;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.List;

/**
 * Created by sid-tech on 12/13/17.
 */

public class BpmRepository extends BaseRepository {
    public BpmRepository(Repository repository) {
        super(repository);
    }

    public List<Bpm> findByEntityId(String entityId) {
        return null;
    }
}
