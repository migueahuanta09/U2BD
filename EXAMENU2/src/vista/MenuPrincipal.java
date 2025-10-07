package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Menú Principal - BancoDBB");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Título principal
        JLabel lblTitulo = new JLabel("BancoDBB - Menú Principal", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(0, 70, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 15, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Botones
        JButton btnAbrirCuenta = crearBotonGrande("Abrir Cuenta", new Color(0, 120, 215));
        JButton btnTransferir = crearBotonGrande("Transferir Dinero", new Color(70, 160, 70));
        JButton btnHistorial = crearBotonGrande("Ver Historial", new Color(255, 140, 0));
        JButton btnListaUsuarios = crearBotonGrande("Lista de Usuarios", new Color(147, 112, 219));
        JButton btnSalir = crearBotonGrande("Cerrar Sesión", new Color(200, 60, 60));

        panelBotones.add(btnAbrirCuenta);
        panelBotones.add(btnTransferir);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnListaUsuarios);
        panelBotones.add(btnSalir);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblInfo = new JLabel("Seleccione una opción para continuar");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 14));
        lblInfo.setForeground(Color.GRAY);
        panelInferior.add(lblInfo);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acciones de los botones
        btnAbrirCuenta.addActionListener(e -> {
            new AbrirCuenta().setVisible(true);
            this.setVisible(false);
        });

        btnTransferir.addActionListener(e -> {
            new Transferencia().setVisible(true);
            this.setVisible(false);
        });

        btnHistorial.addActionListener(e -> {
            new Historial().setVisible(true);
            this.setVisible(false);
        });

        btnListaUsuarios.addActionListener(e -> {
            new ListaUsuarios().setVisible(true);
            this.setVisible(false);
        });

        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                new Login().setVisible(true);
                dispose();
            }
        });
    }

    private JButton crearBotonGrande(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setPreferredSize(new Dimension(450, 60));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorFondo.darker(), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}