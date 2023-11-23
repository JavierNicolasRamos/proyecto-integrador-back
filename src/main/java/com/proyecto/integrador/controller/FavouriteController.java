package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.FavouriteDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/favourite")
@Controller
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    @PutMapping("/add")
    public ResponseEntity<List<Instrument>> addFavourite(@RequestBody FavouriteDto favouriteDto){
        return ResponseEntity.ok(this.favouriteService.addFavourite(favouriteDto));
    }

    @PutMapping("/remove")
    public ResponseEntity<List<Instrument>> removeFavourite(@RequestBody FavouriteDto favouriteDto){
        return ResponseEntity.ok(this.favouriteService.removeFavourite(favouriteDto));
    }

    @GetMapping
    public ResponseEntity<List<Instrument>> getFavourites(@RequestParam String email){
        return ResponseEntity.ok(this.favouriteService.getFavourites(email));
    }
}
