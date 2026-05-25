package persistencia;

import Excepciones.*;
import ProduccionRopa.Prenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArchivoRandomPrenda {
    private RandomAccessFile archivo;
    private String nombre;
    private final long size=64;
    private final long offset=56;

    public ArchivoRandomPrenda(String nombre) {
        this.nombre = nombre;
    }

    public void open() throws FileNotFoundException {
        archivo= new RandomAccessFile(this.nombre,"rw");
    }

    public boolean existe(String modelo){
        boolean existe=false;
        try {
            archivo.seek(0);
            while(true){
                boolean eliminado=archivo.readBoolean();
                if(!eliminado){
                    String mode=archivo.readUTF();
                    if(mode.equals(modelo)){
                        existe=true;
                        break;
                    }
                    archivo.seek(archivo.getFilePointer()+(offset-1));
                }else{
                    archivo.seek(archivo.getFilePointer()+(size-1));
                }
            }
        }catch(IOException e){
        }
        return existe;
    }

    private Prenda getPrenda() throws IOException, ExcepcionDeTemporadaNoValida,
            ExcepcionDeGeneroNoValido, ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        String genero = archivo.readUTF().trim();
        String modelo = archivo.readUTF().trim();
        String tela = archivo.readUTF().trim();
        double costoMaximo = archivo.readDouble();
        double costoProduccion = archivo.readDouble();
        String temporada = archivo.readUTF().trim();
        return new Prenda(genero, modelo, tela, costoMaximo, costoProduccion, temporada);
    }

    public boolean existe(Prenda prenda) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        boolean existe=false;
        try{
            archivo.seek(0);
            while(true){
                boolean eliminado=archivo.readBoolean();
                if(!eliminado){
                    if(prenda.equals(getPrenda())){
                        existe=true;
                        break;
                    }
                }else{
                    archivo.seek(archivo.getFilePointer()+(size-1));
                }
            }
        }catch(IOException e){}
        return existe;
    }

    private void grabarRegistro(Prenda prenda){
        try {
            archivo.seek(archivo.length());
            archivo.writeBoolean(false);
            archivo.writeUTF(String.format("%-10s", prenda.getGenero()));
            archivo.writeUTF(String.format("%-30s", prenda.getModelo()));
            archivo.writeUTF(String.format("%-20s", prenda.getTela()));
            archivo.writeDouble(prenda.getCostoMaximo());
            archivo.writeDouble(prenda.getCostoProduccion());
            archivo.writeUTF(String.format("%-10s", prenda.getTemporada()));
        }catch(IOException e){}
    }

    public void agregaPrenda(Prenda prenda) {
        if(prenda==null)
            throw new NullPointerException("Prenda null!");
        try {
            this.open();
            if(!existe(prenda.getModelo())){
                grabarRegistro(prenda);
                this.close();
            }else {
                this.close();
                throw new IllegalArgumentException("La prenda ya existe!");
            }
        }catch(IOException e){}
    }

    public void eliminarPrenda(Prenda prenda) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        if (prenda == null)
            throw new NullPointerException("Prenda null!");
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                try {
                    long inicioRegistro = archivo.getFilePointer();
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        String genero = archivo.readUTF().trim();
                        String modelo = archivo.readUTF().trim();
                        String tela = archivo.readUTF().trim();
                        double costoMaximo = archivo.readDouble();
                        double costoProduccion = archivo.readDouble();
                        String temporada = archivo.readUTF().trim();
                        if (modelo.equals(prenda.getModelo())) {
                            archivo.seek(inicioRegistro);
                            archivo.writeBoolean(true);
                            break;
                        }
                    } else {
                        archivo.seek(inicioRegistro + size);
                    }
                } catch (IOException e) {
                    break;
                }
            }
            this.close();
        } catch (IOException e) {
        }
    }

    public void modificaPrenda(Prenda prenda) {
        if (prenda == null)
            throw new NullPointerException("Prenda null!");
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                try {
                    long inicioRegistro = archivo.getFilePointer();
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        String genero = archivo.readUTF().trim();
                        String modelo = archivo.readUTF().trim();
                        String tela = archivo.readUTF().trim();
                        double costoMaximo = archivo.readDouble();
                        double costoProduccion = archivo.readDouble();
                        String temporada = archivo.readUTF().trim();
                        if (modelo.equals(prenda.getModelo())) {
                            archivo.seek(inicioRegistro + 1);
                            archivo.writeUTF(String.format("%-10s", prenda.getGenero()));
                            archivo.writeUTF(String.format("%-30s", prenda.getModelo()));
                            archivo.writeUTF(String.format("%-20s", prenda.getTela()));
                            archivo.writeDouble(prenda.getCostoMaximo());
                            archivo.writeDouble(prenda.getCostoProduccion());
                            archivo.writeUTF(String.format("%-10s", prenda.getTemporada()));
                            break;
                        }
                    } else {
                        archivo.seek(inicioRegistro + size);
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
                try {
                    if (!archivo.readBoolean())
                        numeroRegistros++;
                    archivo.seek(archivo.getFilePointer() + (size - 1));
                } catch (IOException e) {
                    break;
                }
            }
        } catch (IOException e) {
        }
        try {
            this.close();
        } catch (IOException e) {
        }
        return numeroRegistros;
    }

    public Prenda obtenerPrenda(String modelo) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        Prenda prenda = null;
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                try {
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        String genero = archivo.readUTF().trim();
                        String mod = archivo.readUTF().trim();
                        String tela = archivo.readUTF().trim();
                        double costoMaximo = archivo.readDouble();
                        double costoProduccion = archivo.readDouble();
                        String temporada = archivo.readUTF().trim();
                        if (mod.equals(modelo)) {
                            prenda = new Prenda(genero, mod, tela, costoMaximo, costoProduccion, temporada);
                            break;
                        }
                    } else {
                        archivo.seek(archivo.getFilePointer() + (size - 1));
                    }
                } catch (IOException e) {
                    break;
                }
            }
        } catch (IOException e) {
        }
        try {
            this.close();
        } catch (IOException e) {
        }
        return prenda;
    }

    public Prenda obtenerPrenda(long numeroRegistro) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido,
            ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido {
        int numReg = 0;
        Prenda prenda = null;
        try {
            this.open();
            archivo.seek(0);
            while (true) {
                try {
                    boolean eliminado = archivo.readBoolean();
                    if (!eliminado) {
                        numReg++;
                        String genero = archivo.readUTF().trim();
                        String modelo = archivo.readUTF().trim();
                        String tela = archivo.readUTF().trim();
                        double costoMaximo = archivo.readDouble();
                        double costoProduccion = archivo.readDouble();
                        String temporada = archivo.readUTF().trim();
                        if (numReg == numeroRegistro) {
                            prenda = new Prenda(genero, modelo, tela, costoMaximo, costoProduccion, temporada);
                            break;
                        }
                    } else {
                        archivo.seek(archivo.getFilePointer() + (size - 1));
                    }
                } catch (IOException e) {
                    break;
                }
            }
        } catch (IOException e) {
        }
        try {
            this.close();
        } catch (IOException e) {
        }
        return prenda;
    }

    public void recuperarEliminados(){
        try{
            this.open();
            archivo.seek(0);
            while(true){
                archivo.readBoolean();
                archivo.seek(archivo.getFilePointer()-1);
                archivo.writeBoolean(false);
                archivo.seek(archivo.getFilePointer()+(size-1));
            }
        }catch(IOException e){}
        try{
            this.close();
        }catch(IOException e){}
    }

    public void close() throws IOException {
        archivo.close();
    }
}