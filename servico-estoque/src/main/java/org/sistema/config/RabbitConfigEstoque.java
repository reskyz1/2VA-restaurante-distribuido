package org.sistema.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigEstoque {

    public static final String FILA_ESTOQUE = "fila.estoque";

    @Bean
    public Queue queueEstoque() {
        return new Queue(FILA_ESTOQUE, true);
    }

    /**
     * Define o MessageConverter com a Lista de Classes Permitidas (Allowed List).
     * Isso é necessário para usar serialização Java segura (Serializable).
     */
    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        
        // Adiciona as classes necessárias para deserialização:
        converter.addAllowedListPatterns(
            "java.util.ArrayList",      // Necessário para a lista de itens (List<ItemDTO>)
            "org.sistema.dto.*"         // Permite todos os DTOs do seu projeto (PedidoDTO, ItemDTO)
        );

        return converter;
    }
}