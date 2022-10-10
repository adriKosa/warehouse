package sk.adrian.stockregistry.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sk.adrian.stockregistry.database.Entity.StockItemOrder;

@Dao
public interface StockItemOrderDao {
    @Insert
    void insert(StockItemOrder s);

    @Update
    void update(StockItemOrder s);

    @Delete
    void delete(StockItemOrder s);

    @Delete
    void deleteAll(List<StockItemOrder> s);

    @Query("SELECT * FROM StockItemOrder")
    List<StockItemOrder> getAll();

    @Query("SELECT * FROM StockItemOrder WHERE sioId=:sioId")
    StockItemOrder getOrderById(int sioId);
}
