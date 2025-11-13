package entities;

import java.time.LocalDateTime;

/**
 * Entidad que representa la Credencial de Acceso (Clase B).
 * Mantiene la relación 1-a-1 con Usuario.
 */
public class CredencialAcceso {

    // --- Atributos ---
    private long id;
    private boolean eliminado;
    private String hashPassword; // Según consigna 
    private String salt;         // Según consigna 
    private LocalDateTime ultimoCambio; // Según consigna 
    
    // --- CAMPO FALTANTE (PUNTO 3) ---
    private Boolean requiereReset; // Según consigna 

    // --- Constructores ---
    public CredencialAcceso() {
        // Constructor vacío
    }

    // Constructor completo (útil para crear el objeto desde el AppMenu)
    public CredencialAcceso(String hashPassword, String salt) {
        this.hashPassword = hashPassword;
        this.salt = salt;
        this.eliminado = false; // Valor por defecto
        this.requiereReset = false; // Valor por defecto
    }

    // --- Getters y Setters ---
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public LocalDateTime getUltimoCambio() {
        return ultimoCambio;
    }

    public void setUltimoCambio(LocalDateTime ultimoCambio) {
        this.ultimoCambio = ultimoCambio;
    }

    // --- Métodos GET/SET para el campo agregado ---
    
    public Boolean getRequiereReset() {
        return requiereReset;
    }

    public void setRequiereReset(Boolean requiereReset) {
        this.requiereReset = requiereReset;
    }

    // --- toString (para debugging) ---
    
    @Override
    public String toString() {
        return "CredencialAcceso{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", hashPassword='[PROTEGIDO]'" +
                ", ultimoCambio=" + ultimoCambio +
                ", requiereReset=" + requiereReset +
                '}';
    }
}