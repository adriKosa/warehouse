package sk.adrian.stockregistry.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;


import java.util.List;

import sk.adrian.stockregistry.CaptureActivityPortrait;
import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItem;
import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.database.dao.StockItemDao;

public class InsertItemFragment extends Fragment {
    private Context context;
    final Fragment fragment = this;
    final Activity activity = fragment.getActivity();
    private View view;
    private List<StockItem> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
         super.onCreateView(inflater, parent, savedInstanceState);

         view = inflater.inflate(R.layout.insert_item_fragment,parent,false);

        attachHandler(view);

         return view;
    }

    private void attachHandler(final View view) {
        loadItems();
        Button btn = view.findViewById(R.id.btn_insert);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDummyStockItemData();
            }
        });

        btn = view.findViewById(R.id.btn_delete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStockItemData();
            }
        });

        btn = view.findViewById(R.id.btn_scan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scannBarcode();
            }
        });
    }



    private void scannBarcode(){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.forFragment(this).initiateScan();

    }

    @SuppressLint("StaticFieldLeak")
    private void insertDummyStockItemData() {
        final EditText name =  getView().findViewById(R.id.insert_name);
        final EditText barcode = getView().findViewById(R.id.insert_barcode);
        final EditText description = getView().findViewById(R.id.insert_description);

        if (name.getText().toString().equals("")){
            Toast.makeText(context,"Policko nazov nemoze byt prazdne",Toast.LENGTH_SHORT).show();
            name.requestFocus();
        }else if (barcode.getText().toString().equals("")){
            Toast.makeText(context, "Policko ciarovy kod nemoze byt prazdne", Toast.LENGTH_SHORT).show();
            barcode.requestFocus();
        }else if (!checkName(name.getText().toString(),itemList)){
            Toast.makeText(context,"Tento nazov uz existuje",Toast.LENGTH_SHORT).show();
            name.requestFocus();
        }else if (!checkBarcode(barcode.getText().toString(), itemList)){
            Toast.makeText(context,"Tento ciarovy kod sa uz pouziva",Toast.LENGTH_SHORT).show();
            barcode.requestFocus();
        }else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    StockItemDao stockItemDao = StockItemDatabase
                            .getInstance(context)
                            .getStockItemDao();


                    stockItemDao.insert(
                            new StockItem(name.getText().toString(), barcode.getText().toString(),description.getText().toString()));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(context,"Bol pridany novy tovar",Toast.LENGTH_SHORT).show();
                    name.setText("");
                    barcode.setText("");
                    description.setText("");
                }
            }.execute();
        }



    }

    private boolean checkName(String name, List<StockItem> items) {
        boolean check = true;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getStockItemLabel().equals(name)) {
                check = false;
                break;
            }
        }
        return check;
    }

    private boolean checkBarcode(String barcode, List<StockItem> items){
        boolean check = true;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getBarcode().equals(barcode)) {
                check = false;
                break;
            }
        }
        return check;
    }

    private List<StockItem> loadItems(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                itemList = StockItemDatabase.getInstance(context).getStockItemDao().getAllStockItems();

                return null;
            }

        }.execute();
        return itemList;
    }

    private void deleteStockItemData(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                StockItemDao stockItemDao = StockItemDatabase
                        .getInstance(context)
                        .getStockItemDao();

                EditText name = getView().findViewById(R.id.insert_name);

                stockItemDao.deleteStockItemByLabel(name.getText().toString());

                return null;
            }
        }.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(context, "Skenovanie bolo zrusene!", Toast.LENGTH_LONG).show();
            } else {
                EditText scaned = getView().findViewById(R.id.insert_barcode);
                scaned.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
