package persistencia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArchivoRandomLote {

    private RandomAccessFile archivo;
    private String nombre;
    private final long size=64;// Tamaño del registro
    private final long offset=56;

    public ArchivoRandomLote(String nombre) {
        this.nombre = nombre;
    }

    public void open() throws FileNotFoundException {
        archivo= new RandomAccessFile(this.nombre,"rw");
    }

    public boolean existe(long numeroControl){
        boolean existe=false;
        try {
            archivo.seek(1);
            while(true){
                long ncontrol=archivo.readLong();
                if(ncontrol==numeroControl){
                    existe=true;
                    break;
                }
                archivo.seek(archivo.getFilePointer()+offset);
            }
        }catch(IOException e){
        }
        return existe;
    }

    private Alumno getAlumno() throws IOException {
        archivo.seek(archivo.getFilePointer()+1);
        long noControl=archivo.readLong();
        String nombre=archivo.readUTF();
        byte semestre=archivo.readByte();
        String carrera=archivo.readUTF();
        float promedio=archivo.readFloat();
        Genero genero=Genero.values()[archivo.readByte()];
        return new Alumno(noControl,nombre,semestre,carrera,promedio,genero);
    }

    public boolean existe(Alumno alumno){
        boolean existe=false;
        try{
            while(true){
                if(alumno.equals(getAlumno())){
                    existe=true;
                    break;
                }
            }
        }catch(IOException e){}
        return existe;
    }

    private void grabarRegistro(Alumno alumno){
        try {
            archivo.seek(archivo.length());
            archivo.writeBoolean(false);
            archivo.writeLong(alumno.getNumeroControl());
            archivo.writeUTF(String.format("%-30s",alumno.getNombre()));
            archivo.writeByte(alumno.getSemestre());
            archivo.writeUTF(String.format("%-15s",alumno.getCarrera()));
            archivo.writeFloat(alumno.getPromedio());
            archivo.writeByte(Genero.valueOf(alumno.getGenero()).ordinal());
        }catch(IOException e){}
    }

    public void agregaAlumno(Alumno alumno) {
        if(alumno==null)
            throw new NullPointerException("Alumno null!");
        try {
            this.open();
            if(!existe(alumno.getNumeroControl())){
                grabarRegistro(alumno);
                this.close();
            }else {
                this.close();
                throw new IllegalArgumentException("Alumno existe!");
            }
        }catch(IOException e){}
    }

    public void eliminarAlumno(Alumno alumno){
        if(alumno==null)
            throw new NullPointerException("Alumno null!");
        try{
            this.open();
            if(existe(alumno)){
                archivo.seek(archivo.getFilePointer()-size);
                archivo.writeBoolean(true);
            }
            this.close();
        }catch(IOException e){}
    }

    public void modificaAlumno(Alumno alumno){
        if(alumno==null)
            throw new NullPointerException("Alumno null!");
        try{
            this.open();
            if(existe(alumno.getNumeroControl())){
                //archivo.seek(archivo.getFilePointer()+30);
                archivo.writeUTF(String.format("%-30s",alumno.getNombre()));
                archivo.writeByte(alumno.getSemestre());
                archivo.writeUTF(String.format("%-15s",alumno.getCarrera()));
                archivo.writeFloat(alumno.getPromedio());
            }
            this.close();
        }catch (IOException e){}
    }

    public long getNumeroRegistros(){
        long numeroRegistros=0;
        try{
            this.open();
            while(true){
                if(!archivo.readBoolean())
                    numeroRegistros++;
                archivo.seek(archivo.getFilePointer()+(size-1));
            }
        }catch(IOException e){}
        try{
            this.close();
        }catch(IOException e){}
        return numeroRegistros;
    }

    public Alumno obtenerAlumno(long numeroControl){
        Alumno alumno=null;
        try{
            this.open();
            while(true){
                if(!archivo.readBoolean())
                    if(numeroControl==archivo.readLong()){
                        String nombre=archivo.readUTF();
                        byte semestre=archivo.readByte();
                        String carrera=archivo.readUTF();
                        float promedio=archivo.readFloat();
                        Genero genero=Genero.values()[archivo.readByte()];
                        alumno=new Alumno(numeroControl,nombre,semestre,carrera,promedio,genero);
                        break;
                    }else
                        archivo.seek(archivo.getFilePointer()+(size-9));
                else
                    archivo.seek(archivo.getFilePointer()+(size-1));
            }
        }catch(IOException e){}
        try{
            this.close();
        }catch(IOException e){}
        return alumno;
    }

    public Alumno obtenerAlumno(int numeroRegistro){
        int numReg=0;
        Alumno alumno=null;
        try{
            this.open();
            while(true){
                if(!archivo.readBoolean())
                    if(++numReg==numeroRegistro){
                        long nc=archivo.readLong();
                        String nombre=archivo.readUTF();
                        byte semestre=archivo.readByte();
                        String carrera=archivo.readUTF();
                        float promedio=archivo.readFloat();
                        Genero genero=Genero.values()[archivo.readByte()];
                        alumno=new Alumno(nc,nombre,semestre,carrera,promedio,genero);
                        break;
                    }
                archivo.seek(archivo.getFilePointer()+(size-1));
            }
        }catch(IOException e){}
        try{
            this.close();
        }catch(IOException e){}
        return alumno;
    }

    public void recuperarEliminados(){
        try{
            this.open();
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
