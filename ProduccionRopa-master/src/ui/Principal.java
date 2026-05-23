package ui;

import Excepciones.*;
import ProduccionRopa.*;
import persistencia.ArchivoRandomLote;
import persistencia.ArchivoRandomPrenda;

import java.util.Arrays;
import java.util.Scanner;

public class Principal {
    private final Scanner input = new Scanner(System.in);
    private final Validacion validacion;
    private final ValidacionLote validacionlote;
    private final ValidacionPrenda validacionPrenda;
    private String menu;
    private final Visualiazacion visualizacion;
    private ArchivoRandomPrenda archivoPrenda;
    private ArchivoRandomLote archivoLote;

    public Principal() {
        validacion = new Validacion(input);
        archivoPrenda = new ArchivoRandomPrenda("prendas.dat");
        archivoLote = new ArchivoRandomLote("lotes.dat");
        validacionlote = new ValidacionLote(validacion, archivoPrenda);
        validacionPrenda = new ValidacionPrenda(validacion);
        visualizacion = new Visualiazacion();
        inicializaMenu();
    }

    private byte opcion() {
        return validacion.leerByte(menu, (byte) 1, (byte) 8, "Opcion invalida");
    }

    private void agregarLote() throws ExcepcionCantidadDePrendasFueraDeLimites, ExcepcionDeTemporadaNoValida,
            ExcepcionDeGeneroNoValido, ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        Lote lote = validacionlote.leerLote();
        try {
            archivoLote.agregaLote(lote);
            System.out.println("Lote agregado exitosamente");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void eliminarPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String modelo = validacionPrenda.leerModelo();
        Prenda prenda = archivoPrenda.obtenerPrenda(modelo);
        if (prenda != null) {
            visualizacion.visualizaPrenda(prenda);
            String resp = validacion.leerString("Es la prenda a eliminar:[Si/No]:", Arrays.asList("Si", "No"), "Opcion incorrecta!!");
            if (resp.equals("Si")) {
                archivoLote.eliminarLotesPorPrenda(modelo);
                archivoPrenda.eliminarPrenda(prenda);
                System.out.println("Prenda y sus lotes eliminados!!");
            }
        } else {
            System.out.println("Prenda no encontrada!!");
        }
    }

    private void agregarPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        Prenda prenda = validacionPrenda.leerPrenda();
        try {
            archivoPrenda.agregaPrenda(prenda);
            System.out.println("Prenda agregada exitosamente");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void eliminarLote() {
        int num = validacionlote.leerNumeroLote();
        Lote lote = archivoLote.obtenerLote(num);
        if (lote != null) {
            visualizacion.visualizaLote(lote);
            String resp = validacion.leerString("Es el lote a eliminar:[Si/No]:", Arrays.asList("Si", "No"), "Opcion incorrecta!!");
            if (resp.equals("Si")) {
                archivoLote.eliminarLote(lote);
                System.out.println("Lote eliminado!!");
            }
        } else {
            System.out.println("Lote no encontrado!!");
        }
    }

    private void inicializaMenu() {
        menu = "Menu principal\n";
        menu += "1.- Prendas\n";
        menu += "2.- Lotes\n";
        menu += "3.- Recuperar registros eliminados\n";
        menu += "4.- Salir\n";
        menu += "Proporciona opción:[1..4]:";
    }

    private void capturaModificacionesLote(Lote lote) {
        String menu = "Menu de opciones\n";
        menu += "1.- Modificar numero de piezas\n";
        menu += "2.- Modificar fecha de fabricacion\n";
        menu += "Selecciona opcion[1..2]:\n";
        byte opcion = validacion.leerByte(menu, (byte) 1, (byte) 2, "Opcion invalida");
        switch (opcion) {
            case 1:
                int nuevasPiezas = validacionlote.leerNumeroPiezas();
                try {
                    lote.setNumPiezas(nuevasPiezas);
                    archivoLote.modificaLote(lote);
                    System.out.println("Lote modificado exitosamente");
                } catch (ExcepcionCantidadDePrendasFueraDeLimites e) {
                    System.err.println(e.getMessage());
                }
                break;
            case 2:
                java.time.LocalDate nuevaFecha = validacionlote.leerFechaFabricacion();
                lote.setFechaFabricacion(nuevaFecha);
                archivoLote.modificaLote(lote);
                System.out.println("Lote modificado exitosamente");
                break;
        }
    }

    private void capturaModificacionesPrenda(Prenda prenda) {
        String menu = "Menu de opciones\n";
        menu += "1.- Modificar tela\n";
        menu += "2.- Modificar costo de produccion\n";
        menu += "3.- Modificar temporada\n";
        menu += "Selecciona opcion[1..3]:\n";
        byte opcion = validacion.leerByte(menu, (byte) 1, (byte) 3, "Opcion invalida");
        switch (opcion) {
            case 1:
                String nuevaTela = validacionPrenda.leerTela();
                prenda.setTela(nuevaTela);
                archivoPrenda.modificaPrenda(prenda);
                System.out.println("Prenda modificada exitosamente");
                break;
            case 2:
                double nuevoCosto = validacionPrenda.leerCostoProduccion();
                try {
                    prenda.setCostoProduccion(nuevoCosto);
                    archivoPrenda.modificaPrenda(prenda);
                    System.out.println("Prenda modificada exitosamente");
                } catch (ExcepcionDeCostoFueraDeLimites e) {
                    System.err.println(e.getMessage());
                }
                break;
            case 3:
                String nuevaTemporada = validacionPrenda.leerTemporada();
                try {
                    prenda.setTemporada(nuevaTemporada);
                    archivoPrenda.modificaPrenda(prenda);
                    System.out.println("Prenda modificada exitosamente");
                } catch (ExcepcionDeTemporadaNoValida e) {
                    System.err.println(e.getMessage());
                }
                break;
        }
    }

    private void modificarLote() {
        int numero = validacionlote.leerNumeroLote();
        Lote lote = archivoLote.obtenerLote(numero);
        if (lote != null) {
            visualizacion.visualizaLote(lote);
            capturaModificacionesLote(lote);
        } else {
            System.err.println("El lote no existe!!");
        }
    }

    private void modificarPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String modelo = validacionPrenda.leerModelo();
        Prenda prenda = archivoPrenda.obtenerPrenda(modelo);
        if (prenda != null) {
            visualizacion.visualizaPrenda(prenda);
            capturaModificacionesPrenda(prenda);
        } else {
            System.err.println("La prenda no existe!!");
        }
    }

