package ca.unb.mobiledev.networkanalysis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ca.unb.mobiledev.networkanalysis.R;
import ca.unb.mobiledev.networkanalysis.network.Device;


public class SearchDeviceListAdapter extends RecyclerView.Adapter<SearchDeviceListAdapter.DeviceHolder>
{

    private Context mContext;
    private List<Device> mDeviceList;
    private String mLocalIp;
    private String mGateIp;

    public SearchDeviceListAdapter(Context context, List<Device> list)
    {
        this.mContext = context;
        this.mDeviceList = list;
       // this.mLocalIp = localIp;
       // this.mGateIp = gateip;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new DeviceHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_device_list, parent, false));
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position)
    {
        Device ip_mac = mDeviceList.get(position);
        if (ip_mac != null)
        {
            holder.mDeviceIp.setText(ip_mac.Ip);
            holder.mDeviceMac.setText(ip_mac.macAddress);
            //holder.mDeviceName.setText("");
           /* if (ip_mac.ip.equals(mLocalIp))
            {
                holder.mDeviceName.setText(mContext.getString(R.string.your_phone) + " "
                        + ip_mac.mDeviceName);
            }
            else if (ip_mac.mIp.equals(mGateIp))
            {
                holder.mDeviceName.setText(mContext.getString(R.string.gate_net));
            }
            else
            {
                holder.mDeviceName.setText(ip_mac.mDeviceName);
            }*/

            holder.mDeviceManufacture.setText(ip_mac.vendor);
        }
    }

    @Override
    public int getItemCount()
    {
        return mDeviceList.size();
    }

    class DeviceHolder extends RecyclerView.ViewHolder
    {
        TextView mDeviceIp;
        TextView mDeviceMac;
        TextView mDeviceName;
        TextView mDeviceManufacture;

        public DeviceHolder(View itemView)
        {
            super(itemView);
            mDeviceIp = (TextView) itemView.findViewById(R.id.device_ip);
            mDeviceMac = (TextView) itemView.findViewById(R.id.device_mac);
            //mDeviceName = (TextView) itemView.findViewById(R.id.device_name);
            mDeviceManufacture = (TextView) itemView
                    .findViewById(R.id.device_manufacture);
        }
    }
}
