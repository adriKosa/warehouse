package sk.adrian.stockregistry.database.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StockItemCustomer {
    @PrimaryKey(autoGenerate = true)
    public int cId;

    public String name;
    public String number;
    public String address;

    public int getcId() {
        return cId;
    }

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StockItemCustomer(){}

    public StockItemCustomer(String name, String number, String address){
        this.name = name;
        this.number = number;
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }
}
