package org.sistema.controller;

import org.sistema.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sistema.dto.PedidoDTO;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/enviar")
    public String criarPedido(@RequestBody PedidoDTO pedido) { 
        pedidoService.criarEPublicarPedido(pedido);
        return "Pedido recebido com sucesso! ID: " + pedido.getId() + ". Aguardando confirmação do estoque.";
    }
}
