package com.example.appSQL.repository;

import com.example.appSQL.model.Estado;
import com.example.appSQL.model.Pedido;
import com.example.appSQL.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends CrudRepository<Pedido, Integer> {


    @Query("SELECT p FROM Pedido p WHERE p.id_pedido = :id_pedido")
    Pedido findByIdPedido(@Param("id_pedido") Integer id_pedido);

    @Query("SELECT p FROM Pedido p WHERE p.estado.estado = :estado")
    List<Pedido> findByEstadoNombre(@Param("estado") String estado);

    @Query("SELECT e FROM Estado e WHERE e.estado = :nombreEstado")
    Estado findByNombreEstado(@Param("nombreEstado") String nombreEstado);

    @Query("SELECT p FROM Pedido p " +
            "JOIN p.usuarios u " +
            "WHERE p.estado.estado = :estado AND u.id_usuario = :id_usuario")
    List<Pedido> findByEstadoNombreAndUsuarioId(@Param("estado") String estado,
                                                @Param("id_usuario") Integer id_usuario);

    @Query("SELECT p FROM Pedido p " +
            "JOIN p.usuarios u " +
            "WHERE p.estado.estado != 'EN CARRITO' AND u.id_usuario = :id_usuario")
    List<Pedido> verMisPedidosRealizados(@Param("id_usuario") Integer id_usuario);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE :usuario MEMBER OF p.usuarios AND p.estado.estado = :estado")
    Integer contarPedidosEnCarritoPorUsuario(@Param("usuario") Usuario usuario, @Param("estado") String estado);


    @Query("SELECT p FROM Pedido p " +
            "JOIN p.usuarios u " +
            "JOIN p.productos prods " +
            "JOIN prods.usuarios user " +
            "WHERE p.estado.estado != 'EN CARRITO' AND user.id_usuario = :id_usuario")
    List<Pedido> verMisPedidosVendidos(@Param("id_usuario") Integer id_usuario);

}
