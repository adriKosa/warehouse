package sk.adrian.stockregistry.database.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import sk.adrian.stockregistry.PrepareOrder;
import sk.adrian.stockregistry.StockItemAmount;
import sk.adrian.stockregistry.database.ListConventer;
import sk.adrian.stockregistry.database.ListPrepareOrderConventer;

@Entity
public class StockItemOrder {
    @PrimaryKey (autoGenerate = true)
    public int sioId;

    @TypeConverters(ListConventer.class)
    public List<StockItemAmount> itemAmounts;
    @TypeConverters(ListPrepareOrderConventer.class)
    public List<PrepareOrder> prepareOrders;
    public int cId;
    public Date orderDate;
    public int state;

    public StockItemOrder(List<StockItemAmount> itemAmounts,List<PrepareOrder> prepareOrders, int cId, Date orderDate, int state) {
        this.itemAmounts = itemAmounts;
        this.prepareOrders = prepareOrders;
        this.cId = cId;
        this.orderDate = orderDate;
        this.state = state;
    }

    public List<PrepareOrder> getPrepareOrders() {
        return prepareOrders;
    }

    public int getSioId() {
        return sioId;
    }

    public List<StockItemAmount> getItemAmounts() {
        return itemAmounts;
    }

    public int getcId() {
        return cId;
    }



}

