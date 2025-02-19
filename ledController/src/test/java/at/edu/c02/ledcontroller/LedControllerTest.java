package at.edu.c02.ledcontroller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class LedControllerTest {

    /**
     * This test is just here to check if tests get executed. Feel free to delete it when adding your own tests.
     * Take a look at the stack calculator tests again if you are unsure where to start.
     */
    @Test
    public void dummyTest() {
        assertEquals(1, 1);
    }


    @Test
    public void testGroupLeds() throws IOException {
        ApiService apiService = mock(ApiService.class);
        LedController ledController = new LedControllerImpl(apiService);

        ledController.getGroupLeds();
        verify(apiService).getLights();
    }

    @Test
    public void testLedSwitchOn() throws IOException {
        LedController ledController = new LedControllerImpl(new ApiServiceImpl());

        ledController.setLed(24, "#80f", false);
        assertFalse(ledController.getStatus(24));
    }

    @Test
    public void testTurnOffAllLeds() throws IOException {
        ApiService apiService = mock(ApiService.class);
        LedController ledController = new LedControllerImpl(apiService);

        ledController.turnAllOff();
        verify(apiService).turnAllOff();
    }

    @Test
    public void testLauflicht() throws Exception {
        // "Echte" Instanz
        ApiServiceImpl realService = new ApiServiceImpl();
        // Spy darauf erstellen
        ApiServiceImpl spyService = Mockito.spy(realService);
        // Methoden stubbing:
        doReturn(true).when(spyService).setLed(anyInt(), anyString(), anyBoolean());

        // Jetzt dein eigentlicher Aufruf
        spyService.lauflicht("#FF0000", 2);

        // Verifications
        verify(spyService, times(2)).turnAllOff();
        verify(spyService, times(48)).setLed(anyInt(), anyString(), anyBoolean());
    }

}
