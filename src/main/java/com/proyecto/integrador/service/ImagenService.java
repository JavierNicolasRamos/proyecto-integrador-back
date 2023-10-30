package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Imagen;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.ImagenGuardadoException;
import com.proyecto.integrador.repository.ImagenRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ImagenService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentoService.class);
    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Transactional
    public void guardarImagenesInstrumento(Instrumento instrumento){
        logger.info("Iniciando el proceso de guardar imágenes del instrumento con ID: " + instrumento.getId());
        try {
            List<Imagen> imagenes = instrumento.getImagen();

            for (Imagen imagen : imagenes) {
                imagen.setInstrumento(instrumento);
            }

            List<Imagen> imagenesGuardadas = imagenRepository.saveAll(imagenes);
            instrumento.setImagen(imagenesGuardadas);
            this.instrumentoRepository.save(instrumento);
            logger.info("Guardado de imágenes del instrumento completado con éxito. Instrumento ID: " + instrumento.getId());
        } catch (ImagenGuardadoException e){
            logger.error("Error al guardar las imágenes del instrumento: " + e.getMessage());
            throw new ImagenGuardadoException("Error al guardar las imágenes del instrumento", e);
        } catch (Exception e){
            logger.error("Error inesperado al guardar las imágenes del instrumento: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void actualizarImagenesInstrumento(Instrumento instrumento) {
        logger.info("Iniciando el proceso de actualizar imágenes del instrumento con ID: " + instrumento.getId());
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
            logger.info("Actualización de imágenes del instrumento completada con éxito. Instrumento ID: " + instrumento.getId());
        } catch (ImagenGuardadoException e) {
            logger.error("Error al actualizar las imágenes del instrumento: " + e.getMessage());
            throw new ImagenGuardadoException("Error al actualizar las imágenes del instrumento", e);
        } catch (Exception e){
            logger.error("Error inesperado al actualizar las imágenes del instrumento: " + e.getMessage(), e);
            throw e;
        }
    }
}
