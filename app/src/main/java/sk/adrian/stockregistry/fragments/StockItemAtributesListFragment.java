package sk.adrian.stockregistry.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItemAttributes;
import sk.adrian.stockregistry.database.StockItemDatabase;



public class StockItemAtributesListFragment extends Fragment {
    private View view;

    public static Context context;
    private AlertDialog dialog;
    private ArrayAdapter adapter;
    private List<StockItemAttributes> itemAtributes;
    private List<StockItemAttributes> itemAttributesOk = new ArrayList<>();
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         super.onCreateView(inflater, container, savedInstanceState);
         view = inflater.inflate(R.layout.item_atribute_list,container,false);

         attachHandlers(view);

         return view;
    }


    private void attachHandlers(final View view) {

        setDialog(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                Date today = cal.getTime();
                itemAtributes = StockItemDatabase
                        .getInstance(context)
                        .getStockItemAttributesDao().getAllStockItemAtributesByDate();

                for (int i = 0 ; i< itemAtributes.size() ; i++){
                    if (today.before(itemAtributes.get(i).expire)){
                        itemAttributesOk.add(itemAtributes.get(i));
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


                adapter = new ArrayAdapter(context, R.layout.list_view_row_item, itemAttributesOk);

                listView = view.findViewById(R.id.listAtributeStock);
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
}
