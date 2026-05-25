package persistencia;

import Excepciones.*;
import ProduccionRopa.Prenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArchivoRandomPrenda {

    private RandomAccessFile archivo;
    private String nombre;

    public ArchivoRandomPrenda(String nombre) {
        this.nombre = nombre;
    }

    public void open() throws FileNotFoundException {
        archivo = new RandomAccessFile(nombre, "rw");
    }

    public void close() throws IOException {
        if (archivo != null)
            archivo.close();
    }

    private Prenda leerPrenda() throws IOException, ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String genero = archivo.readUTF().trim();
        String modelo = archivo.readUTF().trim();
        String tela = archivo.readUTF().trim();
        double costoMaximo = archivo.readDouble();
        double costoProduccion = archivo.readDouble();
        String temporada = archivo.readUTF().trim();
        return new Prenda(genero, modelo, tela, costoMaximo, costoProduccion, temporada
        );
    }

    private void escribirPrenda(Prenda p) throws IOException {
        archivo.writeBoolean(false);
        archivo.writeUTF(String.format("%-10s", p.getGenero()));
        archivo.writeUTF(String.format("%-30s", p.getModelo()));
        archivo.writeUTF(String.format("%-20s", p.getTela()));
        archivo.writeDouble(p.getCostoMaximo());
        archivo.writeDouble(p.getCostoProduccion());
        archivo.writeUTF(String.format("%-10s", p.getTemporada()));
    }

    public boolean existe(String modelo) {

        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                String genero = archivo.readUTF();
                String mod = archivo.readUTF();
                String tela = archivo.readUTF();
                double costoMax = archivo.readDouble();
                double costoProd = archivo.readDouble();
                String temporada = archivo.readUTF();
                if (!eliminado && mod.trim().equals(modelo.trim())) {
                    close();
                    return true;
                }
            }
            close();
        } catch (IOException e) {
        }
        return false;
    }

    public boolean existe(Prenda prenda) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                if (!eliminado) {
                    Prenda actual = leerPrenda();
                    if (actual.equals(prenda)) {
                        close();
                        return true;
                    }
                } else {
                    leerPrenda();
                }
            }
            close();
        } catch (IOException e) {
        }
        return false;
    }

    public void agregaPrenda(Prenda prenda) {
        if (prenda == null)
            throw new NullPointerException("Prenda null");
        try {
            if (existe(prenda.getModelo()))
                throw new IllegalArgumentException("La prenda ya existe");
            open();
            archivo.seek(archivo.length());
            escribirPrenda(prenda);
            close();
            System.out.println("Prenda agregada exitosamente");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminarPrenda(Prenda prenda) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        if (prenda == null)
            throw new NullPointerException("Prenda null");
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                long inicioRegistro = archivo.getFilePointer();
                boolean eliminado = archivo.readBoolean();
                String genero = archivo.readUTF();
                String modelo = archivo.readUTF();
                String tela = archivo.readUTF();
                double costoMax = archivo.readDouble();
                double costoProd = archivo.readDouble();
                String temporada = archivo.readUTF();
                if (!eliminado && modelo.trim().equals(prenda.getModelo().trim())) {
                    archivo.seek(inicioRegistro);
                    archivo.writeBoolean(true);
                    break;
                }
            }
            close();
        } catch (IOException e) {
        }
    }

    public void modificaPrenda(Prenda prenda) {
        if (prenda == null)
            throw new NullPointerException("Prenda null");
        try {
            eliminarPrenda(prenda);
            agregaPrenda(prenda);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getNumeroRegistros() {
        long contador = 0;
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                archivo.readUTF();
                archivo.readUTF();
                archivo.readUTF();
                archivo.readDouble();
                archivo.readDouble();
                archivo.readUTF();
                if (!eliminado)
                    contador++;
            }
            close();
        } catch (IOException e) {
        }
        return contador;
    }

    public Prenda obtenerPrenda(String modelo) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        Prenda encontrada = null;
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                Prenda actual = leerPrenda();
                if (!eliminado && actual.getModelo().equals(modelo)) {
                    encontrada = actual;
                    break;
                }
            }
            close();
        } catch (IOException e) {
        }
        return encontrada;
    }

    public Prenda obtenerPrenda(long numeroRegistro) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {

        long contador = 0;
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                boolean eliminado = archivo.readBoolean();
                Prenda actual = leerPrenda();
                if (!eliminado) {
                    contador++;
                    if (contador == numeroRegistro) {
                        close();
                        return actual;
                    }
                }
            }
            close();
        } catch (IOException e) {
        }
        return null;
    }

    public void recuperarEliminados() {
        try {
            open();
            archivo.seek(0);
            while (archivo.getFilePointer() < archivo.length()) {
                long inicioRegistro = archivo.getFilePointer();
                boolean eliminado = archivo.readBoolean();
                archivo.readUTF();
                archivo.readUTF();
                archivo.readUTF();
                archivo.readDouble();
                archivo.readDouble();
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