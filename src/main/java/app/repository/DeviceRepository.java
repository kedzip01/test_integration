package app.repository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long>, JpaSpecificationExecutor<Device> {

    Device findOneByIpAddress(String ipAddress);

    Device findOneByDeviceId(Long findOneByDeviceId);

    @Query("Select d from Device d where lower(d.ipAddress) like %:exp% ")
    List<Device> findDevicesByIpAddressOrCityOrRoomOrBuildingOrHashTagName(@Param("exp")String expression);

    Device save(Device device);

    @Query("select new app.domain.device.MeetingDeviceDto(d.deviceId,md.meetingId,d.ipAddress) from Device d left outer join AppointmentDevices md on d.deviceId=md.deviceId where md.appointmentId = :appointmentID")
    List<AppointmentDeviceDto> findDevicesForAppointmentId(@Param("appointmentID") Long appointmentID);

    List<Device> findAllByIpAddressIn(List<String> ips);

    List<Device> findAllByCityAndBuildingAndFloor(String city, String building, int floor);

    @Query("SELECT DISTINCT new app.domain.device.tree.DeviceLocation(" +
            "d.city, " +
            "d.building, " +
            "d.floor) " +
            "from Device d")
    List<DeviceLocation> findDevicesLocations();
}
