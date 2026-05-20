package ProduccionRopa;

import Excepciones.ExcepcionCantidadDePrendasFueraDeLimites;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public class Lote implements Comparable <Lote> {

    private static int contadorLote = 0;

    private int numeroLote;
    private int numPiezas;
    private LocalDate fechaFabricacion;
    private Prenda prenda;

    private static final double PORCENTAJE_GANANCIA_POR_PIEZA = 0.15;
    private static final double PORCENTAJE_GANANCIA_POR_LOTE = 0.05;

    private static final int MIN_PIEZAS = 50;
    private static final int MAX_PIEZAS = 350;


    private static Comparator<Lote> comparator = new Comparator<Lote>() {
        public int compare(Lote o1, Lote o2) {
            return o1.compareTo(o2);
        }
    };

    public Lote(int numeroLotem, int numPiezas, LocalDate fechaFabricacion, Prenda p) throws ExcepcionCantidadDePrendasFueraDeLimites {
        this.numeroLote = numeroLote;
        this.numPiezas = numPiezas;
        this.fechaFabricacion = fechaFabricacion;
        this.prenda = p;

        validacion();
    }

    public void validacion() throws ExcepcionCantidadDePrendasFueraDeLimites
    {
        if (numPiezas < MIN_PIEZAS || numPiezas > MAX_PIEZAS)
            throw new ExcepcionCantidadDePrendasFueraDeLimites("Numero de piezas fuera de los limites permitidos");
    }

    public double calcularCostoProduccionLote()
    {
        return numPiezas * prenda.getCostoProduccion();
    }

    public double calcularMontoRecuperacionVentaIndividual() {
        return calcularCostoProduccionLote() + (calcularCostoProduccionLote() * PORCENTAJE_GANANCIA_POR_PIEZA);
    }

    public double calcularMontoRecuperacionVentaPorLote() {
        return calcularCostoProduccionLote() + (calcularCostoProduccionLote() * PORCENTAJE_GANANCIA_POR_LOTE);
    }

    public int getNumeroLote() {
        return numeroLote;
    }

    public int getNumPiezas() {
        return numPiezas;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }

    public Prenda getPrenda() {
        return prenda;
    }

    @Override
    public String toString() {
        return "Lote{" +
                "numero=" + numeroLote +
                ", numPiezas=" + numPiezas +
                ", fechaFabricacion=" + fechaFabricacion +
                ", prenda=" + prenda +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lote lote)) return false;
        return numeroLote == lote.numeroLote && numPiezas == lote.numPiezas && Objects.equals(fechaFabricacion, lote.fechaFabricacion)
                && Objects.equals(prenda, lote.prenda);
    }

    @Override
    public int hashCode() {
        int result = numeroLote;
        result = 31 * result + numPiezas;
        result = 31 * result + Objects.hashCode(fechaFabricacion);
        result = 31 * result + Objects.hashCode(prenda);
        return result;
    }

    public int compareTo(Lote o)
    {
        int r=0;
        if ((r=this.numeroLote - o.numeroLote)!=0)
            return r;
        if ((r=this.numPiezas - o.numPiezas)!=0)
            return r;
        if ((r=this.fechaFabricacion.compareTo(o.fechaFabricacion))!=0)
            return r;
        return this.prenda.compareTo(o.prenda);
    }
}
