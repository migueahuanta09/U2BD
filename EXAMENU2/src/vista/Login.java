package vista;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnEntrar, btnRegistrar;

    public Login() {
        setTitle("BancoDBB - Inicio de Sesión");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Título
        JLabel lblTitulo = new JLabel("🏦 Bienvenido a BancoDBB", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 70, 140));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(4, 1, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblEmail = new JLabel("Correo Electrónico:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(txtEmail);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(txtPassword);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnEntrar = new JButton("Entrar");
        btnRegistrar = new JButton("Registrarse");
        
        btnEntrar.setPreferredSize(new Dimension(120, 40));
        btnRegistrar.setPreferredSize(new Dimension(120, 40));
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelBotones.add(btnEntrar);
        panelBotones.add(btnRegistrar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acciones
        btnEntrar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        btnRegistrar.addActionListener(e -> {
            new Registro().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        // Versión simple sin LookAndFeel
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}