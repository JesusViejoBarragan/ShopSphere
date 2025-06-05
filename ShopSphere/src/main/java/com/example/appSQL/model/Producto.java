package com.example.appSQL.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    private String nombre;

    private String descripcion;

    private Integer stock;

    private String imagen;

    private Double precio;


    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToMany
    @JoinTable(name = "usuariosproductos",
            joinColumns = {@JoinColumn(name = "id_producto")},
            inverseJoinColumns = {@JoinColumn(name = "id_usuario")})
    private List<Usuario> usuarios;

    @ManyToMany
    @JoinTable(name = "productospedidos",
            joinColumns = {@JoinColumn(name = "id_producto")},
            inverseJoinColumns = {@JoinColumn(name = "id_pedido")})
    private List<Pedido> pedidos;

    public Producto() {
    }

    public Producto(Integer id_producto, String nombre, String descripcion, Integer stock, String imagen, Double precio, Categoria categoria, List<Usuario> usuarios, List<Pedido> pedidos) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.precio = precio;
        this.categoria = categoria;
        this.usuarios = usuarios;
        this.pedidos = pedidos;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
