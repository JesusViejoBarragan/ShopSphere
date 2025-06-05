package com.example.appSQL.repository;

import com.example.appSQL.model.Categoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {

    @Query("SELECT c FROM Categoria c WHERE c.id_categoria = :id_categoria")
    Categoria findById_categoria(@Param("id_categoria") Integer id_categoria);

    @Query("SELECT c FROM Categoria c WHERE c.nombre = :nombreCategoria")
    Categoria findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);


}
