/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package primer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Primer extends JFrame {
    private JButton boton;
    private Random random;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private ArrayList<String> cartas;
    private JButton[] botones;
    private JButton primerBotonSeleccionado;
    private int paresRestantes;
    private boolean[] emparejadas;

    public Primer() {
        setTitle("Concentrese");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel(new GridLayout(4, 4));
        
        boton = new JButton("Empezar");
        boton.setBounds(100, 100, 100, 30);
        panel1.setLayout(null); // Habilita el diseño absoluto para panel1
        panel1.add(boton);
        random = new Random();

        boton.addActionListener(e -> realizarAccionCuandoAtrapado());

        Timer timer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverBotonAUbicacionAleatoria();
            }
        });
        timer.start();
        
        
        
        
        cardPanel.add(panel1, "Panel1");
        
        cartas = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            cartas.add(String.valueOf(i));
            cartas.add(String.valueOf(i));
        }

        Collections.shuffle(cartas);
        
        botones = new JButton[16];
        paresRestantes = 8;
        emparejadas = new boolean[16];
        Arrays.fill(emparejadas, false);

        for (int i = 0; i < 16; i++) {
            botones[i] = new JButton("Carta");
            botones[i].setFont(new Font("Arial", Font.PLAIN, 20));
            final int index = i;
            botones[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (botones[index].isEnabled() && !emparejadas[index]) {
                        voltearCarta(index);
                        if (primerBotonSeleccionado == null) {
                            primerBotonSeleccionado = botones[index];
                        } else {
                            int primerIndice = indexOf(primerBotonSeleccionado);
                            int segundoIndice = index;

                            if (primerIndice != -1) {
                                if (cartas.get(primerIndice).equals(cartas.get(segundoIndice))) {
                                    paresRestantes--;
                                    if (paresRestantes == 0) {
                                        JOptionPane.showMessageDialog(null, "¡Has ganado el juego!");
                                        reiniciarJuego();
                                    }
                                    emparejadas[primerIndice] = true;
                                    emparejadas[segundoIndice] = true;
                                    botones[index].setEnabled(false);
                                    botones[primerIndice].setEnabled(false);
                                    primerBotonSeleccionado = null;
                                } else {
                                    Timer timer = new Timer(500, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            voltearCarta(index);
                                            voltearCarta(primerIndice);
                                            primerBotonSeleccionado = null;
                                        }
                                    });
                                    timer.setRepeats(false);
                                    timer.start();
                                }
                            }
                        }
                    }
                }
            });
            panel2.add(botones[i]);
        }

        add(panel2);
        
        // Agrega cardPanel al JFrame
        add(cardPanel, BorderLayout.CENTER);
        cardPanel.add(panel2, "Panel2"); 
    }

    private void moverBotonAUbicacionAleatoria() {
        int newX = random.nextInt(cardPanel.getWidth() - boton.getWidth());
        int newY = random.nextInt(cardPanel.getHeight() - boton.getHeight());
        boton.setLocation(newX, newY);
    }

    private void realizarAccionCuandoAtrapado() {
        JOptionPane.showMessageDialog(this, "¡Atrapaste el botón!");
        cardLayout.next(cardPanel); 
    }
    private int indexOf(JButton button) {
        for (int i = 0; i < botones.length; i++) {
            if (botones[i] == button) {
                return i;
            }
        }
        return -1; // Devolver -1 si no se encuentra el botón
    }

    private void voltearCarta(int index) {
        if (!emparejadas[index]) { 
            if (botones[index].getText().equals("Carta")) {
                botones[index].setText(cartas.get(index));
            } else {
                botones[index].setText("Carta");
            }
        }
    }

    private void reiniciarJuego() {
         SwingUtilities.invokeLater(() -> {
            
            Primer nuevoJuego = new Primer();
            nuevoJuego.setVisible(true);
            dispose(); 
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Primer juego = new Primer();
            juego.setVisible(true);
        });
    }
}
