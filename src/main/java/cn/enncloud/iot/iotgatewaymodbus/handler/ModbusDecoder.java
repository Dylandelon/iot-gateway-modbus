package cn.enncloud.iot.iotgatewaymodbus.handler;


import cn.enncloud.iot.iotgatewaymodbus.entity.ModbusFrame;
import cn.enncloud.iot.iotgatewaymodbus.entity.ModbusFunction;
import cn.enncloud.iot.iotgatewaymodbus.entity.ModbusHeader;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.ModbusError;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.WriteSingleCoil;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.WriteSingleRegister;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.request.*;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.response.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static cn.enncloud.iot.iotgatewaymodbus.server.ModbusConstants.MBAP_LENGTH;


/**
 *
 * @author ag
 */
public class ModbusDecoder extends ByteToMessageDecoder {

    private final boolean serverMode;

    public ModbusDecoder(boolean serverMode) {
        this.serverMode = serverMode;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if (buffer.capacity() < MBAP_LENGTH + 1 /*Function Code*/) {
            return;
        }

        ModbusHeader mbapHeader = ModbusHeader.decode(buffer);

        short functionCode = buffer.readUnsignedByte();

        ModbusFunction function = null;
        switch (functionCode) {
            case ModbusFunction.READ_COILS:
                if (serverMode) {
                    function = new ReadCoilsRequest();
                } else {
                    function = new ReadCoilsResponse();
                }
                break;
            case ModbusFunction.READ_DISCRETE_INPUTS:
                if (serverMode) {
                    function = new ReadDiscreteInputsRequest();
                } else {
                    function = new ReadDiscreteInputsResponse();
                }
                break;
            case ModbusFunction.READ_INPUT_REGISTERS:
                if (serverMode) {
                    function = new ReadInputRegistersRequest();
                } else {
                    function = new ReadInputRegistersResponse();
                }
                break;
            case ModbusFunction.READ_HOLDING_REGISTERS:
                if (serverMode) {
                    function = new ReadHoldingRegistersRequest();
                } else {
                    function = new ReadHoldingRegistersResponse();
                }
                break;
            case ModbusFunction.WRITE_SINGLE_COIL:
                function = new WriteSingleCoil();
                break;
            case ModbusFunction.WRITE_SINGLE_REGISTER:
                function = new WriteSingleRegister();
                break;
            case ModbusFunction.WRITE_MULTIPLE_COILS:
                if (serverMode) {
                    function = new WriteMultipleCoilsRequest();
                } else {
                    function = new WriteMultipleCoilsResponse();
                }
                break;
            case ModbusFunction.WRITE_MULTIPLE_REGISTERS:
                if (serverMode) {
                    function = new WriteMultipleRegistersRequest();
                } else {
                    function = new WriteMultipleRegistersResponse();
                }
                break;
        }

        if (ModbusFunction.isError(functionCode)) {
            function = new ModbusError(functionCode);
        } else if (function == null) {
            function = new ModbusError(functionCode, (short) 1);
        }

        function.decode(buffer.readBytes(buffer.readableBytes()));

        ModbusFrame frame = new ModbusFrame(mbapHeader, function);

        out.add(frame);
    }
}
