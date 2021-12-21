package com.my_virtual_space.staffing.services;

import com.my_virtual_space.staffing.entities.Figure;
import com.my_virtual_space.staffing.repositories.FigureRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FigureService {

    private final FigureRepository figureRepository;

    public FigureService(FigureRepository figureRepository) {
        this.figureRepository = figureRepository;
    }

    private void checkFigure(Figure figure, boolean withId) {
        if (Objects.isNull(figure) || Boolean.TRUE.equals(withId) ? Objects.isNull(figure.getId()) : Objects.nonNull(figure.getId())) {
            throw new IllegalArgumentException("Figura non valida.");
        }

        if (Objects.isNull(figure.getName()) || figure.getName().trim().length() == 0) {
            throw new IllegalArgumentException("Nome non valido.");
        }

        if (Objects.isNull(figure.getHourlyCost()) || BigDecimal.ZERO.compareTo(figure.getHourlyCost()) >= 0) {
            throw new IllegalArgumentException("Costo orario non valido.");
        }

        if (BigDecimal.valueOf(1000).compareTo(figure.getHourlyCost()) <= 0) {
            throw new IllegalArgumentException("Costo orario troppo alto.");
        }
    }

    public Figure save(Figure figure) {
        return privateSave(figure, Boolean.FALSE);
    }

    private Figure privateSave(Figure figure, boolean withId) {
        checkFigure(figure, withId);

        return figureRepository.save(figure);
    }

    public List<Figure> findAllByName(String keyword) {
        return figureRepository.findByAttributeContainsText("name",
                Objects.isNull(keyword) || keyword.trim().length() == 0 ? "" : keyword);
    }

    public Figure update(Figure figure) {
        checkFigure(figure, Boolean.TRUE);

        return figureRepository.findById(figure.getId()).map(oldFigure -> {

            if (oldFigure.getProjects().size() > 0) {
                throw new IllegalArgumentException("Impossibile modificare l'importo della figura in quanto è collegata ad almeno un progetto");
            }

            return privateSave(figure, Boolean.TRUE);
        }).orElseThrow(() -> new EntityNotFoundException("Figura non trovata."));
    }

    public Figure deleteById(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id non valido.");
        }

        return figureRepository.findById(id).map(figure -> {
            figureRepository.deleteById(id);

            return figure;
        }).orElseThrow(() -> new EntityNotFoundException("Figura non trovata."));
    }
}