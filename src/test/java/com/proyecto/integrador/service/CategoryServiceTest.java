package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CategoryDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private InstrumentRepository instrumentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory() {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("TestCategory");
        categoryDto.setDetails("TestDetails");

        MockMultipartFile imageFile = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "Test image content".getBytes());


        when(categoryRepository.findByName("TestCategory")).thenReturn(Optional.empty());


        when(imageService.createImage(imageFile)).thenReturn(new Image());


        Category createdCategory = categoryService.createCategory(categoryDto, imageFile);


        assertNotNull(createdCategory);


        verify(categoryRepository, times(1)).findByName("TestCategory");
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(imageService, times(1)).createImage(imageFile);


        assertEquals("TestCategory", createdCategory.getName());
        assertEquals("TestDetails", createdCategory.getDetails());
        assertFalse(createdCategory.getDeleted());
    }

    @Test
    void categoryByName() {
        String categoryName = "TestCategory";
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName(categoryName);


        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(existingCategory));


        Category resultCategory = categoryService.categoryByName(categoryName);

        assertNotNull(resultCategory);
        assertSame(existingCategory, resultCategory);

        verify(categoryRepository, times(1)).findByName(categoryName);
    }

    @Test
    void countInstrumentsByCategory() {

        Long categoryId = 1L;
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);


        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));


        Long instrumentCount = 10L;
        when(instrumentRepository.countAllByCategory(categoryId)).thenReturn(instrumentCount);


        Long resultCount = categoryService.countInstrumentsByCategory(categoryId);


        assertNotNull(resultCount);
        assertEquals(instrumentCount, resultCount);


        verify(categoryRepository, times(1)).findById(categoryId);
        verify(instrumentRepository, times(1)).countAllByCategory(categoryId);
    }

    @Test
    void deleteCategory() {

        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setImage(new Image());
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));


        doNothing().when(imageService).deleteImage(any());


        List<Instrument> instrumentList = new ArrayList<>();
        Instrument instrument1 = new Instrument();
        instrument1.setId(1L);
        instrument1.setImage(new ArrayList<>());
        instrumentList.add(instrument1);
        when(instrumentRepository.findAllByCategory(categoryId)).thenReturn(instrumentList);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
        verify(instrumentRepository, times(1)).findAllByCategory(categoryId);
        verify(instrumentRepository, times(1)).saveAll(instrumentList);
        verify(imageService, times(1)).deleteImage(any());

        assertTrue(category.getDeleted());
        assertTrue(instrumentList.get(0).getDeleted());
    }

    @Test
    void updateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("UpdatedCategory");


        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("ExistingCategory");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findByName("UpdatedCategory")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));


        Category updatedCategory = categoryService.updateCategory(categoryDto);


        assertNotNull(updatedCategory);


        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findByName("UpdatedCategory");
        verify(categoryRepository, times(1)).save(any());


        assertEquals("UpdatedCategory", updatedCategory.getName());
    }

    @Test
    void listCategories() {

        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = Arrays.asList(category1, category2);


        when(categoryRepository.findAll(false)).thenReturn(categories);


        List<Category> result = categoryService.listCategories();


        assertNotNull(result);
        assertEquals(categories.size(), result.size());
        assertTrue(result.contains(category1));
        assertTrue(result.contains(category2));


        verify(categoryRepository, times(1)).findAll(false);
    }


    @Test
    void getInstrumentsByCategories() {

        List<Long> categoryIdList = Arrays.asList(1L, 2L);


        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(2L);
        when(categoryRepository.findAllById(categoryIdList)).thenReturn(Arrays.asList(category1, category2));


        Instrument instrument1 = new Instrument();
        instrument1.setId(11L);
        Instrument instrument2 = new Instrument();
        instrument2.setId(12L);
        when(instrumentRepository.findAllByCategory(1L)).thenReturn(Arrays.asList(instrument1));
        when(instrumentRepository.findAllByCategory(2L)).thenReturn(Arrays.asList(instrument2));


        List<Instrument> result = categoryService.getInstrumentsByCategories(categoryIdList);


        assertNotNull(result);


        verify(categoryRepository, times(1)).findAllById(categoryIdList);
        verify(instrumentRepository, times(1)).findAllByCategory(1L);
        verify(instrumentRepository, times(1)).findAllByCategory(2L);


        assertEquals(2, result.size());
        assertTrue(result.contains(instrument1));
        assertTrue(result.contains(instrument2));
    }

    @Test
    void categoryById() {

        Long categoryId = 1L;
        Category expectedCategory = new Category();
        expectedCategory.setId(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));


        Category result = categoryService.categoryById(categoryId);


        assertNotNull(result);


        verify(categoryRepository, times(1)).findById(categoryId);


        assertEquals(expectedCategory, result);
    }
}