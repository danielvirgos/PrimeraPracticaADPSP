package com.example.primerapracticaadpsp;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class IncomingCallsReceiver extends BroadcastReceiver {

    private Context contexto;
    private static String numeroTelefono;
    private String nombreLlamada;
    private Llamadas llamadas;

    @Override
    public void onReceive(Context context, Intent intent) {

        contexto = context;

        String estado = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            numeroTelefono = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        } else if (estado.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

            HebraObtenerGuardarDatos();

        }
    }

    private void HebraObtenerGuardarDatos() {
        Thread hebraBuscaContacto = new Thread() {
            public void run() {
                Calendar fecha = Calendar.getInstance();
                int año = fecha.get(Calendar.YEAR);
                int mes = fecha.get(Calendar.MONTH) + 1;
                int dia = fecha.get(Calendar.DAY_OF_MONTH);
                int hora = fecha.get(Calendar.HOUR_OF_DAY);
                int minuto = fecha.get(Calendar.MINUTE);
                int segundo = fecha.get(Calendar.SECOND);

                nombreLlamada = obtenerNombreContacto(contexto.getContentResolver());

                llamadas = new Llamadas(nombreLlamada, año, mes, dia, hora, minuto, segundo, numeroTelefono);
                guardaLlamada(llamadas);
            }
        };
        hebraBuscaContacto.start();
    }

    private void guardaLlamada(Llamadas llamadas) {
        guardaLlamadaFileDir(llamadas);
        guardaLlamadaExternalFilesDir(llamadas);
    }

    private void guardaLlamadaExternalFilesDir(Llamadas llamadas) {

        ArrayList<Llamadas> listaLlamadas=getListLlamadaExternal();
        listaLlamadas.add(llamadas);
        Collections.sort(listaLlamadas,new LlamadaComparar());
        File f = new File(contexto.getExternalFilesDir(null),"llamadas.csv");
        FileWriter fw= null;
        try {
            fw = new FileWriter(f);
            for(Llamadas llamadaNueva :listaLlamadas){
                fw.write(llamadaNueva.toCsv2()+"\n");

            }
            fw.flush();
            fw.close();

        } catch (IOException e) {

        }

    }

    private void guardaLlamadaFileDir(Llamadas llamadas) {
        ArrayList<Llamadas> listaLlamadas=getListLlamadasFileDir();
        listaLlamadas.add(llamadas);
        File f = new File(contexto.getFilesDir(),"historial.csv");
        FileWriter fw= null;
        try {
            fw = new FileWriter(f);
            for(Llamadas llamadaNueva :listaLlamadas){
                fw.write(llamadaNueva.toCsv()+"\n");

            }

            fw.flush();
            fw.close();

        } catch (IOException e) {

        }

    }

    private ArrayList<Llamadas> getListLlamadasFileDir() {
        ArrayList<Llamadas> listaLlamadas = new ArrayList<>();
        File f = new File(contexto.getFilesDir(), "historial.csv");
        BufferedReader br= null;
        try {
            br = new BufferedReader(new FileReader(f));
            String linea;

            while((linea = br.readLine()) != null) {
                Log.v("zzzz", linea);
                listaLlamadas.add(Llamadas.fromStringToLlamada(";",linea));

            }

            br.close();

        } catch (IOException e) {

        }

        return listaLlamadas;
    }

    private ArrayList<Llamadas> getListLlamadaExternal() {
        ArrayList<Llamadas> listaLlamadas = new ArrayList<>();
        File f = new File(contexto.getExternalFilesDir(null), "llamadas.csv");
        BufferedReader br= null;
        try {
            br = new BufferedReader(new FileReader(f));
            String linea;

            while((linea = br.readLine()) != null) {
                Log.v("zzzz", linea);
                listaLlamadas.add(Llamadas.fromStringToLlamada2(";",linea));

            }

            br.close();

        } catch (IOException e) {

        }

        return listaLlamadas;
    }

    private String obtenerNombreContacto(ContentResolver contentResolver) {
        String nombre="Desconocido";
        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor cur2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);

                    while (cur2.moveToNext()) {
                        String phoneNumber = cur2.getString(cur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String numero ="";

                        for(int i =0;i<phoneNumber.length();i++){

                            if(Character.isDigit(phoneNumber.charAt(i))){
                                numero = numero + phoneNumber.charAt(i);
                            }
                        } if(numero.equals(numeroTelefono)){
                            nombre=name;
                        }
                    }
                    cur2.close();
                }
            }

        }

        return nombre;
    }
}
