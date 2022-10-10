package sk.adrian.stockregistry.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
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

public class InsertCustomerFragment extends Fragment {
    private View view;
    private Context context;
    private List<StockItemCustomer> stockCustomers;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.toolbar_add_edit_customer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.insert_customer_fragment,container,false);

        attachHandler(view);

        return view;
    }

    private void attachHandler(View view) {
        Button btn = view.findViewById(R.id.btn_add_customer);
        stockCustomers = loadCustomers();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = getView().findViewById(R.id.insert_customer_name);
                EditText editNumber = getView().findViewById(R.id.insert_customer_number);
                EditText editAddress = getView().findViewById(R.id.insert_customer_address);

                if (editName.getText().toString().equals("")){
                    Toast.makeText(context,R.string.toast_customer_name,Toast.LENGTH_LONG).show();
                    editName.requestFocus();
                }else {
                     if (editNumber.getText().toString().equals("")){
                         Toast.makeText(context,R.string.toast_customer_number,Toast.LENGTH_LONG).show();
                         editNumber.requestFocus();
                     }else{
                         if (editAddress.getText().toString().equals("")){
                             Toast.makeText(context,R.string.toast_customer_address,Toast.LENGTH_LONG).show();
                             editAddress.requestFocus();
                         }else {

                             if (checkName(editName.getText().toString(),stockCustomers)){
                                 insertCustomer(editName.getText().toString(), editNumber.getText().toString(), editAddress.getText().toString());
                                 editName.setText("");
                                 editNumber.setText("");
                                 editAddress.setText("");
                                 loadCustomers();
                             }else {
                                 editName.requestFocus();
                                 Toast.makeText(context,"MENO UZ EXISTUJE",Toast.LENGTH_SHORT).show();
                             }

                         }
                     }
                }


            }
        });


        btn = view.findViewById(R.id.btn_edit_customer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchCustomerFragment());
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



    private void insertCustomer(final String name, final String number, final String address) {
    new AsyncTask<Void,Void,Void>(){
        @Override
        protected Void doInBackground(Void... voids) {
            StockItemCustomerDao stockItemCustomer = StockItemDatabase.getInstance(context).getCustomerDao();

            stockItemCustomer.insert(new StockItemCustomer(name, number, address));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast toast = Toast.makeText(context,R.string.toast_customer_add,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 120);
            toast.show();

        }
    }.execute();

    }

    private void loadFragment(Fragment fragment) {
        if (null != fragment) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(0, 0);
//                    .setCustomAnimations(
//                            R.animator.card_flip_right_in,
//                            R.animator.card_flip_right_out,
//                            R.animator.card_flip_left_in,
//                            R.animator.card_flip_left_out);
            // ft.add(R.id.registration_fragment_holder, new RegistrationCodeFragment());

            ft.replace(R.id.fragment_holder, fragment);
            ft.addToBackStack(null);
//            ft.add(R.id.fragment_holder, new StockItemListFragment());
            ft.commitAllowingStateLoss();
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}
