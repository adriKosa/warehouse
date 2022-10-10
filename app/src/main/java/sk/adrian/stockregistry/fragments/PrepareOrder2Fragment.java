package sk.adrian.stockregistry.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import sk.adrian.stockregistry.R;
import sk.adrian.stockregistry.database.Entity.StockItemOrder;
import sk.adrian.stockregistry.database.StockItemDatabase;

public class PrepareOrder2Fragment extends Fragment {
    View view;
    Context context;
    int oId;
    StockItemOrder stockItemOrder;
    ArrayAdapter adapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.prepare_order2_fragment,container,false);
        Bundle extras = getArguments();
        if (null != extras){
            oId  = extras.getInt("orderId");
        }
        attachHandler(view,oId);
        return view;
    }

    private void attachHandler(View view,int oId) {
        makePrepareList(view,oId);

    }

    private void makePrepareList(final View view, final int oId) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                stockItemOrder = StockItemDatabase.getInstance(context).getStockItemOrderDao().getOrderById(oId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new ArrayAdapter(context,R.layout.listview_order_row,stockItemOrder.getPrepareOrders());
                listView = view.findViewById(R.id.listPrepareOrder2);
                listView.setAdapter(adapter);
            }
        }.execute();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}
