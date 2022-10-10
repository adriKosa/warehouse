package sk.adrian.stockregistry.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItem;
import sk.adrian.stockregistry.database.Entity.StockItemAttributes;
import sk.adrian.stockregistry.database.StockItemDatabase;

public class MainViewFragment extends Fragment {

    private Context context;
    private Dialog dialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        View view = inflater.inflate(R.layout.main_fragment, parent, false);

        //attachHandlers(view);

        return view;
    }

    private void attachHandlers(View view) {
        countDataInDatabase(view);
        Button btn = view.findViewById(R.id.btnMakeOrder);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new InsertItemFragment());
            }
        });

        btn = view.findViewById(R.id.btnInsertDummyItemStockAttributes);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new StockItemListSearchFragment());
            }
        });

        btn = view.findViewById(R.id.btnCountData);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDataInDatabase(v);
            }
        });

        btn = view.findViewById(R.id.bntClearData);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllDataFromDatabase();
            }
        });

        btn = view.findViewById(R.id.showShowStockItems);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new StockItemListFragment());
            }
        });

        btn = view.findViewById(R.id.showStockItemAtributes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new StockItemAtributesListFragment());
            }
        });

        btn=view.findViewById(R.id.btnInsertCustomer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new InsertCustomerFragment());
            }
        });

        btn=view.findViewById(R.id.btnMakeOrder);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MakeOrderFragment());
            }
        });


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

    private void deleteAllDataFromDatabase() {
        setDialog(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<StockItem> tmp = StockItemDatabase
                        .getInstance(context)
                        .getStockItemDao().getAllStockItems();

                StockItemDatabase
                        .getInstance(context)
                        .getStockItemDao().deleteAll(tmp);

                List<StockItemAttributes> tmp2 = StockItemDatabase
                        .getInstance(context)
                        .getStockItemAttributesDao().getAllstockItemAtributes();

                StockItemDatabase
                        .getInstance(context)
                        .getStockItemAttributesDao().deleteAll(tmp2);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                setDialog(false);
            }
        }.execute();
    }

    private void countDataInDatabase(View view) {
        final TextView textView = view.getRootView().findViewById(R.id.labelCountStockItem);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return StockItemDatabase
                        .getInstance(context)
                        .getStockItemDao().getAllStockItems().size();
            }
            @Override
            protected void onPostExecute(Integer count) {
                textView.setText(String.valueOf(count));
            }
        }.execute();


        final TextView textView2 = view.getRootView().findViewById(R.id.labelCountStockItemAttributes);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return StockItemDatabase
                        .getInstance(context)
                        .getStockItemAttributesDao().getAllstockItemAtributes().size();
            }
            @Override
            protected void onPostExecute(Integer count) {
                textView2.setText(String.valueOf(count));
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
