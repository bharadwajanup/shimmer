package org.openmhealth.shimmer.common.schedule;

import org.openmhealth.shim.DataSync;
import org.openmhealth.shim.DataSyncGroup;
import org.openmhealth.shim.DataSyncRepository;
import org.openmhealth.shim.ShimException;
import org.openmhealth.shimmer.common.service.WearableDataStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Bharadwaj on 4/6/2017.
 */
@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private DataSyncRepository dataSyncRepository;



    @Autowired
    WearableDataStorageService service;



    @Scheduled(fixedDelay = 10000)
    public void getUsersToUpdate() throws ShimException, UnknownHostException, InterruptedException {
        List<DataSync> users = null;

//        service.threadTester();
        List<DataSyncGroup> userGroups = dataSyncRepository.getDataSyncDocuments();

        //TODO: test this approach.
        for(DataSyncGroup group:userGroups)
        {
            String shim = group.getShim();
            users = dataSyncRepository.findByUserIdIn(group.getUser());
            service.storeData(users,shim);

        }


    }


}
