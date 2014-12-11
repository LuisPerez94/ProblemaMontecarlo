package montecarlo;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

/**
 * @author Fernando2
 * Created on Dec 7, 2014, 11:59:45 AM
 */

public class MiOyente extends WindowAdapter implements ActionListener{
    private final MiPanel p;
    private Tabla tabla;
    private boolean hayTabla;

    public MiOyente(MiPanel p){
        this.p = p;
        hayTabla = false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        String etiquetaBoton = e.getActionCommand();
        
        switch(etiquetaBoton){
            case "":
                mostrarCreditos();
                break;
                
            case "Simular":
                crearTabla();
                break;
        }
    }
    
    @Override
    public void windowClosing(WindowEvent e){
        // Si el evento viene de la ventana principal...
        if(e.getComponent().getClass().isInstance(new JFrame())){
            int opcion = JOptionPane.showConfirmDialog(null, "¿Desea salir?", "Salir", JOptionPane.OK_CANCEL_OPTION);
            if(opcion == JOptionPane.OK_OPTION){
                System.exit(-1);
            }
            
        }else{
            tabla.dispose();
            hayTabla = false;
        }
    }
    
    public void crearTabla(){
        if(hayTabla){
            int opcion = JOptionPane.showConfirmDialog(null, "Se cerrará la tabla actual", "Nueva simulación", JOptionPane.OK_CANCEL_OPTION);
            
            if(opcion == JOptionPane.OK_OPTION){
                tabla.dispose();
                tabla = new Tabla(Integer.parseInt(p.getClientes().getSelectedItem()+""), 
                                    Integer.parseInt(p.getPorcentaje().getSelectedItem()+""),
                                        Integer.parseInt(p.getCorridas().getSelectedItem()+""));
                tabla.addEventos(this);
                hayTabla = true;
            }
            
        }else{
            tabla = new Tabla(Integer.parseInt(p.getClientes().getSelectedItem()+""), 
                                Integer.parseInt(p.getPorcentaje().getSelectedItem()+""),
                                    Integer.parseInt(p.getCorridas().getSelectedItem()+""));
            tabla.addEventos(this);
            hayTabla = true;
        }
    }
    
    public void mostrarCreditos(){
        JOptionPane.showMessageDialog(null, "Este programa fue desarrollado por"
                + "\n\n º Bobadilla Contreras Miguel Fernando"
                + "\n\n º Chávez San Germán Gerardo"
                + "\n\n º Ordoñez Ruiz Edgar"
                + "\n\n º Ponce Rogel Rosa Carmina"
                + "\n\n º Romero Velázques Jonathan"
                + "\n\n v.1.1.0", 
                "Problema de Montecarlo", JOptionPane.INFORMATION_MESSAGE);
    }
}
