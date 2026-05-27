package com.ejemplo.inventario.controller;

import com.ejemplo.inventario.model.Inventario;
import com.ejemplo.inventario.repository.InventarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioRepository repository;

    public InventarioController(InventarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Inventario> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Inventario agregar(@RequestBody Inventario inventario) {
        return repository.save(inventario);
    }
}