package com.my_virtual_space.staffing.controllers;

import com.my_virtual_space.staffing.entities.Figure;
import com.my_virtual_space.staffing.services.FigureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/figure")
public class FigureApi {

    private final FigureService figureService;

    public FigureApi(FigureService figureService) {
        this.figureService = figureService;
    }

    @PostMapping
    public ResponseEntity<Figure> createFigure(@RequestBody Figure figure) {
        return ResponseEntity.ok(figureService.save(figure));
    }

    @GetMapping
    public ResponseEntity<List<Figure>> findAllByFilter(String name) {
        return ResponseEntity.ok(figureService.findAllByName(name));
    }

    @PutMapping
    public ResponseEntity<Figure> updateFigure(@RequestBody Figure figure) {
        return ResponseEntity.ok(figureService.update(figure));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Figure> deleteFigureById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(figureService.deleteById(id));
    }
}
