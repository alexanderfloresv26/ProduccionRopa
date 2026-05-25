package ui;

import Excepciones.*;
import ProduccionRopa.Lote;
import ProduccionRopa.Prenda;
import persistencia.ArchivoRandomPrenda;

import java.time.LocalDate;

public class ValidacionLote {

    private Validacion validacion;
    private ArchivoRandomPrenda archivoPrenda;

    public ValidacionLote(Validacion validacion, ArchivoRandomPrenda archivoPrenda) {
        this.validacion = validacion;
        this.archivoPrenda = archivoPrenda;
    }

    public int leerNumeroLote(){
        return validacion.leerInt("Proporciona el numero de lote:",1,10000,"Numero de lote no valido!!");
    }

    public int leerNumeroPiezas(){
        return validacion.leerInt("Proporciona el total de piezas:",1,350,"Numero de piezas no valido!!");
    }

    public Prenda leerPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String modelo = validacion.leerString("Proporciona el modelo de la prenda:", null, "Modelo invalido");
        return archivoPrenda.obtenerPrenda(modelo);
    }

    public LocalDate leerFechaFabricacion(){
        return validacion.leerLocalDate("Ingresa la fecha de fabricacion (YYYY-MM-DD):", null, "Fecha de fabricacion invalida");
    }

    public Lote leerLote() throws ExcepcionCantidadDePrendasFueraDeLimites, ExcepcionDeTemporadaNoValida,
            ExcepcionDeGeneroNoValido, ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        int numero=leerNumeroLote();
        int piezas=leerNumeroPiezas();
        LocalDate fechaFabricacion=leerFechaFabricacion();
        Prenda prenda=leerPrenda();
        if(prenda==null){
            throw new IllegalArgumentException("La prenda no existe!!");
        }
        return new Lote(numero,piezas,fechaFabricacion,prenda);
    }
}