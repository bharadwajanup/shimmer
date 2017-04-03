package org.openmhealth.shim.withings.domain;

/**
 * Created by Bharadwaj on 4/3/2017.
 */
public enum WithingsNotifcationType {
    WEIGHT(1),
    BODY_MEASURE(4),
    ACTIVITY_MEASURE(16),
    SLEEP(44);

    private int magicNumber;



    WithingsNotifcationType(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    /**
     * @return the magic number used to refer to this body measure type in responses
     */
    public int getMagicNumber() {
        return magicNumber;
    }
}
