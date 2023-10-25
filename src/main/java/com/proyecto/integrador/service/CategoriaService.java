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
    public CategoriaService(CategoriaRepository categoriaRepository){ //La inyeccion mediante constructor de CategoriaRepository esta de mas, ya que se realiza la inyeccion directamente en la linea 12//ELIMINAR
        this.categoriaRepository = categoriaRepository;//ELIMINAR
    }//ELIMINAR

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

    public CategoriaDto buscarCategoriaPorDescripcion(String descripcion) throws CategoriaNotFoundException { //Por el momento, retornar categoria, no categoriaDto.

        try {
            categoriaRepository.findByDescripcion(descripcion); //Donde se almacena el resultado de esta consulta?
        }catch(CategoriaNotFoundException e){
            throw new CategoriaNotFoundException("La categoria buscada no existe");
        }

        Optional<Categoria> categoria = categoriaRepository.findByDescripcion(descripcion);//Se estan realizando dos consultas, una en la linea 44 y otra en la linea 49. Eliminar de la linea 43 a la linea 47. Realizar validaciones
        //Con los metodos del objeto Optional, en caso de que el Optional no arroje resultados, lanzar la excepcion de la linea 46.
        CategoriaDto categoriaDto = new CategoriaDto();

        categoriaDto.setId(categoria.get().getId());
        categoriaDto.setDescripcion(categoria.get().getDescripcion());
        categoriaDto.setEliminado(categoria.get().getEliminado());
        return categoriaDto;
    }


    public Long contarInstrumentosPorCategoria(Long id){
        return instrumentoRepository.countAllByCategoria(id); //Realizar un manejo de errores en el metodo con un try/catch

    }

    //Realizar metodo de eliminacion de categoria, la eliminacion sera mediante operador logico. En caso de que se elimine, debe pasar todos los instrumentos de la categoria a eliminados.
    public Boolean eliminarInstrunmentosPorCategoria(Long id){ //Como indica el comentario anterior, la eliminacion sera mediante operador logico. Eliminar las querys deleteAllByCategoria y deleteById ya que estan realizando un insert no un update.
        //Elimino todos los instrumentos
        instrumentoRepository.deleteAllByCategoria(id);
        //Elimino la categoria
        categoriaRepository.deleteById(id);
        return true;
    }


}

