package com.proyecto.integrador.dto;

import com.proyecto.integrador.config.ImageFormatAndSize;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ImagenDto {
    private Long id;

    @NotNull(message = "La imagen no puede estar vacía")
    @ImageFormatAndSize(maxSize = 10 * 1024 * 1024, message = "La imagen debe ser un archivo de imagen (formatos permitidos: jpg, jpeg, png, gif) y no debe exceder 10 MB.")
    private MultipartFile imagen;
    private Boolean eliminado;
}
