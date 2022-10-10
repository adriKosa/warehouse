package sk.adrian.stockregistry.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sk.adrian.stockregistry.InsertAtributesVariables;
import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItem;
import sk.adrian.stockregistry.database.Entity.StockItemAttributes;
import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.database.dao.StockItemAttributesDao;

public class InsertItemStockDataAtributes extends Fragment {
    Calendar myCalendar = Calendar.getInstance();

    public static View view;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        Bundle extras = getArguments();
        if (null != extras){
         id  = extras.getInt("stockItemId");
        }
        view = inflater.inflate(R.layout.insert_item_stock_data_atributes,container,false );

         attachHandler(view, id);

         return view;
    }

    private void attachHandler(View view,int id) {
        final InsertAtributesVariables variables = new InsertAtributesVariables();
        setName(id);

        Button btn = view.findViewById(R.id.storeItem);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDummyStockItemData(variables);
            }
        });

        variables.expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    private void insertDummyStockItemData(final InsertAtributesVariables variables) {

        if (variables.quantity.getText().toString().equals("")){
            Toast.makeText(context,"Policko mnozstvo nemoze byt prazdne",Toast.LENGTH_SHORT).show();
            variables.quantity.requestFocus();
        }else if (variables.weight.getText().toString().equals("")){
            Toast.makeText(context,"Policko hmotnost nemoze byt prazdne",Toast.LENGTH_SHORT).show();
            variables.weight.requestFocus();
        }else if (variables.location.getText().toString().equals("")){
            Toast.makeText(context,"Policko umiestnenie nemoze byt prazdne",Toast.LENGTH_SHORT).show();
            variables.location.requestFocus();
        }else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    StockItemAttributesDao stockItemAttributesDao = StockItemDatabase
                            .getInstance(context)
                            .getStockItemAttributesDao();


                    try {
                        Log.i("Datum", String.valueOf(new SimpleDateFormat("dd/MM/yy", Locale.GERMAN).parse(variables.expire.getText().toString())));
                        stockItemAttributesDao.insert(
                                new StockItemAttributes(Integer.parseInt(variables.quantity.getText().toString()),
                                        Double.parseDouble(variables.weight.getText().toString()),
                                        new SimpleDateFormat("dd/MM/yy",Locale.GERMAN).parse(variables.expire.getText().toString()),
                                        variables.location.getText().toString(),id,1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(context,"Tovar bol uspesne naskladneny",Toast.LENGTH_SHORT).show();
                    variables.clearAll();
                }
            }.execute();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        InsertAtributesVariables variables = new InsertAtributesVariables();
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        variables.expire.setText(sdf.format(myCalendar.getTime()));
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    public static int id;
    private Context context;

    @SuppressLint("StaticFieldLeak")
    public void setName(final int id) {
        final TextView name = view.findViewById(R.id.editText_name);
        new AsyncTask<Void, Void, StockItem>(){
            @Override
            protected StockItem doInBackground(Void... voids) {

                StockItem item = StockItemDatabase.getInstance(context).getStockItemDao().findStockItemById(id);

                return item;
            }

            @Override
            protected void onPostExecute(StockItem aVoid) {
                super.onPostExecute(aVoid);
                name.setText(aVoid.getStockItemLabel());
            }
        }.execute();
    }
}
