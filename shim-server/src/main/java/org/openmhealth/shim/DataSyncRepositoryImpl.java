package org.openmhealth.shim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * Anup Bharadwaj
 */
public class DataSyncRepositoryImpl implements DataSyncRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public List<DataSyncGroup> getDataSyncDocuments() {


        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("updateAvailable").is(true)),
                Aggregation.group("shim")
                .push("userId").as("user")
//                .push(new BasicDBObject("user","$userId").append("measure","$measure")).as("records")

                );


        AggregationResults<DataSyncGroup> results = mongoTemplate.aggregate(aggregation,"dataSync",DataSyncGroup.class);
        return results.getMappedResults();
    }
}
