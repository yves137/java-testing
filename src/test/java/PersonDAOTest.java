import org.aucalibray.dao.PersonDAO;
import org.aucalibray.dao.UserDAO;
import org.aucalibray.model.Location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonDAOTest {

    private PersonDAO personDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = Mockito.mock(UserDAO.class);
        personDAO = new PersonDAO();
        personDAO.userdao = userDAO;
    }

    @Test
    public void testGetPersonProvinceName() {
        // Setup mock location data
        String personId = "USR-01";
        String expectedProvinceName = "Kigali";
        Location mockLocation = new Location();
        mockLocation.setLocationName(expectedProvinceName);

        // Define behavior for the mock
        Mockito.when(userDAO.getUserProvince(personId)).thenReturn(mockLocation);

        // Execute the method
        String provinceName = personDAO.getPersonProvinceName(personId);

        // Verify and assert
        assertEquals(expectedProvinceName, provinceName);
        Mockito.verify(userDAO).getUserProvince(personId);
    }
}