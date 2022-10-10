package sk.adrian.stockregistry;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import sk.adrian.stockregistry.fragments.InsertCustomerFragment;
import sk.adrian.stockregistry.fragments.MainViewFragment;
import sk.adrian.stockregistry.fragments.MenuItemFragment;
import sk.adrian.stockregistry.fragments.MenuOrdersFragment;
import sk.adrian.stockregistry.fragments.StockItemListFragment;

import static sk.adrian.stockregistry.R.drawable.ic_arrow_back;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private ActionBar mActionBar;
    NavigationView navigationView;

    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle(R.string.toolbar_home);
        super.onCreate(savedInstanceState);
        // set view - fragment holder
        setContentView(R.layout.main_fragment_holder);
        mDrawer = findViewById(R.id.drawer_layout);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        // let set first fragment
        if (savedInstanceState == null) {
            // Let's first dynamically add a fragment into a frame container
            // get fragment manager
            FragmentManager fm = getFragmentManager();
            // add
            FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(0, 0);
//                    .setCustomAnimations(
//                            R.animator.card_flip_right_in,
//                            R.animator.card_flip_right_out,
//                            R.animator.card_flip_left_in,
//                            R.animator.card_flip_left_out);
            // ft.add(R.id.registration_fragment_holder, new RegistrationCodeFragment());
            ft.add(R.id.fragment_holder, new MainViewFragment());
//            ft.add(R.id.fragment_holder, new StockItemListFragment());
            ft.commitAllowingStateLoss();
        }


    }


    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);

        } else {
            int backStackCount = getFragmentManager().getBackStackEntryCount();
            if (backStackCount >= 1) {
                getFragmentManager().popBackStack();
                // Change to hamburger icon if at bottom of stack
                if(getFragmentManager().getBackStackEntryCount() == 1){
                    showUpButton(false);
                }
            } else {
                super.onBackPressed();
            }
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // Navigation drawer item selection logic goes here
        switch (id){
                            case R.id.items :
                                loadFragment(new MenuItemFragment());

                                break;

                            case R.id.orders:
                                loadFragment(new MenuOrdersFragment());

                                break;

                            case R.id.add_edit_customer:
                                loadFragment(new InsertCustomerFragment());

                                break;

                        }


        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            showUpButton(true);
        }


    }

    private void showUpButton(boolean show) {
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if(show) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // Remove hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            // Show back button
            mActionBar.setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if(!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
            mActionBar.setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            mDrawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;

            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            this.setTitle(R.string.toolbar_home);
        }
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


