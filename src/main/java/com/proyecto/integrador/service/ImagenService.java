package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.dto.ImagenDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Imagen;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.ImagenGuardadoException;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.ImagenRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImagenService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentoService.class);
    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private S3Service s3Service;

    public void guardarImagenesInstrumento(Instrumento instrumento, List<ImagenDto> imagenDtos) {
        logger.info("Iniciando el proceso de guardar imágenes del instrumento con ID: " + instrumento.getId());
        try {
            List<Imagen> imagenesGuardadas = imagenDtos.stream()
                    .map(imagenDto -> {
                        Imagen imagen = new Imagen();
                        imagen.setInstrumento(instrumento);
                        imagen.setImagen(this.s3Service.uploadFile(imagenDto.getImagen()));
                        return imagen;
                    })
                    .collect(Collectors.toList());

            this.imagenRepository.saveAll(imagenesGuardadas);
            instrumento.setImagen(imagenesGuardadas);
            this.instrumentoRepository.save(instrumento);
            logger.info("Guardado de imágenes del instrumento completado con éxito. Instrumento ID: " + instrumento.getId());
        } catch (ImagenGuardadoException e) {
            logger.error("Error al guardar las imágenes del instrumento: " + e.getMessage());
            throw new ImagenGuardadoException("Error al guardar las imágenes del instrumento", e);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar las imágenes del instrumento: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void actualizarImagenesInstrumento(Instrumento instrumento, List<ImagenDto> imagenDtos) {
        logger.info("Iniciando el proceso de actualizar imágenes del instrumento con ID: " + instrumento.getId());
        try {
            List<Imagen> imagenesGuardadas = new ArrayList<>();

            for (ImagenDto imagenDto : imagenDtos) {
                if (imagenDto.getId() != null && imagenDto.getEliminado()) {
                    Optional<Imagen> imagenEliminar = imagenRepository.buscarPorId(imagenDto.getId());
                    if (imagenEliminar.isPresent()) {
                        Imagen imagenEliminada = imagenEliminar.get();
                        imagenEliminada.setEliminado(true);
                        this.s3Service.deleteFileFromS3Bucket(imagenEliminada.getImagen());
                        imagenRepository.save(imagenEliminada);
                    }
                } else {
                    Imagen imagen = new Imagen();
                    imagen.setInstrumento(instrumento);
                    MultipartFile nuevaImagen = imagenDto.getImagen();

                    if (imagenDto.getId() != null) {
                        Optional<Imagen> imagenOptional = imagenRepository.buscarPorId(imagenDto.getId());
                        if (imagenOptional.isPresent()) {
                            Imagen imagenExistente = imagenOptional.get();
                            this.s3Service.deleteFileFromS3Bucket(imagenExistente.getImagen());
                            imagenExistente.setImagen(this.s3Service.uploadFile(nuevaImagen));
                            imagenRepository.save(imagenExistente);
                            imagenesGuardadas.add(imagenExistente);
                        }
                    } else {
                        imagen.setImagen(this.s3Service.uploadFile(nuevaImagen));
                        imagenRepository.save(imagen);
                        imagenesGuardadas.add(imagen);
                    }
                }
            }

            instrumento.setImagen(imagenesGuardadas);
            this.instrumentoRepository.save(instrumento);
            logger.info("Actualización de imágenes del instrumento completada con éxito. Instrumento ID: " + instrumento.getId());
        } catch (ImagenGuardadoException e) {
            logger.error("Error al actualizar las imágenes del instrumento: " + e.getMessage());
            throw new ImagenGuardadoException("Error al actualizar las imágenes del instrumento", e);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar las imágenes del instrumento: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void saveImageCategory(Categoria categoria, ImagenDto imagenDto) {
        logger.info("Iniciando el proceso de guardo de imagen de categoría con ID:" + categoria.getId());
        try {
            Imagen image = new Imagen();
            image.setImagen(this.s3Service.uploadFile(imagenDto.getImagen()));
            this.imagenRepository.save(image);

            Imagen savedImage = imagenRepository.save(image);
            categoria.setImagen(savedImage);

            logger.info("Guardado de imagen de la categoría completado con éxito. Categoria ID: " + categoria.getId());
        } catch (ImagenGuardadoException e) {
            logger.error("Error al guardar la imagen de la categoria: " + e.getMessage());
            throw new ImagenGuardadoException("Error al guardar la imagen de la categoría", e);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar la imagen de la categoría: " + e.getMessage(), e);
            throw e;
        }

    }

    public void deleteImagenCategoria(Categoria categoria){
        try {
            Optional<Imagen> imagenEliminar = imagenRepository.buscarPorId(categoria.getImagen().getId());
            Imagen imagenEliminada = imagenEliminar.get();
            imagenEliminada.setEliminado(true);
            this.s3Service.deleteFileFromS3Bucket(imagenEliminada.getImagen());
            imagenRepository.save(imagenEliminada);
        }
        catch (Exception e){
            throw e; //Agregar expecion personalizada
        }
    }

    @Transactional
    public void updateImageCategory(Categoria categoria, ImagenDto imagenDto) {
        logger.info("Iniciando el proceso de actualizacion de imagen de categoría con ID:" + categoria.getId());
        try {
            Imagen imagenGuardada = new Imagen();

            if (imagenDto.getId() != null && imagenDto.getEliminado()) {
                Optional<Imagen> imagenEliminar = imagenRepository.buscarPorId(imagenDto.getId());
                if (imagenEliminar.isPresent()) {
                    Imagen imagenEliminada = imagenEliminar.get();
                    imagenEliminada.setEliminado(true);

                    this.s3Service.deleteFileFromS3Bucket(imagenEliminada.getImagen());
                    imagenRepository.save(imagenEliminada);
                }
            } else {
                MultipartFile nuevaImagen = imagenDto.getImagen();

                if (imagenDto.getId() != null) {
                    Optional<Imagen> imagenOptional = imagenRepository.buscarPorId(imagenDto.getId());
                    if (imagenOptional.isPresent()) {
                        Imagen imagenExistente = imagenOptional.get();
                        this.s3Service.deleteFileFromS3Bucket(imagenExistente.getImagen());
                        imagenExistente.setImagen(this.s3Service.uploadFile(nuevaImagen));
                        imagenRepository.save(imagenExistente);
                        imagenGuardada = imagenExistente;
                    }
                    else {
                        throw new ImagenGuardadoException("Error al guardar la imagen de la categoría, el id de la imagen es null");
                    }
                }
            }

            categoria.setImagen(imagenGuardada);

            logger.info("Guardado de imagen de la categoría completado con éxito. Categoria ID: " + categoria.getId());
        } catch (ImagenGuardadoException e) {
            logger.error("Error al guardar la imagen de la categoria: " + e.getMessage());
            throw new ImagenGuardadoException("Error al guardar la imagen de la categoría", e);
        }
    }

}
