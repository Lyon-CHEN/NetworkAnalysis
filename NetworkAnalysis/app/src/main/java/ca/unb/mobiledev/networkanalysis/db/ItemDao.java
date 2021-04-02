package ca.unb.mobiledev.networkanalysis.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ItemDao {

    @Query("SELECT * from oui where assignment Like '%' || :searchKey || '%' COLLATE NOCASE ")
    List<Item> searchRecords(String searchKey);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Delete
    void deleteItem(Item item);
}
