package org.openmhealth.shim.withings.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.openmhealth.schema.domain.omh.*;
//import org.openmhealth.shim.withings.domain.WithingsWorkoutCategory;

import org.openmhealth.schema.domain.omh.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import static org.openmhealth.shim.common.mapper.JsonNodeMappingSupport.*;

/**
 * Created by Bharadwaj on 3/31/2017.
 */
public class WithingsDailyWorkoutsDataPointMapper extends WithingsListDataPointMapper<PhysicalActivity> {


    @Override
    Optional<DataPoint<PhysicalActivity>> asDataPoint(JsonNode node) {

        String name = "Walk";//WithingsWorkoutCategory.WALK.getName();
        PhysicalActivity.Builder builder = new PhysicalActivity.Builder(name);



        JsonNode dataNode = node.get("data");

        double calories = asRequiredDouble(dataNode,"calories");
        double distance = asRequiredDouble(dataNode,"distance");

        builder.setCaloriesBurned(new KcalUnitValue(KcalUnit.KILOCALORIE,calories));
        builder.setDistance(new LengthUnitValue(LengthUnit.METER,distance));

        long startTimeUnixEpoch = asRequiredLong(node,"startdate");
        String zone = asRequiredString(node,"timezone");
        OffsetDateTime startDate = OffsetDateTime.ofInstant(Instant.ofEpochSecond(startTimeUnixEpoch), ZoneId.of(zone));
        long endTimeUnixEpoch = asRequiredLong(node ,"enddate");
        OffsetDateTime endDate = OffsetDateTime.ofInstant(Instant.ofEpochSecond(endTimeUnixEpoch), ZoneId.of(zone));

        builder.setEffectiveTimeFrame(TimeInterval.ofStartDateTimeAndEndDateTime(startDate,endDate));

        PhysicalActivity measure = builder.build();

        Optional<JsonNode> raw =  asOptionalNode(node,"data");

        if(includeRaw)
            measure.setAdditionalProperty("raw",node);

        return Optional.of(newDataPoint(measure,Long.parseLong("123"),true,null));
    }

    @Override
    String getListNodeName() {
        return "series";
    }
}
