package sk.adrian.stockregistry.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import sk.adrian.stockregistry.database.Entity.StockItemAttributes;

@Dao
public interface StockItemAttributesDao {
    @Insert
    void insert(StockItemAttributes... stockItemAttributes);

    @Update
    void update(StockItemAttributes... stockItemAttributes);

    @Delete
    void delete(StockItemAttributes... stockItemAttributes);

    @Delete
    void deleteAll(List<StockItemAttributes> stockItemAttributes);

    @Query("SELECT * FROM StockItemAttributes")
    List<StockItemAttributes> getAllstockItemAtributes();

    @Query("SELECT * FROM StockItemAttributes WHERE state = 1 ORDER BY expire")
    List<StockItemAttributes> getAllStockItemAtributesByDate();

//    @Query("SELECT * FROM StockItemAttributes WHERE expire  date('now') ")
//    List<StockItemAttributes> getAllStockItemAtributesAfterExpire();

    @Query("SELECT * FROM StockItemAttributes WHERE expire BETWEEN :from AND :to")
    List<StockItemAttributes> getAllStockItemAtributesSoonExpire(Date from, Date to);

    @Query("SELECT * FROM StockItemAttributes WHERE siId=:siId and state = 1 ORDER BY expire")
     List<StockItemAttributes> findStockItemById(final int siId);

    @Query("SELECT * FROM StockItemAttributes WHERE location=:location")
    List<StockItemAttributes> findStockItemByLocation(final int location);



}
