package montecarlo;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;

/**
 * @author Fernando2 
 * Created on 7/12/2014, 12:56:05 PM
 */
public final class Tabla extends JFrame {
    private final int totalClientes;
    private final int corridas;
    private final int porcentaje;
    private double promedioClientes = 0;
    private ArrayList<Double> intA;
    private ArrayList<Double> intB;
    private ArrayList<Double> intC;

    public Tabla(int clientes, int porcentaje, int corridas) {
        totalClientes = clientes;
        this.porcentaje = porcentaje;
        this.corridas = corridas;
        
        this.setSize(850, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Iniciamos los arrays que usaremos para generar los intervalos de confianza
        intA = new ArrayList<>();
        intB = new ArrayList<>();
        intC = new ArrayList<>();
        
        // Hacemos un panel con pestañas donde estarán las corridas...
        JTabbedPane tabs = new JTabbedPane();
        
        // Hacemos 5 corridas
        for (int i = 0; i < corridas; i++) {
            tabs.add(nuevoPanel());
            tabs.setTitleAt(i, "Corrida " + (i + 1));
        }

        tabs.add(panelIC(), "Intervalo de confianza");
        
        this.add(tabs);
        this.setVisible(true);
    }

    public JPanel nuevoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(this.getBackground(), 5));

        // Nombres de las columnas 
        String[] columnas = {"Cliente", "Tiempo entre llegadas", "Tiempo de llegada", "Inicio de servicio",
            "Tiempo de servicio", "Termina servicio", "Tiempo de servicio",
            "Tiempo de espera en la fila", "Permanencia promedio en la fila",
            "Porcentaje de tiempo ocupado", "Promedio porcentaje tiempo ocupado"
        };

        // Generacion de los datos de cada fila
        Object[][] filas = generarFilas(columnas);

        JTable tablaMont = new JTable(filas, columnas);
        
        JScrollPane scroll = new JScrollPane(tablaMont);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JPanel norte = new JPanel();
        JTextArea textNorte = new JTextArea();
        textNorte.setFont(new Font("Arial", Font.PLAIN, 12));
        textNorte.setText("a) Número promedio de clientes en el sistema: " + promedioClientes
                + "\nb) Porcentaje de tiempo que el operador estuvo ocupado: " + filas[totalClientes-1][10] + "%"
                + "\nc) Tiempo promedio de permanencia en la fila: " + filas[totalClientes-1][8] + " min");
        textNorte.setEditable(false);
        textNorte.setBackground(this.getBackground());
        norte.add(textNorte);
        
        intA.add(promedioClientes);
        intB.add((double)filas[totalClientes-1][10]);
        intC.add((double)filas[totalClientes-1][8]);
        
        panel.add(scroll, "Center");
        panel.add(norte, "North");

