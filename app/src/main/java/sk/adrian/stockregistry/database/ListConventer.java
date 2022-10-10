package sk.adrian.stockregistry.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import sk.adrian.stockregistry.StockItemAmount;

public class ListConventer implements Serializable {
    @TypeConverter
    public String fromCountryLangList(List<StockItemAmount> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StockItemAmount>>() {
        }.getType();
        String json = gson.toJson(countryLang, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<StockItemAmount> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StockItemAmount>>() {
        }.getType();
        List<StockItemAmount> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }
}
