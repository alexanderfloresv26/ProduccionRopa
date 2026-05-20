package ui;

import ProduccionRopa.Lote;
import ProduccionRopa.Prenda;
import java.util.ArrayList;

public class Visualiazacion {

    public void visualizaPrenda(Prenda prenda){
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%08d ",prenda.getModelo()));
        sb.append(String.format("%-30s ",prenda.getGenero()));
        sb.append(String.format("%02d ",prenda.getTemporada()));
        sb.append(String.format("%-20s ",prenda.getTela()));
        sb.append(String.format("%6.2f ",prenda.getCostoProduccion()));
        sb.append(String.format("%-12s ",prenda.getCostoMaximo()));
        System.out.println(sb.toString());
    }

    public void visualizaEncabezadoPrenda(){
        System.out.println(String.format("%-8s %-30s %-4s %-20s %-6s %-12s","Modelo","Genero","Temporada",
                "Tela","Costo Produccion","Costo Maximo de Produccion"));
    }

    public void visualizaTodosPrenda(ArrayList<Prenda> prendas){
        visualizaEncabezadoPrenda();
        for(Prenda prenda:prendas){
            visualizaPrenda(prenda);
        }
    }

    public void visualizaLote(Lote lote){
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%08d ",lote.getNumeroLote()));
        sb.append(String.format("%-30s ",lote.getPrenda()));
        sb.append(String.format("%02d ",lote.getFechaFabricacion()));
        sb.append(String.format("%-20s ",lote.getNumPiezas()));
        System.out.println(sb.toString());
    }

    public void visualizaEncabezadoLote(){
        System.out.println(String.format("%-8s %-30s %-4s %-20s %-6s %-12s","No. Lote","Prenda","Fecha de Fabricacion",
                "Numero de Piezas"));
    }

    public void visualizaTodosLotes(ArrayList<Lote> lotes){
        visualizaEncabezadoLote();
        for(Lote lote:lotes){
            visualizaLote(lote);
        }
    }
}
