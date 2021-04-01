package ca.unb.mobiledev.networkanalysis;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ca.unb.mobiledev.networkanalysis.network.Constant;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class TestViewModel extends AndroidViewModel {
    private static final String TAG = "TestViewModel";

    public static final int DOWNLOAD_MESSAGE_PROGRESS_CODE = 100001;
    public static final int DOWNLOAD_MESSAGE_CODE = 100004;
    public static final int DOWNLOAD_MESSAGE_FAIL_CODE = 100002;
    public static final int DOWNLOAD_MESSAGE_COMPLETE_CODE = 100003;
    public static final int UPLOAD_MESSAGE_PROGRESS_CODE = 200001;
    public static final int UPLOAD_MESSAGE_CODE = 200004;
    public static final int UPLOAD_MESSAGE_FAIL_CODE = 200002;
    public static final int UPLOAD_MESSAGE_COMPLETE_CODE = 200003;

    private MutableLiveData<String> mDownloadSpeed;
    private MutableLiveData<String> mUploadSpeed;
    private MutableLiveData<Integer> mDownloadProgress;
    private MutableLiveData<Integer> mUploadProgress;
    static Handler  handler;

    private Semaphore mLock ;
    private static  int mTestStep=0;
    private DecimalFormat mDecimalFormat2;

    public LiveData<String> getDownloadSpeed() {
        return mDownloadSpeed;
    }
    public LiveData<Integer> getDownloadProgress() {
        return mDownloadProgress;
    }
    public LiveData<String> getUploadSpeed() {
        return mUploadSpeed;
    }
    public LiveData<Integer> getUploadProgress() {
        return mUploadProgress;
    }

    public TestViewModel(@NonNull Application application) {
        super(application);
        mDownloadSpeed = new MutableLiveData<String>();
        mDownloadProgress = new MutableLiveData<Integer>();
        mUploadSpeed = new MutableLiveData<String>();
        mUploadProgress = new MutableLiveData<Integer>();

        mLock = new Semaphore(1);
        mDecimalFormat2 =new DecimalFormat("#.00");
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {


            public boolean handleMessage(Message msg) {

                switch (msg.what){
                    case DOWNLOAD_MESSAGE_PROGRESS_CODE:
                        if(msg.obj!=null)
                            mDownloadProgress.postValue((Integer) msg.obj);
                        break;
                    case DOWNLOAD_MESSAGE_CODE:
                        if(msg.obj!=null)
                            mDownloadSpeed.postValue("Rate in KB /s : " + mDecimalFormat2.format(msg.obj));
                        break;
                    case DOWNLOAD_MESSAGE_COMPLETE_CODE:
                        mDownloadProgress.postValue(Constant.PROGRESS_MAX);
                        mDownloadSpeed.postValue("Download Average Rate in KB /s : " + mDecimalFormat2.format(msg.obj));
                        break;
                    case UPLOAD_MESSAGE_PROGRESS_CODE:
                        if(msg.obj!=null)
                            mUploadProgress.postValue((Integer) msg.obj);
                        break;
                    case UPLOAD_MESSAGE_CODE:
                        if(msg.obj!=null)
                            mUploadSpeed.postValue("Rate in KB /s : " + mDecimalFormat2.format(msg.obj));
                        break;
                    case UPLOAD_MESSAGE_COMPLETE_CODE:
                        mUploadProgress.postValue(Constant.PROGRESS_MAX);
                        mUploadSpeed.postValue("Upload Average Rate in KB /s : " + mDecimalFormat2.format(msg.obj));

                        break;
                }
                return false;
            };
        });

        new DownloadTestTask().execute();
        new UploadTestTask().execute();

    }

    public void testRun(){
        new DownloadTestTask().execute();
        new UploadTestTask().execute();
    }

    class DownloadTestTask extends AsyncTask<Void, Void, String> {

        private final static String testUrl = "http://ipv4.ikoula.testdebit.info/10M.iso";

        @Override
        protected String doInBackground(Void... params) {
            try {
                mLock.acquire();
            } catch (InterruptedException e){

            }
            SpeedTestSocket speedTestSocket = new SpeedTestSocket();
          // add a listener to wait for speedtest completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    // called when download/upload is finished
                    Message message = Message.obtain();
                    message.obj = report.getTransferRateOctet().divide( new BigDecimal(1000));
                    message.what = DOWNLOAD_MESSAGE_COMPLETE_CODE;
                    handler .sendMessage(message);
                    mLock.release();
                    //Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download/upload error occur
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    // called to notify download/upload progress

                    Message message = Message.obtain();
                    message.obj =  (int)percent;
                    message.what = DOWNLOAD_MESSAGE_PROGRESS_CODE;
                    handler .sendMessage(message);

                    Message message2 = Message.obtain();
                    message2.obj =  report.getTransferRateOctet().divide( new BigDecimal(1000));
                    message2.what = DOWNLOAD_MESSAGE_CODE;
                    handler .sendMessage(message2);

                    //Log.v("speedtest", "[PROGRESS] progress : " + (int)percent + "%");
                    //Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    //Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });

            speedTestSocket.startDownload(testUrl);

            return null;
        }
    }

    class UploadTestTask extends AsyncTask<Void, Void, String> {

        private final static String testUrl = "http://ipv4.ikoula.testdebit.info/";

        @Override
        protected String doInBackground(Void... params) {
            try {
                mLock.acquire();
            } catch (InterruptedException e){

            }

            SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            // add a listener to wait for speedtest completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    // called when download/upload is finished
                    Message message = Message.obtain();
                    message.obj = report.getTransferRateOctet().divide( new BigDecimal(1000));
                    message.what = UPLOAD_MESSAGE_COMPLETE_CODE;
                    handler .sendMessage(message);
                    mLock.release();
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download/upload error occur
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    // called to notify download/upload progress

                    Message message = Message.obtain();
                    message.obj =  (int)percent;

                    message.what = UPLOAD_MESSAGE_PROGRESS_CODE;
                    handler .sendMessage(message);

                    Message message2 = Message.obtain();
                    message2.obj =  report.getTransferRateOctet().divide( new BigDecimal(1000));
                    message2.what = UPLOAD_MESSAGE_CODE;
                    handler .sendMessage(message2);
                }
            });
            
            speedTestSocket.startUpload(testUrl, 10000000);

            return null;
        }
    }
}
