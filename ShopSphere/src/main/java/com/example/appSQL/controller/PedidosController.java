package com.example.appSQL.controller;

import com.example.appSQL.model.Pedido;
import com.example.appSQL.model.Usuario;
import com.example.appSQL.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/carrito")
    public String carrito(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/";
        }
        List<Pedido> listaPedidos = pedidoService.listaCarrito(usuario);
        Integer totalCarrito = pedidoService.contarPedidosEnCarritoPorUsuario(usuario);

        model.addAttribute("pedidos", listaPedidos);
        model.addAttribute("totalCarrito", totalCarrito);

        return "carrito";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/";
        }
        List<Pedido> listaPedidos = pedidoService.listaMisPedidos(usuario);
        Integer totalCarrito = pedidoService.contarPedidosEnCarritoPorUsuario(usuario);
        
        model.addAttribute("totalCarrito", totalCarrito);
        model.addAttribute("pedidos", listaPedidos);

        return "misPedidos";

    }

    @PostMapping("/pedidos/anyadirproducto")
    public String anyadirproducto(Integer id_producto, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/";
        }
        pedidoService.crearPedidoEnCarritoConProducto(id_producto, usuario);
        System.out.println("PEDIDO CREADO CORRECTAMENTE");
        return "redirect:/shop";

    }

    @PostMapping("/borrarPedido")
    public String borrarPedido(Integer id_pedido) {

        pedidoService.deleteById(id_pedido);
        System.out.println("Borrado correctamente");
        return "redirect:/carrito";
    }

    @PostMapping("/realizarPedido")
    public String realizarPedido(@RequestParam("id_producto") Integer idProducto,
                                 @RequestParam("id_pedido_a_procesar") Integer idPedido,
                                 @RequestParam("cantidadProducto") Integer cantidad,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario.getDireccion() == null || usuario.getDireccion().isEmpty()) {
            redirectAttributes.addFlashAttribute("mostrarAlertaDireccion", true);
            redirectAttributes.addFlashAttribute("mensajeAlerta",
                    "¡Importante! Para poder realizar tu pedido, por favor actualiza tu dirección de envío en tu perfil.");
            return "redirect:/carrito";
        }
        redirectAttributes.addFlashAttribute("pedidoRealizado", true);
        redirectAttributes.addFlashAttribute("mensajeAlerta",
                "Pedido realizado con éxito.");
        pedidoService.realizarPedido(idPedido, usuario, cantidad);
        System.out.println("Pedido realizado correctamente");
        return "redirect:/carrito";
    }


}
