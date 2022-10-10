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
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sk.adrian.stockregistry.PrepareOrder;
import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.StockItemAmount;
import sk.adrian.stockregistry.database.Entity.StockItem;
import sk.adrian.stockregistry.database.Entity.StockItemAttributes;
import sk.adrian.stockregistry.database.Entity.StockItemOrder;
import sk.adrian.stockregistry.database.StockItemDatabase;
import sk.adrian.stockregistry.database.dao.StockItemAttributesDao;
import sk.adrian.stockregistry.database.dao.StockItemOrderDao;

public class MakeOrder2Fragment extends Fragment {
    private View view;

    private Context context;
    private AlertDialog dialog;
    private ArrayAdapter adapter;
    private List<StockItem> stockItems;
    private List<StockItemAttributes> stockItemAttributes;
    private List<StockItemAmount> itemAmounts = new ArrayList<>();
    private ListView listView;
    private int id;
    private int siId;
    private String siLabel;
    private int m = 0;
    private boolean check = true;
    private int p = 0;
    private List<PrepareOrder> prepareOrders = new ArrayList<>() ;
    private StockItem stockItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        Bundle extras = getArguments();
        if (null != extras){
            id  = extras.getInt("customerId");
        }


        view = inflater.inflate(R.layout.make_order2_fragment, parent, false);

        attachHandlers(view);





