package com.jacob.jp.projeto_extensao.controller;

import com.jacob.jp.projeto_extensao.dto.AtualizarVarianteDTO;
import com.jacob.jp.projeto_extensao.dto.ReporEstoqueDTO;
import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.service.VarianteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class VarianteController {
    private final VarianteService varianteService;

    public VarianteController(VarianteService varianteService) {
        this.varianteService = varianteService;
    }

    @GetMapping("/produtos/{idProduto}/variantes")
    public List<VarianteDTO> listarPorProduto(@PathVariable Integer idProduto) {
        return varianteService.listarPorProduto(idProduto);
    }

    @PostMapping("/produtos/{idProduto}/variantes")
    public ResponseEntity<VarianteDTO> adicionar(
            @PathVariable Integer idProduto,
            @Valid @RequestBody VarianteDTO dto) {
        VarianteDTO criada = varianteService.adicionar(idProduto, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/variantes/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/variantes/{id}")
    public VarianteDTO atualizar(@PathVariable Integer id, @Valid @RequestBody AtualizarVarianteDTO dto) {
        return varianteService.atualizar(id, dto);
    }

    @PatchMapping("/variantes/{id}/estoque")
    public VarianteDTO reporEstoque(@PathVariable Integer id, @Valid @RequestBody ReporEstoqueDTO dto) {
        return varianteService.reporEstoque(id, dto.getQuantidade());
    }

    @DeleteMapping("/variantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        varianteService.deletar(id);
    }
}
