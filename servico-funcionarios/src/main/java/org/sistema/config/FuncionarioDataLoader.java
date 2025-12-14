package org.sistema.config;

import org.sistema.model.Funcionario;
import org.sistema.repository.FuncionarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuncionarioDataLoader {

    @Bean
    public CommandLineRunner carregarFuncionariosIniciais(FuncionarioRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                // alguns exemplos
                repo.save(new Funcionario(
                        "Ana Gerente",
                        "ana.gerente@restaurante.com",
                        "123",          // senha simples só pro demo
                        "GERENTE",
                        true            // ativo
                ));

                repo.save(new Funcionario(
                        "Bruno Atendente",
                        "bruno.atendente@restaurante.com",
                        "123",
                        "ATENDENTE",
                        true            // ativo
                ));

                repo.save(new Funcionario(
                        "Carlos Demitido",
                        "carlos@restaurante.com",
                        "123",
                        "ATENDENTE",
                        false           // NÃO ativo
                ));

                System.out.println("Funcionários iniciais carregados no H2.");
            }
        };
    }
}
