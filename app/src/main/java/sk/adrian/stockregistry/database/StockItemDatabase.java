package sk.adrian.stockregistry.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import sk.adrian.stockregistry.database.Entity.StockItem;
import sk.adrian.stockregistry.database.Entity.StockItemAttributes;
import sk.adrian.stockregistry.database.Entity.StockItemCustomer;
import sk.adrian.stockregistry.database.Entity.StockItemOrder;
import sk.adrian.stockregistry.database.dao.StockItemAttributesDao;
import sk.adrian.stockregistry.database.dao.StockItemCustomerDao;
import sk.adrian.stockregistry.database.dao.StockItemDao;
import sk.adrian.stockregistry.database.dao.StockItemOrderDao;

@Database(entities = { StockItem.class, StockItemAttributes.class, StockItemCustomer.class, StockItemOrder.class},
        version =12 , exportSchema = false)
@TypeConverters({Conventers.class, ListConventer.class, ListPrepareOrderConventer.class})

public abstract class StockItemDatabase extends RoomDatabase {

    private static final String DB_NAME = "stockItemDatabase.db";
    private static volatile StockItemDatabase instance;

    // to have one database
    public static synchronized StockItemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }



    // if does not exist - create
    private static StockItemDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                StockItemDatabase.class,
                DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    public abstract StockItemDao getStockItemDao();
    public abstract StockItemAttributesDao getStockItemAttributesDao();
    public abstract StockItemCustomerDao getCustomerDao();
    public abstract StockItemOrderDao getStockItemOrderDao();

}
