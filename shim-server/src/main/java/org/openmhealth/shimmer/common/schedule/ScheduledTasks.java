package org.openmhealth.shimmer.common.schedule;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.shim.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private ShimRegistry shimRegistry;

    @Autowired
    private AccessParametersRepo accessParametersRepo;

    @Autowired
    private UserRepo userRepo;

    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.uri}")
    private java.lang.String uri;



    @Scheduled(fixedDelay = 10000)
    public void getUsersToUpdate() throws ShimException, UnknownHostException {
        List<DataSync> users = dataSyncRepository.findAllByUpdateAvailableTrue();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            userRepo.save(new WearableUser(1,"Anup Bharadwaj",25,"Male",simpleDateFormat.parse("2017-04-03"),"Withings Activite","withings"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
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
                    insertData(userRecord.getUserId(),userRecord.getShim(),notificationType,measureVal);
                }
                measureVal.setLastRetrieveTime(new Date(System.currentTimeMillis()));

            }
            userRecord.setUpdateAvailable(false);
            dataSyncRepository.save(userRecord);
        }

    }

    private void insertData(String userId,String shim,String notificationType,SyncTimeMeasure dateRange) throws ShimException, UnknownHostException {
        List<String> endPoints = shimRegistry.getShim(shim).getEndPoints(notificationType.toUpperCase());
        MongoTemplate template = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(uri)),database));
        for(String endPoint:endPoints)
        {
            ShimDataResponse response = makeRequest(userId, shim, dateRange, endPoint);
            storeResponse(response,template,userId,endPoint);
        }

    }

    private void storeResponse(ShimDataResponse response, MongoTemplate template, String userId,String endPoint) {
        List<DataPoint> body = (List<DataPoint>) response.getBody();

        for(DataPoint dataPoint:body)
        {
            dataPoint.setAdditionalProperty("user_id",userId);
            template.save(dataPoint,endPoint);
        }

    }

    private ShimDataResponse makeRequest(String userId, String shim, SyncTimeMeasure dateRange, String endPoint) throws ShimException {
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

    private OffsetDateTime toOffsetDateTime(Date date)
    {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
    }
}
