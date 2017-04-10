package org.openmhealth.shim;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Bharadwaj on 4/8/2017.
 */
public interface UserRepo extends MongoRepository<WearableUser,Long> {
    WearableUser findBy_id(long id);

}
