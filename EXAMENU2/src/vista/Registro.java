package vista;

import modelo.BancoDAO;
import javax.swing.*;
import java.awt.*;

public class Registro extends JFrame {
    private JTextField txtEmail, txtNombre, txtApellidos;
    private JPasswordField txtPassword;
    private JButton btnRegistrar, btnVolver;

    public Registro() {
        setTitle("Registro - BancoDBB");
        setSize(600, 500); // Ventana más grande
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Título
        JLabel lblTitulo = new JLabel("👤 Registro de Usuario - BancoDBB", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 70, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de campos de formulario
        JPanel panelCampos = new JPanel(new GridLayout(8, 1, 10, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Campo Email
        JLabel lblEmail = new JLabel("Correo Electrónico:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setPreferredSize(new Dimension(400, 35));
        panelCampos.add(txtEmail);

        // Campo Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(400, 35));
        panelCampos.add(txtNombre);

        // Campo Apellidos
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblApellidos);

        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Arial", Font.PLAIN, 16));
        txtApellidos.setPreferredSize(new Dimension(400, 35));
        panelCampos.add(txtApellidos);

        // Campo Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setPreferredSize(new Dimension(400, 35));
        panelCampos.add(txtPassword);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnVolver = new JButton("← Volver al Login");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVolver.setPreferredSize(new Dimension(150, 40));
        btnVolver.setBackground(new Color(200, 200, 200));
        btnVolver.setForeground(Color.BLACK);
        
        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setPreferredSize(new Dimension(180, 45));
        btnRegistrar.setBackground(new Color(70, 160, 70));
        btnRegistrar.setForeground(Color.WHITE);

        panelBotones.add(btnVolver);
        panelBotones.add(btnRegistrar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> {
            if (validarCampos()) {
                BancoDAO dao = new BancoDAO();
                boolean ok = dao.registrarUsuario(
                    txtEmail.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellidos.getText().trim(),
                    new String(txtPassword.getPassword())
                );

                if (ok) {
                    JOptionPane.showMessageDialog(this, 
                        "✅ Usuario registrado con éxito.\nAhora puede iniciar sesión con sus credenciales.",
                        "Registro Exitoso", 
                        JOptionPane.INFORMATION_MESSAGE);
                    new Login().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Error al registrar el usuario.\nEl email podría estar ya en uso.",
                        "Error de Registro", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón Volver
        btnVolver.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
    }

    // Método para validar los campos del formulario
    private boolean validarCampos() {
        String email = txtEmail.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validar campos vacíos
        if (email.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Todos los campos son obligatorios.", 
                "Campos Incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar formato de email básico
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Por favor, ingrese un email válido.", 
                "Email Inválido", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar longitud mínima de contraseña
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ La contraseña debe tener al menos 6 caracteres.", 
                "Contraseña Demasiado Corta", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar que nombre y apellidos solo contengan letras y espacios
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") || !apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ El nombre y apellidos solo pueden contener letras y espacios.", 
                "Caracteres Inválidos", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}