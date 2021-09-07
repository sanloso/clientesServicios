package co.edu.escuelaing.webapp;

import co.edu.escuelaing.springplus.Component;
import co.edu.escuelaing.springplus.Service;

@Component
public class Cuadrado {

    @Service
    public static String cuadrado(){
        return "2.0 * 2.0";
    }
}
