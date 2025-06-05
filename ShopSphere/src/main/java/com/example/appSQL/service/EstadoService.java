package com.example.appSQL.service;

import com.example.appSQL.model.Estado;
import com.example.appSQL.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> findAll() {
        List<Estado> listaEstados = (List<Estado>) estadoRepository.findAll();
        listaEstados.remove(estadoRepository.findByEstado("EN CARRITO"));
        return listaEstados;
    }

    public Estado findByIdEstado(Integer id_estado) {
        return estadoRepository.findByIdEstado(id_estado);
    }

}

