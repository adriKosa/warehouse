package sk.adrian.stockregistry.database.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StockItem {
    @PrimaryKey (autoGenerate = true)
    public int siId;

    public String stockItemLabel;
    public String barcode;
    public String stockItemDescription;

    public StockItem() {
    }

    public StockItem(String stockItemLabel, String barcode, String stockItemDescription) {
        this.stockItemLabel = stockItemLabel;
        this.barcode = barcode;
        this.stockItemDescription = stockItemDescription;
    }

    public int getSiId(){
        return this.siId;
    }

    public String getStockItemLabel(){return this.stockItemLabel;}

    public String getBarcode() {
        return barcode;
    }

    // what to return on toString call on StockItem
    @Override
    public String toString() {
        return "Nazov: " + stockItemLabel + "\n" + "Ciarovy kod: " + barcode + "\nPopis:" + " (" + stockItemDescription + ")";
    }
}