package com.jacob.jp.projeto_extensao.controller;

import com.jacob.jp.projeto_extensao.config.SecurityConfig;
import com.jacob.jp.projeto_extensao.dto.AtualizarProdutoDTO;
import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@Import(SecurityConfig.class)
class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProdutoService produtoService;

    private static VarianteDTO varianteDTO(Integer id, String medida, String preco, Integer estoque) {
        VarianteDTO dto = new VarianteDTO();
        dto.setId(id);
        dto.setMedida(medida);
        dto.setPreco(new BigDecimal(preco));
        dto.setEstoque(estoque);
        dto.setIdProduto(1);
        return dto;
    }

    private static ProdutoDTO produtoDTO(Integer id) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(id);
        dto.setNome("Queijo canastra");
        dto.setDescricao("Meia cura");
        dto.setIdFornecedor(7);
        dto.setVariantes(new ArrayList<>(List.of(
                varianteDTO(5, "500g", "49.90", 12),
                varianteDTO(6, "1kg", "89.90", 5))));
        return dto;
    }

    @Test
    void listarDevolveOsProdutosComAsVariantesAninhadas() throws Exception {
        when(produtoService.listar()).thenReturn(List.of(produtoDTO(1)));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Queijo canastra"))
                .andExpect(jsonPath("$[0].variantes.length()").value(2))
                .andExpect(jsonPath("$[0].variantes[0].medida").value("500g"));
    }

    @Test
    void buscarPorIdDevolveOProduto() throws Exception {
        when(produtoService.buscarPorId(1)).thenReturn(produtoDTO(1));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idFornecedor").value(7));
    }

    @Test
    void buscarPorIdInexistenteDevolve404() throws Exception {
        when(produtoService.buscarPorId(99))
                .thenThrow(new RecursoNaoEncontradoException("Produto", 99));

        mockMvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Produto 99 nao encontrado"));
    }

    @Test
    void criarDevolve201ComLocation() throws Exception {
        ProdutoDTO enviado = produtoDTO(null);
        when(produtoService.criar(any(ProdutoDTO.class))).thenReturn(produtoDTO(1));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/produtos/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void criarSemVariantesDevolve400() throws Exception {
        ProdutoDTO enviado = produtoDTO(null);
        enviado.setVariantes(new ArrayList<>());

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("variantes")));
    }

    @Test
    void atualizarDevolve200() throws Exception {
        AtualizarProdutoDTO enviado = new AtualizarProdutoDTO();
        enviado.setNome("Queijo canastra curado");
        enviado.setDescricao("Meia cura");
        enviado.setIdFornecedor(7);
        when(produtoService.atualizar(eq(1), any(AtualizarProdutoDTO.class))).thenReturn(produtoDTO(1));

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deletarDevolve204() throws Exception {
        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());

        verify(produtoService).deletar(1);
    }

    @Test
    void deletarProdutoEmUsoDevolve409() throws Exception {
        doThrow(new DataIntegrityViolationException("violacao de integridade"))
                .when(produtoService).deletar(1);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
}
