package com.borboletta.vueltaaltablero.exception;

import java.sql.SQLException;

public class DataAccessException extends RuntimeException {
    
    private final String sqlState;
    private final int vendorCode;
    
    public DataAccessException(String message) {
        super(message);
        this.sqlState = null;
        this.vendorCode = 0;
    }
    
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        if (cause instanceof java.sql.SQLException sqlEx) {
            this.sqlState = sqlEx.getSQLState();
            this.vendorCode = sqlEx.getErrorCode();
        } else {
            this.sqlState = null;
            this.vendorCode = 0;
        }
    }
    
    public DataAccessException(String message, String sqlState, int vendorCode) {
        super(message);
        this.sqlState = sqlState;
        this.vendorCode = vendorCode;
    }
    
    public String getSqlState() { return sqlState; }
    public int getVendorCode() { return vendorCode; }
    
    /**
     * Factory method para crear desde SQLException.
     * Mapea códigos de error MySQL comunes a mensajes amigables.
     */
    public static DataAccessException from(SQLException e) {
        String message = switch (e.getErrorCode()) {
            case 1062 -> "Error: Entrada duplicada (email ya existe)";
            case 1452 -> "Error: Violación de clave foránea";
            case 1048 -> "Error: Campo obligatorio nulo";
            case 0 -> e.getMessage();  // Error genérico
            default -> "Error de base de datos: " + e.getMessage();
        };
        return new DataAccessException(message, e);
    }
}
