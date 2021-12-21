package com.my_virtual_space.staffing.services;

import com.my_virtual_space.staffing.ReplaceCamelCase;
import com.my_virtual_space.staffing.dtos.request.UserCredentials;
import com.my_virtual_space.staffing.entities.Figure;
import com.my_virtual_space.staffing.security.constants.SecurityConstants;
import com.my_virtual_space.staffing.security.entities.Role;
import com.my_virtual_space.staffing.security.services.AuthenticationService;
import com.my_virtual_space.staffing.security.services.RoleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
@DisplayNameGeneration(ReplaceCamelCase.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FigureServiceTest {
    private static final Logger log = LoggerFactory.getLogger(FigureServiceTest.class);

    private static final String name = "correct";
    private static final String fakeName = "error";
    private static final BigDecimal hourlyCost = BigDecimal.TEN;

    @Autowired
    private FigureService figureService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RoleService roleService;

    @BeforeAll
    void initializeAuthentication() throws UnexpectedException {
        if (roleService.findByValue(SecurityConstants.ROLE_USER_VALUE).isEmpty()) {
            roleService.save(new Role(SecurityConstants.ROLE_USER_VALUE));
        }

        try {
            authenticationService.signUp(new UserCredentials("username", "password"));
        } catch (Exception e) {
            authenticationService.signIn(new UserCredentials("username", "password"));
        }
    }

    public Stream<Arguments> getFigures() {
        Figure notSetName = new Figure();
        notSetName.setHourlyCost(hourlyCost);

        Figure notSetHourlyCost = new Figure();
        notSetHourlyCost.setName(name);

        return Stream.of(
                Arguments.of(null, fakeName),
                Arguments.of(new Figure(), fakeName),
                Arguments.of(notSetName, fakeName),
                Arguments.of(notSetHourlyCost, fakeName),
                Arguments.of(new Figure(name, hourlyCost), name));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class save {
        private void testError(Figure figure, String expected) {
            Figure result = new Figure(fakeName, hourlyCost);

            try {
                result = figureService.save(figure);
            } catch (Exception e) {
                log.debug(e.getMessage());
            }

            assert result.getName().equals(expected);
        }

        public Stream<Arguments> getSaveValues() {
            return getFigures();
        }

        @MethodSource("getSaveValues")
        @ParameterizedTest(name = "value {0}, expected {1}")
        void figure(Figure figure, String expected) {
            testError(figure, expected);
        }

        @NullAndEmptySource
        @ValueSource(strings = {"  "})
        @ParameterizedTest(name = "value {0}, expected error")
        void name(String value) {
            testError(new Figure(value, hourlyCost), fakeName);
        }

        @NullSource
        @ParameterizedTest(name = "value {0}, expected error")
        @ValueSource(strings = {"0", "9999999999.9999999999"})
        void hourlyCost(BigDecimal value) {
            testError(new Figure(name, value), fakeName);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class find {
        private Stream<Arguments> getValues() {
            return Stream.of(
                    Arguments.of(null, 1000000),
                    Arguments.of("", 1000000),
                    Arguments.of("   ", 1000000),
                    Arguments.of(fakeName, 0),
                    Arguments.of("stack", 1000000));
        }

        @MethodSource("getValues")
        @ParameterizedTest(name = "value {0}, excepted {1}")
        void byName(String value, int expected) {
            List<Figure> results = figureService.findAllByName(value);
            assert results.size() <= expected;
        }
    }

    private Stream<Arguments> getUpdateValues() {
        Figure notSetName = new Figure();
        notSetName.setId(UUID.randomUUID());
        notSetName.setHourlyCost(hourlyCost);

        Figure notSetHourlyCost = new Figure();
        notSetHourlyCost.setId(UUID.randomUUID());
        notSetHourlyCost.setName(name);

        Figure fakeId = new Figure(name, hourlyCost);
        fakeId.setId(UUID.randomUUID());

        Figure correctFigure = figureService.save(new Figure(fakeName, hourlyCost));
        correctFigure.setName(name);

        return Stream.of(
                Arguments.of(null, fakeName),
                Arguments.of(new Figure(), fakeName),
                Arguments.of(notSetName, fakeName),
                Arguments.of(notSetHourlyCost, fakeName),
                Arguments.of(new Figure(fakeName, hourlyCost), fakeName),
                Arguments.of(fakeId, fakeName),
                Arguments.of(correctFigure, name));
    }

    @MethodSource("getUpdateValues")
    @ParameterizedTest(name = "value {0}, excepted {1}")
    void update(Figure value, String expected) {
        Figure result = new Figure(fakeName, hourlyCost);

        try {
            result = figureService.update(value);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        assert result.getName().equals(expected);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class delete {
        private void testError(UUID id, String excepted) {
            Figure result = new Figure(fakeName, hourlyCost);

            try {
                result = figureService.deleteById(id);
            } catch (Exception e) {
                log.debug(e.getMessage());
            }

            assert result.getName().equals(excepted);
        }

        private Stream<Arguments> getValues() {
            Figure figure = figureService.save(new Figure(name, hourlyCost));

            return Stream.of(
                    Arguments.of(null, fakeName),
                    Arguments.of(figure.getId(), name),
                    Arguments.of(UUID.randomUUID(), fakeName));
        }

        @MethodSource("getValues")
        @ParameterizedTest(name = "value {0}, excepted {1}")
        void byId(UUID id, String expected) {
            testError(id, expected);
        }
    }
}