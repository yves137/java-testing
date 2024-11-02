import org.aucalibray.aucaenum.LocationType;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.LocationDAO;
import org.aucalibray.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LocationDAOTest {
    private LocationDAO locationDAO;
    private DatabaseConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        locationDAO = new LocationDAO();
        locationDAO.dbConnection = mockDbConnection; // Injecting mock database connection
    }

    @Test
    public void testCreateLocations() throws SQLException {
        locationDAO.createLocations();

        verify(mockConnection, times(5)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(5)).executeUpdate();

        // Verify that locations are created with correct hierarchy
        verify(mockPreparedStatement).setString(2, "PRV-01");
        verify(mockPreparedStatement).setString(3, "Kigali");
        verify(mockPreparedStatement).setString(4, LocationType.PROVINCE.toString());
        verify(mockPreparedStatement).setString(2, "DIS-01");
        verify(mockPreparedStatement).setString(3, "Gasabo");
        verify(mockPreparedStatement).setString(4, LocationType.DISTRICT.toString());
        verify(mockPreparedStatement).setString(2, "SEC-01");
        verify(mockPreparedStatement).setString(3, "Kimironko");
        verify(mockPreparedStatement).setString(4, LocationType.SECTOR.toString());
        verify(mockPreparedStatement).setString(2, "CEL-01");
        verify(mockPreparedStatement).setString(3, "Kibagabaga");
        verify(mockPreparedStatement).setString(4, LocationType.CELL.toString());
        verify(mockPreparedStatement).setString(2, "VIL-01");
        verify(mockPreparedStatement).setString(3, "Karisimbi");
        verify(mockPreparedStatement).setString(4, LocationType.VILLAGE.toString());

    }

    @Test
    public void testFindProvince() {
        LocationDAO locationDAO = new LocationDAO();

        UUID provinceId = UUID.randomUUID();
        UUID districtId = UUID.randomUUID();
        UUID sectorId = UUID.randomUUID();
        UUID cellId = UUID.randomUUID();
        UUID villageId = UUID.randomUUID();

        // Mock locations
        Location province = new Location(provinceId, "PRV-01", "Kigali", LocationType.PROVINCE, null);
        Location district = new Location(districtId, "DIS-01", "Gasabo", LocationType.DISTRICT, provinceId);
        Location sector = new Location(sectorId, "SEC-01", "Kimironko", LocationType.SECTOR, districtId);
        Location cell = new Location(cellId, "CEL-01", "Kibagabaga", LocationType.CELL, sectorId);
        Location village = new Location(villageId, "VIL-01", "Kibagabaga", LocationType.VILLAGE, cellId);

        // Mock the findLocationById() to return locations as needed
        LocationDAO spyLocationDAO = spy(locationDAO);
        doReturn(province).when(spyLocationDAO).findLocationById(provinceId);
        doReturn(district).when(spyLocationDAO).findLocationById(districtId);
        doReturn(sector).when(spyLocationDAO).findLocationById(sectorId);
        doReturn(cell).when(spyLocationDAO).findLocationById(cellId);
        doReturn(village).when(spyLocationDAO).findLocationById(villageId);

        Location foundProvince = spyLocationDAO.findProvince(village);

        assertEquals(province, foundProvince, "The found province should match the expected province.");
    }
}
