package app.controller;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/device")
public class DeviceController {

    private DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService){
        this.deviceService = deviceService;
    }

    @GetMapping(value = "/devicebyipaddress",produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Device getDevice(@RequestParam(value = "ipAddress", defaultValue = "") String ipAddress){
        return deviceService.getOneByIpAddress(ipAddress);
    }

    @GetMapping(value = "/devicesbyexpression",produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Device> getDevicesByExpression(@RequestParam(value = "expression", defaultValue = "") String expression){
        return deviceService.getDevicesByExpression(expression);
    }

    @GetMapping(value = "/devices",produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Device> getDevices(){
        return deviceService.getAll();
    }

    @PostMapping(value="device", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Device saveDevice(@RequestBody Device device){
        return deviceService.save(device);
    }


    @GetMapping(value = "/devicebydeviceid",produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Device getDeviceByDeviceId(@RequestParam(value = "deviceId") Long deviceId){
        return deviceService.getOneByDeviceId(deviceId);
    }

    @RequestMapping(value = "/search-devices", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity searchDevices(DeviceSpecification search) {
        Page<Device> page = deviceService.findDevices(search);
        return ResponseEntity.ok(page);
    }

    @RequestMapping(value = "/search-devices-by-params", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity searchDevicesByParams(
            @RequestParam(value = "ipaddress", required = false) String ipaddress, DeviceSpecification search) {

        Page<Device> page = deviceService.findDevicesByParams(search, ipaddress);
        return ResponseEntity.ok(page);
    }

}
