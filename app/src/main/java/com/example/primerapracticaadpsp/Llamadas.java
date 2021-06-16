package com.example.primerapracticaadpsp;

import java.util.Objects;

public class Llamadas implements Comparable<Llamadas> {

    private String nombreContacto;
    private int year;
    private int mes;
    private int dia;
    private int hora;
    private int min;
    private int seg;
    private String numTelefono;

    public Llamadas(String nombreContacto, int year, int mes, int dia, int hora, int min, int seg, String numTelefono) {
        this.nombreContacto = nombreContacto;
        this.year = year;
        this.mes = mes;
        this.dia = dia;
        this.hora = hora;
        this.min = min;
        this.seg = seg;
        this.numTelefono = numTelefono;
    }

    public Llamadas( int year, int mes, int dia, int hora, int minutos, int segundos, String numeroTelefono,String nombreContacto) {

        this.nombreContacto = nombreContacto;
        this.year = year;
        this.mes = mes;
        this.dia = dia;
        this.hora = hora;
        this.min = minutos;
        this.seg = segundos;
        this.numTelefono = numeroTelefono;
    }

    public static Llamadas fromStringToLlamada(String s, String linea) {
        String [] cadenaArchivo = linea.split(s);

        Llamadas llamadas = null;
        if (cadenaArchivo.length==8) {
            llamadas = new Llamadas(
                    Integer.parseInt(cadenaArchivo[0].trim()),
                    Integer.parseInt(cadenaArchivo[1].trim()),
                    Integer.parseInt(cadenaArchivo[2].trim()),
                    Integer.parseInt(cadenaArchivo[3].trim()),
                    Integer.parseInt(cadenaArchivo[4].trim()),
                    Integer.parseInt(cadenaArchivo[5].trim()),
                    cadenaArchivo[6].trim(),
                    (cadenaArchivo[7].trim()));
        }

        return llamadas;
    }

    public static Llamadas fromStringToLlamada2(String s, String linea) {
        String [] cadenaArchivo = linea.split(s);

        Llamadas llamada = null;
        if(cadenaArchivo.length==8){

            llamada = new Llamadas(
                    cadenaArchivo[0].trim(),
                    Integer.parseInt(cadenaArchivo[1].trim()),
                    Integer.parseInt(cadenaArchivo[2].trim()),
                    Integer.parseInt(cadenaArchivo[3].trim()),
                    Integer.parseInt(cadenaArchivo[4].trim()),
                    Integer.parseInt(cadenaArchivo[5].trim()),
                    Integer.parseInt(cadenaArchivo[6].trim()),
                    (cadenaArchivo[7].trim()));

        }

        return llamada;
    }

    @Override
    public int compareTo(Llamadas o) {
        return 0;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public int getYear() {
        return year;
    }

    public int getMes() {
        return mes;
    }

    public int getDia() {
        return dia;
    }

    public int getHora() {
        return hora;
    }

    public int getMin() {
        return min;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setSeg(int seg) {
        this.seg = seg;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public int getSeg() {
        return seg;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    @Override
    public String toString() {
        return "Llamadas{" +
                "nombreContacto='" + nombreContacto + '\'' +
                ", year=" + year +
                ", mes=" + mes +
                ", dia=" + dia +
                ", hora=" + hora +
                ", min=" + min +
                ", seg=" + seg +
                ", numTelefono='" + numTelefono + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Llamadas)) return false;
        Llamadas llamada = (Llamadas) o;
        return year == llamada.year &&
                mes == llamada.mes &&
                dia == llamada.dia &&
                hora == llamada.hora &&
                min == llamada.min &&
                seg == llamada.seg &&
                Objects.equals(nombreContacto, llamada.nombreContacto) &&
                Objects.equals(numTelefono, llamada.numTelefono);
    }


    public String toCsv() {
        return year + "; " + mes + "; " + dia + "; " + hora + "; " + min + "; " + seg + "; " + numTelefono + "; " + nombreContacto;
    }

    public String toCsv2() {
        return nombreContacto + "; " + year + "; " + mes + "; " + dia + "; " + hora + "; " + min + "; " + seg + "; " + numTelefono;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreContacto, year, mes, dia, hora, min, seg, numTelefono);
    }
}
