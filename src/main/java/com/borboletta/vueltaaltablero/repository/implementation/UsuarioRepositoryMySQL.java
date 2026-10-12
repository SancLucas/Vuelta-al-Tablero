package com.borboletta.vueltaaltablero.repository.implementation;

import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.exception.DataAccessException;
import com.borboletta.vueltaaltablero.config.DatabaseConfig;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryMySQL implements UsuarioRepository {

     static {
        DatabaseConfig.loadDriver();
    }
    
    // ============================================
    // SQL CONSTANTS
    // ============================================
    private static final String SQL_SAVE = 
        "INSERT INTO usuario (nombre, apellido, dni, correo, contrasenia, telefono, fecha_registro, rol_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_FIND_BY_ID = 
        "SELECT nro, nombre, apellido, dni, correo, contrasenia, telefono, fecha_registro FROM usuario WHERE nro = ?";
    
    private static final String SQL_FIND_ALL = 
        "SELECT nro, nombre, apellido, dni, correo, contrasenia, telefono, fecha_registro FROM usuario ORDER BY nro";
    
    private static final String SQL_UPDATE = 
        "UPDATE usuario SET nombre = ?, apellido = ?, dni = ?, correo = ?, contrasenia = ?, telefono = ?, fecha_registro = ?, updated_at = CURRENT_TIMESTAMP WHERE nro = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM usuario WHERE nro = ?";
    
    private static final String SQL_EXISTS = 
        "SELECT 1 FROM usuario WHERE nro = ?";
    
    // ============================================
    // CONEXIÓN Y UTILIDADES
    // ============================================
    

    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }
    
    private Usuario mapRow(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getLong("nro"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("dni"),
            rs.getString("correo"),
            rs.getString("contrasenia"),
            rs.getString("telefono"),
            rs.getObject("fecha_registro", LocalDate.class),
            rs.getBoolean("rol_admin")
        );
    }
    
    // ============================================
    // CRUD - AUTO-COMMIT (Simple, una operación = una transacción)
    // ============================================
    
    @Override
    public Usuario save(Usuario usuario) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getDni());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getTelefono());
            ps.setObject(7, usuario.getFecha_registro());
            ps.setBoolean(8, usuario.isRol_admin());
            
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Inserción falló, ninguna fila afectada");
            }
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getLong(1));
                } else {
                    throw new RuntimeException("No se generó ID auto-incremental");
                }
            }
            return usuario;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Optional<Usuario> findById(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Usuario> findAll() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
            return usuarios;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean deleteById(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Usuario update(Usuario usuario) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getDni());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getTelefono());
            ps.setObject(7, usuario.getFecha_registro());
            ps.setBoolean(8, usuario.isRol_admin());
            
            int affected = ps.executeUpdate();
            if (affected == 0) {
                return null;  // No encontrado
            }
            return usuario;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    // ============================================
    // CRUD - TRANSACCIONES MANUALES (Operaciones compuestas)
    // ============================================
    
    /**
     * Ejemplo: Transferir usuario entre sistemas (operación atómica múltiple).
     * Requiere deshabilitar auto-commit y manejar commit/rollback manual.
     */
    public Usuario saveWithAudit(Usuario usuario, String auditAction) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);  // INICIAR TRANSACCIÓN
            
            // 1. Insertar usuario
            Long generatedId;
            try (PreparedStatement ps = conn.prepareStatement(
                    SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getApellido());
                ps.setString(3, usuario.getDni());
                ps.setString(4, usuario.getCorreo());
                ps.setString(5, usuario.getContrasenia());
                ps.setString(6, usuario.getTelefono());
                ps.setObject(7, usuario.getFecha_registro());
                ps.setBoolean(8, usuario.isRol_admin());
                ps.executeUpdate();
                
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedId = keys.getLong(1);
                        usuario.setId(generatedId);
                    } else {
                        throw new RuntimeException("No se generó ID");
                    }
                }
            }
            
            // 2. Insertar auditoría (tabla hipotética audit_log)
            String sqlAudit = "INSERT INTO audit_log (action, entity_id, entity_type) VALUES (?, ?, 'USER')";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudit)) {
                ps.setString(1, auditAction);
                ps.setLong(2, generatedId);
                ps.executeUpdate();
            }
            
            conn.commit();  // CONFIRMAR TRANSACCIÓN
            return usuario;
            
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
        }
    }
    
    /**
     * Ejemplo: Actualizar usuario + log de cambios (transacción manual).
     */
    public boolean updateWithLog(Usuario usuario, String changedBy) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // 1. Actualizar usuario
            int affected;
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getApellido());
                ps.setString(3, usuario.getDni());
                ps.setString(4, usuario.getCorreo());
                ps.setString(5, usuario.getContrasenia());
                ps.setString(6, usuario.getTelefono());
                affected = ps.executeUpdate();
            }
            
            if (affected == 0) {
                conn.rollback();
                return false;
            }
            
            // 2. Log de cambios
            String sqlLog = "INSERT INTO user_changes (user_id, changed_by, change_date) VALUES (?, ?, NOW())";
            try (PreparedStatement ps = conn.prepareStatement(sqlLog)) {
                ps.setLong(1, usuario.getNro());
                ps.setString(2, changedBy);
                ps.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
        }
    }
    
    // ============================================
    // HELPERS PARA TRANSACCIONES MANUALES
    // ============================================
    
    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
    }
    
    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try { 
                conn.setAutoCommit(true);  // Restaurar default
                conn.close(); 
            } catch (SQLException ignored) {}
        }
    }
}