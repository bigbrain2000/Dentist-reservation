package servicesTests;

import exceptions.fields.FieldNotCompletedException;
import exceptions.username.DentistServiceNameAlreadyExistsException;
import model.Service;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import services.DentistService;
import services.FileSystemService;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class DentistServiceTest {

    private final String NAME = "Extraction";
    private final float PRICE = 40.30f;

    @BeforeEach
    @DisplayName("Check if DentistServiceRepository path is correct created and database is initialized")
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER=".test-dentist_service_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        DentistService.initDatabase();
    }

    @AfterEach
    @DisplayName("Check if DentistService database is closed after every test")
    public void tearDown() {DentistService.getDatabase().close();}

    @Test
    @DisplayName("Check if DentistService database is initialized and there are no services available")
    public void testDentistServiceRepositoryIsInitializedAndNoServiceIsPersisted() {
        assertThat(DentistService.getServiceList()).isNotNull();
        assertThat(DentistService.getServiceList()).isEmpty();
    }

    @Test
    @DisplayName("Check if dentist`s service is successfully added to DentistService database")
    public void testDentistIsAddedToDB() throws FieldNotCompletedException, DentistServiceNameAlreadyExistsException {

        DentistService.addDentistService(NAME, PRICE);

        assertThat(DentistService.getServiceList()).isNotEmpty();
        assertThat(DentistService.getServiceList()).size().isEqualTo(1);
        Service service = DentistService.getServiceList().get(0);
        assertThat(service).isNotNull();

        assertAll("Should check all fields of a dentist service",
                () -> assertEquals(NAME, service.getName()),
                () -> assertEquals(PRICE, service.getPrice()));
    }

    @Test
    @DisplayName("Dentist`s service fields can not be empty")
    public void testAllFieldsAreCompleted() {
        assertThrows(FieldNotCompletedException.class, () -> DentistService.checkAllFieldsAreCompleted("", PRICE));
    }

    @Test
    @DisplayName("Multiple services can not have the same name")
    public void testDentistServiceNameAlreadyExist() {
        assertThrows(DentistServiceNameAlreadyExistsException.class, () -> {
            testDentistIsAddedToDB();
            DentistService.checkDentistServiceNameAlreadyExist(NAME);
        });
    }

    @Test
    @DisplayName("Check if service price is float")
    public void testPriceIsFloat() {
        assertTrue(DentistService.checkIfPriceIsAFloat(String.valueOf(PRICE)));
        assertFalse(DentistService.checkIfPriceIsAFloat("PRICE"));
        assertFalse(DentistService.checkIfPriceIsAFloat(""));
        assertFalse(DentistService.checkIfPriceIsAFloat("PRICE1"));
        assertFalse(DentistService.checkIfPriceIsAFloat(String.valueOf(true)));
        assertFalse(DentistService.checkIfPriceIsAFloat("P"));
    }

    @Test
    @DisplayName("Get a list with all services name")
    public void testDentistsFirstNameListIsReturned() throws FieldNotCompletedException, DentistServiceNameAlreadyExistsException {
        testDentistIsAddedToDB();
        //another example
        DentistService.addDentistService("Filling", 44.90f);

        ArrayList<String> dentistServiceNameList = DentistService.getDentistServiceNameList();

        assertEquals(NAME, dentistServiceNameList.get(0));
        assertEquals("Filling", dentistServiceNameList.get(1));
    }
}
