package com.ejemplo.pedidos.controller;

import com.ejemplo.pedidos.client.ProductoClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidoController {

    private final ProductoClient productoClient;

    public PedidoController(ProductoClient productoClient) {
        this.productoClient = productoClient;
    }

    @GetMapping("/pedidos")
    public String pedidos() {
        return "Pedidos creados con productos: " + productoClient.obtenerProductos();
    }
}