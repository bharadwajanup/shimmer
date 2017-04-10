package org.openmhealth.shim.withings.domain;

import org.openmhealth.shim.withings.WithingsShim;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bharadwaj on 4/3/2017.
 */
public enum WithingsNotifcationType {
    WEIGHT(1, Arrays.asList(WithingsShim.WithingsDataType.BODY_WEIGHT)),
    BODY_MEASURE(4,Arrays.asList(WithingsShim.WithingsDataType.HEART_RATE, WithingsShim.WithingsDataType.BLOOD_PRESSURE)),
    ACTIVITY_MEASURE(16,Arrays.asList(WithingsShim.WithingsDataType.STEPS, WithingsShim.WithingsDataType.CALORIES, WithingsShim.WithingsDataType.ACTIVITY)),
    SLEEP(44,Arrays.asList(WithingsShim.WithingsDataType.SLEEP));

    private int magicNumber;
    public List<WithingsShim.WithingsDataType> endPoints;


    WithingsNotifcationType(int magicNumber, List<WithingsShim.WithingsDataType> endPoints) {
        this.magicNumber = magicNumber;
        this.endPoints = endPoints;
    }

    /**
     * @return the magic number used to refer to this body measure type in responses
     */
    public int getMagicNumber() {
        return magicNumber;
    }
}
