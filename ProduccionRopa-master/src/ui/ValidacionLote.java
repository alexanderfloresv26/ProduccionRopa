package ui;

import Excepciones.ExcepcionCantidadDePrendasFueraDeLimites;
import ProduccionRopa.Lote;
import ProduccionRopa.Prenda;

import java.time.LocalDate;
import java.util.ArrayList;

public class ValidacionLote {

    private Validacion validacion;
    private ArrayList<Prenda> prendas;

    public ValidacionLote(Validacion validacion, ArrayList<Prenda> prendas) {
        this.validacion = validacion;
        this.prendas = prendas;
    }

    public int leerNumeroLote(){
        return validacion.leerInt("Proporciona el Lote:",1,1000,"El lote no es valido!!");
    }

    public int leerNumeroPiezas(){
        return validacion.leerInt("Proporciona el total de piezas:",1,1000,"El lote no es valido!!");
    }

    public Prenda leerPrenda(){
        String modelo = validacion.leerString("Proporciona el modelo de la prenda:", null, "Modelo invalido");
        for(Prenda p : prendas){
            if(p.getModelo().equalsIgnoreCase(modelo))
                return p;
        }
        return null;
    }

    public LocalDate leerFechaFabricacion(){
        return validacion.leerLocalDate("Ingresa la fecha de fabricacion:", null, "Fecha de fabricacion invalida");
    }

    public Lote leerLote() throws ExcepcionCantidadDePrendasFueraDeLimites {
        int numero=leerNumeroLote();
        int piezas=leerNumeroPiezas();
        LocalDate fechaFabricacion=leerFechaFabricacion();
        Prenda prenda=leerPrenda();
        return new Lote(numero,piezas,fechaFabricacion,prenda);
    }

}
