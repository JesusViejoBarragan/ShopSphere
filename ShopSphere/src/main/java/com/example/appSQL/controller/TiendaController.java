package com.example.appSQL.controller;

import com.example.appSQL.model.Producto;
import com.example.appSQL.model.Usuario;
import com.example.appSQL.service.PedidoService;
import com.example.appSQL.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TiendaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Producto> listaProductos = productoService.findProductos(usuario);
        model.addAttribute("productos", listaProductos);

        Integer totalCarrito = 0;

        if (usuario != null) {
            totalCarrito = pedidoService.contarPedidosEnCarritoPorUsuario(usuario);
        }

        model.addAttribute("totalCarrito", totalCarrito);

        return "shop";
    }

    @GetMapping("/buscarProductos")
    public String buscarProductos(Model model,
                                  @RequestParam(name = "filtro", required = false) String filtro,
                                  @RequestParam(name = "category", required = false) String categoryName,
                                  HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Producto> listaProductos;

        if (categoryName != null && !categoryName.isEmpty()) {
            listaProductos = productoService.buscarPorCategoria(categoryName, usuario);
            model.addAttribute("filtro", categoryName);
        } else if (filtro != null && !filtro.isEmpty()) {
            listaProductos = productoService.findByNombreContainingIgnoreCase(usuario, filtro);
            model.addAttribute("filtro", filtro);
        } else {
            listaProductos = productoService.findProductos(usuario);
            model.addAttribute("filtro", "");
        }

        model.addAttribute("productos", listaProductos);

        Integer totalCarrito = 0;
        if (usuario != null) {
            totalCarrito = pedidoService.contarPedidosEnCarritoPorUsuario(usuario);
        }
        model.addAttribute("totalCarrito", totalCarrito);

        return "buscarProductos";
    }

    @GetMapping("/contarProductosCarrito")
    public Integer contarProductosCarrito(HttpSession session, Model model, String filtro) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            return pedidoService.contarPedidosEnCarritoPorUsuario(usuario);
        }

        return 0;
    }


}
