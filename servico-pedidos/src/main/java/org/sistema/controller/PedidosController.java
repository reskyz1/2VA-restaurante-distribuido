package org.sistema.controller;

import org.sistema.dto.PedidoDTO;
import org.sistema.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    // Mantém o endpoint antigo (usado no relatório)
    @PostMapping("/enviar")
    public String criarPedidoMensagem(@RequestBody PedidoDTO pedido) {
        PedidoDTO criado = pedidoService.criarEPublicarPedido(pedido);
        return "Pedido recebido com sucesso! ID: " + criado.getId()
                + ". Aguardando confirmação do estoque.";
    }

    // Endpoint REST "bonitinho" para a interface gráfica
    @PostMapping
    public PedidoDTO criarPedido(@RequestBody PedidoDTO pedido) {
        return pedidoService.criarEPublicarPedido(pedido);
    }

    @GetMapping
    public List<PedidoDTO> listarPedidos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPedido(@PathVariable Long id) {
        try {
            PedidoDTO dto = pedidoService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
