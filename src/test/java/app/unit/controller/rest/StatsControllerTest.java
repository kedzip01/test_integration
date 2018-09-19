package app.unit.controller.rest;

import java.util.Collections;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class StatsControllerTest {
    
    @InjectMocks
    private StatsController controller;

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private AppointmentServiceImpl AppointmentService;


    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new StatsController(authenticationService, AppointmentService);
    }

    @Test
    public void shouldReturnOneAppointment() throws Exception {
        when(AppointmentService.getResultFromMediator(anyObject())).thenReturn(getMeetings());
        final List<StatsDto> statisticsByDates = controller.getStatisticsByDates("", "");
        assertEquals(1, statisticsByDates.size());
    }

    @Test
    public void shouldReturnOneAppointment2() throws Exception {
        when(AppointmentService.getResult(anyObject())).thenReturn(getAppointment());
        final List<StatsDto> statisticsByDates = controller.getStatisticsByAppointmentId("");
        assertEquals(1, statisticsByDates.size());
    }

    @Test
    public void shouldReturnOneAppointment3() throws Exception {
        when(AppointmentService.getResult(anyObject())).thenReturn(getAppointment());
        final List<StatsDto> statisticsByDates = controller.getStatisticsByUserName("");
        assertEquals(1, statisticsByDates.size());
    }

    @Test
    public void shouldReturnOneAppointment4() throws Exception {
        when(AppointmentService.getResultWithPagination(anyObject())).thenReturn(getAppointmentWithPaging());
        final StatsWithPagingDto statisticsByDatesWithPaging = controller.getStatisticsByDatesWithPaging("", "", "", 1, 1);
        assertEquals(1, statisticsByDatesWithPaging.getSize());
    }

    private List<StatsDto> getAppointment(){
        return Collections.singletonList(new StatsDto("id", "appId", "userName"));
    }

    private StatsWithPagingDto getAppointmentWithPaging(){
        return new StatsWithPagingDto(getAppointment(),1,1,true,1,1,getSortDto(),true,1);
    }

    private List<SortDto> getSortDto(){
        return Collections.singletonList(new SortDto("", "", true, "", true, true));
    }


}
