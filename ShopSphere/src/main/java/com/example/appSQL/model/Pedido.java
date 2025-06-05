package com.example.appSQL.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    private String direccion;

    @Column(name = "cantidad_pedida")
    private Integer cantidadPedida;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @ManyToMany
    @JoinTable(name="usuariospedidos",
            joinColumns = {@JoinColumn(name = "id_pedido")},
            inverseJoinColumns = {@JoinColumn(name = "id_usuario")})
    private List<Usuario> usuarios;

    @ManyToMany
    @JoinTable(name="productospedidos",
            joinColumns = {@JoinColumn(name = "id_pedido")},
            inverseJoinColumns = {@JoinColumn(name = "id_producto")})
    private List<Producto> productos;

    public Pedido() {
    }

    public Pedido(Integer id_pedido, LocalDate fechaPedido, String direccion, Integer cantidadPedida, Estado estado, List<Usuario> usuarios, List<Producto> productos) {
        this.id_pedido = id_pedido;
        this.fechaPedido = fechaPedido;
        this.direccion = direccion;
        this.cantidadPedida = cantidadPedida;
        this.estado = estado;
        this.usuarios = usuarios;
        this.productos = productos;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(Integer cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
