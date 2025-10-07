package vista;

import modelo.BancoDAO;
import javax.swing.*;
import java.awt.*;

public class AbrirCuenta extends JFrame {
    private JTextField txtUsuarioId, txtSaldoInicial;
    private JButton btnAbrir, btnVolver;

    public AbrirCuenta() {
        setTitle("Abrir Nueva Cuenta - BancoDBB");
        setSize(500, 400); // Aumenté la altura para que quepa todo
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // Título
        JLabel lblTitulo = new JLabel("📁 Abrir Nueva Cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 70, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de campos - Reducido a 2 filas
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 15, 20));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Campo ID Usuario
        JLabel lblUsuarioId = new JLabel("ID del Usuario:");
        lblUsuarioId.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(lblUsuarioId);

        txtUsuarioId = new JTextField();
        txtUsuarioId.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuarioId.setPreferredSize(new Dimension(200, 30));
        panelCampos.add(txtUsuarioId);

        // Campo Saldo Inicial
        JLabel lblSaldoInicial = new JLabel("Saldo Inicial:");
        lblSaldoInicial.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(lblSaldoInicial);

        JPanel panelMonto = new JPanel(new BorderLayout(5, 0));
        txtSaldoInicial = new JTextField();
        txtSaldoInicial.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSaldoInicial.setPreferredSize(new Dimension(200, 30));
        
        JLabel lblSimbolo = new JLabel("$");
        lblSimbolo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSimbolo.setForeground(Color.GRAY);
        
        panelMonto.add(lblSimbolo, BorderLayout.WEST);
        panelMonto.add(txtSaldoInicial, BorderLayout.CENTER);
        panelCampos.add(panelMonto);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Panel de botones - Más compacto
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 13));
        btnVolver.setPreferredSize(new Dimension(120, 35));
        btnVolver.setBackground(new Color(200, 200, 200));
        btnVolver.setForeground(Color.BLACK);
        
        btnAbrir = new JButton("Abrir Cuenta");
        btnAbrir.setPreferredSize(new Dimension(140, 40));
        btnAbrir.setFont(new Font("Arial", Font.BOLD, 14));
        btnAbrir.setBackground(new Color(0, 120, 215));
        btnAbrir.setForeground(Color.WHITE);

        panelBoton.add(btnVolver);
        panelBoton.add(btnAbrir);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acción del botón con manejo de errores
        btnAbrir.addActionListener(e -> {
            if (validarCampos()) {
                try {
                    int id = Integer.parseInt(txtUsuarioId.getText().trim());
                    double saldo = Double.parseDouble(txtSaldoInicial.getText().trim());

                    BancoDAO dao = new BancoDAO();
                    if (dao.abrirCuenta(id, saldo)) {
                        JOptionPane.showMessageDialog(this, 
                            "✅ Cuenta creada correctamente.\n\n" +
                            "ID Usuario: " + id + "\n" +
                            "Saldo Inicial: $" + String.format("%.2f", saldo),
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "❌ Error al crear la cuenta.\n\n" +
                            "Verifique que:\n" +
                            "• El ID de usuario exista\n" +
                            "• El usuario no tenga ya una cuenta",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Error inesperado: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón Volver
        btnVolver.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        // Hacer visible al final
        setVisible(true);
    }

    // Método para validar los campos
    private boolean validarCampos() {
        String idText = txtUsuarioId.getText().trim();
        String saldoText = txtSaldoInicial.getText().trim();

        // Validar campos vacíos
        if (idText.isEmpty() || saldoText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Todos los campos son obligatorios.", 
                "Campos vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int id = Integer.parseInt(idText);
            double saldo = Double.parseDouble(saldoText);

            // Validar que el ID sea positivo
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "❌ El ID de usuario debe ser un número positivo.", 
                    "ID inválido", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validar que el saldo no sea negativo
            if (saldo < 0) {
                JOptionPane.showMessageDialog(this, 
                    "❌ El saldo inicial no puede ser negativo.", 
                    "Saldo inválido", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validar monto mínimo
            if (saldo < 0.01) {
                JOptionPane.showMessageDialog(this, 
                    "❌ El saldo mínimo es $0.01.", 
                    "Saldo muy pequeño", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Por favor, ingrese valores numéricos válidos:\n\n" +
                "• ID Usuario: número entero\n" +
                "• Saldo: número decimal", 
                "Error de formato", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // Método para limpiar los campos después de una operación exitosa
    private void limpiarCampos() {
        txtUsuarioId.setText("");
        txtSaldoInicial.setText("");
        txtUsuarioId.requestFocus();
    }

    // Método main para probar la ventana individualmente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AbrirCuenta();
        });
    }
}