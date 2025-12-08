package org.sistema.service;

import org.sistema.dto.PedidoDTO;
import org.sistema.dto.ItemDTO; 
import org.sistema.model.Pedido;
import org.sistema.model.ItemPedido;
import org.sistema.repository.PedidoRepository;
import org.sistema.messaging.PedidosPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PedidosPublisher publisher;

     // cria o pedido, salva no banco e publica mensagem pro estoque
    public PedidoDTO criarEPublicarPedido(PedidoDTO pedidoDTO) {

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(pedidoDTO.getCliente());
        novoPedido.setStatus("AGUARDANDO_ESTOQUE");

        // Converter ItemDTO -> ItemPedido
        List<ItemPedido> itens = pedidoDTO.getItens().stream()
                .map(dto -> {
                    ItemPedido item = new ItemPedido();
                    item.setProdutoId(dto.getProdutoId());
                    item.setQuantidade(dto.getQuantidade());
                    return item;
                })
                .collect(Collectors.toList());

        novoPedido.setItens(itens);

        // Persistência
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        // Atualiza o DTO com id/status gerados
        pedidoDTO.setId(pedidoSalvo.getId());
        pedidoDTO.setStatus(pedidoSalvo.getStatus());

        // Publicar mensagem para o serviço de estoque
        publisher.enviarPedido(pedidoDTO);
        System.out.println("Pedido " + pedidoSalvo.getId() +
                " criado com sucesso e evento AGUARDANDO_ESTOQUE publicado.");

        return pedidoDTO;
    }

     // listar todos os pedidos para a interface
    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

     // buscar pedido por id (para mostrar status detalhado)
    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        return toDTO(pedido);
    }

    private PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setCliente(pedido.getCliente());
        dto.setStatus(pedido.getStatus());

        if (pedido.getItens() != null) {
            List<ItemDTO> itensDTO = pedido.getItens().stream()
                    .map(item -> {
                        ItemDTO i = new ItemDTO();
                        i.setProdutoId(item.getProdutoId());
                        i.setQuantidade(item.getQuantidade());
                        return i;
                    })
                    .collect(Collectors.toList());
            dto.setItens(itensDTO);
        }

        return dto;
    }
    
}   