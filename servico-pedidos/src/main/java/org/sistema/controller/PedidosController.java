package org.sistema.controller;

import org.sistema.messaging.PedidosPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.sistema.dto.PedidoDTO;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    @Autowired
    private PedidosPublisher publisher;

    @PostMapping("/enviar")
    public String enviarPedido(@RequestBody PedidoDTO pedido) { //Ajuste pro tipo tipado -
        publisher.enviarPedido(pedido);
        return "Pedido enviado com sucesso!";
    }
}
