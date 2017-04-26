package org.openmhealth.shim;

/**
 * Anup Bharadwaj
 */
public class APICallRateThrottler {
    long start;
    long requests;
    long currentLimit;
    long durationLength; //In MilliSeconds

    public APICallRateThrottler(long requests, long duration) throws InterruptedException {
        this.requests = requests;
        this.durationLength = duration;

        reset(0);
    }

    private void reset(long duration) throws InterruptedException {
        Thread.sleep(duration);
        currentLimit = requests;
        start = System.currentTimeMillis();
    }

    public void acquire() throws InterruptedException {
        long cur = System.currentTimeMillis();
        long diff = cur - start;


        if (diff < durationLength) {
            //Sleep for the remaining time.
            if(currentLimit <= 0)
                reset(durationLength - diff);

        } else {
            reset(0);
        }
        currentLimit--;
    }

    public static APICallRateThrottler getThrottler(String shim) throws InterruptedException {
        if(shim == "withings")
            return new APICallRateThrottler(60,60000);
        return null;
    }
}
