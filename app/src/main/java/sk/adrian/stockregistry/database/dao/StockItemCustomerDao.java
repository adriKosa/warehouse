package sk.adrian.stockregistry.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

import sk.adrian.stockregistry.database.Entity.StockItemCustomer;

@Dao
public interface StockItemCustomerDao {

    @Insert
    void insert(StockItemCustomer... customer);

    @Update
    void update(StockItemCustomer...customer);

    @Delete
    void delete(StockItemCustomer...customer);

    @Query("SELECT * FROM StockItemCustomer")
    List<StockItemCustomer> getAllStockItemCustomers();

    @Query("SELECT * FROM StockItemCustomer WHERE cId=:cId")
    public StockItemCustomer findStockItemCustomerById(final int cId);

    @Query("DELETE FROM StockItemCustomer WHERE name=:name")
    abstract void deleteStockItemCustomerByName(String name);
}
