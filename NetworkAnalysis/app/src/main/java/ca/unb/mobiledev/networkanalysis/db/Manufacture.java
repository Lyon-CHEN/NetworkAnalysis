package ca.unb.mobiledev.networkanalysis.db;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.unb.mobiledev.networkanalysis.R;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract  class Manufacture extends RoomDatabase
{
    private final static String TAG = "Manufacture";
    private static Manufacture mInstance;
    private static final int NUMBER_OF_THREADS = 4;
    private final static String DB_NAME = "oui.db";

    public abstract ItemDao itemDao();

    // Define an ExecutorService with a fixed thread pool which is used to run database operations asynchronously on a background thread
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static Manufacture getDatabase(final Context mContext) {

         if (mInstance == null)
        {
            synchronized (Manufacture.class)
            {
                if (mInstance == null)                {
                    mInstance = Room.databaseBuilder(mContext.getApplicationContext(),
                            Manufacture.class, DB_NAME).createFromAsset(DB_NAME)
                            .build();
                }
            }
        }

        return mInstance;
    }
}
