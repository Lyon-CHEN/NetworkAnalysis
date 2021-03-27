package ca.unb.mobiledev.networkanalysis;


import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class TestFragment extends Fragment {
    private static final String TAG = "TestFragmentView";
    private View TestFragmentView;
    private TextView speedResult;
    private ProgressBar runningBar;
    static Handler  handler;

    public static final int DOWNLOAD_MESSAGE_CODE = 100001;
    public static final int DOWNLOAD_MESSAGE_FAIL_CODE = 100002;
    public static final int DOWNLOAD_MESSAGE_COMPLETE_CODE = 100003;

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TestFragmentView = inflater.inflate(R.layout.fragment_test, container, false);

        speedResult = TestFragmentView.findViewById(R.id.speedResult);
        runningBar = TestFragmentView.findViewById(R.id.runningBar);

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {

            public boolean handleMessage(Message msg) {

                switch (msg.what){
                    case DOWNLOAD_MESSAGE_CODE:
                        if (runningBar.getVisibility() == View.GONE)
                            runningBar.setVisibility(View.VISIBLE);
                        if(msg.obj!=null)
                            runningBar.setProgress( (Integer) msg.obj);
                        break;
                    case DOWNLOAD_MESSAGE_COMPLETE_CODE:
                        runningBar.setVisibility(View.GONE);
                        speedResult.setText("Rate in KB /s : " + msg.obj);
                        break;
                }


                return false;
            };
        });

        new SpeedTestTask().execute();



        return TestFragmentView;
    }

    class SpeedTestTask extends AsyncTask<Void, Void, String> {

        private final static String testUrl = "http://ipv4.ikoula.testdebit.info/1M.iso";

        @Override
        protected String doInBackground(Void... params) {
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

                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
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
                    message.what = DOWNLOAD_MESSAGE_CODE;
                    handler .sendMessage(message);
                    //Log.v("speedtest", "[PROGRESS] progress : " + (int)percent + "%");
                    //Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    //Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });

            speedTestSocket.startDownload(testUrl);

            return null;
        }
    }
}