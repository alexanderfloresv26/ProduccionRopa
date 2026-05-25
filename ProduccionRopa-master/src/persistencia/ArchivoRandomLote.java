package persistencia;

import Excepciones.ExcepcionCantidadDePrendasFueraDeLimites;
import ProduccionRopa.Lote;
import ProduccionRopa.Prenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;

public class ArchivoRandomLote {
    private RandomAccessFile archivo;
    private String nombre;
    private final long size = 64;
    private final long offset = 56;

    public ArchivoRandomLote(String nombre) {
        this.nombre = nombre;
    }

    public void open() throws FileNotFoundException {
        archivo = new RandomAccessFile(this.nombre, "rw");
    }

    public boolean existe(int numeroLote) {
        boolean existe = false;
        try {
            archivo.seek(0);
            while (true) {
                boolean eliminado = archivo.readBoolean();
                if (!eliminado) {
                    int numLote = archivo.readInt();
                    if (numLote == numeroLote) {
                        existe = true;
                        break;
                    }
                    archivo.seek(archivo.getFilePointer() + (offset - 5));
                } else {
                    archivo.seek(archivo.getFilePointer() + (size - 1));
                }
            }
        } catch (IOException e) {
        }
        return existe;
    }

    private Lote getLote(Prenda prenda) throws IOException, ExcepcionCantidadDePrendasFueraDeLimites {
        int numeroLote = archivo.readInt();
        int numPiezas = archivo.readInt();
        String fechaStr = archivo.readUTF();
        String modeloPrenda = archivo.readUTF();
        LocalDate fechaFabricacion = LocalDate.parse(fechaStr);
        return new Lote(numeroLote, numPiezas, fechaFabricacion, prenda);
    }

    public boolean existe(Lote lote) {
        boolean existe = false;
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                boolean eliminado = archivo.readBoolean();
                if (!eliminado) {
                    int numLote = archivo.readInt();
                    if (numLote == lote.getNumeroLote()) {
                        existe = true;
                        break;
                    }
                    archivo.seek(archivo.getFilePointer() + (offset - 5));
                } else {
                    archivo.seek(archivo.getFilePointer() + (size - 1));
                }
            }
            this.close();
        } catch (IOException e) {
        }
        return existe;
    }

    private void grabarRegistro(Lote lote) {
        try {
            archivo.seek(archivo.length());
            archivo.writeBoolean(false);
            archivo.writeInt(lote.getNumeroLote());
            archivo.writeInt(lote.getNumPiezas());
            archivo.writeUTF(lote.getFechaFabricacion().toString());
            archivo.writeUTF(String.format("%-30s", lote.getPrenda().getModelo()));
        } catch (IOException e) {
        }
    }

    public void agregaLote(Lote lote) {
        if (lote == null)
            throw new NullPointerException("Lote null!");
        try {
            this.open();
            if (!existe(lote.getNumeroLote())) {
                grabarRegistro(lote);
                this.close();
            } else {
                this.close();
                throw new IllegalArgumentException("El lote ya existe!");
            }
        } catch (IOException e) {
        }
    }

    public void eliminarLote(Lote lote) {
        if (lote == null)
            throw new NullPointerException("Lote null!");
        try {
            this.open();
            archivo.seek(0);
            long posicion = 0;
            while (true) {
                try {
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        int numLote = archivo.readInt();
                        int numPiezas = archivo.readInt();
                        String fechaStr = archivo.readUTF();
                        String modeloPrenda = archivo.readUTF().trim();
                        if (numLote == lote.getNumeroLote()) {
                            archivo.seek(posicion);
                            archivo.writeBoolean(true);
                            break;
                        }
                        posicion = archivo.getFilePointer();
                    } else {
                        archivo.seek(archivo.getFilePointer() + (size - 1));
                        posicion = archivo.getFilePointer();
                    }
                } catch (IOException e) {
                    break;
                }
            }
            this.close();
        } catch (IOException e) {
        }
    }

    public void modificaLote(Lote lote) {
        if (lote == null)
            throw new NullPointerException("Lote null!");
        try {
            this.open();
            archivo.seek(0);
            long posicion = 0;
            while (true) {
                try {
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        int numLote = archivo.readInt();
                        int numPiezas = archivo.readInt();
                        String fechaStr = archivo.readUTF();
                        String modeloPrenda = archivo.readUTF().trim();
                        if (numLote == lote.getNumeroLote()) {
                            archivo.seek(posicion + 1);
                            archivo.writeInt(lote.getNumPiezas());
                            archivo.writeUTF(lote.getFechaFabricacion().toString());
                            archivo.writeUTF(String.format("%-30s", lote.getPrenda().getModelo()));
                            break;
                        }
                        posicion = archivo.getFilePointer();
                    } else {
                        archivo.seek(archivo.getFilePointer() + (size - 1));
                        posicion = archivo.getFilePointer();
                    }
                } catch (IOException e) {
                    break;
                }
            }
            this.close();
        } catch (IOException e) {
        }
    }

    public void eliminarLotesPorPrenda(String modeloPrenda) {
        try {
            this.open();
            archivo.seek(0);
            long posicion = 0;
            while (true) {
                try {
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        int numLote = archivo.readInt();
                        int numPiezas = archivo.readInt();
                        String fechaStr = archivo.readUTF();
                        String modelo = archivo.readUTF();
                        if (modelo.trim().equals(modeloPrenda)) {
                            archivo.seek(posicion);
                            archivo.writeBoolean(true);
                        }
                        posicion = archivo.getFilePointer();
                    } else {
                        archivo.seek(archivo.getFilePointer() + (size - 1));
                        posicion = archivo.getFilePointer();
                    }
                } catch (IOException e) {
                    break;
                }
            }
            this.close();
        } catch (IOException e) {
        }
    }

    public long getNumeroRegistros() {
        long numeroRegistros = 0;
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                if (!archivo.readBoolean())
                    numeroRegistros++;
                archivo.seek(archivo.getFilePointer() + (size - 1));
            }
        } catch (IOException e) {
        }
        try {
            this.close();
        } catch (IOException e) {
        }
        return numeroRegistros;
    }

    public Lote obtenerLote(int numeroLote) {
        Lote lote = null;
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                boolean eliminado = archivo.readBoolean();
                if (!eliminado) {
                    int numLote = archivo.readInt();
                    int numPiezas = archivo.readInt();
                    String fechaStr = archivo.readUTF();
                    String modeloPrenda = archivo.readUTF().trim();
                    if (numLote == numeroLote) {
                        LocalDate fechaFabricacion = LocalDate.parse(fechaStr);
                        ArchivoRandomPrenda arp = new ArchivoRandomPrenda("prendas.dat");
                        arp.open();
                        Prenda prenda = arp.obtenerPrenda(modeloPrenda);
                        arp.close();
                        if (prenda != null) {
                            lote = new Lote(numLote, numPiezas, fechaFabricacion, prenda);
                        }
                        break;
                    }
                } else {
                    archivo.seek(archivo.getFilePointer() + (size - 1));
                }
            }
        } catch (IOException e) {
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        try {
            this.close();
        } catch (IOException e) {
        }
        return lote;
    }

    public void recuperarEliminados() {
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                archivo.readBoolean();
                archivo.seek(archivo.getFilePointer() - 1);
                archivo.writeBoolean(false);
                archivo.seek(archivo.getFilePointer() + (size - 1));
            }
        } catch (IOException e) {
        }
        try {
            this.close();
        } catch (IOException e) {
        }
    }

    public void close() throws IOException {
        archivo.close();
    }
}