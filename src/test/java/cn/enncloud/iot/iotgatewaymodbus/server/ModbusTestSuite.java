package cn.enncloud.iot.iotgatewaymodbus.server;


import cn.enncloud.iot.iotgatewaymodbus.example.ClientForTests;
import cn.enncloud.iot.iotgatewaymodbus.example.ServerForTests;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author ares
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({cn.enncloud.iot.iotgatewaymodbus.server.ModbusReadCoilsTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusWriteSingleRegisterTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusReadHoldingRegistersTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusReadDiscreteInputsTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusReadInputRegistersTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusWriteMultipleRegistersTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusWriteMultipleCoilsTest.class, cn.enncloud.iot.iotgatewaymodbus.server.ModbusWriteSingleCoilTest.class, cn.enncloud.iot.iotgatewaymodbus.example.ClientForTests.class})
public class ModbusTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        ClientForTests.getInstance().getModbusClient().close();
        ServerForTests.getInstance().getModbusServer().close();
    }

}
