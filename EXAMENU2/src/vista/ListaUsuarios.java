package vista;

import modelo.BancoDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ListaUsuarios extends JFrame {
    private JTextArea txtListaUsuarios;
    private JButton btnActualizar, btnVolver;

    public ListaUsuarios() {
        setTitle("Lista de Usuarios - BancoDBB");
        setSize(1000, 600); // Ventana más ancha
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Título
        JLabel lblTitulo = new JLabel("👥 Lista de Usuarios y Cuentas - BancoDBB", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 70, 140));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnActualizar = new JButton("🔄 Actualizar Lista");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.setPreferredSize(new Dimension(180, 35));
        btnActualizar.setBackground(new Color(0, 120, 215));
        btnActualizar.setForeground(Color.WHITE);

        btnVolver = new JButton("← Volver al Menú");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVolver.setPreferredSize(new Dimension(150, 35));
        btnVolver.setBackground(new Color(200, 200, 200));
        btnVolver.setForeground(Color.BLACK);

        panelControles.add(btnActualizar);
        panelControles.add(btnVolver);
        panelPrincipal.add(panelControles, BorderLayout.NORTH);

        // Área de texto para mostrar la lista
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Usuarios Registrados y sus Cuentas"
        ));

        txtListaUsuarios = new JTextArea();
        txtListaUsuarios.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtListaUsuarios.setEditable(false);
        txtListaUsuarios.setBackground(new Color(248, 248, 248));
        
        JScrollPane scroll = new JScrollPane(txtListaUsuarios);
        scroll.setPreferredSize(new Dimension(950, 400));
        panelLista.add(scroll, BorderLayout.CENTER);

        panelPrincipal.add(panelLista, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblInfo = new JLabel("💡 Use los IDs de CUENTA para realizar transferencias");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        panelInferior.add(lblInfo);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acción del botón Actualizar
        btnActualizar.addActionListener(e -> {
            cargarListaUsuarios();
        });

        // Acción del botón Volver
        btnVolver.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        // Cargar lista automáticamente al abrir la ventana
        cargarListaUsuarios();
    }

    // Método para cargar la lista de usuarios
    private void cargarListaUsuarios() {
        try {
            BancoDAO dao = new BancoDAO();
            ResultSet rs = dao.obtenerListaUsuariosCompleta();

            StringBuilder lista = new StringBuilder();
            int totalUsuarios = 0;
            int totalCuentas = 0;
            double saldoTotal = 0.0;

            if (rs != null) {
                // Encabezado de la tabla
                lista.append("┌────────┬────────────┬──────────────────┬──────────────────┬──────────────────────┬──────────────┬─────────────────────┐\n");
                lista.append("│ ID User│ ID Cuenta  │      NOMBRE      │    APELLIDOS     │        EMAIL         │    SALDO     │   FECHA REGISTRO    │\n");
                lista.append("├────────┼────────────┼──────────────────┼──────────────────┼──────────────────────┼──────────────┼─────────────────────┤\n");

                try {
                    while (rs.next()) {
                        totalUsuarios++;
                        int usuarioId = rs.getInt("usuario_id");
                        int cuentaId = rs.getInt("cuenta_id");
                        String nombre = rs.getString("nombre");
                        String apellidos = rs.getString("apellidos");
                        String email = rs.getString("email");
                        double saldo = rs.getDouble("saldo");
                        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
                        
                        // Formatear fecha
                        String fechaFormateada = "";
                        if (fechaRegistro != null) {
                            fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").format(fechaRegistro);
                        }
                        
                        // Contar cuentas y saldo
                        if (cuentaId > 0) {
                            totalCuentas++;
                            saldoTotal += saldo;
                        }
                        
                        // Formatear los datos para mejor presentación
                        String nombreFormateado = formatoTexto(nombre, 16);
                        String apellidosFormateado = formatoTexto(apellidos, 16);
                        String emailFormateado = formatoTexto(email, 20);
                        
                        lista.append(String.format("│ %-6d │ %-10d │ %-16s │ %-16s │ %-20s │ $%-11.2f │ %-19s │\n",
                            usuarioId,
                            cuentaId,
                            nombreFormateado,
                            apellidosFormateado,
                            emailFormateado,
                            saldo,
                            fechaFormateada
                        ));
                    }
                    
                    if (totalUsuarios > 0) {
                        lista.append("└────────┴────────────┴──────────────────┴──────────────────┴──────────────────────┴──────────────┴─────────────────────┘\n");
                        
                        // Resumen estadístico
                        lista.append("\n📊 RESUMEN ESTADÍSTICO:\n");
                        lista.append("├────────────────────────────────────────────────────────────────────────────────────────┤\n");
                        lista.append(String.format("│ 👥 Total de usuarios registrados: %-63d │\n", totalUsuarios));
                        lista.append(String.format("│ 🏦 Total de cuentas activas: %-66d │\n", totalCuentas));
                        lista.append(String.format("│ 💰 Saldo total en el sistema: $%-60.2f │\n", saldoTotal));
                        lista.append(String.format("│ 📈 Saldo promedio por cuenta: $%-58.2f │\n", 
                            totalCuentas > 0 ? saldoTotal / totalCuentas : 0));
                        lista.append("└────────────────────────────────────────────────────────────────────────────────────────┘\n");
                    } else {
                        lista.setLength(0);
                        lista.append("📭 No hay usuarios registrados en el sistema.\n");
                    }
                    
                } catch (SQLException ex) {
                    lista.setLength(0);
                    lista.append("❌ Error al leer los datos: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } finally {
                    try {
                        if (rs != null) rs.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                lista.append("❌ No se pudieron cargar los datos de usuarios.\n");
            }

            txtListaUsuarios.setText(lista.toString());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error al cargar la lista de usuarios: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Método auxiliar para formatear texto
    private String formatoTexto(String texto, int longitudMaxima) {
        if (texto == null) return "";
        if (texto.length() > longitudMaxima) {
            return texto.substring(0, longitudMaxima - 3) + "...";
        }
        return texto;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ListaUsuarios().setVisible(true);
        });
    }
}