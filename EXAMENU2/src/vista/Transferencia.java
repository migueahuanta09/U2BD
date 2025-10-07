package vista;

import modelo.BancoDAO;
import javax.swing.*;
import java.awt.*;

public class Transferencia extends JFrame {
    private JTextField txtEmisora, txtReceptora, txtMonto, txtNota;
    private JButton btnTransferir, btnVolver, btnVerCuentas;

    public Transferencia() {
        setTitle("Transferencia - BancoDBB");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Título
        JLabel lblTitulo = new JLabel("💸 Transferencia Bancaria", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 70, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de campos de formulario
        JPanel panelCampos = new JPanel(new GridLayout(5, 1, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Campo Cuenta Emisora
        JLabel lblEmisora = new JLabel("ID de Cuenta Emisora (Origen):");
        lblEmisora.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(lblEmisora);

        txtEmisora = new JTextField();
        txtEmisora.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(txtEmisora);

        // Campo Cuenta Receptora
        JLabel lblReceptora = new JLabel("ID de Cuenta Receptora (Destino):");
        lblReceptora.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(lblReceptora);

        txtReceptora = new JTextField();
        txtReceptora.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(txtReceptora);

        // Campo Monto
        JLabel lblMonto = new JLabel("Monto a Transferir:");
        lblMonto.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(lblMonto);

        JPanel panelMonto = new JPanel(new BorderLayout());
        txtMonto = new JTextField();
        txtMonto.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblSimbolo = new JLabel("$");
        lblSimbolo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSimbolo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        lblSimbolo.setForeground(Color.GRAY);
        
        panelMonto.add(lblSimbolo, BorderLayout.WEST);
        panelMonto.add(txtMonto, BorderLayout.CENTER);
        panelCampos.add(panelMonto);

        // Campo Nota
        JLabel lblNota = new JLabel("Nota (Opcional):");
        lblNota.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(lblNota);

        txtNota = new JTextField();
        txtNota.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(txtNota);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnVerCuentas = new JButton("📋 Ver Cuentas");
        btnVerCuentas.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVerCuentas.setPreferredSize(new Dimension(120, 35));
        btnVerCuentas.setBackground(new Color(255, 165, 0));
        btnVerCuentas.setForeground(Color.WHITE);
        
        btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setPreferredSize(new Dimension(100, 35));
        btnVolver.setBackground(new Color(200, 200, 200));
        btnVolver.setForeground(Color.BLACK);
        
        btnTransferir = new JButton("Realizar Transferencia");
        btnTransferir.setFont(new Font("Arial", Font.BOLD, 14));
        btnTransferir.setPreferredSize(new Dimension(180, 40));
        btnTransferir.setBackground(new Color(70, 160, 70));
        btnTransferir.setForeground(Color.WHITE);

        panelBotones.add(btnVerCuentas);
        panelBotones.add(btnVolver);
        panelBotones.add(btnTransferir);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acción del botón Ver Cuentas
        btnVerCuentas.addActionListener(e -> {
            new ListaUsuarios().setVisible(true);
        });

        // Acción del botón Transferir
        btnTransferir.addActionListener(e -> {
            if (validarCampos()) {
                int emisora = Integer.parseInt(txtEmisora.getText().trim());
                int receptora = Integer.parseInt(txtReceptora.getText().trim());
                double monto = Double.parseDouble(txtMonto.getText().trim());
                String nota = txtNota.getText().trim();

                // Verificar saldo suficiente
                BancoDAO dao = new BancoDAO();
                double saldoActual = dao.obtenerSaldo(emisora);
                
                if (saldoActual < monto) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Saldo insuficiente\n\n" +
                        "Saldo actual: $" + String.format("%.2f", saldoActual) + "\n" +
                        "Monto a transferir: $" + String.format("%.2f", monto) + "\n" +
                        "Faltan: $" + String.format("%.2f", (monto - saldoActual)),
                        "Saldo Insuficiente",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirmación
                int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Confirmar transferencia?\n\n" +
                    "De: Cuenta " + emisora + "\n" +
                    "Para: Cuenta " + receptora + "\n" +
                    "Monto: $" + String.format("%.2f", monto) +
                    (nota.isEmpty() ? "" : "\nNota: " + nota),
                    "Confirmar Transferencia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean exito = dao.transferir(emisora, receptora, monto, nota);

                    if (exito) {
                        JOptionPane.showMessageDialog(this,
                            "✅ Transferencia exitosa\n\n" +
                            "Monto: $" + String.format("%.2f", monto) + "\n" +
                            "De: Cuenta " + emisora + "\n" +
                            "Para: Cuenta " + receptora + "\n" +
                            "Nuevo saldo: $" + String.format("%.2f", dao.obtenerSaldo(emisora)),
                            "Transferencia Completada",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        limpiarCampos();
                    }
                }
            }
        });

        // Acción del botón Volver
        btnVolver.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });
    }

    private boolean validarCampos() {
        String emisoraText = txtEmisora.getText().trim();
        String receptoraText = txtReceptora.getText().trim();
        String montoText = txtMonto.getText().trim();

        if (emisoraText.isEmpty() || receptoraText.isEmpty() || montoText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Los campos Cuenta Emisora, Cuenta Receptora y Monto son obligatorios.",
                "Campos Incompletos",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int emisora = Integer.parseInt(emisoraText);
            int receptora = Integer.parseInt(receptoraText);
            double monto = Double.parseDouble(montoText);

            if (emisora == receptora) {
                JOptionPane.showMessageDialog(this,
                    "❌ La cuenta emisora y receptora no pueden ser la misma.",
                    "Cuentas Iguales",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (emisora <= 0 || receptora <= 0) {
                JOptionPane.showMessageDialog(this,
                    "❌ Los IDs de cuenta deben ser positivos.",
                    "IDs Inválidos",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (monto <= 0) {
                JOptionPane.showMessageDialog(this,
                    "❌ El monto debe ser positivo.",
                    "Monto Inválido",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Verificar que las cuentas existan
            BancoDAO dao = new BancoDAO();
            if (!dao.existeCuenta(emisora)) {
                JOptionPane.showMessageDialog(this,
                    "❌ La cuenta emisora " + emisora + " no existe.",
                    "Cuenta No Encontrada",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!dao.existeCuenta(receptora)) {
                JOptionPane.showMessageDialog(this,
                    "❌ La cuenta receptora " + receptora + " no existe.",
                    "Cuenta No Encontrada",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Ingrese valores numéricos válidos.",
                "Error de Formato",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtMonto.setText("");
        txtNota.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Transferencia().setVisible(true);
        });
    }
}