        return view;
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
            fragmentBundle.putInt("stockItemId",id);
            fragment.setArguments(fragmentBundle);
            ft.replace(R.id.fragment_holder, fragment);
            ft.addToBackStack(null);
//            ft.add(R.id.fragment_holder, new StockItemListFragment());
            ft.commitAllowingStateLoss();
        }


    }

    private void attachHandlers(final View view) {
        final TextView selected = view.findViewById(R.id.selectedItem);
        final TextView amountOnStore = view.findViewById(R.id.tv_store_balance);
        setDialog(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                stockItems = StockItemDatabase
                        .getInstance(context)
                        .getStockItemDao().getAllStockItems();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                EditText filter = getView().findViewById(R.id.searchItem);
                adapter = new ArrayAdapter(context, R.layout.list_view_row_item, stockItems);

                listView = view.findViewById(R.id.searchItemList);
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

                        StockItem item = (StockItem) listView.getItemAtPosition(position);
                        m = 0;
                        siId = item.getSiId();
                        siLabel = item.getStockItemLabel();
                        List<StockItemAttributes>  allItems = StockItemDatabase.getInstance(context).getStockItemAttributesDao().findStockItemById(siId);
                        for (int i = 0 ; i<StockItemDatabase.getInstance(context).getStockItemAttributesDao().findStockItemById(siId).size() ; i++){
                            m = m + allItems.get(i).getQuantity();
                        }
                        amountOnStore.setText(String.valueOf(m));
                        selected.setText(siLabel);

                    }
                });


                setDialog(false);
            }
        }.execute();

        Button btn = view.findViewById(R.id.btn_next);
        final EditText amount = view.findViewById(R.id.et_amount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkQuantity(m,amount)){
                    itemAmounts.add(new StockItemAmount(siId, Integer.parseInt(amount.getText().toString()),siLabel));
                    amount.setText("");
                    selected.setText("Tovar");
                    Log.i("List", itemAmounts.get(0).getStockItemLabel());
                }
            }
        });

        btn = view.findViewById(R.id.btn_make_order);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrder(selected,amount) && checkQuantity(m,amount)){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    itemAmounts.add(new StockItemAmount(siId, Integer.parseInt(amount.getText().toString()),siLabel));
                                    for (int i = 0 ; i < itemAmounts.size(); i++){
                                        toPrepare(itemAmounts.get(i));
                                    }
                                    insertOrder(id,itemAmounts);
                                    goBack();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }


                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Chces spravit objednavku?").setPositiveButton("Ano", dialogClickListener)
                            .setNegativeButton("Nie", dialogClickListener).show();
                }


            }
        });
    }

    private void toPrepare(final StockItemAmount item){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                stockItemAttributes = StockItemDatabase.getInstance(context).getStockItemAttributesDao().findStockItemById(item.getSiId());
                StockItemAttributesDao dao = StockItemDatabase.getInstance(context).getStockItemAttributesDao();
                stockItem = StockItemDatabase.getInstance(context).getStockItemDao().findStockItemById(item.getSiId());
                while(check){
                    if (stockItemAttributes.get(p).quantity > item.getAmount()){
                        dao.insert(new StockItemAttributes(stockItemAttributes.get(0).quantity - item.getAmount() ,
                                stockItemAttributes.get(p).weight,
                                stockItemAttributes.get(p).expire,
                                stockItemAttributes.get(p).location,
                                stockItemAttributes.get(p).siId,1 ));
                        dao.insert(new StockItemAttributes(item.getAmount() ,
                                stockItemAttributes.get(p).weight,
                                stockItemAttributes.get(p).expire,
                                stockItemAttributes.get(p).location,
                                stockItemAttributes.get(p).siId,2));
                        StockItemDatabase.getInstance(context).getStockItemAttributesDao().delete(stockItemAttributes.get(p));
                        prepareOrders.add(new PrepareOrder (stockItem.getStockItemLabel()
                                ,item.getAmount()
                                ,stockItemAttributes.get(p).location));
                        check = false;
                    }
                    if (stockItemAttributes.get(p).quantity == item.getAmount()){
                        stockItemAttributes.get(p).setState(2);
                        StockItemDatabase.getInstance(context).getStockItemAttributesDao().update(stockItemAttributes.get(p));
                        prepareOrders.add(new PrepareOrder (stockItem.getStockItemLabel()
                                ,stockItemAttributes.get(p).quantity
                                ,stockItemAttributes.get(p).location));
                        check = false;
                    }
                    if (stockItemAttributes.get(p).quantity < item.getAmount()){
                        item.setAmount(item.getAmount() - stockItemAttributes.get(p).quantity);
                        stockItemAttributes.get(p).setState(2);
                        StockItemDatabase.getInstance(context).getStockItemAttributesDao().update(stockItemAttributes.get(p));
                        prepareOrders.add(new PrepareOrder (stockItem.getStockItemLabel()
                                ,stockItemAttributes.get(p).quantity
                                ,stockItemAttributes.get(p).location));
                    }
                    p++;
                }
                p = 0;
                check = true;
                return null;
            }
        }.execute();
    }

    private boolean checkQuantity(int m, EditText amount) {
        boolean check = true;
        if (m < Integer.parseInt(amount.getText().toString())){
            Toast.makeText(context,"Nemame tolko kusov na sklade treba vybrat mensie mnozstvo",Toast.LENGTH_LONG).show();
            check = false;
        }
        return check;
    }

    private boolean checkOrder(TextView selected, EditText amount) {
        boolean check = true;
        if (selected.getText().toString().equals("Tovar")){
            Toast.makeText(context,"Najprv vyber tovar az potom vies spravit objednavku",Toast.LENGTH_LONG).show();
            check = false;
        }else if (amount.getText().toString().equals("")){
            amount.requestFocus();
            Toast.makeText(context,"Vyber mnozstvo",Toast.LENGTH_SHORT).show();
            check = false;
        }
        return check;
    }

    private void goBack() {
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.popBackStack();
    }

    @SuppressLint("StaticFieldLeak")
    private void insertOrder(final int id, final List<StockItemAmount> itemAmounts) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                StockItemOrderDao dao = StockItemDatabase.getInstance(context).getStockItemOrderDao();
                Date date = new Date();
                dao.insert(new StockItemOrder(itemAmounts,prepareOrders,id, date,1));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context,"Spravil si novu objednavku", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void setDialog(boolean show){
        if (null == dialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(R.layout.progress);
            dialog = builder.create();
        }

        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

}
