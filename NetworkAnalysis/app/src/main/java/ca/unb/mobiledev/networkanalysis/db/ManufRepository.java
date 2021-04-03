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

    public Item searchManufacture(String searchKey){
        Item searchResult = null;
        Future<List<Item>> future  = Manufacture.databaseWriterExecutor.submit(new Callable<List<Item>>() {
            public List<Item> call() throws Exception {
                return  itemDao.searchRecords( searchKey);
            }
        });
        try{
            List<Item> manufList = future.get();
            if(manufList.size()>0)
                searchResult = manufList.get(0);
        }catch (Exception e){
            Log.e(Tag, e.getMessage());
        }
       return searchResult;
    }
}
