package ui;

import Excepciones.*;
import ProduccionRopa.*;
import persistencia.ArchivoRandomPrenda;

import java.util.ArrayList;
import java.util.Scanner;

public class Principal {
    private Scanner input = new Scanner(System.in);
    private Validacion validacion;
    private ValidacionLote validacionlote;
    private ValidacionPrenda validacionPrenda;
    private String menu;
    private Visualiazacion visualizacion;
    private ArrayList<Prenda> prendaz;
    ArchivoRandomPrenda archivoPrenda;

    public Principal(){
        validacion = new Validacion(input);
        validacionlote= new ValidacionLote(validacion, prendaz);
        validacionPrenda = new ValidacionPrenda(validacion);
        visualizacion = new Visualiazacion();
        inicializaMenu();
    }

    private byte opcion(){
        return validacion.leerByte(menu,(byte)1,(byte)3,"Opcion invalida");
    }


    private void agregarLote() throws ExcepcionCantidadDePrendasFueraDeLimites{
        Lote lote= validacionlote.leerLote();
        try {
            lotes.addLote(lote);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    private void eliminarAlumno(){
        long nc=validacion.leerNumeroControl();
        Alumno alumno= archivoPrenda.obtenerAlumno(nc);
        if(alumno!=null) {
            visualizacion.visualizaAlumno(alumno);
            String resp = validacion.leerString("Es el alumno a eliminar:[Si/No]:", Arrays.asList("Si", "No"), "Opcion incorrecta!!");
            if (resp.equals("Si")) {
                archivoPrenda.eliminarAlumno(alumno);
                System.out.println("Alumno eliminado!!");
            }
        }
        else
            System.out.println("Alumno no encontrado!!");
    }

    private void agregarPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        Prenda prenda= validacionPrenda.leerPrenda();
        try {
            archivoPrenda.addPrenda(prenda);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }

    private void inicializaMenu(){
        menu="Menu pricipal\n";
        menu+="1.- Prendas\n";
        menu+="2.- Lotes\n";
        menu+="3.- Salir\n";
        menu+="Proporciona opción:[1..3]:";
    }

    private void capturaModificacionesLotes(){
        String menu="Menu de opciones\n";
        menu+="1.- Modificar semestre\n";
        menu+="2.- Modificar carrera\n";
        menu+="3.- Modificar promedio\n";
        menu+="Selecciona opcion[1..3]:\n";
        byte  opcion=validacion.leerByte(menu,(byte)1,(byte)3,"Opcion invalida");
        switch(opcion){
            case 1:modificaSemestre(alumno);break;
            case 2:modificaCarrera(alumno);break;
            case 3:modificaPromedio(alumno);
        }
    }

    private void capturaModificacionesPrenda(){
        String menu="Menu de opciones\n";
        menu+="1.- Modificar semestre\n";
        menu+="2.- Modificar carrera\n";
        menu+="3.- Modificar promedio\n";
        menu+="Selecciona opcion[1..3]:\n";
        byte  opcion=validacion.leerByte(menu,(byte)1,(byte)3,"Opcion invalida");
        switch(opcion){
            case 1:modificaSemestre(alumno);break;
            case 2:modificaCarrera(alumno);break;
            case 3:modificaPromedio(alumno);
        }
    }

    private void modificarLote(){
        int numero = validacionlote.leerNumeroLote();
        Lote lote = lotes.getLote(numero);
        if(lote!=null){
            visualizacion.visualizaLote(lote);
            capturaModificacionesLotes();
        }else
            System.err.println("El lote no existe!!");
    }

    private void modificarPrenda(){
        String modelo=validacionPrenda.leerModelo();
        Prenda prenda = prendas.getPrenda(modelo);
        if(prenda!=null){
            visualizacion.visualizaPrenda(prenda);
            capturaModificacionesPrenda();
        }else
            System.err.println("La prenda no existe!!");
    }


    public void run() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido, ExcepcionCantidadDePrendasFueraDeLimites {
        byte opcion;
        do{
            opcion=opcion();
            switch(opcion){
                case 1: runR();break;
                case 2: runL();break;
            }
        }while(opcion!=3);
    }

    public void runR() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        byte opcion;
        menu="Menu de opciones para: Prendas\n";
        menu+="1.- Agregar prenda\n";
        menu+="2.- Eliminar prenda\n";
        menu+="3.- Modificar prenda\n";
        menu+="4.- Mostrar prenda\n";
        menu+="5.- Listar prenda\n";
        menu+="6.- Salir\n";
        menu+="Proporciona opción:[1..6]:";
        do{
            opcion = validacion.leerByte(menu,(byte)1,(byte)6,"Opcion invalida");
            switch(opcion){
                case 1: agregarPrenda();break;
                case 2: eliminarPrenda();break;
                case 3: modificarPrenda();break;
                case 4: mostrarPrenda();break;
                case 5: listarPrenda();
            }
        }while(opcion!=6);
    }

    public void runL() throws ExcepcionCantidadDePrendasFueraDeLimites {
        byte opcion;
        menu="Menu de opciones para: Lotes\n";
        menu+="1.- Agregar lote\n";
        menu+="2.- Eliminar lote\n";
        menu+="3.- Modificar lote\n";
        menu+="4.- Mostrar lote\n";
        menu+="5.- Listar lote\n";
        menu+="6.- Salir\n";
        menu+="Proporciona opción:[1..6]:";
        do{
            opcion = validacion.leerByte(menu,(byte)1,(byte)6,"Opcion invalida");
            switch(opcion){
                case 1: agregarLote();break;
                case 2: eliminarLote();break;
                case 3: modificarLote();break;
                case 4: mostrarLote();break;
                case 5: listarLote();
            }
        }while(opcion!=6);
    }
}
