package co.edu.escuelaing.networking;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class UrlExamples
{
    public static void main( String[] args )
    {
        try {
            URL firstSite = new URL("https://www.bmw.com.co:4545/es/all-models/4-series/coupe/2020/vision-general-bmw-serie4-coupe.html?modelo=bmwserie4#M4");
            System.out.println("La URL es: " + firstSite.toString());
            System.out.println("El host es: " + firstSite.getHost());
            System.out.println("El puerto es: " + firstSite.getPort());
            System.out.println("El Authority es: " + firstSite.getAuthority());
            System.out.println("El archivo es: " + firstSite.getFile());
            System.out.println("El path es: " + firstSite.getPath());
            System.out.println("El protocolo es: " + firstSite.getProtocol());
            System.out.println("El query es: " + firstSite.getQuery());
            System.out.println("La referencia es: " + firstSite.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