        return panel;
    }

    // Este sera el metodo donde se desarrolle todo el problema...
    // Aquí está todo el folklore :v
    public Object[][] generarFilas(String[] columnas) {
        int clientesPorHora = 10;
        int minutosPorCliente = 6;
        int tiempoServicio = 4;
        int cont = 1;

        Object[][] filas = new Object[totalClientes][columnas.length];

        // Atenderemos 5000 clientes, por qué? Porque sí
        for (int i = 0; i < totalClientes; i++) {
            // 0.- # de cliente...
            filas[i][0] = (i + 1);

            // 1.- tiempo entre llegadas (r exponencial);
            // x = - 1 / lambda * ln(1 - r); tranformada inversa dist. exp.
            filas[i][1] = Math.rint((-minutosPorCliente * Math.log(1 - Math.random())) * 1000)/1000;

            // 2.- tiempo de llegada a la gasolinería
            // tiempo entre llegada(actual) + tiempo de llegada a la gasolinería(anterior)
            // si es la primera iteración, no hacemos la suma, puesto que no hay valor anterior.
            if (i == 0) {
                filas[i][2] = filas[i][1];

            } else {
                filas[i][2] = Math.rint(((double) filas[i][1] + (double) filas[i - 1][2]) * 1000)/1000;
            }
            

            // 3.- inicia servicio
            // Max(tiempo llegada a la gasolinería(actual) + termina servicio(anterior)
            // si es la primera iteración, no hacemos la suma, puesto que no hay valor anterior.
            if (i == 0) {
                filas[i][3] = filas[i][2];

            } else {
                filas[i][3] = Math.max((double) filas[i][2], (double) filas[i - 1][5]);
            }
            
            // 4.- tiempo de servicio
            // x = - 1 / lambda * ln(1 - r); tranformada inversa dist. exp.
            filas[i][4] = Math.rint(((-tiempoServicio) * Math.log(1 - Math.random()))* 1000)/1000;

            //5.- Termino del servicio
            //Se suma el inicio del servicio y el tiempo de servicio
            filas[i][5] =  Math.rint(((double) filas[i][3] + (double) filas[i][4]) * 1000)/1000;

            //6.- tiempo de cobro
            //diferencia entre el termino de servicio y el tiempo de entrada de la a la 
            filas[i][6] =  Math.rint(((double) filas[i][5] - (double) filas[i][2]) * 1000)/1000;

            //7.- tiempo en cola
            //Es la diferencia entre inicia servicio y la entrada a la gasolinera
            filas[i][7] =  Math.rint(((double) filas[i][3] - (double) filas[i][2]) * 1000)/1000;

            //8.- Permanencia promedio en cola
            //Cuando es la primera iteracion, se deja el mismo valor, en caso contrario se saca el promedio
            if (i == 0) {
                filas[i][8] = filas[i][7];
            } else {
                double promedio = 0;
                for (int j = 0; j <= i; j++) {
                    promedio += Double.parseDouble(filas[j][7] + "");
                }
                filas[i][8] =  Math.rint((promedio / (i+1)) * 1000)/1000;
            }
            
        }
        
        
        //Este ultimo for es para las columnas 9 y  10 ya que usan valores que en el primer for aun
        //no existen en la matriz
        for (int i = 0; i < totalClientes; i++) {
            //9.- Porcentaje de tiempo ocupado
            //Porcentaje entre el tiempo de servicio entre la diferencia del inicio de servicio(actual) y el inicio de servicio     
            if (i == totalClientes-1) {
                filas[i][9] = Math.rint(( (double) filas[i][4] / ((double) filas[i][3])) * 100);
//                filas[i][9] = (double) filas[i][4] / ((double) filas[i][3]);
                
                if((filas[i][9]+ "").equals("NaN")){
                    filas[i][9] = 100.0;
//                    System.out.println("NAN!");
                }
            }else
                filas[i][9] =  Math.rint(((double) filas[i][4] / ((double) filas[i + 1][3] - (double) filas[i][3])) * 10000)/100;
                
                if((filas[i][9]+ "").equals("NaN")){
                    filas[i][9] = 100.0;
//                    System.out.println("NAN!");
                }
//                filas[i][9] =  (double) filas[i][4] / ((double) filas[i + 1][3] - (double) filas[i][3]);
            
            
            
            //10.-Promedio del porcentaje del tiempo ocupado
            //Promedio del porcentaje :v
            if (i == 0) {
                filas[i][10] = filas[i][9];
            } else {
                double promedio = 0;
                for (int j = 0; j <= i; j++) {
                    promedio += (double) filas[j][9];
                }
                filas[i][10] = Math.rint((promedio / (i+1)) * 100)/100;
            }

        }
        
        for (int i = 1; i < totalClientes; i++) {
            
            if ((double) filas[i][2] >= 60*cont && (double) filas[i-1][2] <= 60*cont) {
                cont++;
            }
        }

        
        promedioClientes = totalClientes/cont;
//        System.out.println(cont + " " + promedioClientes);
        
        return filas;
    }
    
    // Panel que va a contener los datos necesarios para realizar el intervalo
    // de confianza de los tres incisos.
    public JPanel panelIC(){
        JPanel ic = new JPanel();
        DecimalFormat df = new DecimalFormat("####.##");
        // Un arreglo de arraylist, lol :v
        ArrayList[] incisos = {intA, intB, intC};
        double media = 0, desviacion = 0, limi = 0, lims = 0;
        String respuesta = "";
        String[] casos = {"a) Número promedio de clientes en el sistema:",
                            "b) Porcentaje de tiempo que el operador estuvo ocupado:",
                                "c) Tiempo promedio de permanencia en la fila:"};

        // Necesitamos hacer 3 veces el ciclo para contestar a los 3 puntos
        for(int i = 0; i < 3; i++){
            media = generarMedia(incisos[i]);
            desviacion = generarDesviacion(incisos[i], media);
            limi = generarLimit(incisos[i], media, desviacion, 0);
            lims = generarLimit(incisos[i], media, desviacion, 1);
            
            respuesta += "\n" + casos[i] + "\n\tIntervalo de confianza: {" + df.format(limi) + ", " + 
                            df.format(lims) + "} = " + (100-porcentaje) + "%\n";
        }
        
        JTextArea et = new JTextArea();
        et.setFont(new Font("Arial", Font.PLAIN, 14));
        et.setBackground(this.getBackground());
        et.setEditable(false);
        et.setText(respuesta);
        
        ic.add(et);
        
        return ic;
    }
    
    public double generarMedia(ArrayList<Double> inciso){
        double media = 0;
        
        for(Double i: inciso){
            media += i;
        }
        
        media = media / inciso.size();
        
        return media;
    }
    
    public double generarDesviacion(ArrayList<Double> inciso, double media){
        double desviacion = 0;
        double sumatoria = 0;
        
        for(Double i: inciso){
            sumatoria += Math.pow((i-media), 2);
        }
        
        sumatoria = sumatoria / (inciso.size()-1);
        
        desviacion = Math.sqrt(sumatoria);
        
        return desviacion;
    }
    
    public double generarLimit(ArrayList<Double> inciso, double media, double desviacion, int tipoLim){
//        1% es 4.604, 5% 2.776, 7% ni idea, 10% es 2.132
        double t = 0;
        double limit;
        // i es igual a corridas -1, sin embargo, la matriz comienza a contar en 0
        // así que la igualamos a corridas - 2 para compensarlo
        int i = corridas-2, j = 0;
        
        switch(porcentaje){
            case 1:
                j = 0;
                break;
                
            case 5:
                j = 1;
                break;
                
            case 10:
                j = 2;
                break;
        }
        
        double[][] tStudent = {
                        //     1%      5%      10%
                            {63.657, 12.706,  6.314},
                            { 9.925,  4.303,  2.920},
                            { 5.841,  3.182,  2.353},
                            { 4.604,  2.776,  2.132},
                            { 4.032,  2.571,  2.052},
                            { 3.707,  2.447,  1.943},
                            { 3.499,  2.365,  1.895},
                            { 3.355,  2.306,  1.860},
                            { 3.250,  2.262,  1.833},
                            { 3.169,  2.228,  1.812}
                            };
        
        t = tStudent[i][j];
//        System.out.println("t de student: " + t);
        
        // Si tipoLim == 0 se trata del límite inferior
        // Si tipoLim == 1 se trata del límite superior
        if(tipoLim == 0){
            limit = media - ((desviacion / Math.sqrt(inciso.size())) * t);
        
        }else{
            limit = media + ((desviacion / Math.sqrt(inciso.size())) * t);
        }
        return limit;
    }
    

    public final void addEventos(MiOyente oyente) {
        this.addWindowListener(oyente);
    }
}
