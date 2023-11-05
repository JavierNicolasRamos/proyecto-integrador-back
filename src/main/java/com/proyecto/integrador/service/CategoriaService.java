package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.ImagenRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentoService.class);
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private InstrumentoRepository instrumentoRepository;
    @Autowired
    private ImagenService imagenService;

    @Transactional
    public Categoria crearCategoria(CategoriaDto categoriaDto){

        logger.info("Iniciando el proceso de creación de categoría con descripción: " + categoriaDto.getDescripcion());
        try {
            Optional<Categoria> optionalCategoria = categoriaRepository.findByDescripcion(categoriaDto.getDescripcion());
            if (optionalCategoria.isPresent()) {
                logger.error("Ya existe una categoria con ese nombre: " + categoriaDto.getDescripcion());
                throw new DuplicateCategoriaException("Ya existe una categoría con ese nombre: " + categoriaDto.getDescripcion());
            }
            Categoria categoria = new Categoria();
            categoria.setDescripcion(categoriaDto.getDescripcion());
            categoria.setEliminado(false);

            categoriaRepository.save(categoria);
            this.imagenService.saveImageCategory(categoria, categoriaDto.getImagen());
            logger.info("Categoría creada con éxito. Descripción: " + categoriaDto.getDescripcion());
            return categoria;
        } catch (DuplicateCategoriaException e) {
            logger.error("Error al crear la categoría: " + e.getMessage());
            throw e;
        } catch (Exception e){
            logger.error("Error inesperado al crear la categoría: " + e.getMessage(), e);
            throw e;
        }
    }

    public Categoria buscarCategoriaPorDescripcion(String descripcion){
        logger.info("Iniciando la búsqueda de categoría con descripción: " + descripcion);
        try {
            Categoria categoria = categoriaRepository.findByDescripcion(descripcion).orElseThrow(() ->
                    new CategoriaNotFoundException("La categoria no existe"));
            logger.info("Búsqueda de categoría completada con éxito. Descripción: " + descripcion);
            return categoria;
        }catch(CategoriaNotFoundException e){
            logger.error("Error al buscar la categoría: " + e.getMessage());
            throw e;
        }catch(Exception e){
            logger.error("Error inesperado al buscar la categoría: " + e.getMessage(), e);
            throw e;
        }
    }
    public Long contarInstrumentosPorCategoria(Long id){
        logger.info("Iniciando el conteo de instrumentos por categoría con ID: " + id);
        try {
            Categoria categoria = categoriaRepository.buscarPorId(id).orElseThrow(() ->
                    new CategoriaNotFoundException("La categoria no existe"));
            logger.info("Conteo de instrumentos por categoría completado con éxito.");
            return instrumentoRepository.countAllByCategoriaAndEliminado(categoria.getId());
        } catch (CategoriaNotFoundException e){
            logger.error("Error al contar instrumentos por categoría: " + e.getMessage());
            throw e;
        } catch (Exception e){
            logger.error("Error inesperado al contar instrumentos por categoría: " + e.getMessage(), e);
            throw e;
        }
    }

    public void deleteCategoria(Long id){
        logger.info("Iniciando el proceso de eliminación de instrumentos por categoría con ID: " + id);
        try {
            Categoria categoria = categoriaRepository.buscarPorId(id).orElseThrow(() ->
                    new CategoriaNotFoundException("La categoria no existe"));
            logger.info("Eliminando categoría con ID: " + categoria.getId());
            categoria.setEliminado(true);
            this.imagenService.deleteImagenCategoria(categoria);
            categoriaRepository.save(categoria);//

            List<Instrumento> instrumentoList = instrumentoRepository.findAllByCategoriaAndEliminado(categoria, false);
            for (Instrumento instrumento : instrumentoList) {
                logger.info("Eliminando instrumento con ID: " + instrumento.getId());
                instrumento.setEliminado(true);
            }
            instrumentoRepository.saveAll(instrumentoList);
            logger.info("Eliminación de instrumentos por categoría completada con éxito. Categoría ID: " + id);
        } catch(CategoriaNotFoundException e) {
            logger.error("Error al eliminar instrumentos por categoría: " + e.getMessage());
            throw e;
        } catch(Exception e) {
            logger.error("Error inesperado al eliminar instrumentos por categoría: " + e.getMessage(), e);
            throw e;
        }
    }

    public Categoria updateCategory(CategoriaDto categoriaDto){
        logger.info("Starting category update process");
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaDto.getId());
        try{
            if(optionalCategoria.isPresent()){
                Categoria categoria = optionalCategoria.get();

                if(!categoria.getDescripcion().equals(categoriaDto.getDescripcion())){
                    Optional<Categoria> sameDescriptionCategory = categoriaRepository.findByDescripcion(categoriaDto.getDescripcion());
                    if(sameDescriptionCategory.isPresent()){
                        logger.error("Ya existe una categoria con la misma descripcion: " + categoriaDto.getDescripcion());
                        throw new DuplicateCategoriaException("Ya existe una categoria con esa misma descripción");
                    }
                }

                categoria.setDescripcion(categoriaDto.getDescripcion());

                this.imagenService.updateImageCategory(categoria, categoriaDto.getImagen());
                logger.info("Categoria con ID " + categoria.getId() + "actualizada con éxito");
                return categoriaRepository.save(categoria);
            }else{
                throw new NonExistentCategoryException("No se encontro la categoria con ID: " + categoriaDto.getId());
            }


        }catch(DuplicateCategoriaException | NonExistentCategoryException e){
            logger.error("Error al actualizar el instrumento: " + e.getMessage());
            throw e;
        }
        catch (Exception e){
            logger.error("Unexpected error while trying to update category");
            throw e;
        }
    }


    public List<Categoria> listarCategorias(){
        return categoriaRepository.findAllByEliminado(false);
    }


    public List<Instrumento> getInstrumentsByCategories(List<Categoria> categoryList){
        List<Instrumento> instrumentList = new ArrayList<Instrumento>();

        try{
            for ( Categoria category : categoryList){
                List<Instrumento> instrumentListI = instrumentoRepository.findAllByCategoriaAndEliminado(category, false);
                instrumentList.addAll(instrumentListI);
            }
            return instrumentList;

        }catch (Exception e){
            logger.error("Error inesperado al recuperar las lista de instrumentos por categorias");
            throw new InstrumentoGetAllException("Error al recuperar la lista de instrumentos");
        }
    }


}

