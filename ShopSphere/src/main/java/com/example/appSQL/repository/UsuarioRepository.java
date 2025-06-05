package com.example.appSQL.repository;

import com.example.appSQL.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    Usuario findByEmail(String email);

    Optional<Usuario> findById(Integer id_usuario);
}
