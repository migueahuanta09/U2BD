package vista;

import modelo.BancoDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Historial extends JFrame {
    private JTextField txtCuentaId;
    private JTextArea txtHistorial;
    private JButton btnConsultar;

    public Historial() {
        setTitle("Historial de Movimientos - BancoDBB");
        setSize(700, 500); // Ventana más grande
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Título
        JLabel lblTitulo = new JLabel("📊 Historial de Movimientos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 70, 140));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel superior de búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel lblCuentaId = new JLabel("Número de Cuenta:");
        lblCuentaId.setFont(new Font("Arial", Font.PLAIN, 16));
        panelSuperior.add(lblCuentaId);

        txtCuentaId = new JTextField(12);
        txtCuentaId.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCuentaId.setPreferredSize(new Dimension(120, 35));
        panelSuperior.add(txtCuentaId);

        btnConsultar = new JButton("Consultar Historial");
        btnConsultar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConsultar.setPreferredSize(new Dimension(180, 35));
        btnConsultar.setBackground(new Color(0, 120, 215));
        btnConsultar.setForeground(Color.WHITE);
        panelSuperior.add(btnConsultar);

        panelPrincipal.add(panelSuperior, BorderLayout.CENTER);

        // Área de texto para historial
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Movimientos de la Cuenta"
        ));

        txtHistorial = new JTextArea();
        txtHistorial.setFont(new Font("Consolas", Font.PLAIN, 14)); // Fuente monoespaciada para mejor alineación
        txtHistorial.setEditable(false);
        txtHistorial.setBackground(new Color(248, 248, 248));
        
        JScrollPane scroll = new JScrollPane(txtHistorial);
        scroll.setPreferredSize(new Dimension(650, 300));
        panelHistorial.add(scroll, BorderLayout.CENTER);

        panelPrincipal.add(panelHistorial, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acción del botón con manejo de errores
        btnConsultar.addActionListener(e -> {
            try {
                String cuentaIdText = txtCuentaId.getText().trim();
                
                if (cuentaIdText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Por favor, ingrese un número de cuenta.", 
                        "Campo vacío", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int cuentaId = Integer.parseInt(cuentaIdText);
                
                if (cuentaId <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ El número de cuenta debe ser un valor positivo.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BancoDAO dao = new BancoDAO();
                ResultSet rs = dao.obtenerHistorial(cuentaId);

                StringBuilder historial = new StringBuilder();
                boolean tieneMovimientos = false;

                if (rs != null) {
                    try {
                        while (rs.next()) {
                            tieneMovimientos = true;
                            historial.append(String.format("│ %-4d │ %-12d │ %-13d │ %-10.2f │ %-19s │\n",
                                rs.getInt("id"),
                                rs.getInt("cuenta_emisora"),
                                rs.getInt("cuenta_receptora"),
                                rs.getDouble("monto"),
                                rs.getString("fecha")
                            ));
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, 
                            "❌ Error al leer los datos: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (tieneMovimientos) {
                    // Encabezado de la tabla
                    String encabezado = String.format("┌──────┬──────────────┬───────────────┬────────────┬─────────────────────┐\n" +
                                                     "│ %-4s │ %-12s │ %-13s │ %-10s │ %-19s │\n" +
                                                     "├──────┼──────────────┼───────────────┼────────────┼─────────────────────┤\n",
                                                     "ID", "Cuenta Emisora", "Cuenta Receptora", "Monto", "Fecha");
                    String pie = "└──────┴──────────────┴───────────────┴────────────┴─────────────────────┘\n";
                    
                    txtHistorial.setText(encabezado + historial.toString() + pie);
                } else {
                    txtHistorial.setText("📭 No se encontraron movimientos para la cuenta: " + cuentaId);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Por favor, ingrese un número de cuenta válido (solo números enteros).", 
                    "Error de formato", 
                    JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error inesperado: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}