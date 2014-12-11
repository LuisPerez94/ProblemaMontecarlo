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
    private JComboBox clientes;
    private JComboBox porcentaje;
    private JComboBox corridas;
    
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
        
        String[] strClientes = {"100", "300", "500", "800", "1000",
                                "1200", "1500", "1800", "2000"};
        clientes = new JComboBox(strClientes);
        clientes.setSelectedIndex(1);
        
        String[] strPorcentaje = {"1", "5", "10"};
        porcentaje = new JComboBox(strPorcentaje);
        porcentaje.setSelectedIndex(1);
        
        String[] strCorridas = {"2","3","4","5","6","7","8","9","10"};
        corridas = new JComboBox(strCorridas);
        corridas.setSelectedIndex(3);
        
        JPanel centro = new JPanel();
        JPanel sur = new JPanel();
        JPanel surCombos = new JPanel();
        JPanel surBoton = new JPanel();
        JPanel surInfo = new JPanel();
        
        JPanel grid1 = new JPanel();
        JPanel grid2 = new JPanel();
        JPanel grid3 = new JPanel();
        
        surCombos.setBorder(BorderFactory.createLineBorder(this.getBackground(), 5));
        Font arial = new Font("Arial", Font.PLAIN, 12);
        JLabel lab1 = new JLabel("Cantidad de clientes a atender:");
        lab1.setFont(arial);
        clientes.setFont(arial);
        JLabel lab2 = new JLabel("Porcentaje de error (Para los IC): ");
        lab2.setFont(arial);
        porcentaje.setFont(arial);
        JLabel lab3 = new JLabel("Cantidad de corridas: ");
        lab3.setFont(arial);
        corridas.setFont(arial);
        
        centro.add(textoProblema);
        sur.setLayout(new BorderLayout());
        grid1.add(lab1);
        grid2.add(lab2);
        grid3.add(lab3);
        grid1.add(clientes);
        grid2.add(porcentaje);
        grid3.add(corridas);
        surCombos.add(grid1);
        surCombos.add(grid2);
        surCombos.add(grid3);
        surBoton.add(simular);
        surInfo.add(info);
//        sur.add(surCombos, "North");
        sur.add(surBoton, "Center");
        sur.add(surInfo, "East");
        
        this.add(centro, "North");
        this.add(surCombos, "Center");
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

    public JComboBox getClientes() {
        return clientes;
    }

    public JComboBox getPorcentaje() {
        return porcentaje;
    }

    public JComboBox getCorridas() {
        return corridas;
    }
    
}
