package cn.enncloud.iot.iotgatewaymodbus.http.service;


import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.impl.DmsGatewayServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "iot-service-meta-manager-v1", fallback = DmsGatewayServiceImpl.class)
@FeignClient(url = "http://127.0.0.1:8240",name = "aaa",fallback = DmsGatewayServiceImpl.class)
public interface DmsGatewayService {

    /**
     * 获取网关信息
     *
     * @param id        网关id
     * @param serialNum 网关序列号
     * @param domain    业务域
     * @param name      网关名称
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/meta/dmsGateway", method = RequestMethod.GET)
    DataRespBody dmsGatewayGet(@RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "serialNum", required = false) String serialNum,
                               @RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "name", required = false) String name) throws Exception;

    /**
     * 获取设备信息
     *
     * @param id           设备id
     * @param dmsGatewayId 网关id
     * @param serialNum    设备序列号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/meta/dmsDevice", method = RequestMethod.GET)
    DataRespBody dmsDeviceGet(@RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "dmsGatewayId", required = false) Long dmsGatewayId,
                              @RequestParam(value = "serialNum", required = false) String serialNum) throws Exception;

    /**
     * 获取点表信息
     *
     * @param dmsDeviceId 设备id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/meta/dmsPointForGateway", method = RequestMethod.GET)
    DataRespBody dmsPointForGateway(@RequestParam(value = "dmsDeviceId", required = false) Long dmsDeviceId) throws Exception;

    /**
     * 获取376.1协议信息
     *
     * @param protocolTypeId 协议类型id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/dmsXinao3761ProtocolPoint", method = RequestMethod.GET)
    DataRespBody dmsXinao3761ProtocolPoint(@RequestParam(value = "protocolTypeId", required = false) Long protocolTypeId) throws Exception;

}
