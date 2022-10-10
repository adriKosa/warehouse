package sk.adrian.stockregistry.database;

import android.arch.persistence.room.TypeConverter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;


import sk.adrian.stockregistry.StockItemAmount;

public class Conventers {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


}
