package ui;

import ProduccionRopa.Lote;
import ProduccionRopa.Prenda;
import java.util.ArrayList;

public class Visualiazacion {

    public void visualizaPrenda(Prenda prenda){
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%-30s ", prenda.getModelo()));
        sb.append(String.format("%-12s ", prenda.getGenero()));
        sb.append(String.format("%-12s ", prenda.getTemporada()));
        sb.append(String.format("%-20s ", prenda.getTela()));
        sb.append(String.format("%10.2f ", prenda.getCostoProduccion()));
        sb.append(String.format("%10.2f ", prenda.getCostoMaximo()));
        System.out.println(sb.toString());
    }

    public void visualizaEncabezadoPrenda(){
        System.out.println(String.format("%-30s %-12s %-12s %-20s %10s %10s","Modelo","Genero","Temporada",
                "Tela","Costo Prod.","Costo Max."));
    }

    public void visualizaTodosPrenda(ArrayList<Prenda> prendas){
        visualizaEncabezadoPrenda();
        for(Prenda prenda:prendas){
            visualizaPrenda(prenda);
        }
    }

    public void visualizaLote(Lote lote){
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%08d ", lote.getNumeroLote()));
        sb.append(String.format("%-30s ", lote.getPrenda().getModelo()));
        sb.append(String.format("%-12s ", lote.getFechaFabricacion().toString()));
        sb.append(String.format("%6d ", lote.getNumPiezas()));
        System.out.println(sb.toString());
    }

    public void visualizaEncabezadoLote(){
        System.out.println(String.format("%-8s %-30s %-12s %-6s","No. Lote","Modelo Prenda","Fecha Fabricacion","Piezas"));
    }

    public void visualizaTodosLotes(ArrayList<Lote> lotes){
        visualizaEncabezadoLote();
        for(Lote lote:lotes){
            visualizaLote(lote);
        }
    }
}