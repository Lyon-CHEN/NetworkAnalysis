package ca.unb.mobiledev.networkanalysis;


import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;

import ca.unb.mobiledev.networkanalysis.network.Constant;
import ca.unb.mobiledev.networkanalysis.radarview.RadarView;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class TestFragment extends Fragment {
    private static final String TAG = "TestFragmentView";
    private View vTestFragmentView;
    private TextView tDownloadResult;
    private ProgressBar mDownloadProgressBar;
    private TextView tUploadResult;
    private ProgressBar mUploadProgressBar;
    private RadarView radarView;
    private Button vTestButton;

    private TestViewModel mTestViewModel;


    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vTestFragmentView = inflater.inflate(R.layout.fragment_test, container, false);
        vTestButton = vTestFragmentView.findViewById(R.id.testBtn);

        tDownloadResult = vTestFragmentView.findViewById(R.id.testDownloadResult);
        mDownloadProgressBar = vTestFragmentView.findViewById(R.id.testDownloadProgressBar);
        mDownloadProgressBar.setMax(Constant.PROGRESS_MAX);

        tUploadResult = vTestFragmentView.findViewById(R.id.testUploadResult);
        mUploadProgressBar = vTestFragmentView.findViewById(R.id.testUploadProgressBar);
        mUploadProgressBar.setMax(Constant.PROGRESS_MAX);

        mTestViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);

        vTestButton.setOnClickListener(
                new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                mTestViewModel.testRun();
                }
            }
        );

        mTestViewModel.getDownloadSpeed().observe(getViewLifecycleOwner(),  mDownloadSpeed-> {
            tDownloadResult.setText(mDownloadSpeed.toString());
        });
        mTestViewModel.getDownloadProgress().observe(getViewLifecycleOwner(),  mProgress-> {
            mDownloadProgressBar.setProgress(mProgress);
        });

        mTestViewModel.getUploadSpeed().observe(getViewLifecycleOwner(),  mUploadSpeed-> {
            tUploadResult.setText(mUploadSpeed.toString());
        });
        mTestViewModel.getUploadProgress().observe(getViewLifecycleOwner(),  mProgress-> {
            mUploadProgressBar.setProgress(mProgress);
        });


        return vTestFragmentView;
    }


}