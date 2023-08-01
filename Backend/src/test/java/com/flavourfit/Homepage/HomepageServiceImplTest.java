package com.flavourfit.HomePage;

import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Homepage.HomepageDaoImpl;
import com.flavourfit.Homepage.HomepageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class HomepageServiceImplTest {
    @Mock
    private HomepageDaoImpl homepageDao;

    @InjectMocks
    private HomepageServiceImpl homepageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //    trackersController = new TrackersController(calorieHistoryService, null, authService);
    }

    @Test
    public void getExerciseByUserTest() throws SQLException {
        int userId = 7; // Replace with a valid user ID for testing
        List<RoutineDTO> mockRoutines = new ArrayList<>();
        RoutineDTO mockRoutine = new RoutineDTO();
        mockRoutine.setRoutineDescription("This is description");
        mockRoutine.setRoutineName("Routine name");
        mockRoutine.setTips("This is tips");
        mockRoutines.add(mockRoutine);
        String mockQuote = "This is a mock quote of the day.";

        when(homepageDao.getRoutinesByUser(userId)).thenReturn(mockRoutines);
        when(homepageDao.getQuoteOfTheDay()).thenReturn(mockQuote);

        HashMap<String, Object> result = homepageService.getExerciseByUser(userId);

        // Assert the result contains the expected routines and quoteOfTheDay
        assertNotNull(result);
        assertEquals(2, result.size()); // Assuming routines is a List<RoutineDTO>
        assertEquals(mockQuote, result.get("quoteOfTheDay"));
    }
}
