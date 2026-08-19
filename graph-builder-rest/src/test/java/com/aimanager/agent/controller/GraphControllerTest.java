package com.aimanager.agent.controller;

import com.aimanager.agent.Form.CreateGraphForm;
import com.aimanager.agent.Form.UpdateGraphForm;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.services.GraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GraphControllerTest {

    @Mock
    private GraphService graphService;

    @InjectMocks
    private GraphController graphController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(graphController).build();
    }

    @Test
    void createGraph_ShouldReturnSuccess() throws Exception {
        GraphEntity graphEntity = new GraphEntity();
        graphEntity.setId(1L);
        graphEntity.setName("Test Graph");

        when(graphService.createGraph(any(CreateGraphForm.class))).thenReturn(graphEntity);

        mockMvc.perform(post("/graph/create")
                        .param("name", "Test Graph")
                        .param("description", "Test Description")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Graph created successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Test Graph"));

        verify(graphService, times(1)).createGraph(any(CreateGraphForm.class));
    }

    @Test
    void getGraph_ShouldReturnGraph() throws Exception {
        GraphEntity graphEntity = new GraphEntity();
        graphEntity.setId(1L);
        graphEntity.setName("Test Graph");

        when(graphService.getGraph(1L)).thenReturn(graphEntity);

        mockMvc.perform(get("/graph/get")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Graph retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Test Graph"));

        verify(graphService, times(1)).getGraph(1L);
    }

    @Test
    void getAllGraphs_ShouldReturnPage() throws Exception {
        GraphEntity graphEntity = new GraphEntity();
        graphEntity.setId(1L);
        graphEntity.setName("Test Graph");

        Page<GraphEntity> page = new PageImpl<>(Collections.singletonList(graphEntity), PageRequest.of(0, 10), 1);
        when(graphService.getGraphs(any())).thenReturn(page);

        mockMvc.perform(get("/graph")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Graphs retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(1L));

        verify(graphService, times(1)).getGraphs(any());
    }

    @Test
    void editGraph_ShouldReturnSuccess() throws Exception {
        GraphEntity graphEntity = new GraphEntity();
        graphEntity.setId(1L);
        graphEntity.setName("Updated Graph");

        when(graphService.editGraph(any(UpdateGraphForm.class))).thenReturn(graphEntity);

        mockMvc.perform(put("/graph/edit")
                        .param("id", "1")
                        .param("name", "Updated Graph")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Graph edited successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Updated Graph"));

        verify(graphService, times(1)).editGraph(any(UpdateGraphForm.class));
    }

    @Test
    void deleteGraph_ShouldReturnSuccess() throws Exception {
        doNothing().when(graphService).deleteGraph(1L);

        mockMvc.perform(delete("/graph/delete")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // On ne vérifie que le statut 200

        verify(graphService, times(1)).deleteGraph(1L);
    }
}