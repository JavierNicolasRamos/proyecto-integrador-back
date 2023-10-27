package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.CategoriaNotFoundException;
import com.proyecto.integrador.exception.DuplicateCategoriaException;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Transactional
    public Categoria crearCategoria(CategoriaDto categoriaDto){
        Optional<Categoria> optionalCategoria = categoriaRepository.findByDescripcion(categoriaDto.getDescripcion());
        if(optionalCategoria.isPresent()){
            throw new DuplicateCategoriaException("Ya existe una categoría con ese nombre: " + categoriaDto.getDescripcion());
        }
        Categoria categoria = new Categoria();
        categoria.setDescripcion(categoriaDto.getDescripcion());
        categoria.setEliminado(false);

        categoriaRepository.save(categoria);
        return categoria;
    }

    public Categoria buscarCategoriaPorDescripcion(String descripcion){
        Optional<Categoria> optionalCategoria = Optional.ofNullable(categoriaRepository.findByDescripcion(descripcion).orElseThrow(() ->
                   new CategoriaNotFoundException("La categoria no existe")));
            return optionalCategoria.get();
    }
    public Long contarInstrumentosPorCategoria(Long id){
        Optional<Categoria> optionalCategoria = Optional.ofNullable(categoriaRepository.findById(id).orElseThrow(() ->
                new CategoriaNotFoundException("La categoria no existe")));
        return instrumentoRepository.countAllByCategoriaAndEliminado(id);

    }

    public void eliminarInstrunmentosPorCategoria(Long id){
        Optional<Categoria> optionalCategoria = Optional.ofNullable(categoriaRepository.findById(id).orElseThrow(() ->
                new CategoriaNotFoundException("La categoria no existe")));

        Categoria categoria = optionalCategoria.get();
        categoria.setEliminado(true);
        categoriaRepository.save(categoria);//

        List<Instrumento> instrumentoList = instrumentoRepository.findAllByCategoria(categoria);
        for (Instrumento instrumento : instrumentoList ) {
            instrumento.setEliminado(true);
        }
        instrumentoRepository.saveAll(instrumentoList);
    }

    public List<Categoria> listarCategorias(){
        return categoriaRepository.findAllByEliminado(false);
    }


}

