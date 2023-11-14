package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CategoryDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Category> categoryByName(@PathVariable String name){
        return ResponseEntity.ok(categoryService.categoryByName(name));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Category> categoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.categoryById(id));
    }

    @GetMapping("/countinstrument/{id}")
    public ResponseEntity<Long> countInstrumentsByCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.countInstrumentsByCategory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        this.categoryService.deleteCategory(id);
        return ResponseEntity.ok("La categoría con id:" + id + " se eliminó correctametnte");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> listCategories(){
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryDto category){
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @GetMapping("/instruments")
    public ResponseEntity<List<Instrument>> getInstrumentsByCategories(@RequestBody List<Long> categoryIdList){
        return ResponseEntity.ok(categoryService.getInstrumentsByCategories(categoryIdList));
    }
}
