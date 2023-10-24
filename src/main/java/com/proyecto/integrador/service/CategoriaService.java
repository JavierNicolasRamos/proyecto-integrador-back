package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService; //Se esta inyectando a si misma, eliminar la linea.

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository){ //La inyeccion mediante constructor de CategoriaRepository esta de mas, ya que se realiza la inyeccion directamente en la linea 12
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria crearCategoria(Categoria categoria){
        //Que el metodo reciba un dto, no la entidad categoria
        //Realizar una query en el metodo, que verifique que el nombre de la categoria no exista, en caso de existir, que arroje una excepcion personalizada.
        //Realizar un manejo de errores en el metodo con un try/catch
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> buscarCategoriaPorDescripcion(String descripcion){
        //Realizar un manejo de errores en el metodo con un try/catch
        //Que el metodo no retorne un Optional, sino la categoria
        //En caso de retornar una categoria, lanzar una excepcion personalizada notfound indicando que no existe la categoria.
        return categoriaRepository.findByDescripcion(descripcion);
    }

    //Realizar metodo count, que indique la cantidad de instrumentos que posee la categoria.
    //Realizar metodo de eliminacion de categoria, la eliminacion sera mediante operador logico. En caso de que se elimine, debe pasar todos los instrumentos de la categoria a eliminados.
}

