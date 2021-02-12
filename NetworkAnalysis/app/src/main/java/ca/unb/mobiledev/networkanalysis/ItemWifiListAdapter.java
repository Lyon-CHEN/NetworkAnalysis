package ca.unb.mobiledev.networkanalysis;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class ItemWifiListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<ScanResult> list;
    Context context;
    public ItemWifiListAdapter(Context context, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        view = inflater.inflate(R.layout.item_wifi_list, null);
        ScanResult scanResult = list.get(position);
        //ssid
        TextView textView = (TextView) view.findViewById(R.id.itemWifiSSID);
        if (scanResult.SSID.equals("")) {
            textView.setText(R.string.hiddenWifi);
        } else {
            textView.setText(scanResult.SSID);
        }


        //bssid
        textView = (TextView) view.findViewById(R.id.itemWifiBSSID);
        textView.setText(scanResult.BSSID);
         /*
        //capabilities
        textView = (TextView) view.findViewById(R.id.itemWifiCap);
        textView.setText(scanResult.capabilities);
        //standard

        textView = (TextView) view.findViewById(R.id.itemWifiStandard);
        int intStandard= scanResult.getWifiStandard();
        switch ( intStandard) {
            case ScanResult.WIFI_STANDARD_11AC:
                textView.setText("802.11AC");
                break;
            case ScanResult.WIFI_STANDARD_11AX:
                textView.setText("802.11AX");
                break;
            case ScanResult.WIFI_STANDARD_11N:
                textView.setText("802.11N");
                break;
            case ScanResult.WIFI_STANDARD_LEGACY:
                textView.setText("802.11a/b/g");
                break;
            default:
                textView.setText("UNKNOWN");
                break;
        }*/

        TextView signalStrength = (TextView) view.findViewById(R.id.itemWifiSignalStrength);
        signalStrength.setText(String.valueOf(Math.abs(scanResult.level)));

        //icon
        ImageView imageView = (ImageView) view.findViewById(R.id.itemWifiImageView);

        if (Math.abs(scanResult.level) > 100) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wifi_strength_poor));
        } else if (Math.abs(scanResult.level) > 80) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wifi_strength_low));
        } else if (Math.abs(scanResult.level) > 70) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wifi_strength_low));
        } else if (Math.abs(scanResult.level) > 60) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wifi_strength_medium));
        } else if (Math.abs(scanResult.level) > 50) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wifi_strength_high));
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wifi_strength_high));
        }
        return view;
    }

}
