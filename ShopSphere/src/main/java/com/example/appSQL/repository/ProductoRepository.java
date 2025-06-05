package com.example.appSQL.repository;

import com.example.appSQL.model.Categoria;
import com.example.appSQL.model.Pedido;
import com.example.appSQL.model.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.usuarios u " +
            "WHERE u.id_usuario != :id_usuario AND p.nombre LIKE :nombre% ")
    List<Producto> findByNombreContainingIgnoreCaseAndIdUsuario(@Param("id_usuario") Integer id_usuario,
                                                                @Param("nombre") String nombre);

    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.usuarios u " +
            "WHERE u.id_usuario != :id_usuario ")
    List<Producto> findProductos(
            @Param("id_usuario") Integer id_usuario);

    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.usuarios u " +
            "WHERE u.id_usuario = :id_usuario ")
    List<Producto> findMisProductos(
            @Param("id_usuario") Integer id_usuario);

    @Query("SELECT p FROM Producto p " +
            "JOIN p.pedidos ped " +
            "JOIN p.usuarios u " +
            "WHERE ped.estado.estado IN ('EN PREPARACION', 'ENVIADO')" +
            "AND u.id_usuario = :id_usuario")
    List<Producto> verProductosVendidos(@Param("id_usuario") Integer id_usuario);

    @Query("SELECT p FROM Producto p " +
            "JOIN p.categoria c " +
            "WHERE c.nombre = :nombreCategoria")
    List<Producto> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);

    @Query("SELECT p FROM Producto p " +
            "JOIN p.categoria c " +
            "JOIN p.usuarios u " +
            "WHERE c.nombre = :nombreCategoria " +
            "AND u.id_usuario != :id_usuario")
    List<Producto> findByNombreCategoriaYUsuario(@Param("nombreCategoria") String nombreCategoria,
                                                 @Param("id_usuario") Integer id_usuario);
}
