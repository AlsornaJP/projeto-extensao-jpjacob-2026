package com.jacob.jp.projeto_extensao.controller;

import com.jacob.jp.projeto_extensao.config.SecurityConfig;
import com.jacob.jp.projeto_extensao.dto.AtualizarVarianteDTO;
import com.jacob.jp.projeto_extensao.dto.ReporEstoqueDTO;
import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.exception.RegraDeNegocioException;
import com.jacob.jp.projeto_extensao.service.VarianteService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VarianteController.class)
@Import(SecurityConfig.class)
class VarianteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VarianteService varianteService;

    private static VarianteDTO varianteDTO(Integer id, String medida, String preco, Integer estoque) {
        VarianteDTO dto = new VarianteDTO();
        dto.setId(id);
        dto.setMedida(medida);
        dto.setPreco(new BigDecimal(preco));
        dto.setEstoque(estoque);
        dto.setIdProduto(1);
        return dto;
    }

    @Test
    void listarPorProdutoDevolveAsVariantes() throws Exception {
        when(varianteService.listarPorProduto(1)).thenReturn(List.of(
                varianteDTO(5, "500g", "49.90", 12),
                varianteDTO(6, "1kg", "89.90", 5)));

        mockMvc.perform(get("/produtos/1/variantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].medida").value("500g"))
                .andExpect(jsonPath("$[0].idProduto").value(1));
    }

    @Test
    void listarDeProdutoInexistenteDevolve404() throws Exception {
        when(varianteService.listarPorProduto(99))
                .thenThrow(new RecursoNaoEncontradoException("Produto", 99));

        mockMvc.perform(get("/produtos/99/variantes"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Produto 99 nao encontrado"));
    }

    @Test
    void adicionarDevolve201ComLocation() throws Exception {
        VarianteDTO enviado = varianteDTO(null, "2kg", "159.90", 4);
        when(varianteService.adicionar(eq(1), any(VarianteDTO.class)))
                .thenReturn(varianteDTO(7, "2kg", "159.90", 4));

        mockMvc.perform(post("/produtos/1/variantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/variantes/7"))
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void adicionarComMedidaRepetidaDevolve409() throws Exception {
        VarianteDTO enviado = varianteDTO(null, "500g", "49.90", 4);
        when(varianteService.adicionar(eq(1), any(VarianteDTO.class)))
                .thenThrow(new RegraDeNegocioException("O produto 1 ja tem uma variante com a medida 500g"));

        mockMvc.perform(post("/produtos/1/variantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("O produto 1 ja tem uma variante com a medida 500g"));
    }

    @Test
    void atualizarDevolve200() throws Exception {
        AtualizarVarianteDTO enviado = new AtualizarVarianteDTO();
        enviado.setMedida("600g");
        enviado.setPreco(new BigDecimal("54.90"));
        when(varianteService.atualizar(eq(5), any(AtualizarVarianteDTO.class)))
                .thenReturn(varianteDTO(5, "600g", "54.90", 12));

        mockMvc.perform(put("/variantes/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medida").value("600g"));
    }

    @Test
    void reporEstoqueDevolve200() throws Exception {
        ReporEstoqueDTO enviado = new ReporEstoqueDTO();
        enviado.setQuantidade(10);
        when(varianteService.reporEstoque(5, 10)).thenReturn(varianteDTO(5, "500g", "49.90", 22));

        mockMvc.perform(patch("/variantes/5/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estoque").value(22));
    }

    @Test
    void reporEstoqueComQuantidadeNegativaDevolve400() throws Exception {
        ReporEstoqueDTO enviado = new ReporEstoqueDTO();
        enviado.setQuantidade(-3);

        mockMvc.perform(patch("/variantes/5/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletarDevolve204() throws Exception {
        mockMvc.perform(delete("/variantes/5"))
                .andExpect(status().isNoContent());

        verify(varianteService).deletar(5);
    }

    @Test
    void deletarUltimaVarianteDevolve409() throws Exception {
        doThrow(new RegraDeNegocioException(
                "A variante 5 e a unica do produto 1; apague o produto em vez da variante"))
                .when(varianteService).deletar(5);

        mockMvc.perform(delete("/variantes/5"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("A variante 5 e a unica do produto 1; apague o produto em vez da variante"));
    }

    @Test
    void deletarVarianteEmUsoDevolve409() throws Exception {
        doThrow(new DataIntegrityViolationException("violacao de integridade"))
                .when(varianteService).deletar(5);

        mockMvc.perform(delete("/variantes/5"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
}
