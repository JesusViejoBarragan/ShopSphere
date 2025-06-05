package com.example.appSQL.service;

import com.example.appSQL.model.Categoria;
import com.example.appSQL.model.Pedido;
import com.example.appSQL.model.Producto;
import com.example.appSQL.model.Usuario;
import com.example.appSQL.repository.CategoriaRepository;
import com.example.appSQL.repository.ProductoRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Producto> findAll() {
        return (List<Producto>) productoRepository.findAll();
    }

    public List<Producto> findProductos(Usuario usuario) {
        if (usuario == null) {
            return findAll();
        }
        return productoRepository.findProductos(usuario.getId_usuario());
    }

    public List<Producto> findByNombreContainingIgnoreCase(Usuario usuario, String nombre) {
        if (usuario == null) {
            return productoRepository.findByNombreContainingIgnoreCase(nombre);
        }

        return productoRepository.findByNombreContainingIgnoreCaseAndIdUsuario(usuario.getId_usuario(), nombre);
    }

    public List<Producto> findByNombreContainingIgnoreCaseAndIdUsuario(Usuario usuario, String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndIdUsuario(usuario.getId_usuario(), nombre);
    }

    public List<Producto> findMisProductos(Usuario usuario) {
        return productoRepository.findMisProductos(usuario.getId_usuario());
    }

    public List<Producto> verProductosVendidos(Usuario usuario) {
        return productoRepository.verProductosVendidos(usuario.getId_usuario());
    }

    public List<Producto> buscarPorCategoria(String nombreCategoria, Usuario usuario) {
        if (usuario == null) {
            return productoRepository.findByNombreCategoria(nombreCategoria);
        }
        return productoRepository.findByNombreCategoriaYUsuario(nombreCategoria, usuario.getId_usuario());
    }

    @Transactional
    public void crearNuevoProducto(Producto producto, Usuario usuario) {

        producto.setCategoria(entityManager.merge(producto.getCategoria()));

        Usuario managedUsuario = entityManager.merge(usuario);
        List<Usuario> usuarios;

        if (producto.getUsuarios() == null) {
            usuarios = new ArrayList<>();
            usuarios.add(managedUsuario);
        } else {
            producto.getUsuarios().add(managedUsuario);
        }
        managedUsuario.getProductos().add(producto);


        productoRepository.save(producto);
    }

    @Transactional
    public ResponseEntity<?> deleteById(Integer id_producto) {
        try {
            System.out.println("entra delete " + id_producto);
            productoRepository.deleteById(id_producto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Producto eliminado satisfactoriamente: " + id_producto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrada: " + id_producto);
        }
    }


}
