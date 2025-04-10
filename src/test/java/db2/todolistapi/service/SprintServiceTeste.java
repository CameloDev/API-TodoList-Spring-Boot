package db2.todolistapi.service;


import db2.todolistapi.model.Sprint;
import db2.todolistapi.repository.SprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SprintServiceTeste {

    private SprintRepository sprintRepository;
    private SprintService sprintService;

    @BeforeEach
    void setUp() {
        sprintRepository = mock(SprintRepository.class);
        sprintService = new SprintService(sprintRepository);
    }

    @Test
    void testSaveSprint() {
        Sprint sprint = new Sprint();
        when(sprintRepository.save(sprint)).thenReturn(sprint);

        Sprint result = sprintService.saveSprint(sprint);

        assertEquals(sprint, result);
        verify(sprintRepository, times(1)).save(sprint);
    }

    @Test
    void testGetAllSprints() {
        Sprint sprint1 = new Sprint();
        Sprint sprint2 = new Sprint();
        List<Sprint> expected = Arrays.asList(sprint1, sprint2);

        when(sprintRepository.findAll()).thenReturn(expected);

        List<Sprint> result = sprintService.getAllSprints();

        assertEquals(expected, result);
        verify(sprintRepository, times(1)).findAll();
    }

    @Test
    void testGetActiveSprint_WhenFound() {
        Sprint activeSprint = new Sprint();
        activeSprint.setActive(true);
        when(sprintRepository.findByActiveTrue()).thenReturn(List.of(activeSprint));

        Sprint result = sprintService.getActiveSprint();

        assertEquals(activeSprint, result);
        verify(sprintRepository, times(1)).findByActiveTrue();
    }

    @Test
    void testGetActiveSprint_WhenNotFound() {
        when(sprintRepository.findByActiveTrue()).thenReturn(List.of());

        Sprint result = sprintService.getActiveSprint();

        assertNull(result);
        verify(sprintRepository, times(1)).findByActiveTrue();
    }

    @Test
    void testDeactivateOtherSprints() {
        Sprint sprint1 = new Sprint();
        sprint1.setId(1L);
        sprint1.setActive(true);

        Sprint sprint2 = new Sprint();
        sprint2.setId(2L);
        sprint2.setActive(true);

        when(sprintRepository.findByActiveTrue()).thenReturn(List.of(sprint1, sprint2));

        sprintService.deactivateOtherSprints(1L);

        assertTrue(sprint1.isActive());
        assertFalse(sprint2.isActive());
        verify(sprintRepository, times(1)).save(sprint2);
        verify(sprintRepository, never()).save(sprint1);
    }
}
