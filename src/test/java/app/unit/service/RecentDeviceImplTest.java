package app.unit.service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecentDeviceImplTest {

    @Mock
    private RecentDevicesRepository recentDevicesRepository;
    @Mock
    private DeviceRepository deviceRepository;

    private RecentDevicesService recentDevicesService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recentDevicesService = new RecentDevicesServiceImpl(recentDevicesRepository,deviceRepository);
    }

    @Test
    public void shouldReturnRecentDevicesBydUserId() throws Exception {
        when(recentDevicesService.getRecentDevicesBydSingleId("USER_ID")).thenReturn(getRecentDevices());
        final List<RecentDevice> userId = recentDevicesService.getRecentDevicesBydUserId("USER_ID");

        assertEquals(getRecentDevices().size(),userId.size());
    }

    @Test
    public void shouldMarkToggleFavourite() throws Exception {
        RecentDevice recentDevice = spy(new RecentDevice());
        when(deviceRepository.findOneByDeviceId(1l)).thenReturn(getDevice());
        when(recentDevicesRepository.findOneBySingleIdAndRecentDevice_DeviceId(anyString(), anyLong())).thenReturn(recentDevice);

        recentDevicesService.toggleFavourite("USER_ID", 1l);

        verify(recentDevicesRepository, times(1)).saveAndFlush(any(RecentDevice.class));
    }

    @Test
    public void shouldUnmarkToggleFavourite() throws Exception {
        RecentDevice recentDevice = spy(new RecentDevice());
        recentDevice.setFavouriteDate(OffsetDateTime.now());
        when(deviceRepository.findOneByDeviceId(1l)).thenReturn(getDevice());
        when(recentDevicesRepository.findOneBySingleIdAndRecentDevice_DeviceId(anyString(), anyLong())).thenReturn(recentDevice);

        recentDevicesService.toggleFavourite("USER_ID", 1l);

        verify(recentDevicesRepository, times(1)).saveAndFlush(any(RecentDevice.class));
    }

    @Test
    public void shouldManageAddOrUpdateRecentDevices() throws Exception {
        when(deviceRepository.findOneByIpAddress(anyString())).thenReturn(getDevice());
        when(recentDevicesRepository.findOneBySingleIdAndRecentDevice_DeviceId(anyString(), anyLong())).thenReturn(null);
        recentDevicesService.addOrUpdateRecentDevices("USER_ID",getIpAddressSet());

        verify(recentDevicesRepository,times(1)).saveAndFlush(any(RecentDevice.class));
    }

    @Test
    public void shouldUpdateRecentDevice() throws Exception {
        RecentDevice recentDevice = spy(new RecentDevice());
        when(recentDevicesRepository.findOneBySingleIdAndRecentDevice_DeviceId(anyString(), anyLong())).thenReturn(recentDevice);
        recentDevicesService.addOrUpdateRecentDevice("USER_ID", getDevice());

        verify(recentDevicesRepository,times(1)).saveAndFlush(any(RecentDevice.class));
    }

    @Test
    public void shouldAddNewRecentDevice() throws Exception {
        when(recentDevicesRepository.findOneBySingleIdAndRecentDevice_DeviceId(anyString(), anyLong())).thenReturn(null);
        recentDevicesService.addOrUpdateRecentDevice("USER_ID", getDevice());

        verify(recentDevicesRepository,times(1)).saveAndFlush(any(RecentDevice.class));
    }

    @NotNull
    private List<RecentDevice> getRecentDevices(){
        return Arrays.asList(new RecentDevice(1l,"userId",getDevice(), OffsetDateTime.now(ZoneId.of("UTC")),OffsetDateTime.now(ZoneId.of("UTC"))));
    }

    @NotNull
    private Device getDevice(){
        return new Device(1l,"test device","1.0.0.0");
    }

    @NotNull
    private Set<String> getIpAddressSet() {
        Set<String> recentDevices = new HashSet<>();
        recentDevices.add("1.0.0.0");
        return recentDevices;
    }
}
