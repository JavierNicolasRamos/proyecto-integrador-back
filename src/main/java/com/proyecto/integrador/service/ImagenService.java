package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Imagen;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.ImagenGuardadoException;
import com.proyecto.integrador.repository.ImagenRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Transactional
    public void guardarImagenesInstrumento(Instrumento instrumento){
        try {
            List<Imagen> imagenes = instrumento.getImagen();

            for (Imagen imagen : imagenes) {
                imagen.setInstrumento(instrumento);
            }

            List<Imagen> imagenesGuardadas = imagenRepository.saveAll(imagenes);
            instrumento.setImagen(imagenesGuardadas);
            this.instrumentoRepository.save(instrumento);
        }
        catch (Exception e){
            throw new ImagenGuardadoException("Error al guardar las imágenes del instrumento", e);
        }
    }

    @Transactional
    public void actualizarImagenesInstrumento(Instrumento instrumento) {
        try {
            List<Imagen> imagenes = instrumento.getImagen();

            for (Imagen imagen : imagenes) {
                imagen.setInstrumento(instrumento);

                if (imagen.getId() != null) {
                    imagenRepository.save(imagen);
                } else {
                    Imagen nuevaImagen = new Imagen();
                    nuevaImagen.setImagen(imagen.getImagen());
                    nuevaImagen.setInstrumento(instrumento);

                    imagenRepository.save(nuevaImagen);
                }
            }

            instrumento.setImagen(imagenes);
            this.instrumentoRepository.save(instrumento);
        } catch (Exception e) {
            throw new ImagenGuardadoException("Error al guardar las imágenes del instrumento", e);
        }
    }
}
