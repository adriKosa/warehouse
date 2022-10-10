package sk.adrian.stockregistry.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItemOrder;
import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.database.dao.StockItemCustomerDao;

public class PrepareOrderFragment extends Fragment {
    View view;
    private List<StockItemOrder> itemOrders;
    private List<String> orders = new ArrayList<>();
    private Context context;
    private ArrayAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.prepare_order_fragment,container,false);
        attachHandlers(view);
        return view;
    }

    private void attachHandlers(View view) {
        loadAllOrders(view);
    }

    private void loadAllOrders(final View view){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                itemOrders = StockItemDatabase.getInstance(context).getStockItemOrderDao().getAll();
                StockItemCustomerDao dao = StockItemDatabase.getInstance(context).getCustomerDao();
                if (orders.size()==0){
                    for (int i = 0 ; i < itemOrders.size(); i++){
                        orders.add("Zákazník: " + dao.findStockItemCustomerById(itemOrders.get(i).getcId()).getName() +
                                "\nČíslo objednávky: " + String.valueOf(itemOrders.get(i).getSioId()));
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                EditText filter = getView().findViewById(R.id.searchFilter);

                adapter = new ArrayAdapter(context,R.layout.listview_order_row,orders);
                listView = view.findViewById(R.id.listPrepareOrder);
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
                        String oreder = (String) listView.getItemAtPosition(position);
                        oreder = oreder.substring(oreder.lastIndexOf(" ")+1);
                        loadFragment(new PrepareOrder2Fragment(),Integer.parseInt(oreder));
                    }
                });
            }
        }.execute();
    }

    private void loadFragment(Fragment fragment, int id) {

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
            fragmentBundle.putInt("orderId",id);
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
