package com.przybylskik.stachn.notowaniaakcjifirm;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.przybylskik.stachn.notowaniaakcjifirm.fragments.CompareFragment;
import com.przybylskik.stachn.notowaniaakcjifirm.fragments.FavouriteFragment;
import com.przybylskik.stachn.notowaniaakcjifirm.fragments.HistoricalFragment;
import com.przybylskik.stachn.notowaniaakcjifirm.fragments.StocksFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    //------ tworzenie obiektĂłw fragmentĂłw aplikacji
    CompareFragment compareFragment;
    FavouriteFragment favouriteFragment;
    HistoricalFragment historicalFragment;
    StocksFragment stocksFragment;
    //------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //------ Inicjalizacja fragmentĂłw
        compareFragment = new CompareFragment();
        favouriteFragment = new FavouriteFragment();
        historicalFragment = new HistoricalFragment();
        stocksFragment = new StocksFragment();
        //------
        FragmentTransaction ftrans = getFragmentManager().beginTransaction();//Inicjalizacja obiektu dla zmiany fragmentu
        ftrans.replace(R.id.container, stocksFragment);//Aktywacja fragmentu "Akcje"
        ftrans.commit();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Kod, ktĂłry wykonuje siÄ™ kiedy uĹĽytkownik otwarza inny rozdziaĹ‚ menu aplikacji
        //ftrans.replace() zmienia fragment AktywnoĹ›ci na ten, ktĂłry wĹ‚Ä…czyĹ‚ uĹĽytkownik
        FragmentTransaction ftrans = getFragmentManager().beginTransaction();//Inicjalizacja obiektu dla zmiany fragmentu
        if (id == R.id.nav_stocks)
        {
            ftrans.replace(R.id.container, stocksFragment);//Aktywacja fragmentu "Akcje"
        }
        else
        if (id == R.id.nav_favourite)
        {
            ftrans.replace(R.id.container, favouriteFragment);//Aktywacja fragmentu "Ulubione firmy"
        }
        else
        if (id == R.id.nav_historical)
        {
            ftrans.replace(R.id.container, historicalFragment);//Aktywacja fragmentu "Dane historyczne"
        }
        else
        if (id == R.id.nav_compare)
        {
            ftrans.replace(R.id.container, compareFragment);//Aktywacja fragmentu "PorĂłwnania akcje"
        }
        else
        if (id == R.id.nav_contact)
        {
            //Contact activity
            //Toast.makeText(MainActivity.this, "Tu będzie napisanie email", Toast.LENGTH_SHORT).show();
            Intent mailto = new Intent(Intent.ACTION_SEND);
            mailto.setType("*/*");
            mailto.putExtra(Intent.EXTRA_EMAIL, "email@email.com");
            mailto.putExtra(Intent.EXTRA_SUBJECT, "Mail z aplikacji mobilnej");
            mailto.putExtra(Intent.EXTRA_TEXT, "Zgłaszam następujące uwagi...");
            startActivity(mailto);
        }
        else
        if (id == R.id.nav_about)
        {
            Intent intent = new Intent(MainActivity.this, autorzy.class);
            startActivity(intent);
        }
        ftrans.commit();//Zapisywanie zmian i aktualizacja aktywnoĹ›ci

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
