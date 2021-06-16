package com.example.primerapracticaadpsp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Repository {

    private TextView tvLlamada;
    private TextView tvTitulo;
    private char modo;
    private View view;

    public Repository (View v) {
        this.view = v;
    }

    public void LeerLlamadas(Context context, SharedPreferences sharedPreferences) {
        identificaView();
        obtenerPreferenciasActuales(sharedPreferences);

        Log.v("zzzz", "identifica el modo del archivo");

        if(modo=='f'){
            tvTitulo.setText(R.string.str_TituloDatosFileDir);
            File f = new File(context.getFilesDir(), "historial.csv");
            BufferedReader br= null;
            try {
                br = new BufferedReader(new FileReader(f));
                String linea;
                StringBuilder texto = new StringBuilder();

                while((linea = br.readLine()) != null) {
                    texto.append(linea);
                    Log.v("zzzz", String.valueOf(texto));
                    texto.append('\n');
                }

                tvLlamada.setText(texto);
                Log.v("zzzz", String.valueOf(texto));

                br.close();

            } catch (IOException e) {
                Log.v("zzzz", "algo salio mal");
                Log.v("zzzzz", e.toString());
            }

        }else if(modo=='x'){
            tvTitulo.setText(R.string.str_TituloDatosExternalFileDir);
            File f = new File(context.getExternalFilesDir(null), "llamadas.csv");
            BufferedReader br= null;

            try {
                br = new BufferedReader(new FileReader(f));
                String linea;
                StringBuilder texto = new StringBuilder();

                while((linea = br.readLine()) != null) {
                    texto.append(linea);
                    texto.append('\n');
                }

                tvLlamada.setText(texto);
                Log.v("zzzz", String.valueOf(texto));

                br.close();

            } catch (IOException e) {
                Log.v("zzzz", "algo salio mal");
            }

        }else if(modo=='e'){

            tvTitulo.setText("");
            tvLlamada.setText(R.string.str_preferencias);
        }

    }

    private void identificaView() {
        Log.v("zzzzz", "pone el id a los TV");
        tvTitulo = view.findViewById(R.id.tvTitulo);
        tvLlamada = view.findViewById(R.id.tvContactos);
    }

    public void obtenerPreferenciasActuales(SharedPreferences sharedPreferences) {
        boolean activado = true;

        if ( (sharedPreferences.getString("tipofichero", "intcsv")).equals("extcsv") ) {
            modo = 'x';
        } else if ( (sharedPreferences.getString("tipofichero", "intcsv")).equals("intcsv") ){
            modo = 'f';
        } else {
            modo = 'e';
        }
    }
}
