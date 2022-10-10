package sk.adrian.stockregistry.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItemCustomer;
import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.database.dao.StockItemCustomerDao;

public class SearchCustomerFragment extends Fragment {
    private View view;
    private List<StockItemCustomer> stockCustomers;
    private Context context;
    private ListView listView;
    private ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         view = inflater.inflate(R.layout.customer_stock_search,container,false);

         attachHandler(view);

         return view;
    }

    private void attachHandler(final View view) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                stockCustomers = StockItemDatabase.getInstance(context).getCustomerDao().getAllStockItemCustomers();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                EditText filter = getView().findViewById(R.id.searchFilter);

                adapter = new ArrayAdapter(context, R.layout.listview_customer_row, stockCustomers);

                listView = view.findViewById(R.id.listCustomer);
                listView.setAdapter(adapter);

                filter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StockItemCustomer customer = (StockItemCustomer) listView.getItemAtPosition(position);

                        loadFragment(new EditCustomerFragment(),customer.getcId());

                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   final int pos, long id) {

                        final StockItemCustomer customer = (StockItemCustomer) listView.getItemAtPosition(pos);

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:

                                        deleteCustomer(customer);
                                        attachHandler(view);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:

                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Chces vymazat zakaznika "+customer.getName() + " ?").setPositiveButton("Ano", dialogClickListener)
                                .setNegativeButton("Nie", dialogClickListener).show();

                        return true;
                    }
                });


            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteCustomer(final StockItemCustomer customer) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                StockItemCustomerDao stockItemCustomer = StockItemDatabase.getInstance(context).getCustomerDao();
                stockItemCustomer.delete(customer);
                return null;
            }
        }.execute();

    }

    private void loadFragment(Fragment fragment, int cId) {

        if (null != fragment) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(0, 0);
//                    .setCustomAnimations(
//                            R.animator.card_flip_right_in,
//                            R.animator.card_flip_right_out,
//                            R.animator.card_flip_left_in,
//                            R.animator.card_flip_left_out);
            // ft.add(R.id.registration_fragment_holder, new RegistrationCodeFragment());
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putInt("StockItemCustomer", cId);
            fragment.setArguments(fragmentBundle);
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
