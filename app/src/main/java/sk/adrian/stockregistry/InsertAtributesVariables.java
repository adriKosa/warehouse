package sk.adrian.stockregistry;

import android.widget.EditText;

import sk.adrian.stockregistry.fragments.InsertItemStockDataAtributes;

public class InsertAtributesVariables {
    public EditText quantity = InsertItemStockDataAtributes.view.findViewById(R.id.quantityItem);
    public EditText weight = InsertItemStockDataAtributes.view.findViewById(R.id.weightItem);
    public EditText expire = InsertItemStockDataAtributes.view.findViewById(R.id.expireItem);
    public EditText location = InsertItemStockDataAtributes.view.findViewById(R.id.locationItem);

    public void clearAll(){
        quantity.setText("");
        weight.setText("");
        expire.setText("");
        location.setText("");
    }
}
