package ca.unb.mobiledev.networkanalysis.db;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ManufRepository {
    private final static String Tag = "ManufRepository";
    private ItemDao itemDao;

    public ManufRepository(Context mContext) {
        Manufacture db = Manufacture.getDatabase(mContext);
        itemDao = db.itemDao();
    }

    public String searchManufacture(String searchKey){
        String searchResult = null;
        List<Item> manufList = itemDao.searchRecords( searchKey);

        if(manufList.size()>0)
            searchResult = manufList.get(0).getOrganizationName();

        return searchResult;
    }
}
