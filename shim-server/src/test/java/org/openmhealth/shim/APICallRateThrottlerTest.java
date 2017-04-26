package org.openmhealth.shim;

import org.junit.Assert;
import org.junit.Test;


/**
 * Anup Bharadwaj
 */
public class APICallRateThrottlerTest {
    APICallRateThrottler throttler;
    @Test
    public void testAcquire() throws Exception {
        long rate = 10;
        long duration = 1000;
         throttler = new APICallRateThrottler(rate,duration);
        long start = System.currentTimeMillis();
        for(int i =0; i<=rate; i++)
        {
            throttler.acquire();
            System.out.println(i);

        }
        long diff = System.currentTimeMillis() - start;
        Assert.assertTrue(String.valueOf(diff),diff>=duration);

    }

}