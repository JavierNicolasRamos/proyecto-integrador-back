package com.proyecto.integrador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.proyecto.integrador.dto.CategoryDto;
import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.*;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import com.proyecto.integrador.service.CategoryService;
import com.proyecto.integrador.service.CharacteristicService;
import com.proyecto.integrador.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private InstrumentRepository instrumentRepository;

    @Mock
    private ImageService imageService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void createCategory() throws Exception {

    }

    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void categoryByName() throws Exception {

    }


    @Test
    void categoryById() throws Exception{

        Long categoryId = 1L;
        Category mockCategory = new Category();
        mockCategory.setId(categoryId);
        mockCategory.setName("TestCategory");

        when(categoryService.categoryById(categoryId)).thenReturn(mockCategory);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/category/id/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(mockCategory.getName()))
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        Category responseCategory = objectMapper.readValue(responseBody, Category.class);

        assertEquals(mockCategory.getId(), responseCategory.getId());
        assertEquals(mockCategory.getName(), responseCategory.getName());
    }

    @Test
    void countInstrumentsByCategory() throws Exception{

        Long categoryId = 1L;
        Long instrumentCount = 5L;

        when(categoryService.countInstrumentsByCategory(categoryId)).thenReturn(instrumentCount);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/category/countinstrument/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(instrumentCount))
                .andReturn();
    }

    @Test
    void deleteCategory() throws Exception{
        Long categoryId = 1L;

        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        verify(categoryService).deleteCategory(categoryId);

    }

    @Test
    void listCategories() throws Exception{
        List<Category> categoryList = Arrays.asList(
                new Category(),
                new Category()

        );


        when(categoryService.listCategories()).thenReturn(categoryList);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/category/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/category/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()))
                .andExpect(jsonPath("$[0].id").value(categoryList.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(categoryList.get(0).getName()))
                .andExpect(jsonPath("$[0].details").value(categoryList.get(0).getDetails()))
                .andExpect(jsonPath("$[1].id").value(categoryList.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(categoryList.get(1).getName()))
                .andExpect(jsonPath("$[1].details").value(categoryList.get(1).getDetails()));
    }

    @Test
    void updateCategory() throws Exception{

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("UpdatedCategory");
        categoryDto.setDetails("UpdatedDetails");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryDto.getId());
        updatedCategory.setName(categoryDto.getName());
        updatedCategory.setDetails(categoryDto.getDetails());


        when(categoryService.updateCategory(any(CategoryDto.class))).thenReturn(updatedCategory);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCategory.getId()))
                .andExpect(jsonPath("$.name").value(updatedCategory.getName()))
                .andExpect(jsonPath("$.details").value(updatedCategory.getDetails()));

    }

    @Test
    void getInstrumentsByCategories() throws Exception{

    }
}