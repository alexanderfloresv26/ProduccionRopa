package ui;

import Excepciones.*;
import ProduccionRopa.Prenda;
import java.util.ArrayList;

public class ValidacionPrenda {

    private Validacion validacion;

    public ValidacionPrenda(Validacion validacion){
        this.validacion = validacion;
    }

    public String leerModelo(){
        return validacion.leerString("Proporciona el modelo:", null, "El modelo no es válido!!");
    }

    public String leerTela(){
        return validacion.leerString("Proporciona la Tela:", null, "La tela no es válida!!");
    }

    public double leerCostoMaximo(){
        return validacion.leerDouble("Proporciona el costo máximo de fabricación de la prenda:", 0, 1000000, "Costo inválido!!");
    }

    public double leerCostoProduccion(){
        return validacion.leerDouble("Proporciona el costo de producción de la prenda:", 0, 100000000, "Costo inválido!!");
    }

    public String leerTemporada(){
        ArrayList<String> temporadas = new ArrayList<>();
        for (Temporada t : Temporada.values()) {
            temporadas.add(t.name());
        }
        return validacion.leerString("Proporciona la temporada:", temporadas, "La temporada no es válida!!");
    }

    public String leerGenero(){
        ArrayList<String> generos = new ArrayList<>();
        for (Genero g : Genero.values()) {
            generos.add(g.name());
        }
        return validacion.leerString("Proporciona el género:", generos, "Género inválido!!");
    }

    public Prenda leerPrenda() throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String genero = leerGenero();
        String modelo = leerModelo();
        String tela = leerTela();
        double costoMaximo = leerCostoMaximo();
        double costoProduccion = leerCostoProduccion();
        String temporada = leerTemporada();
        return new Prenda(genero, modelo, tela, costoMaximo, costoProduccion, temporada);
    }
}