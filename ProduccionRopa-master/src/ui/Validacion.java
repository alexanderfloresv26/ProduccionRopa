package ui;

import ProduccionRopa.Lote;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Validacion {

    private static Scanner input;

    public Validacion(Scanner input) {
        this.input = input;
    }

    public static long leerLong(String texto, long min, long max, String error){
        long enteroLargo;
        if(min>=max)
            throw new IllegalArgumentException("EL valor mínimo debe ser menor al máximo");
        do{
            System.out.print(texto);
            try {
                enteroLargo = Long.parseLong(input.nextLine());
            }catch(NumberFormatException e){
                enteroLargo = min-1;
            }
            if(enteroLargo<min || enteroLargo>max)
                System.err.println(error);
        }while(enteroLargo<min || enteroLargo>max);
        return enteroLargo;
    }

    public byte leerByte(String texto, byte min, byte max, String error){
        return (byte)leerLong(texto,min,max,error);
    }

    public int leerInt(String texto, int min, int max, String error){
        return (int)leerLong(texto,min,max,error);
    }

    public double leerDouble(String texto,double min,double max,String error){
        double doble;
        if(min>=max)
            throw new IllegalArgumentException("EL valor mínimo debe ser menor al máximo");
        do{
            System.out.print(texto);
            try {
                doble = Double.parseDouble(input.nextLine());
            }catch(NumberFormatException e){
                doble = min-1;
            }
            if(doble<min || doble>max)
                System.err.println(error);
        }while(doble<min || doble>max);
        return doble;
    }

    public String leerString(String texto, List<String> validacion, String error){
        String resultado;
        do{
            System.out.print(texto);
            resultado=input.nextLine();
            if(validacion!=null)
                resultado = validacion.contains(resultado)?resultado:null;
            if(resultado==null)
                System.err.println(error);
        }while(resultado==null || resultado.isEmpty());
        return resultado;
    }

    public LocalDate leerLocalDate(String texto, List<LocalDate> validacion, String error){
        LocalDate resultado = null;
        do{
            try{
                System.out.print(texto);
                resultado = LocalDate.parse(input.nextLine());
                if(validacion != null)
                    resultado = validacion.contains(resultado) ? resultado : null;
                if(resultado == null)
                    System.err.println(error);
                else if(resultado.isAfter(LocalDate.now())){
                    resultado = null;
                    System.err.println("La fecha de fabricación no puede ser futura");
                }
            }catch(DateTimeParseException e){
                resultado = null;
                System.err.println(error);
            }
        }while(resultado == null);
        return resultado;
    }

}
