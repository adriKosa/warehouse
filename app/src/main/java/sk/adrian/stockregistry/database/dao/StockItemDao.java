package sk.adrian.stockregistry.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sk.adrian.stockregistry.database.Entity.StockItem;

@Dao
public interface StockItemDao {
    @Insert
    void insert(StockItem... stockItems);

    @Update
    void update(StockItem... stockItems);

    @Delete
    void delete(StockItem... stockItems);

    @Delete
    void deleteAll(List<StockItem> stockItems);

    @Query("SELECT * FROM StockItem")
    List<StockItem> getAllStockItems();

    @Query("SELECT * FROM StockItem WHERE siId=:siId")
    public StockItem findStockItemById(final int siId);

    @Query("SELECT * FROM StockItem WHERE siId=:stockItemLabel")
    List<StockItem> findStockItemByLabel(final int stockItemLabel);

    @Query("DELETE FROM StockItem WHERE stockItemLabel = :stockItemLabel")
    abstract void deleteStockItemByLabel(String stockItemLabel);

}