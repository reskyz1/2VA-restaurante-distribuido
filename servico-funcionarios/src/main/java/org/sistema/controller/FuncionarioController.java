package org.sistema.controller;

import org.sistema.dto.LoginRequest;
import org.sistema.model.Funcionario;
import org.sistema.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<Funcionario> criarFuncionario(@RequestBody Funcionario funcionario) {
        Funcionario salvo = funcionarioService.salvar(funcionario);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        try {
            Funcionario funcionario = funcionarioService.buscarPorId(id);
            return ResponseEntity.ok(funcionario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Funcionario> login(@RequestBody LoginRequest loginRequest) {
        return funcionarioService.login(loginRequest.getEmail(), loginRequest.getSenha())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
