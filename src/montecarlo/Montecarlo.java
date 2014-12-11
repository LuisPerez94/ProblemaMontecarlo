package montecarlo;

import javax.swing.*;

/**
 * @author Fernando2
 * Created on Dec 7, 2014, 11:07:19 AM
 */

public class Montecarlo {

    public static void main(String[] args) {
        // Colocamos el look and feel del sistema operativo que estemos usando
        setLookAndFeel();
        
        // Se muestra la primera pantalla con el texto del problema
        dibujarVentana();
    }
    
    public static void dibujarVentana(){
        JFrame f = new JFrame("Problema de Montecarlo");
        f.setSize(570, 220);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setResizable(false);
        
        MiPanel p = new MiPanel();
        MiOyente o = new MiOyente(p);
        
        f.addWindowListener(o);
        p.addEventos(o);
        
        f.add(p);
        f.setVisible(true);
    }

    public static void setLookAndFeel(){
        try{
            //Poner el nombre de la aplicacion en la barra de tareas (OS X)
            System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "PinaProject" );
            
            //Poner el JMenuBar en la barra de tareas de OS X
            System.setProperty( "com.apple.macos.useScreenMenuBar", "true" );
            
            //Poner el JMenuBar en la barra de tareas de OS X -Versiones antiguas de Java
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            
            //Colocar el look and feel del sistema operativo que se esté usando
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        }catch(UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e){
            
        }
    }
}
