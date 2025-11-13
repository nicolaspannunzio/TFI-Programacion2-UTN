package main;

import entities.Usuario;
import entities.CredencialAcceso;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import service.UsuarioService;

/**
 * Capa de "Frontend" (Consola).
 * Interactúa con el usuario y llama al Service.
 * Maneja las entradas del usuario y la visualización de errores.
 */
public class AppMenu {

    private final Scanner scanner;
    private final UsuarioService usuarioService;

    public AppMenu() {
        scanner = new Scanner(System.in);
        // Instancia el servicio que usará toda la aplicación
        usuarioService = new UsuarioService(); 
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Crear nuevo usuario");
            System.out.println("2. Listar usuarios");
            System.out.println("3. Buscar usuario por ID");
            System.out.println("4. Actualizar usuario");
            System.out.println("5. Eliminar usuario (baja logica)");
            System.out.println("6. Buscar usuario por Username (TFI)"); // NUEVO (Punto 5)
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> listarUsuarios();
                case 3 -> buscarUsuarioPorId();
                case 4 -> actualizarUsuario();
                case 5 -> eliminarUsuario();
                case 6 -> buscarUsuarioPorUsername(); // NUEVO (Punto 5)
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void crearUsuario() {
        try {
            System.out.println("\n--- Crear nuevo usuario ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            // Simulación de ingreso de contraseña
            System.out.print("Hash Password: "); 
            String hash = scanner.nextLine();
            System.out.print("Salt: ");
            String salt = scanner.nextLine();

            // 1. Crear Credencial (Entidad B)
            CredencialAcceso cred = new CredencialAcceso();
            cred.setHashPassword(hash);
            cred.setSalt(salt);
            cred.setRequiereReset(false); // Campo del Punto 3
            cred.setEliminado(false);

            // 2. Crear Usuario (Entidad A)
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setEmail(email);
            u.setActivo(true); // Se activa por defecto
            u.setEliminado(false);
            
            // 3. Vincular A -> B
            u.setCredencial(cred);

            // 4. Llamar al Service
            usuarioService.insertar(u);
            System.out.println("Usuario creado con exito.");

        // CORRECTO (Punto 5): Capturamos ambos tipos de errores
        } catch (IllegalArgumentException | SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        try {
            System.out.println("\n--- Lista de usuarios ---");
            List<Usuario> usuarios = usuarioService.getAll();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios registrados.");
            } else {
                for (Usuario u : usuarios) {
                    // El toString() de Usuario debe estar bien definido
                    System.out.println(u); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
    }

    private void buscarUsuarioPorId() {
        try {
            System.out.print("\nIngrese el ID del usuario: ");
            long id = leerLong();
            Usuario u = usuarioService.getById(id);
            if (u != null) {
                System.out.println(u);
            } else {
                System.out.println("Usuario no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
    }

    // NUEVO (Punto 5): Método para cumplir requisito TFI
    private void buscarUsuarioPorUsername() {
        // Esta funcionalidad no la programamos en el DAO/Service, 
        // ¡pero podemos simularla!
        // Simplemente listamos todos y filtramos en Java.
        try {
            System.out.print("\nIngrese el Username a buscar: ");
            String usernameBusqueda = scanner.nextLine();
            
            List<Usuario> usuarios = usuarioService.getAll();
            
            Usuario encontrado = null;
            for(Usuario u : usuarios) {
                if(u.getUsername().equalsIgnoreCase(usernameBusqueda)) {
                    encontrado = u;
                    break;
                }
            }
            
            if (encontrado != null) {
                System.out.println("Usuario encontrado:");
                System.out.println(encontrado);
            } else {
                System.out.println("Usuario no encontrado.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
    }


    private void actualizarUsuario() {
        try {
            System.out.print("\nIngrese el ID del usuario a actualizar: ");
            long id = leerLong();
            
            // 1. Traer el usuario y su credencial
            Usuario u = usuarioService.getById(id);
            if (u == null) {
                System.out.println("Usuario no encontrado.");
                return;
            }
            
            // Traer la credencial completa (no está implementado, así que la simulamos)
            // En un caso real, necesitaríamos un credencialService.getById()
            // Por ahora, solo creamos una nueva si la actualiza.
            System.out.println("Editando usuario: " + u.getUsername());

            // 2. Pedir nuevos datos
            System.out.print("Nuevo email (actual: " + u.getEmail() + "): ");
            u.setEmail(scanner.nextLine());
            
            System.out.print("Activo? (true/false) (actual: " + u.isActivo() + "): ");
            u.setActivo(Boolean.parseBoolean(scanner.nextLine()));
            
            System.out.print("¿Desea cambiar la contraseña? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Nuevo Hash Password: ");
                String hash = scanner.nextLine();
                System.out.print("Nuevo Salt: ");
                String salt = scanner.nextLine();
                
                CredencialAcceso cred = u.getCredencial(); // Reutilizamos la credencial existente
                cred.setHashPassword(hash);
                cred.setSalt(salt);
                cred.setUltimoCambio(LocalDateTime.now());
                u.setCredencial(cred);
            }

            // 3. Llamar al Service
            usuarioService.actualizar(u);
            System.out.println("Usuario actualizado con exito.");

        // CORRECTO (Punto 5): Capturamos ambos tipos de errores
        } catch (IllegalArgumentException | SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            System.out.print("\nIngrese el ID del usuario a eliminar: ");
            long id = leerLong();
            usuarioService.eliminar(id);
            System.out.println("Usuario eliminado (baja logica).");
            
        // CORRECTO (Punto 5): Capturamos ambos tipos de errores
        } catch (IllegalArgumentException | SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    /**
     * Método helper robusto para leer un Entero.
     * Vuelve a pedir el número si el formato es incorrecto.
     */
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }
        }
    }

    /**
     * Método helper robusto para leer un Long (IDs).
     * Vuelve a pedir el número si el formato es incorrecto.
     */
    private long leerLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }
        }
    }
}