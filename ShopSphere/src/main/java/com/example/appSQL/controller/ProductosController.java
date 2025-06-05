package com.example.appSQL.controller;

import com.example.appSQL.model.*;
import com.example.appSQL.service.CategoriaService;
import com.example.appSQL.service.EstadoService;
import com.example.appSQL.service.PedidoService;
import com.example.appSQL.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private EstadoService estadoService;

    @GetMapping("/misProductos")
    public String misProductos(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/";
        }

        List<Producto> listaProductos = productoService.findMisProductos(usuario);
        model.addAttribute("productos", listaProductos);

        Integer totalCarrito = 0;
        if (usuario != null) {
            totalCarrito = pedidoService.contarPedidosEnCarritoPorUsuario(usuario);
        }

        model.addAttribute("totalCarrito", totalCarrito);

        List<Categoria> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);

        List<Pedido> pedidosVendidos = pedidoService.verMisPedidosVendidos(usuario);
        model.addAttribute("pedidosVendidos", pedidosVendidos);

        List<Estado> listaEstados = estadoService.findAll();
        model.addAttribute("listaEstados", listaEstados);

        return "misProductos";
    }

    @PostMapping("/crearProducto")
    public String crearProducto(@RequestParam String nombre,
                                @RequestParam String descripcion,
                                @RequestParam Integer stock,
                                @RequestParam Integer id_categoria,
                                @RequestParam Double precio,
                                @RequestParam String url,
                                HttpSession session) {

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setImagen(url);
        producto.setStock(stock);

        Categoria categoria = categoriaService.findById_categoria(id_categoria);
        producto.setCategoria(categoria);

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        productoService.crearNuevoProducto(producto, usuario);

        return "redirect:/misProductos";
    }

    @PostMapping("/borrarProducto")
    public String borrarProducto(Integer id_producto) {

        productoService.deleteById(id_producto);
        System.out.println("Borrado correctamente");
        return "redirect:/misProductos";
    }

    @PostMapping("/editarPedido")
    public String editarPedido(Integer id_pedido, Integer id_estado) {

        Pedido pedido = pedidoService.findByIdPedido(id_pedido);
        Estado estado = estadoService.findByIdEstado(id_estado);
        pedido.setEstado(estado);

        pedidoService.save(pedido);
        return "redirect:/misProductos";
    }
}
