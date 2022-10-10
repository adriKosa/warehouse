package sk.adrian.stockregistry.database.Entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.fragments.StockItemAtributesListFragment;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = StockItem.class,
        parentColumns = "siId",
        childColumns = "siId",
        onDelete = CASCADE))


public class StockItemAttributes{
    @PrimaryKey(autoGenerate = true)
    public int siaId;

    public int quantity;
    public final double weight;
    public final Date expire;
    public final String location;
    public final int siId;
    public int state;


    public StockItemAttributes(int quantity, double weight, Date expire, String location, int siId,
                               int state) {
        this.quantity = quantity;
        this.weight = weight;
        this.expire = expire;
        this.location = location;
        this.siId = siId;
        this.state = state;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setState(int state){
        this.state = state;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        StockItem Item;
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Item = StockItemDatabase
                .getInstance(StockItemAtributesListFragment.context)
                .getStockItemDao().findStockItemById(siId);
        return "Nazov: " + Item.stockItemLabel + "\n" + "Mnozstvo: " + quantity + " | " + "Hmotnost: " + weight + " | " +
                "Trvanlivost: " + df.format(expire) + " | " + "Umiestnenie: " + location;

    }


}
