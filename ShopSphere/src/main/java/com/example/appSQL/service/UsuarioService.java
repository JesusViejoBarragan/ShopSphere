package com.example.appSQL.service;

import com.example.appSQL.model.Usuario;
import com.example.appSQL.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario validarInicio(String email, String contrasenya) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && BCrypt.checkpw(contrasenya, usuario.getContrasenya())) {
            System.out.println("entra");
            return usuario;
        }
        return null;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Integer id_usuario){
        return usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Boolean validarRegistro(Usuario usuario){
        Usuario user = usuarioRepository.findByEmail(usuario.getEmail());

        if(user != null){
            return false;
        }else{
            return true;
        }
    }

    public ResponseEntity<?> deleteById(Integer id) {
        try {
            usuarioRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuario eliminado satisfactoriamente: " + id);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + id);
        }
    }




}





