package com.ejemplo.pedidos.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductoClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String obtenerProductos() {
        String url = "http://localhost:8080/productos";
        return restTemplate.getForObject(url, String.class);
    }
}