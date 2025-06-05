package com.example.appSQL.controller;

import com.example.appSQL.model.Usuario;
import com.example.appSQL.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/")
    public String inicioSesion() {
        return "inicioSesion";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/miCuenta")
    public String miCuenta(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuario);

        return "miCuenta";
    }

    @GetMapping("/cerrarSesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/inicioSesion")
    public String procesarLogin(
            @RequestParam("email") String email,
            @RequestParam("contraseña") String contrasenya,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        Usuario usuario;

        if (email != null && contrasenya != null) {
            usuario = usuarioService.validarInicio(email, contrasenya);

            if (usuario != null) {
                session.setAttribute("usuario", usuario);
                System.out.println("Credenciales correctas");
                return "redirect:/shop";
            } else {
                redirectAttrs.addFlashAttribute("error", "Credenciales incorrectas");
                System.out.println("Credenciales incorrectas");
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(Usuario usuario, RedirectAttributes redirectAttrs) {

        if (usuarioService.validarRegistro(usuario)) {
            //Encriptamos la contraseña
            String contraseña = BCrypt.hashpw(usuario.getContrasenya(), BCrypt.gensalt());
            usuario.setContrasenya(contraseña);

            System.out.println("Registrando: " + usuario.getEmail());
            usuarioService.save(usuario);

            return "redirect:/";
        } else {
            redirectAttrs.addFlashAttribute("error", "El correo electrónico ya está registrado");
            System.out.println("Ese nombre de usuario ya está registrado");
            return "redirect:/registro";
        }
    }

    @PostMapping("/modificarDatos")
    public String modificarDatos(@RequestParam String nombre,
                                 @RequestParam String apellidos,
                                 @RequestParam String email,
                                 @RequestParam(required = false) String direccion,
                                 @RequestParam(name = "changePasswordCheckbox", defaultValue = "false") boolean changePasswordCheckbox,
                                 @RequestParam(required = false) String contrasenyaActual,
                                 @RequestParam(required = false) String contrasenyaNueva,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Actualizar la información básica del usuario (siempre se actualiza)
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);
        // Si direccion es null o vacío, lo puedes manejar aquí si quieres mantener la anterior
        // Por ejemplo: if (direccion != null && !direccion.isEmpty()) { usuario.setDireccion(direccion); }
        // O simplemente:
        usuario.setDireccion(direccion);

        // Verificar si el usuario ha marcado la opción de cambiar contraseña
        if (changePasswordCheckbox) {
            // Validar que los campos de contraseña no estén vacíos si el checkbox está marcado
            if (contrasenyaActual == null || contrasenyaActual.isEmpty() ||
                    contrasenyaNueva == null || contrasenyaNueva.isEmpty()) {
                redirectAttrs.addFlashAttribute("error", "Por favor, completa todos los campos de contraseña para cambiarla.");
                return "redirect:/miCuenta"; // Redirige a la página de mi cuenta
            }

            // Validar la contraseña actual
            if (BCrypt.checkpw(contrasenyaActual, usuario.getContrasenya())) {
                // Validar que la nueva contraseña no sea igual a la actual
                if (!contrasenyaNueva.equals(contrasenyaActual)) {
                    // Encriptar y establecer la nueva contraseña
                    String contraseñaEncriptada = BCrypt.hashpw(contrasenyaNueva, BCrypt.gensalt());
                    usuario.setContrasenya(contraseñaEncriptada);
                } else {
                    redirectAttrs.addFlashAttribute("error", "La nueva contraseña no puede ser igual a la actual.");
                    return "redirect:/miCuenta";
                }
            } else {
                redirectAttrs.addFlashAttribute("error", "La contraseña actual es errónea.");
                return "redirect:/miCuenta";
            }
        }

        // Guardar los cambios del usuario (ya sea con o sin cambio de contraseña)
        usuarioService.save(usuario);
        session.setAttribute("usuario", usuario); // Actualizar la sesión del usuario

        redirectAttrs.addFlashAttribute("success", "Perfil actualizado correctamente.");
        System.out.println("¡Actualizado correctamente!");
        return "redirect:/miCuenta"; // Redirige a la página de mi cuenta
    }

    @PostMapping("/eliminarCuenta")
    public String eliminarCuenta(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        usuarioService.deleteById(usuario.getId_usuario());
        System.out.println("Borrado correctamente");
        return "redirect:/";
    }


}
