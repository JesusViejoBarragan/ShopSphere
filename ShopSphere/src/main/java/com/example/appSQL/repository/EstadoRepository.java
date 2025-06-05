package com.example.appSQL.repository;

import com.example.appSQL.model.Estado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EstadoRepository extends CrudRepository<Estado, Integer> {

    @Query("SELECT e FROM Estado e WHERE e.estado = :estado")
    Estado findByEstado(@Param("estado") String estado);

    @Query("SELECT e FROM Estado e WHERE e.id_estado = :id_estado")
    Estado findByIdEstado(@Param("id_estado") Integer id_estado);
}
