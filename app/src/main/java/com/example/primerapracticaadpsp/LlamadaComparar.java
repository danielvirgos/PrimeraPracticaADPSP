package com.example.primerapracticaadpsp;

import java.util.Comparator;

public class LlamadaComparar implements Comparator<Llamadas> {
    @Override
    public int compare(Llamadas o1, Llamadas o2) {
        int aux=0;
        aux = o1.getNombreContacto().compareTo(o2.getNombreContacto());
        if(aux==0){
            aux = o1.getYear()-o2.getYear();
            if(aux==0){
                aux= o1.getHora()-o2.getHora();

            }
        }
        return aux;
    }
}
