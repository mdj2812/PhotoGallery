package com.bignerdranch.android.photogallery;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

/**
 * Created by MA on 24/10/2017.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PollJobService extends JobService {
    private static final String TAG = "PollJobService" ;

    private static final boolean JOB_NOT_DONE_YET = true ;
    private static final boolean JOB_IS_DONE = false ;
    private static final boolean INTERRUPT_IF_RUNNING = true ;

    private static final int JOB_ID = 1 ;

    private PollTask mCurrentTask = new PollTask() ;

    public static void setServiceAlarm(Context context, boolean isOn) {

        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        if ( isOn ) {
            JobInfo jobInfo = new JobInfo.Builder( JOB_ID, new ComponentName( context, PollJobService.class ) )
                    .setRequiredNetworkType( JobInfo.NETWORK_TYPE_UNMETERED )
                    .setPeriodic(PollServiceUtils.POLL_INTERVAL_MS)
                    .build();

            scheduler.schedule( jobInfo ) ;
        } else {
            scheduler.cancel( JOB_ID );
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == JOB_ID ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i( TAG, "onStartJob") ;
        mCurrentTask.execute( params ) ;
        return JOB_NOT_DONE_YET;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i( TAG, "onStopJob") ;
        if ( mCurrentTask != null ) {
            mCurrentTask.cancel( INTERRUPT_IF_RUNNING ) ;
        }
        return JOB_IS_DONE;
    }

    private class PollTask extends AsyncTask<JobParameters,Void,Void> {

        @Override
        protected Void doInBackground( JobParameters... params ) {
            JobParameters jobParams = params[ 0 ] ;

            try {
                // Poll Flickr for new images
                PollServiceUtils.FetchNewImages(PollJobService.this) ;

                jobFinished( jobParams, JOB_IS_DONE );
            }
            catch ( Exception ex ) {
                jobFinished( jobParams, JOB_NOT_DONE_YET );
            }
            return null;
        }
    }
}
