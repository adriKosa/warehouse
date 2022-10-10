package sk.adrian.stockregistry.fragments;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItemCustomer;
import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.database.dao.StockItemCustomerDao;

public class EditCustomerFragment extends Fragment {
    private View view;
    private Context context;
    private StockItemCustomer customer;
    private int cId;
    private List<StockItemCustomer> stockCustomers;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         view = inflater.inflate(R.layout.edit_customer_fragment,container,false);

        Bundle extras = getArguments();
        if (null != extras){
             cId = extras.getInt("StockItemCustomer");
        }

         attachHandler(view,cId);
         
         return view;
    }

    private void attachHandler(View view, int id) {
        stockCustomers = loadCustomers();
        customer = setFields(id);

        Button btn = view.findViewById(R.id.btn_edit_customer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctFill()){
                    updateCustomer();
                }
            }
        });
    }

    private boolean checkName(String name,List<StockItemCustomer> customers){
        boolean check = true;
        for (int i = 0; i<customers.size();i++){
            if (customers.get(i).getName().equals(name)){
                check = false;
                break;
            }
        }
        return check;
    }

    private List<StockItemCustomer> loadCustomers(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                stockCustomers = StockItemDatabase.getInstance(context).getCustomerDao().getAllStockItemCustomers();

                return null;
            }

        }.execute();
        return stockCustomers;
    }

    @SuppressLint("StaticFieldLeak")
    private void updateCustomer() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                EditText editName = view.findViewById(R.id.insert_customer_name);
                EditText editNumber = view.findViewById(R.id.insert_customer_number);
                EditText editAddress = view.findViewById(R.id.insert_customer_address);

                customer.setName(editName.getText().toString());
                customer.setNumber(editNumber.getText().toString());
                customer.setAddress(editAddress.getText().toString());

                StockItemCustomerDao stockItemCustomer = StockItemDatabase.getInstance(context).getCustomerDao();
                stockItemCustomer.update(customer);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context,"Zakaznik bol upraveny",Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private boolean correctFill(){
        EditText editName = view.findViewById(R.id.insert_customer_name);
        EditText editNumber = view.findViewById(R.id.insert_customer_number);
        EditText editAddress = view.findViewById(R.id.insert_customer_address);
        boolean correct = true;

        if (editName.getText().toString().equals("")){
            Toast.makeText(context,R.string.toast_customer_name,Toast.LENGTH_LONG).show();
            editName.requestFocus();
            correct = false;
        }else {
            if (editNumber.getText().toString().equals("")){
                Toast.makeText(context,R.string.toast_customer_number,Toast.LENGTH_LONG).show();
                editNumber.requestFocus();
                correct = false;
            }else{
                if (editAddress.getText().toString().equals("")){
                    Toast.makeText(context,R.string.toast_customer_address,Toast.LENGTH_LONG).show();
                    editAddress.requestFocus();
                    correct = false;
                }else {

                    if (checkName(editName.getText().toString(),stockCustomers)){


                    }else {
                        editName.requestFocus();
                        Toast.makeText(context,"MENO UZ EXISTUJE",Toast.LENGTH_SHORT).show();
                        correct = false;
                    }

                }
            }
        }
        return correct;
    }


    @SuppressLint("StaticFieldLeak")
    private StockItemCustomer setFields(final int id){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                customer = StockItemDatabase.getInstance(context).getCustomerDao().findStockItemCustomerById(id);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                EditText editName = view.findViewById(R.id.insert_customer_name);
                EditText editNumber = view.findViewById(R.id.insert_customer_number);
                EditText editAddress = view.findViewById(R.id.insert_customer_address);

                editName.setText(customer.getName());
                editNumber.setText(customer.getNumber());
                editAddress.setText(customer.getAddress());
            }
        }.execute();
        return customer;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}
