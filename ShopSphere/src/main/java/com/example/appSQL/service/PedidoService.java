package com.example.appSQL.service;

import com.example.appSQL.model.Estado;
import com.example.appSQL.model.Pedido;
import com.example.appSQL.model.Producto;
import com.example.appSQL.model.Usuario;
import com.example.appSQL.repository.PedidoRepository;
import com.example.appSQL.repository.ProductoRepository;
import com.example.appSQL.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Pedido> findAll() {
        return (List<Pedido>) pedidoRepository.findAll();
    }

    public Pedido findByIdPedido(Integer id_pedido) {
        return pedidoRepository.findByIdPedido(id_pedido);
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listaCarrito(Usuario usuario) {
        return pedidoRepository.findByEstadoNombreAndUsuarioId("EN CARRITO", usuario.getId_usuario());
    }

    public List<Pedido> listaMisPedidos(Usuario usuario) {
        return pedidoRepository.verMisPedidosRealizados(usuario.getId_usuario());
    }

    public List<Pedido> verMisPedidosVendidos(Usuario usuario) {
        return pedidoRepository.verMisPedidosVendidos(usuario.getId_usuario());
    }

    public Pedido crearPedidoEnCarritoConProducto(Integer idProducto, Usuario usuario) {
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.now());

        Estado estadoEnCarrito = pedidoRepository.findByNombreEstado("EN CARRITO");
        if (estadoEnCarrito == null) {
            throw new RuntimeException("Estado 'EN CARRITO' no encontrado");
        }
        pedido.setEstado(estadoEnCarrito);

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        pedido.setProductos(new ArrayList<>());
        pedido.getProductos().add(producto);

        Usuario usuarioGestionado = usuarioRepository.findById(usuario.getId_usuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (pedido.getUsuarios() == null) {
            pedido.setUsuarios(new ArrayList<>());
        }
        pedido.getUsuarios().add(usuarioGestionado);

        return pedidoRepository.save(pedido);
    }

    public ResponseEntity<?> deleteById(Integer id_pedido) {
        try {
            pedidoRepository.deleteById(id_pedido);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pedido eliminado satisfactoriamente: " + id_pedido);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrada: " + id_pedido);
        }
    }

    public Integer contarPedidosEnCarritoPorUsuario(Usuario usuario) {
        return pedidoRepository.contarPedidosEnCarritoPorUsuario(usuario, "EN CARRITO");
    }

    public void realizarPedido(Integer id_pedido, Usuario usuario, Integer cantidad) {
        Pedido pedido = pedidoRepository.findByIdPedido(id_pedido);
        pedido.setDireccion(usuario.getDireccion());
        pedido.setCantidadPedida(cantidad);

        Estado estado = pedidoRepository.findByNombreEstado("EN PREPARACION");
        pedido.setEstado(estado);

        pedidoRepository.save(pedido);
    }


}
