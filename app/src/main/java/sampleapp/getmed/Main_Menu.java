package sampleapp.getmed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

            showEditDialog();
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

    private void showEditDialog() {
        LayoutInflater li = LayoutInflater.from(Main_Menu.this);
        View promptsView = li.inflate(R.layout.dialoglay, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Main_Menu.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_PRIVATE);
                                String pref = preferences.getString("usuario","");

                                new Asyncs().execute(pref,userInput.getText().toString(),null);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

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


    private class Asyncs extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
              String   motivo = params[1];

            if (usuario.equals("") ||motivo.equals("")) {
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(getApplicationContext(), "Ingrese los datos",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                try {
                    HttpHandler sh = new HttpHandler(getApplicationContext());
                    String resu = sh.get("http://192.168.2.3/getmed/Agendar.php?user=" + usuario + "&motivo=" + motivo);


                    switch (resu) {
                        case "Todo ok":
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Su consulta se ha agendado con exito",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });


                            //startActivity(new Intent(MainActivity.this, Main_Menu.class));//ir a perfil luego del login
                            break;

                        case "No consulta":
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "No quedan horas disponibles",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                            break;
                        case "ERROR":
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "No se pudo conectar al servidor o no quedan horas",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                            break;
                        default:
                            //

                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
            return null;
        }



    }
}



