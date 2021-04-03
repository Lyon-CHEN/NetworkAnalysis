package ca.unb.mobiledev.networkanalysis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.unb.mobiledev.networkanalysis.R;
import ca.unb.mobiledev.networkanalysis.network.Device;


public class SearchDeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerView.OnItemTouchListener
{
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_ITEM = 2;

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
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;

        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return false;
        //return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return false;
        //return position > mDeviceList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view=null;
        /*if (viewType == TYPE_ITEM) {
            view = new DeviceHolder(LayoutInflater.from(mContext).inflate(
                    R.layout.item_device_list, parent, false));
        } else if (viewType == TYPE_HEADER) {
            view = new HeaderViewHolder( LayoutInflater.from(mContext).inflate(R.layout.item_device_list_head, parent, false) );
        }*/
        view = new DeviceHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_device_list, parent, false));
       return view;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mDeviceIpHead.setText("IP Address:     ");
            ((HeaderViewHolder) holder).mDeviceMacHead.setText("Mac Address:   ");
            ((HeaderViewHolder) holder).mDeviceManufactureHead.setText("Manufacture:");
        } else if ( holder instanceof DeviceHolder) {
            Device device = mDeviceList.get(position);
            if (device != null) {
                ((DeviceHolder) holder).mDeviceIp.setText("IP: "+device.Ip);
                ((DeviceHolder) holder).mDeviceMac.setText("Mac Addr: "+device.macAddress);
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
                //String vendor = device.vendor.length() > 10 ? device.vendor.substring(0, 10) : device.vendor;
                ((DeviceHolder) holder).mDeviceManufactureFullName.setText(device.vendor);
                ((DeviceHolder) holder).mDeviceManufactureAddress.setText(device.address);

            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mDeviceList.size();
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        TextView mDeviceIpHead;
        TextView mDeviceMacHead;
        TextView mDeviceName;
        TextView mDeviceManufactureHead;

        public HeaderViewHolder(View itemView)
        {
            super(itemView);
            mDeviceIpHead = (TextView) itemView.findViewById(R.id.device_ip_head);
            mDeviceMacHead = (TextView) itemView.findViewById(R.id.device_mac_head);
            //mDeviceName = (TextView) itemView.findViewById(R.id.device_name);
            mDeviceManufactureHead = (TextView) itemView
                    .findViewById(R.id.device_manufacture_head);


        }

    }

    class DeviceHolder extends RecyclerView.ViewHolder
    {
        TextView mDeviceIp;
        TextView mDeviceMac;
        TextView mDeviceName;
        TextView mDeviceManufactureAddress;
        TextView mDeviceManufactureFullName;

        public DeviceHolder(View itemView)
        {
            super(itemView);
            mDeviceIp = (TextView) itemView.findViewById(R.id.device_ip);
            mDeviceMac = (TextView) itemView.findViewById(R.id.device_mac);
            //mDeviceName = (TextView) itemView.findViewById(R.id.device_name);

            mDeviceManufactureAddress = (TextView) itemView
                    .findViewById(R.id.device_manufacture_address);
            mDeviceManufactureFullName = (TextView) itemView
                    .findViewById(R.id.device_manufacture_fullName);
        }


    }
}
