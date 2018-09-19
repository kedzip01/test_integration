package app.it.controller;


import app.it.config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;


public class DeviceControllerIT extends TestConfig {

    private static final String ADDRESS_IP = "127.0.0.1";

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @Sql(scripts = {"/database/device/create-device.sql","/database/insert-device/device.sql"})
    public void shouldReturnDeviceForIpAddress() throws Exception {
        final ResponseEntity<Device> deviceResponseEntity = restTemplate.getForEntity(host +":"+port+contextPath+"/rest/device/devicebyipaddress?ipAddress=127.0.0.1", Device.class);
        Assert.assertEquals(200, deviceResponseEntity.getStatusCodeValue());
        Assert.assertEquals(deviceRepository.findOneByDeviceId(1l).getIpAddress(), deviceResponseEntity.getBody().getIpAddress());
    }

    @Test
    @Sql(scripts = {"/database/device/create-device.sql","/database/insert-device/device.sql"})
    public void shouldReturnAllDevices() throws Exception {
        final ResponseEntity<Device[]> devicesResponseEntity = restTemplate.getForEntity(host+":"+port+contextPath+"/rest/device/devices", Device[].class);
        Assert.assertEquals(200, devicesResponseEntity.getStatusCodeValue());
        Assert.assertEquals(deviceRepository.findAll().size(), devicesResponseEntity.getBody().length);
    }

    @Test
    @Sql(scripts = {"/database/device/create-device.sql","/database/insert-device/device.sql"})
    public void shouldReturnDevicesForExpression() throws Exception {
        final ResponseEntity<Device[]> devicesResponseEntity = restTemplate.getForEntity(host+":"+port+contextPath+"/rest/device/devicesbyexpression?expression=test", Device[].class);
        Assert.assertEquals(200, devicesResponseEntity.getStatusCodeValue());
        Assert.assertEquals(deviceRepository.findDevicesByIpAddressOrCityOrRoomOrBuildingOrHashTagName("test").size(), devicesResponseEntity.getBody().length);
    }

//    @Test
//    @Sql(scripts = {"/database/device/create-device.sql","/database/insert-device/device.sql","/database/meeting-device/create-db.sql","/database/meeting-device/meeting-device-db.sql"})
//    public void shouldReturnDevicesForMeetingId() throws Exception {
//        final ResponseEntity<MeetingDeviceDto[]> devicesResponseEntity = restTemplate.getForEntity(host + ":"+port+contextPath+"/rest/device/devicesbymeetingid/1", MeetingDeviceDto[].class);
//        Assert.assertEquals(200, devicesResponseEntity.getStatusCodeValue());
//        Assert.assertEquals(deviceRepository.findDevicesForMeetingId(1l).size(), devicesResponseEntity.getBody().length);
//    }

    @Test
    @Sql(scripts = {"/database/device/create-device.sql","/database/insert-device/device.sql"})
    public void shouldReturnSavedDevice() throws Exception{
        final Device device = Device.builder().ipAddress(ADDRESS_IP).name("").build();
        final Device postDevice = restTemplate.postForObject(host +":"+port+contextPath+"/rest/device/device", device, Device.class);
        Assert.assertEquals(deviceRepository.findOneByIpAddress(ADDRESS_IP).getIpAddress(),postDevice.getIpAddress());
    }
}