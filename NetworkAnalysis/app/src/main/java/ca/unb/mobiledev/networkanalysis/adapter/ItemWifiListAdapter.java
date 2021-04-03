package ca.unb.mobiledev.networkanalysis.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

import ca.unb.mobiledev.networkanalysis.R;
import ca.unb.mobiledev.networkanalysis.network.NetworkUtil;

public class ItemWifiListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final static String TAG = "WifiActivity - listview";
    LayoutInflater inflater;
    List<ScanResult> list;
    Context context;
    private final boolean[] showControl; // control show or invisible


    public ItemWifiListAdapter(Context context, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.showControl = new boolean[list.size()];
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (showControl[position]) {
            showControl[position] = false;
        } else {
            showControl[position] = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_wifi_list, null);

        if (showControl[position]) {
            view.findViewById(R.id.itemDetailHiddenArea).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.itemDetailHiddenArea).setVisibility(View.GONE);
        }

        ScanResult scanResult = list.get(position);
        //ssid
        TextView textView = (TextView) view.findViewById(R.id.itemWifiSSID);
        if (scanResult.SSID.equals("")) {
            textView.setText(R.string.hiddenWifi);
        } else {
            textView.setText(scanResult.SSID);
        }


        //HZ
        textView = (TextView) view.findViewById(R.id.itemWifiHz);
        if (NetworkUtil.is24GHz(scanResult.frequency) ){
            textView.setText("2.4G");
        } else if ( NetworkUtil.is5GHz(scanResult.frequency) ) {
            textView.setText("5G");
        } else {
            textView.setText("unknown");
        }

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

        textView = (TextView) view.findViewById(R.id.itemWifiSignalStrength);
        textView.setText(String.valueOf(Math.abs(scanResult.level)));

        //wifi strength icon
        ImageView imageView = (ImageView) view.findViewById(R.id.itemWifiImageView);
        if (Math.abs(scanResult.level) > 100) {
            imageView.setImageResource(R.drawable.icon_wifi_strength_poor);
        } else if (Math.abs(scanResult.level) > 80) {
            imageView.setImageResource(R.drawable.icon_wifi_strength_low);
        } else if (Math.abs(scanResult.level) > 70) {
            imageView.setImageResource(R.drawable.icon_wifi_strength_low);
        } else if (Math.abs(scanResult.level) > 60) {
            imageView.setImageResource(R.drawable.icon_wifi_strength_medium);
        } else if (Math.abs(scanResult.level) > 50) {
            imageView.setImageResource(R.drawable.icon_wifi_strength_high);
        } else {
            imageView.setImageResource(R.drawable.icon_wifi_strength_high);
        }
        //Bssid hidden default
        textView = view.findViewById(R.id.itemWifiBSSIDText);
        textView.setText(scanResult.BSSID);
        //Bandwidth hidden default
        textView = (TextView) view.findViewById(R.id.itemWifiBandWidthText);
        switch ( scanResult.channelWidth) {
            case ScanResult.CHANNEL_WIDTH_160MHZ:
                textView.setText("160 MHZ");
                break;
            case ScanResult.CHANNEL_WIDTH_20MHZ:
                textView.setText("20 MHZ");
                break;
            case ScanResult.CHANNEL_WIDTH_40MHZ:
                textView.setText("40 MHZ");
                break;
            case ScanResult.CHANNEL_WIDTH_80MHZ:
                textView.setText("80 MHZ");
                break;
            case ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ:
                textView.setText("80 MH + 80 MH");
                break;
            default:
                textView.setText("UNKNOWN");
                break;
        }

        return view;
    }




}
