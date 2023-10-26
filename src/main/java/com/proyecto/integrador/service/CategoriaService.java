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
        //LOGGER.info("Categoria guardada correctamente")
        return categoria;
    }

    public Categoria buscarCategoriaPorDescripcion(String descripcion){ //Por el momento, retornar categoria, no categoriaDto
        Optional<Categoria> optionalCategoria = Optional.ofNullable(categoriaRepository.findByDescripcion(descripcion).orElseThrow(() ->
                   new CategoriaNotFoundException("La categoria no existe"))); //Donde se almacena el resultado de esta consulta?
            return optionalCategoria.get();
    }
    public Long contarInstrumentosPorCategoria(Long id){
        Optional<Categoria> optionalCategoria = Optional.ofNullable(categoriaRepository.findById(id).orElseThrow(() ->
                new CategoriaNotFoundException("La categoria no existe")));
        return instrumentoRepository.countAllByCategoriaAndEliminado(id);//Realizar un manejo de errores en el metodo con un try/catch

    }

    //Realizar metodo de eliminacion de categoria, la eliminacion sera mediante operador logico. En caso de que se elimine, debe pasar todos los instrumentos de la categoria a eliminados.
    public void eliminarInstrunmentosPorCategoria(Long id){
        //Verifico que la categoria existe
        Optional<Categoria> optionalCategoria = Optional.ofNullable(categoriaRepository.findById(id).orElseThrow(() ->
                new CategoriaNotFoundException("La categoria no existe")));

        Categoria categoria = optionalCategoria.get();//Si existe
        categoria.setEliminado(Boolean.TRUE);//Cambio el atributo a eliminado
        categoriaRepository.save(categoria);//Guardo el objeto con la categoria cambiada

        //FindAllById
        List<Instrumento> instrumentoList = instrumentoRepository.findAllByCategoria(categoria);
            //For
        for (Instrumento instrumento : instrumentoList ) {
            //modificar
            instrumento.setEliminado(Boolean.TRUE);
        }
        //saveAll
        instrumentoRepository.saveAll(instrumentoList);
    }


}

