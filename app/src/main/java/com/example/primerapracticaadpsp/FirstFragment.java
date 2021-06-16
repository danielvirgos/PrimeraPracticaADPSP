package com.example.primerapracticaadpsp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FirstFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String TAG = "xzy";
    public static boolean permiso = false;
    private final int PERMISOSAPP = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private char modo;
    private TextView tvLlamada;
    private TextView tvTitulo;
    private NavController navController;
    private Context context;
    private Repository repository;
    //------------------------------------------------------

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar;
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        init();
    }

    private void init() {
        repository = new Repository(getView());
        Button bthisorial = getView().findViewById(R.id.btHistorial);
        listener = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        navController = Navigation.findNavController(getView());

        bthisorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perdirPermiso();
                if(permiso) {
                    Log.v("zzzz", "llama al repositorio");
                    repository.LeerLlamadas(getContext(), sharedPreferences);
                }
            }
        });
    }

    private void perdirPermiso() {
        int permisoPhoneState = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
        int permisoReadCallLog = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG);
        int permisoReadContacts = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (permisoPhoneState == PackageManager.PERMISSION_GRANTED &&
                        permisoReadCallLog == PackageManager.PERMISSION_GRANTED &&
                        permisoReadContacts == PackageManager.PERMISSION_GRANTED)) {
            permiso = true;

        } else {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                mostrarInfromacionDetallada();

            } else {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, PERMISOSAPP);
            }

        }
    }

    private void mostrarInfromacionDetallada() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.titulo_permiso);
        builder.setMessage(R.string.mensaje_permiso);

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, PERMISOSAPP);
            }
        });
        builder.setNegativeButton(R.string.cancelar, null);
        builder.show();
    }

    /*private boolean abrirAjustes (){
        navController.navigate(R.id.settingsFragment);
        return true;
    }*/

    //------------------------

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return abrirAjustes();
        }

        return true;
    }*/

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        repository.obtenerPreferenciasActuales(sharedPreferences);
        repository.LeerLlamadas(getContext(), sharedPreferences);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISOSAPP:
                int contador = 0;

                for (int i = 0; i < grantResults.length; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        contador++;
                    }
                }

                if (contador == grantResults.length) {
                    permiso = true;

                } else {

                }
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        repository.obtenerPreferenciasActuales(sharedPreferences);
    }

    @Override
    public void onStart() {
        super.onStart();
        repository.obtenerPreferenciasActuales(sharedPreferences);
    }
}