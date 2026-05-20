package ProduccionRopa;

import Excepciones.*;

import java.util.Comparator;
import java.util.Objects;

public class Prenda implements Comparable <Prenda>{

    private String genero;
    private String modelo;
    private String tela;
    private double costoMaximo;
    private double costoProduccion;
    private String temporada;


    private static Comparator<Prenda> comparator = new Comparator<Prenda>(){
        public int compare(Prenda o1, Prenda o2){
            return o1.compareTo(o2);
        }
    };

    public Prenda(String genero, String modelo, String tela, double costoMaximo, double costoProduccion, String temporada)
            throws ExcepcionDeCostoMaximoNoValido, ExcepcionDeCostoFueraDeLimites, ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido
    {
        this.genero = genero;
        this.modelo = modelo;
        this.tela = tela;
        this.costoMaximo = costoMaximo;
        this.costoProduccion = costoProduccion;
        this.temporada = temporada;
        validacion();
    }

    public void validacion() throws ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido, ExcepcionDeTemporadaNoValida
            , ExcepcionDeGeneroNoValido{
        if (costoMaximo <= 0)
            throw new ExcepcionDeCostoMaximoNoValido("El costo de producción maximo es invalido!!!");

        if (costoProduccion < 0 || costoProduccion > costoMaximo)
            throw new ExcepcionDeCostoFueraDeLimites("El costo de producción está fuera de los limites permitidos!");

        try {
            Temporada.valueOf(temporada.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ExcepcionDeTemporadaNoValida("La temporada es invalida");
        }

        try {
            Genero.valueOf(genero.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ExcepcionDeGeneroNoValido("El genero es invalido");
        }
    }


    public void setCostoProduccion(double costoProduccion) throws ExcepcionDeCostoFueraDeLimites
    {
        if (0 < costoProduccion && costoProduccion < getCostoMaximo())
            this.costoProduccion = costoProduccion;
        else
            throw new ExcepcionDeCostoFueraDeLimites("El costo de producción está fuera de los limites permitidos!");
    }

    public String getGenero() {return genero;}

    public String getModelo() {return modelo;}

    public String getTela() {return tela;}

    public double getCostoMaximo() {return costoMaximo;}

    public double getCostoProduccion() {return costoProduccion;}

    public String getTemporada() {return temporada;}

    @Override
    public String toString() {
        return "Prenda{" +
                "genero='" + genero + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tela='" + tela + '\'' +
                ", costoMaximo=" + costoMaximo +
                ", costoProduccion=" + costoProduccion +
                ", temporada='" + temporada + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Prenda prenda)) return false;
        return Objects.equals(genero, prenda.genero) && Objects.equals(modelo, prenda.modelo) && Objects.equals(tela, prenda.tela)
                &&Double.compare(costoProduccion, prenda.costoProduccion) == 0 && Double.compare(costoMaximo, prenda.costoMaximo) == 0 &&  Objects.equals(temporada, prenda.temporada);
    }

    @Override
    public int hashCode() {
       int result = Double.hashCode(costoProduccion);
       result = 31 * result + Objects.hashCode(costoMaximo);
        result = 31 * result + Objects.hashCode(genero);
        result = 31 * result + Objects.hashCode(modelo);
        result = 31 * result + Objects.hashCode(tela);
        result = 31 * result + Objects.hashCode(temporada);
        return result;
    }

    @Override
    public int compareTo(Prenda o) {
        int r = 0;
        if((r = Double.compare(this.costoProduccion, o.costoProduccion))!=0)
            return r;
        if((r = Double.compare(this.costoMaximo, o.costoMaximo))!=0)
            return r;
        if((r=this.modelo.compareTo(o.modelo))!=0)
            return r;
        if((r=this.tela.compareTo(o.tela))!=0)
            return r;
        if((r=this.temporada.compareTo(o.temporada))!=0)
            return r;
        return this.genero.compareTo(o.genero);
    }
}

