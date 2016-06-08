package com.woody.moment.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by john on 6/3/16.
 */
public class StatDataStruct {
    final private static int DEFAULT_ALERT_THRESHOLD = 30;

    private static AtomicInteger screenOnCount;
    private static AtomicInteger screenOffCount;
    private static AtomicInteger userPresentCount;
    private static StatDataStruct sInstance = new StatDataStruct();
    private static int mAlertThreshold = DEFAULT_ALERT_THRESHOLD;

    private StatDataStruct() {
        screenOnCount = new AtomicInteger(0);
        screenOffCount = new AtomicInteger(0);
        userPresentCount = new AtomicInteger(0);
    }

    public static StatDataStruct getStatDataInstance() {
        return sInstance;
    }

    /**
     * get Screen On times
     *
     * @return screen on count
     */
    final public int getScreenOnCount() {
        return screenOnCount.get();
    }

    /**
     * Count Screen On times
     */
    final public void increaseScreenOnCount() {
        screenOnCount.incrementAndGet();
    }

    /**
     * get Screen off times
     *
     * @return screen off count
     */
    final public int getScreenOffCount() {
        return screenOffCount.get();
    }

    /**
     * Count Screen off times
     */
    final public void increaseScreenOffCount() {
        this.screenOffCount.incrementAndGet();
    }

    /**
     * get user present times
     *
     * @return user present times
     */
    final public int getUserPresentCount() {
        return userPresentCount.get();
    }

    /**
     * Count user present times
     */
    final public void increaseUserPresentCount() {
        this.userPresentCount.incrementAndGet();
    }

    /**
     * Check if threshold arrive
     */
    final public boolean isAboveThreshold() {
        return userPresentCount.get() >= mAlertThreshold;
    }
}
