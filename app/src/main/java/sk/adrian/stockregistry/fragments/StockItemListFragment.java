package sk.adrian.stockregistry.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.List;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItem;
import sk.adrian.stockregistry.database.StockItemDatabase;

public class StockItemListFragment extends Fragment {
    private View view;

    private Context context;
    private AlertDialog dialog;
    private ArrayAdapter adapter;
    private List<StockItem> stockItems;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        view = inflater.inflate(R.layout.item_stock_list, parent, false);

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

                adapter = new ArrayAdapter(context, R.layout.list_view_row_item, stockItems);

                listView = view.findViewById(R.id.listItemStock);
                listView.setAdapter(adapter);


                setDialog(false);
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
//    @Override
//    public void onListItemClick(ListView listView,View view, int position, long id) {
//        Toast.makeText(context,"skuska",Toast.LENGTH_LONG).show();
//    }
}
