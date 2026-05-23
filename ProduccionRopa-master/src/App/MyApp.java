package App;

import Excepciones.*;
import ui.Principal;

public class MyApp {

    public static void main(String[] args) throws ExcepcionDeTemporadaNoValida, ExcepcionDeGeneroNoValido, ExcepcionDeCostoFueraDeLimites, ExcepcionDeCostoMaximoNoValido, ExcepcionCantidadDePrendasFueraDeLimites {
        new Principal().run();

    }
}
