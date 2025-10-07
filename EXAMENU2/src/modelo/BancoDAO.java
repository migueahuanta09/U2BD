package modelo;

import java.sql.*;
import javax.swing.JOptionPane;

public class BancoDAO {
    private Connection conn;

    public BancoDAO() {
        conn = Conexion.getConnection();
    }
    
    public boolean registrarUsuario(String email, String nombre, String apellidos, String password) {
        try {
            CallableStatement stmt = conn.prepareCall("{CALL registrar_usuario(?, ?, ?, ?)}");
            stmt.setString(1, email);
            stmt.setString(2, nombre);
            stmt.setString(3, apellidos);
            stmt.setString(4, password);
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean abrirCuenta(int usuarioId, double saldoInicial) {
        try {
            CallableStatement stmt = conn.prepareCall("{CALL abrir_cuenta(?, ?)}");
            stmt.setInt(1, usuarioId);
            stmt.setDouble(2, saldoInicial);
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir cuenta: " + e.getMessage());
            return false;
        }
    }

    public boolean transferir(int cuentaEmisora, int cuentaReceptora, double monto, String nota) {
        try {
            CallableStatement stmt = conn.prepareCall("{CALL transferir(?, ?, ?, ?)}");
            stmt.setInt(1, cuentaEmisora);
            stmt.setInt(2, cuentaReceptora);
            stmt.setDouble(3, monto);
            stmt.setString(4, nota);
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "✅ Transferencia realizada con éxito.");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error en transferencia: " + e.getMessage());
            return false;
        }
    }

    public ResultSet obtenerHistorial(int cuentaId) {
        try {
            String sql = "SELECT * FROM transferencias WHERE cuenta_emisora = ? OR cuenta_receptora = ? ORDER BY fecha DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cuentaId);
            pstmt.setInt(2, cuentaId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar historial: " + e.getMessage());
            return null;
        }
    }

    public ResultSet obtenerListaUsuariosCompleta() {
        try {
            String sql = "SELECT " +
                        "u.id as usuario_id, " +
                        "c.id as cuenta_id, " +
                        "u.nombre, " +
                        "u.apellidos, " +
                        "u.email, " +
                        "COALESCE(c.saldo, 0) as saldo, " +
                        "u.fecha_registro " +
                        "FROM usuarios u " +
                        "LEFT JOIN cuentas c ON u.id = c.usuario_id " +
                        "ORDER BY u.id, c.id";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            System.out.println("Error al obtener lista de usuarios: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public double obtenerSaldo(int cuentaId) {
        try {
            String sql = "SELECT saldo FROM cuentas WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cuentaId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double saldo = rs.getDouble("saldo");
                rs.close();
                pstmt.close();
                return saldo;
            }
            rs.close();
            pstmt.close();
            return 0.0;
        } catch (SQLException e) {
            System.out.println("Error al obtener saldo: " + e.getMessage());
            return 0.0;
        }
    }

    public boolean existeCuenta(int cuentaId) {
        try {
            String sql = "SELECT id FROM cuentas WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cuentaId);
            ResultSet rs = pstmt.executeQuery();
            boolean existe = rs.next();
            rs.close();
            pstmt.close();
            return existe;
        } catch (SQLException e) {
            System.out.println("Error al verificar cuenta: " + e.getMessage());
            return false;
        }
    }

    public boolean existeUsuario(int usuarioId) {
        try {
            String sql = "SELECT id FROM usuarios WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            boolean existe = rs.next();
            rs.close();
            pstmt.close();
            return existe;
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            return false;
        }
    }

    public int obtenerIdCuentaPorUsuario(int usuarioId) {
        try {
            String sql = "SELECT id FROM cuentas WHERE usuario_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int cuentaId = rs.getInt("id");
                rs.close();
                pstmt.close();
                return cuentaId;
            }
            rs.close();
            pstmt.close();
            return -1;
        } catch (SQLException e) {
            System.out.println("Error al obtener ID de cuenta: " + e.getMessage());
            return -1;
        }
    }
}