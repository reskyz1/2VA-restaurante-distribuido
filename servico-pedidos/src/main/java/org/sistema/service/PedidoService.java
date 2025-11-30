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

    public void criarEPublicarPedido(PedidoDTO pedidoDTO) {
        
        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(pedidoDTO.getCliente());
        novoPedido.setStatus("AGUARDANDO_ESTOQUE"); 
        
        // Conversão
        List<ItemPedido> itens = pedidoDTO.getItens().stream().map(itemDto -> {
            ItemPedido item = new ItemPedido();
            item.setProdutoId(itemDto.getProdutoId());
            item.setQuantidade(itemDto.getQuantidade());
            item.setPedido(novoPedido); 
            return item;
        }).collect(Collectors.toList());
        
        novoPedido.setItens(itens);

        //Persistência
        
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);
        
        //PUBLICAR MENSAGEM (Comunicação Assíncrona)
        
        pedidoDTO.setId(pedidoSalvo.getId()); 
        
        publisher.enviarPedido(pedidoDTO);
        
        System.out.println("Pedido " + pedidoSalvo.getId() + " criado com sucesso e evento AGUARDANDO_ESTOQUE publicado.");
    }
}   