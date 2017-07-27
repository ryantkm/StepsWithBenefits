package com.eventdee.stepswithbenefits;

import android.database.Cursor;
import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.Filter;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.Calendar;

public class StepCountReporter {
    private final HealthDataStore mStore;

    public StepCountReporter(HealthDataStore store) {
        mStore = store;
    }

    public void start() {
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);
        readTodayStepCount();
    }

    // Read the today's step count on demand
    private void readTodayStepCount() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = System.currentTimeMillis();
        Filter filter = Filter.and(Filter.greaterThanEquals(HealthConstants.StepCount.START_TIME, startTime),
                                   Filter.lessThanEquals(HealthConstants.StepCount.START_TIME, endTime));

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                                                        .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                                                        .setProperties(new String[] {HealthConstants.StepCount.COUNT})
                                                        .setFilter(filter)
                                                        .build();

        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
            Log.e(DashboardFragment.APP_TAG, e.getClass().getName() + " - " + e.getMessage());
            Log.e(DashboardFragment.APP_TAG, "Getting step count fails.");
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListener = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            int count = 0;
            Cursor c = null;

            try {
                c = result.getResultCursor();
                if (c != null) {
                    while (c.moveToNext()) {
                        count += c.getInt(c.getColumnIndex(HealthConstants.StepCount.COUNT));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            DashboardFragment.getInstance().drawStepCount(String.valueOf(count));
        }
    };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d(DashboardFragment.APP_TAG, "Observer receives a data changed event");
            readTodayStepCount();
        }
    };

}
