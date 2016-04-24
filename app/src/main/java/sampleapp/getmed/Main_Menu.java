package sampleapp.getmed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Main_Menu extends AppCompatActivity {

    private DrawerLayout mDrawerLayout; //declaracion de Layout para contener el menu
    private ActionBarDrawerToggle drawerToggle; //accion q permite esconder el menu
    private Toolbar toolbar;//declaracion de toolbar barra superior


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inicio");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);////instanciar toolbar
        if (toolbar != null) {//ver si no es nula
            setSupportActionBar(toolbar);//utilizar toolbar como barra principal
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);//asignar icono de menu
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//instanciar layout del menu
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);//dar valores para utilizar menu
        mDrawerLayout.setDrawerListener(drawerToggle);//asignar el menu para poder esconderlo

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//ver que el menu no sea nulo
        if (navigationView != null) {
            setupDrawerContent(navigationView);//metodo para crear el menu

        }
    }

    public void setupDrawerContent(NavigationView navigationView){//metodo de botones de menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_sub_item_1:
                        Toast.makeText(getApplicationContext(), "Menu 1", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_sub_item_2:
                        SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getApplicationContext(), "sesi\u00F3n cerrada", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        return true;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        // do nothing.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
           //Do Shit
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }




}

