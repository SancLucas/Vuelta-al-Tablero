package com.borboletta.vueltaaltablero.config;

/* Nombres de rol en un solo lugar. Spring les antepone "ROLE_" por su cuenta. */
public final class Roles{

    public static final String ADMINISTRADOR = "ADMIN";
    public static final String USUARIO = "USER";

    /* Así se llama el rol dentro de Spring Security (con el prefijo ROLE_). */
    public static final String AUTORIDAD_ADMINISTRADOR = "ROLE_" + ADMINISTRADOR;
    
    private Roles() { }
}