    private void mostrarPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String modelo = validacionPrenda.leerModelo();
        Prenda prenda = archivoPrenda.obtenerPrenda(modelo);
        if (prenda != null) {
            visualizacion.visualizaPrenda(prenda);
        } else {
            System.out.println("Prenda no encontrada!!");
        }
    }

    private void listarPrenda() {
        long numReg = archivoPrenda.getNumeroRegistros();
        for(int i = 1; i <= numReg; i++){
            try {
                Prenda prenda = archivoPrenda.obtenerPrenda(i);
                if(prenda != null){
                    visualizacion.visualizaPrenda(prenda);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void mostrarLote() {
        int numero = validacionlote.leerNumeroLote();
        Lote lote = archivoLote.obtenerLote(numero);
        if (lote != null) {
            visualizacion.visualizaLote(lote);
            System.out.println("Costo de produccion del lote: $" + lote.calcularCostoProduccionLote());
            System.out.println("Monto recuperacion venta individual (15%): $" + lote.calcularMontoRecuperacionVentaIndividual());
            System.out.println("Monto recuperacion venta por lote (5%): $" + lote.calcularMontoRecuperacionVentaPorLote());
        } else {
            System.out.println("Lote no encontrado!!");
        }
    }

    private void listarLote() {
        long numReg = archivoLote.getNumeroRegistros();
        for(int i = 1; i <= numReg; i++){
            Lote lote = archivoLote.obtenerLote(i);
            if(lote != null){
                visualizacion.visualizaLote(lote);
            }
        }
    }

    private void recuperarEliminados() {
        archivoPrenda.recuperarEliminados();
        archivoLote.recuperarEliminados();
        System.out.println("Registros eliminados recuperados!!");
    }

    public void run() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido, ExcepcionCantidadDePrendasFueraDeLimites {
        byte opcion;
        do {
            opcion = this.opcion();
            switch (opcion) {
                case 1:
                    runR();
                    break;
                case 2:
                    runL();
                    break;
                case 3:
                    recuperarEliminados();
                    break;
            }
        } while (opcion != 4);
    }

    public void runR() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        byte opcion;
        String menuPrendas = "Menu de opciones para: Prendas\n";
        menuPrendas += "1.- Agregar prenda\n";
        menuPrendas += "2.- Eliminar prenda\n";
        menuPrendas += "3.- Modificar prenda\n";
        menuPrendas += "4.- Mostrar prenda\n";
        menuPrendas += "5.- Listar prendas\n";
        menuPrendas += "6.- Salir\n";
        menuPrendas += "Proporciona opción:[1..6]:";
        do {
            opcion = validacion.leerByte(menuPrendas, (byte) 1, (byte) 6, "Opcion invalida");
            switch (opcion) {
                case 1:
                    agregarPrenda();
                    break;
                case 2:
                    eliminarPrenda();
                    break;
                case 3:
                    modificarPrenda();
                    break;
                case 4:
                    mostrarPrenda();
                    break;
                case 5:
                    listarPrenda();
                    break;
            }
        } while (opcion != 6);
    }

    public void runL() throws ExcepcionCantidadDePrendasFueraDeLimites, ExcepcionDeTemporadaNoValida,
            ExcepcionDeGeneroNoValido, ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        byte opcion;
        String menuLotes = "Menu de opciones para: Lotes\n";
        menuLotes += "1.- Agregar lote\n";
        menuLotes += "2.- Eliminar lote\n";
        menuLotes += "3.- Modificar lote\n";
        menuLotes += "4.- Mostrar lote\n";
        menuLotes += "5.- Listar lotes\n";
        menuLotes += "6.- Salir\n";
        menuLotes += "Proporciona opción:[1..6]:";
        do {
            opcion = validacion.leerByte(menuLotes, (byte) 1, (byte) 6, "Opcion invalida");
            switch (opcion) {
                case 1:
                    agregarLote();
                    break;
                case 2:
                    eliminarLote();
                    break;
                case 3:
                    modificarLote();
                    break;
                case 4:
                    mostrarLote();
                    break;
                case 5:
                    listarLote();
                    break;
            }
        } while (opcion != 6);
    }
}