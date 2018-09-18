/*
 * Copyright 2012 modjn Project
 *
 * The modjn Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.enncloud.iot.iotgatewaymodbus.server;


import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ErrorResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.NoResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.response.ReadInputRegistersResponse;
import cn.enncloud.iot.iotgatewaymodbus.example.ClientForTests;
import cn.enncloud.iot.iotgatewaymodbus.example.ServerForTests;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 *
 * @author Andreas Gabriel <ag.gandev@googlemail.com>
 */
public class ModbusReadInputRegistersTest {

    ModbusClient modbusClient;
    ModbusServer modbusServer;

    @Before
    public void setUp() throws Exception {
        modbusServer = ServerForTests.getInstance().getModbusServer();
        modbusClient = ClientForTests.getInstance().getModbusClient();
    }

    @Test
    public void testReadInputRegisters() throws NoResponseException, ErrorResponseException, ConnectionException {
        ReadInputRegistersResponse readInputRegisters = modbusClient.readInputRegisters(12321, 10);

        assertNotNull(readInputRegisters);

        System.out.println(readInputRegisters);
    }
}
