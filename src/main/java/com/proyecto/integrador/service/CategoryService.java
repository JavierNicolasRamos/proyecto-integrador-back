package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CategoryDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private ImageService imageService;

    @Transactional
    public Category createCategory(@NotNull CategoryDto categoryDto){
        logger.info("Iniciando el proceso de creación de categoría con descripción: " + categoryDto.getName());

        try {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryDto.getName());
            if (optionalCategory.isPresent()) {
                logger.error("Ya existe una categoría con ese nombre: " + categoryDto.getName());
                throw new DuplicateCategoryException("Ya existe una categoría con ese nombre: " + categoryDto.getName());
            }
            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setDetails(categoryDto.getDetails());
            category.setDeleted(false);

            categoryRepository.save(category);
            this.imageService.saveImageCategory(category, categoryDto.getImageDto());

            logger.info("Categoría creada con éxito. Descripción: " + categoryDto.getName());
            return category;

        } catch (DuplicateCategoryException e) {
            logger.error("Error al crear la categoría: " + e.getMessage());
            throw e;
        } catch (Exception e){
            logger.error("Error inesperado al crear la categoría: " + e.getMessage(), e);
            throw e;
        }
    }

    public Category categoryByName(String name){
        logger.info("Iniciando la búsqueda de categoría con descripción: " + name);

        try {
            Category category = categoryRepository.findByName(name).orElseThrow(() -> {
                    logger.error("No se encontró la categoría con descripción: " + name);
                    return new CategoryNotFoundException("La categoria no existe");
            });
            logger.info("Búsqueda de categoría completada con éxito. Descripción: " + name);
            return category;
        }catch(Exception e){
            logger.error("Error inesperado al buscar la categoría: " + e.getMessage(), e);
            throw e; //TODO: sumar la excepcion customizada
        }
    }
    public Long countInstrumentsByCategory(Long id){
        logger.info("Iniciando el conteo de instrumentos por categoría con ID: " + id);
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() ->
                    new CategoryNotFoundException("La categoria no existe"));
            logger.info("Conteo de instrumentos por categoría completado con éxito.");
            return instrumentRepository.countAllByCategory(category.getId());
        } catch (CategoryNotFoundException e){
            logger.error("Error al contar instrumentos por categoría: " + e.getMessage());
            throw e;
        } catch (Exception e){
            logger.error("Error inesperado al contar instrumentos por categoría: " + e.getMessage(), e);
            throw e;
        }
    }

    public void deleteCategory(Long id){
        logger.info("Iniciando el proceso de eliminación de instrumentos por categoría con ID: " + id);
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() ->
                    new CategoryNotFoundException("La categoria no existe"));
            logger.info("Eliminando categoría con ID: " + category.getId());
            category.setDeleted(true);
            this.imageService.deleteImageCategory(category);
            categoryRepository.save(category);

            List<Instrument> instrumentList = instrumentRepository.findAllByCategory(category);
            for (Instrument instrument : instrumentList) {
                logger.info("Eliminando instrumento con ID: " + instrument.getId());
                instrument.setDeleted(true);
            }
            instrumentRepository.saveAll(instrumentList);
            logger.info("Eliminación de instrumentos por categoría completada con éxito. Categoría ID: " + id);
        } catch(CategoryNotFoundException e) {
            logger.error("Error al eliminar instrumentos por categoría: " + e.getMessage());
            throw e;
        } catch(Exception e) {
            logger.error("Error inesperado al eliminar instrumentos por categoría: " + e.getMessage(), e);
            throw e;
        }    }

    public Category updateCategory(CategoryDto categoryDto){
        logger.info("Starting category update process");
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getId());
        try{
            if(optionalCategory.isPresent()){
                Category category = optionalCategory.get();

                if(!category.getName().equals(categoryDto.getName())){
                    Optional<Category> sameNameCategory = categoryRepository.findByName(categoryDto.getName());
                    if(sameNameCategory.isPresent()){
                        logger.error("Ya existe una categoria con el mismo nombre: " + categoryDto.getName());
                        throw new DuplicateCategoryException("Ya existe una categoria con el mismo nombre");
                    }
                }

                category.setName(categoryDto.getName());

                this.imageService.updateImageCategory(category, categoryDto.getImageDto());
                logger.info("Categoria con ID " + category.getId() + "actualizada con éxito");
                return categoryRepository.save(category);
            }else{
                throw new NonExistentCategoryException("No se encontro la categoria con ID: " + categoryDto.getId());
            }

        }catch(DuplicateCategoryException | NonExistentCategoryException e){
            logger.error("Error al actualizar el instrumento: " + e.getMessage());
            throw e;
        }
        catch (Exception e){
            logger.error("Unexpected error while trying to update category");
            throw e;
        }
    }

    public List<Category> listCategories(){
        return categoryRepository.findAll(false);
    }

    public List<Instrument> getInstrumentsByCategories(List<Long> categoryIdList){
        List<Instrument> instrumentList = new ArrayList<Instrument>();
        List<Category> categoryList = this.categoryRepository.findAllById(categoryIdList);

        try{
            for ( Category category : categoryList){
                List<Instrument> instrumentListI = instrumentRepository.findAllByCategory(category);
                instrumentList.addAll(instrumentListI);
            }
            return instrumentList;

        }catch (Exception e){
            logger.error("Error inesperado al recuperar las lista de instrumentos por categorias");
            throw new InstrumentGetAllException("Error al recuperar la lista de instrumentos");
        }
    }

    public Category categoryById(Long id){
        logger.info("Iniciando la búsqueda de categoría con id: " + id);

        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> {
                logger.error("No se encontró la categoría con ID: " + id);
                return new CategoryNotFoundException("La categoria no existe");
            });
            logger.info("Búsqueda de categoría completada con éxito. ID: " + id);
            return category;
        }catch(Exception e){
            logger.error("Error inesperado al buscar la categoría: " + e.getMessage(), e);
            throw e; //TODO: sumar la excepcion customizada
        }
    }
}