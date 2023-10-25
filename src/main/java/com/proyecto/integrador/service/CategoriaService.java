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

import java.util.Optional;
@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository){ //La inyeccion mediante constructor de CategoriaRepository esta de mas, ya que se realiza la inyeccion directamente en la linea 12
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Categoria crearCategoria(CategoriaDto categoriaDto){
        Optional<Categoria> optionalCategoria = categoriaRepository.findByDescripcion(categoriaDto.getDescripcion());
        if(optionalCategoria.isPresent()){
            throw new DuplicateCategoriaException("Ya existe una categoría con ese nombre: " + categoriaDto.getDescripcion());
        }
        Categoria categoria = new Categoria();
        categoria.setDescripcion(categoriaDto.getDescripcion());

        categoriaRepository.save(categoria);
        //LOGGER.info("Categoria guardada correctamente")
        return categoria;
    }

    public CategoriaDto buscarCategoriaPorDescripcion(String descripcion) throws CategoriaNotFoundException {
        //Realizar un manejo de errores en el metodo con un try/catch
        //Que el metodo no retorne un Optional, sino la categoria
        //En caso de retornar una categoria, lanzar una excepcion personalizada notfound indicando que no existe la categoria.
        try {
            categoriaRepository.findByDescripcion(descripcion);
        }catch(CategoriaNotFoundException e){
            throw new CategoriaNotFoundException("La categoria buscada no existe");
        }

        Optional<Categoria> categoria = categoriaRepository.findByDescripcion(descripcion);
        CategoriaDto categoriaDto = new CategoriaDto();

        categoriaDto.setId(categoria.get().getId());
        categoriaDto.setDescripcion(categoria.get().getDescripcion());
        categoriaDto.setEliminado(categoria.get().getEliminado());
        return categoriaDto;
    }


    //Realizar metodo count, que indique la cantidad de instrumentos que posee la categoria.
    public Long contarInstrumentosPorCategoria(Long id){
        //Utilizo el instrumentoRepository para contar la cantidad de instrumentos con una categoria
        return instrumentoRepository.countAllByCategoria(id);

    }

    //Realizar metodo de eliminacion de categoria, la eliminacion sera mediante operador logico. En caso de que se elimine, debe pasar todos los instrumentos de la categoria a eliminados.

    public Boolean eliminarInstrunmentosPorCategoria(Long id){
        //Elimino todos los instrumentos
        instrumentoRepository.deleteAllByCategoria(id);
        //Elimino la categoria
        categoriaRepository.deleteById(id);
        return true;
    }


}

