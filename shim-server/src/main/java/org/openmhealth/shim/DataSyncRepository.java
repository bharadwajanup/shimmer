package org.openmhealth.shim;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Bharadwaj on 4/6/2017.
 */
public interface DataSyncRepository extends MongoRepository<DataSync,String>
{
    List<DataSync> findAllByUpdateAvailableTrue();

}
