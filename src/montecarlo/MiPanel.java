package montecarlo;

import javax.swing.*;
import java.awt.*;

/**
 * @author Fernando2
 * Created on Dec 7, 2014, 11:43:31 AM
 */

public class MiPanel extends JPanel {
    private JButton simular;
    private JButton info;
    
    public MiPanel(){
        addComponentes();
    }
    
    public final void addComponentes(){
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
        
        simular = new JButton("Simular");
        info = new JButton("");
        info.setIcon(new ImageIcon("src/img/info.png"));

        Font fuente = new Font("Arial", Font.PLAIN, 12);
        JTextArea textoProblema = new JTextArea("");
        textoProblema.setFont(fuente);
        textoProblema.setText("14.- A una estación de gasolina que cuenta con una sola bomba "
                + "llegan clientes a una tasa de 10 por hora con distribución exponencial. Estos "
                + "clientes son atendidos por el operador de la bomba, que les da servicio y les cobra. "
                + "El tiempo de servicio se distribuye exponencialmente con media de 4 minutos por cliente."
                + "\n\n\ta) Determine el número promedio de clientes en el sistema."
                + "\n\tb) Determine el porcentaje del tiempo que el operador está ocupado."
                + "\n\tc) Determine el tiempo promedio de permanencia en la fila.");
        textoProblema.setSize(550, 200);
        textoProblema.setBackground(this.getBackground());
        textoProblema.setEditable(false);
        textoProblema.setWrapStyleWord(true);
        textoProblema.setLineWrap(true);
        
        JPanel centro = new JPanel();
        JPanel sur = new JPanel();
        JPanel surBoton = new JPanel();
        JPanel surInfo = new JPanel();
        
        centro.add(textoProblema);
        sur.setLayout(new BorderLayout());
        surBoton.add(simular);
        surInfo.add(info);
        sur.add(surBoton, "Center");
        sur.add(surInfo, "East");
        
        this.add(centro, "Center");
        this.add(sur, "South");
    }
    
    public void addEventos(MiOyente oyente){
        simular.addActionListener(oyente);
        info.addActionListener(oyente);
    }

    public JButton getSimular() {
        return simular;
    }

    public JButton getInfo() {
        return info;
    }
    
}
