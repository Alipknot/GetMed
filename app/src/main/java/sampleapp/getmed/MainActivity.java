package sampleapp.getmed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btLogin, btRegistro; //declaracion botones
    EditText etUser, etPassword;//declaracion Caja de texto


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btLogin = (Button) findViewById(R.id.BtGps);//instanciar boton
        btRegistro = (Button) findViewById(R.id.btRegistro);//instanciar boton
        etUser = (EditText) findViewById(R.id.etUser);//instanciar caja de texto
        etPassword = (EditText) findViewById(R.id.etPassword);//instanciar caja de texto
        final CheckBox chkSesion = (CheckBox) findViewById(R.id.cbNocerrar);//instanciar checkbox

        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enviar a registro",
                        Toast.LENGTH_SHORT).show();
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUser.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                Boolean ses = chkSesion.isChecked();
                new Asyncs().execute(usuario, password, ses.toString());

            }
        });

        consultaSesion();

    }

    public void consultaSesion() {//metodo ve si la sesion esta iniciada

        SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_PRIVATE);

        if (preferences.contains("sesion")) {
            startActivity(new Intent(this, Main_Menu.class));
        }
    }

    private class Asyncs extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0],
                    password = params[1],
                    check = params[2];
            Boolean ses;
            ses = Boolean.parseBoolean(check);
            if (usuario.equals("") || password.equals("")) {
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(getApplicationContext(), "Ingrese sus datos",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                try {
                    HttpHandler sh = new HttpHandler(getApplicationContext());
                    String login = sh.get("http://crevent.cu.cc/CreApp/Login.php?user=" + usuario + "&password=" + password);


                    switch (login) {
                        case "Usuario encontrado":
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Usuario encontrado",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                            SharedPreferences preferences = getSharedPreferences("Preferencia_usuario", Context.MODE_PRIVATE);//guardar datos de user en el celular
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("usuario", usuario);



                            if (ses) {
                                editor.putBoolean("sesion", true);
                            }
                            editor.apply();
                            startActivity(new Intent(MainActivity.this, Main_Menu.class));//ir a perfil luego del login
                            break;

                        case "Usuario no encontrado":
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Usuario no encontrado",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                            break;
                        case "ERROR":
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getApplicationContext(), "No se pudo conectar al servidor",
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
