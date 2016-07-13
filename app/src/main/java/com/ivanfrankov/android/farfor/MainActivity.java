package com.ivanfrankov.android.farfor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_ERROR = 0;
    private static final String KEY_COUNT = "com.ivanfrankov.android.farfor.count";
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        if (savedInstanceState != null) count = savedInstanceState.getInt(KEY_COUNT);
        if (count == 0) new FetchItemsTask().execute();
        else {
            ProgressBar progressBar = (ProgressBar )findViewById(R.id.progressBar);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int errorCode = GooglePlayServicesUtil.
                isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil
                    .getErrorDialog(errorCode, this, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    finish();
                                }
                            });
            errorDialog.show();
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_COUNT, count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ProgressBar progressBar = (ProgressBar )findViewById(R.id.progressBar);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            new FetchItemsTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment oldFragment = fm.findFragmentById(R.id.fragmentContainer);
        Fragment oldFragmentAdd = fm.findFragmentById(R.id.fragmentContainerAdd);
        Fragment newFragment;
        if(oldFragment != null)
            ft.remove(oldFragment);
        if(oldFragmentAdd != null) ft.remove(oldFragmentAdd);

        if (id == R.id.nav_catalogue) {
            newFragment = new CategoriesListFragment();
            ft.add(R.id.fragmentContainer, newFragment);
        } else if (id == R.id.nav_contact) {
            newFragment = new MapFragment();
            ft.add(R.id.fragmentContainer, newFragment);
            newFragment = new AdditionFragment();
            ft.add(R.id.fragmentContainerAdd, newFragment);

            //mapFragment = SupportMapFragment.newInstance();
            //ft.add(R.id.fragmentContainer, mapFragment);
        }
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<Dish>> {
        @Override
        protected ArrayList<Dish> doInBackground(Void... params) {
            return new FlickFetch().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Dish> result) {
            if (result.size() == 0) {
                Toast.makeText(MainActivity.this, "Проверьте интернет подключение", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Каталог загружан", Toast.LENGTH_SHORT).show();
            }
            ProgressBar progressBar = (ProgressBar )findViewById(R.id.progressBar);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
            if(fragment == null) {
                fragment = new CategoriesListFragment();
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
            }
            DishLab.get().setDishList(result);
            count = result.size();
        }
    }
}
