package persistencia;

import Excepciones.*;
import ProduccionRopa.Lote;
import ProduccionRopa.Prenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;

public class ArchivoRandomLote {

    private RandomAccessFile archivo;
    private String nombre;

    public ArchivoRandomLote(String nombre) {
        this.nombre = nombre;
    }

    public void open() throws FileNotFoundException {
        archivo = new RandomAccessFile(nombre, "rw");
    }

    public void close() throws IOException {
        if (archivo != null)
            archivo.close();
    }

    private void escribirLote(Lote lote) throws IOException {
        archivo.writeBoolean(false);
        archivo.writeInt(lote.getNumeroLote());
        archivo.writeInt(lote.getNumPiezas());
        archivo.writeUTF(lote.getFechaFabricacion().toString());
        archivo.writeUTF(String.format("%-30s", lote.getPrenda().getModelo()));
    }

    public boolean existe(int numeroLote) {
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                int numLote = archivo.readInt();
                archivo.readInt();
                archivo.readUTF();
                archivo.readUTF();
                if (!eliminado && numLote == numeroLote) {
                    close();
                    return true;
                }
            }
            close();
        } catch (IOException e) {
        }
        return false;
    }

    public void agregaLote(Lote lote) {
        if (lote == null)
            throw new NullPointerException("Lote null!");
        try {
            if (existe(lote.getNumeroLote()))
                throw new IllegalArgumentException("El lote ya existe!");
            open();
            archivo.seek(archivo.length());
            escribirLote(lote);
            close();
            System.out.println("Lote agregado exitosamente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminarLote(Lote lote) {
        if (lote == null)
            throw new NullPointerException("Lote null!");
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                long inicioRegistro = archivo.getFilePointer();
                boolean eliminado = archivo.readBoolean();
                int numLote = archivo.readInt();
                archivo.readInt();
                archivo.readUTF();
                archivo.readUTF();

                if (!eliminado && numLote == lote.getNumeroLote()) {
                    archivo.seek(inicioRegistro);
                    archivo.writeBoolean(true);
                    break;
                }
            }
            close();
        } catch (IOException e) {
        }
    }

    public void modificaLote(Lote lote) {
        if (lote == null)
            throw new NullPointerException("Lote null!");
        eliminarLote(lote);
        agregaLote(lote);
    }

    public void eliminarLotesPorPrenda(
            String modeloPrenda) {
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                long inicioRegistro = archivo.getFilePointer();
                boolean eliminado = archivo.readBoolean();
                archivo.readInt();
                archivo.readInt();
                archivo.readUTF();
                String modelo = archivo.readUTF().trim();
                if (!eliminado && modelo.equals(modeloPrenda)) {
                    archivo.seek(inicioRegistro);
                    archivo.writeBoolean(true);
                    archivo.seek(inicioRegistro + 1);
                }
            }
            close();
        } catch (IOException e) {
        }
    }

    public long getNumeroRegistros() {
        long contador = 0;
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                archivo.readInt();
                archivo.readInt();
                archivo.readUTF();
                archivo.readUTF();
                if (!eliminado)
                    contador++;
            }
            close();
        } catch (IOException e) {
        }
        return contador;
    }

    public Lote obtenerLote(int numeroLote) {
        Lote lote = null;
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                int numLote = archivo.readInt();
                int numPiezas = archivo.readInt();
                String fechaStr = archivo.readUTF();
                String modeloPrenda = archivo.readUTF().trim();
                if (!eliminado && numLote == numeroLote) {
                    LocalDate fechaFabricacion = LocalDate.parse(fechaStr);
                    ArchivoRandomPrenda arp = new ArchivoRandomPrenda("prendas.dat");
                    Prenda prenda = arp.obtenerPrenda(modeloPrenda);
                    if (prenda != null) {
                        lote = new Lote(numLote, numPiezas, fechaFabricacion, prenda);
                        break;
                    }
                }
            }
            close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lote;
    }

    public Lote obtenerLote(long numeroRegistro) {
        long contador = 0;
        Lote lote = null;
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                int numLote = archivo.readInt();
                int numPiezas = archivo.readInt();
                String fechaStr = archivo.readUTF();
                String modeloPrenda = archivo.readUTF().trim();
                if (!eliminado) {
                    contador++;
                    if (contador == numeroRegistro) {
                        LocalDate fechaFabricacion = LocalDate.parse(fechaStr);
                        ArchivoRandomPrenda arp = new ArchivoRandomPrenda("prendas.dat");
                        Prenda prenda = arp.obtenerPrenda(modeloPrenda);
                        if (prenda != null) {
                            lote = new Lote(numLote, numPiezas, fechaFabricacion, prenda);
                            break;
                        }
                    }
                }
            }
            close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lote;
    }

    public void recuperarEliminados() {
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                long inicioRegistro = archivo.getFilePointer();
                boolean eliminado = archivo.readBoolean();
                archivo.readInt();
                archivo.readInt();
                archivo.readUTF();
                archivo.readUTF();
                if (eliminado) {
                    archivo.seek(inicioRegistro);
                    archivo.writeBoolean(false);
                    archivo.seek(inicioRegistro + 1);
                }
            }
            close();
        } catch (IOException e) {
        }
    }
}