package org.openmhealth.shimmer.common.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.shim.*;
import org.openmhealth.shimmer.common.schedule.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Anup Bharadwaj
 */
@Service
public class WearableDataStorageService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private ShimRegistry shimRegistry;

    @Autowired
    private AccessParametersRepo accessParametersRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DataSyncRepository dataSyncRepository;


    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.uri}")
    private java.lang.String uri;

    @Async
    public void threadTester() throws InterruptedException {
        Thread.sleep(1000);
        log.info(Thread.currentThread().getName());
    }

    //TODO: IMPORTANT! - Handle failed insertions!
    @Async
    public void storeData(List<DataSync> users,String shim) throws ShimException, UnknownHostException, InterruptedException {
        APICallRateThrottler throttler = APICallRateThrottler.getThrottler(shim);
        for(DataSync userRecord:users)
        {
            Map<String,SyncTimeMeasure> syncmeasures = userRecord.getMeasure();
            for (Map.Entry<String,SyncTimeMeasure> entry: syncmeasures.entrySet())
            {
                String notificationType = entry.getKey();
                SyncTimeMeasure measureVal = entry.getValue();
                if(measureVal.getLastRetrieveTime() == null || measureVal.getLastRetrieveTime().before(measureVal.getLastSyncTime()))
                {
                    log.info(String.format("User %s has new data available", userRecord.getUserId()));
                    throttler.acquire();
                    insertData(userRecord.getUserId(),shim,notificationType,measureVal);
                }
                measureVal.setLastRetrieveTime(new Date(System.currentTimeMillis()));

            }
            userRecord.setUpdateAvailable(false);
            dataSyncRepository.save(userRecord);
        }
    }

    private synchronized void insertData(String userId,String shim,String notificationType,SyncTimeMeasure dateRange) throws ShimException, UnknownHostException {
        List<String> endPoints = shimRegistry.getShim(shim).getEndPoints(notificationType.toUpperCase());
        MongoTemplate template = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(uri)),database));

        for(String endPoint:endPoints)
        {
            ShimDataResponse response = makeRequest(userId, shim, dateRange, endPoint);
            storeResponse(response,template,userId,endPoint);
        }

    }

    private synchronized void storeResponse(ShimDataResponse response, MongoTemplate template, String userId,String endPoint) {
        List<DataPoint> body = (List<DataPoint>) response.getBody();

        for(DataPoint dataPoint:body)
        {
            dataPoint.setAdditionalProperty("user_id",userId);
            template.save(dataPoint,endPoint);
        }

    }

    private synchronized ShimDataResponse makeRequest(String userId, String shim, SyncTimeMeasure dateRange, String endPoint) throws ShimException {
        ShimDataRequest shimDataRequest = new ShimDataRequest();
        shimDataRequest.setDataTypeKey(endPoint);
        shimDataRequest.setNormalize(true);
        shimDataRequest.setIncludeRaw(true);

        if(dateRange.getLastRetrieveTime() != null)
            shimDataRequest.setStartDateTime(toOffsetDateTime(dateRange.getLastRetrieveTime()));
        else
        {
            WearableUser user = userRepo.findBy_id(Long.parseLong(userId));
            //TODO: Handle exception if the user doesn't exist.
            shimDataRequest.setStartDateTime(toOffsetDateTime(user.getStartDate()));
        }

        shimDataRequest.setEndDateTime(toOffsetDateTime(dateRange.getLastSyncTime()));

        AccessParameters accessParameters = accessParametersRepo.findByUsernameAndShimKey(
                userId, shim, new Sort(Sort.Direction.DESC, "dateCreated"));

        if (accessParameters == null) {
            throw new ShimException("User '" + userId + "' has not authorized shim: '" + shim + "'");
        }
        shimDataRequest.setAccessParameters(accessParameters);

        return shimRegistry.getShim(shim).getData(shimDataRequest);
    }

    private synchronized OffsetDateTime toOffsetDateTime(Date date)
    {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
    }
}
