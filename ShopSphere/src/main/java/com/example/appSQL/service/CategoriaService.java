package com.example.appSQL.service;

import com.example.appSQL.model.Categoria;
import com.example.appSQL.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return (List<Categoria>) categoriaRepository.findAll();
    }

    public Categoria findById_categoria(Integer id_categoria) {
        return categoriaRepository.findById_categoria(id_categoria);
    }

    public Categoria findByNombreCategoria(String nombreCategoria) {
        return categoriaRepository.findByNombreCategoria(nombreCategoria);
    }
}
