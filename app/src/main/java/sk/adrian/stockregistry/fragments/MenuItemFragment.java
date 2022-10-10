package sk.adrian.stockregistry.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import sk.adrian.stockregistry.R;

public class MenuItemFragment extends Fragment {
    private View view;
    private Context context;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.toolbar_items);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.menu_item_fragment, container, false);

        attachHandlers(view);

        return view;
    }

    private void attachHandlers(View view) {

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
