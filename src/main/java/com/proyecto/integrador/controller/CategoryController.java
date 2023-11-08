package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CategoryDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @GetMapping("/{name}")
    public Category categoryByName(@PathVariable String name){
        return categoryService.categoryByName(name);
    }

    @GetMapping("/countinstrument/{id}")
    public Long countInstrumentsByCategory(@PathVariable Long id){
        return categoryService.countInstrumentsByCategory(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        this.categoryService.deleteCategory(id);
    }

    @GetMapping("/list")
    public List<Category> listCategories(){
        return categoryService.listCategories();
    }

    @PutMapping
    public Category updateCategory(@RequestBody CategoryDto category){
        return categoryService.updateCategory(category);
    }

    @GetMapping("/instruments")
    public List<Instrument> getInstrumentsByCategories(@RequestBody List<Long> categoryIdList){
        return categoryService.getInstrumentsByCategories(categoryIdList);
    }
}
