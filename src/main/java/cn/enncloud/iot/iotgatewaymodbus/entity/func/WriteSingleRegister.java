package cn.enncloud.iot.iotgatewaymodbus.entity.func;

import static cn.enncloud.iot.iotgatewaymodbus.entity.ModbusFunction.WRITE_SINGLE_REGISTER;

/**
 *
 * @author ares
 */
public class WriteSingleRegister extends AbstractFunction {

    //registerAddress;
    //registerValue;
    public WriteSingleRegister() {
        super(WRITE_SINGLE_REGISTER);
    }

    public WriteSingleRegister(int outputAddress, int value) {
        super(WRITE_SINGLE_REGISTER, outputAddress, value);
    }

    public int getRegisterAddress() {
        return address;
    }

    public int getRegisterValue() {
        return value;
    }

    @Override
    public String toString() {
        return "WriteSingleInputRegister{" + "registerAddress=" + address + ", registerValue=" + value + '}';
    }
}
