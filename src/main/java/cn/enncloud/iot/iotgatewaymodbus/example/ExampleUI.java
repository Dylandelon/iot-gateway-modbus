package cn.enncloud.iot.iotgatewaymodbus.example;



import cn.enncloud.iot.iotgatewaymodbus.entity.ModbusFrame;
import cn.enncloud.iot.iotgatewaymodbus.entity.ModbusFunction;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ErrorResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.NoResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.Util;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.WriteSingleCoil;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.WriteSingleRegister;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.request.*;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.response.*;
import cn.enncloud.iot.iotgatewaymodbus.handler.ModbusRequestHandler;
import cn.enncloud.iot.iotgatewaymodbus.handler.ModbusResponseHandler;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusClient;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusServer;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author ares
 */
public class ExampleUI extends javax.swing.JFrame {

    private static ModbusClient modbusClient;
    private static ModbusServer modbusServer;
    //
    private boolean asyncClient;

    /**
     * Creates new form ModbusTCPExampleUI
     */
    public ExampleUI() {
        initComponents();
    }

    private void callModbusFunction(int functionCode) {
        if (modbusClient == null) {
            return;
        }

        int addr = Integer.parseInt(tfAddr.getText());
        int quantity = Integer.parseInt(tfQuantity.getText());

        int addrWrite = Integer.parseInt(tfAddrWrite.getText());
        int valueWrite = Integer.parseInt(tfValueWrite.getText());

        try {
            String response = "FUNCTION NOT SUPPORTED";
            switch (functionCode) {
                case ModbusFunction.READ_COILS:
                    if (asyncClient) {
                        modbusClient.readCoilsAsync(addr, quantity);
                    } else {
                        ReadCoilsResponse readCoilsResponse = modbusClient.readCoils(addr, quantity);
                        response = Util.getBinaryString(readCoilsResponse.getByteCount(), readCoilsResponse.getCoilStatus());
                    }
                    break;
                case ModbusFunction.READ_DISCRETE_INPUTS:
                    if (asyncClient) {
                        modbusClient.readDiscreteInputs(addr, quantity);
                    } else {
                        ReadDiscreteInputsResponse readDiscreteInputs = modbusClient.readDiscreteInputs(addr, quantity);
                        response = Util.getBinaryString(readDiscreteInputs.getByteCount(), readDiscreteInputs.getInputStatus());
                    }
                    break;
                case ModbusFunction.READ_HOLDING_REGISTERS:
                    if (asyncClient) {
                        modbusClient.readHoldingRegisters(addr, quantity);
                    } else {
                        ReadHoldingRegistersResponse readHoldingRegistersResponse = modbusClient.readHoldingRegisters(addr, quantity);
                        response = Arrays.toString(readHoldingRegistersResponse.getRegisters());
                    }
                    break;
                case ModbusFunction.READ_INPUT_REGISTERS:
                    if (asyncClient) {
                        modbusClient.readInputRegisters(addr, quantity);
                    } else {
                        ReadInputRegistersResponse readInputRegistersResponse = modbusClient.readInputRegisters(addr, quantity);
                        response = Arrays.toString(readInputRegistersResponse.getInputRegisters());
                    }
                    break;
                case ModbusFunction.WRITE_SINGLE_COIL:
                    if (asyncClient) {
                        modbusClient.writeSingleCoil(addrWrite, valueWrite > 0);
                    } else {
                        WriteSingleCoil writeSingleCoil = modbusClient.writeSingleCoil(addrWrite, valueWrite > 0);
                        response = writeSingleCoil.toString();
                    }
                    break;
                case ModbusFunction.WRITE_SINGLE_REGISTER:
                    if (asyncClient) {
                        modbusClient.writeSingleRegister(addrWrite, valueWrite);
                    } else {
                        WriteSingleRegister writeSingleRegister = modbusClient.writeSingleRegister(addrWrite, valueWrite);
                        response = writeSingleRegister.toString();
                    }
                    break;
            }

            if (!asyncClient) {
                taLog.append(response + "\n");
            }
        } catch (NoResponseException | ErrorResponseException | ConnectionException ex) {
            taLog.append(ex.getLocalizedMessage() + "\n");
        }
    }

    private void setupClient(ModbusResponseHandler handler) {
        if (modbusClient != null) {
            modbusClient.close();
        }

        asyncClient = handler != null;

        String host = tfHost.getText();
        String port = tfRemotePort.getText();

        modbusClient = new ModbusClient(host, Integer.valueOf(port)); //ModbusConstants.MODBUS_DEFAULT_PORT);

        modbusClient.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ModbusClient.PROP_CONNECTIONSTATE)) {
                    ModbusClient.CONNECTION_STATES state = (ModbusClient.CONNECTION_STATES) evt.getNewValue();
                    switch (state) {
                        case connected:
                            lbClient.setText("connected");
                            break;
                        case notConnected:
                            lbClient.setText("not connected");
                            break;
                        case pending:
                            lbClient.setText("pending");
                            break;
                    }
                }
            }
        });

        try {
            modbusClient.setup(handler);
        } catch (ConnectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pConnection = new javax.swing.JPanel();
        btListen = new javax.swing.JButton();
        tfHost = new javax.swing.JTextField();
        tfPort = new javax.swing.JTextField();
        btConnect = new javax.swing.JButton();
        tfRemotePort = new javax.swing.JTextField();
        lbClient = new javax.swing.JLabel();
        lbClients = new javax.swing.JLabel();
        btConnectAsync = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taLog = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        btReadInputRegisters = new javax.swing.JButton();
        btReadHoldingRegisters = new javax.swing.JButton();
        btReadCoils = new javax.swing.JButton();
        tfAddr = new javax.swing.JTextField();
        btReadDiscreteInputs = new javax.swing.JButton();
        tfQuantity = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        tfAddrWrite = new javax.swing.JTextField();
        tfValueWrite = new javax.swing.JTextField();
        btWriteCoil = new javax.swing.JButton();
        btWriteSingleRegister = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pConnection.setBorder(javax.swing.BorderFactory.createTitledBorder("Connection"));

        btListen.setText("listen");
        btListen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListenActionPerformed(evt);
            }
        });

        tfHost.setText("10.0.1.55");

        tfPort.setText("30502");

        btConnect.setText("connect");
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });

        tfRemotePort.setText("502");

        lbClient.setText("not connected");

        lbClients.setText("server down");

        btConnectAsync.setText("connect async");
        btConnectAsync.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectAsyncActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pConnectionLayout = new javax.swing.GroupLayout(pConnection);
        pConnection.setLayout(pConnectionLayout);
        pConnectionLayout.setHorizontalGroup(
            pConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConnectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pConnectionLayout.createSequentialGroup()
                        .addComponent(tfHost, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfRemotePort, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btConnectAsync)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(318, 318, 318))
                    .addGroup(pConnectionLayout.createSequentialGroup()
                        .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btListen, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbClients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(177, 177, 177))
        );
        pConnectionLayout.setVerticalGroup(
            pConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConnectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbClients)
                        .addComponent(btListen))
                    .addComponent(tfPort, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(pConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btConnect)
                    .addComponent(tfRemotePort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbClient)
                    .addComponent(btConnectAsync))
                .addContainerGap())
        );

        taLog.setColumns(20);
        taLog.setRows(5);
        jScrollPane1.setViewportView(taLog);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Read"));

        btReadInputRegisters.setText("ReadInputRegisters");
        btReadInputRegisters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReadInputRegistersActionPerformed(evt);
            }
        });

        btReadHoldingRegisters.setText("ReadHoldingRegisters");
        btReadHoldingRegisters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReadHoldingRegistersActionPerformed(evt);
            }
        });

        btReadCoils.setText("ReadCoils");
        btReadCoils.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReadCoilsActionPerformed(evt);
            }
        });

        tfAddr.setText("12288");

        btReadDiscreteInputs.setText("ReadDiscreteInputs");
        btReadDiscreteInputs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReadDiscreteInputsActionPerformed(evt);
            }
        });

        tfQuantity.setText("10");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btReadCoils)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btReadInputRegisters))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btReadDiscreteInputs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btReadHoldingRegisters)))
                .addGap(46, 46, 46)
                .addComponent(tfAddr, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btReadCoils)
                            .addComponent(btReadInputRegisters))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btReadDiscreteInputs)
                            .addComponent(btReadHoldingRegisters)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Write"));

        tfAddrWrite.setText("12288");

        tfValueWrite.setText("10");

        btWriteCoil.setText("WriteSingleCoil");
        btWriteCoil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btWriteCoilActionPerformed(evt);
            }
        });

        btWriteSingleRegister.setText("WriteSingleRegister");
        btWriteSingleRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btWriteSingleRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btWriteCoil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btWriteSingleRegister)
                .addGap(18, 18, 18)
                .addComponent(tfAddrWrite, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfValueWrite, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfAddrWrite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfValueWrite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btWriteCoil)
                        .addComponent(btWriteSingleRegister)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pConnection, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pConnection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btListenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListenActionPerformed
        String port = tfPort.getText();

        if (modbusServer != null) {
            modbusServer.close();
        }

        modbusServer = new ModbusServer(Integer.valueOf(port));

        modbusServer.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ModbusServer.PROP_CONNECTIONSTATE)) {
                    ModbusServer.CONNECTION_STATES state = (ModbusServer.CONNECTION_STATES) evt.getNewValue();
                    switch (state) {
                        case down:
                            lbClients.setText("server down");
                            break;
                        case listening:
                            lbClients.setText("listening");
                            break;
                        case clientsConnected:
                            lbClients.setText(modbusServer.getClientChannels().size() + " clients connected");
                            break;
                    }
                }
            }
        });

        try {
            modbusServer.setup(new ModbusRequestHandler() {

                @Override
                protected WriteSingleCoil writeSingleCoil(WriteSingleCoil request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected WriteSingleRegister writeSingleRegister(WriteSingleRegister request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected ReadCoilsResponse readCoilsRequest(ReadCoilsRequest request) {
                    BitSet coils = new BitSet(request.getQuantityOfCoils());

                    for (int i = 0; i < request.getQuantityOfCoils(); i++) {
                        coils.set(i);
                    }

                    return new ReadCoilsResponse(coils);
                }

                @Override
                protected ReadDiscreteInputsResponse readDiscreteInputsRequest(ReadDiscreteInputsRequest request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected ReadInputRegistersResponse readInputRegistersRequest(ReadInputRegistersRequest request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected ReadHoldingRegistersResponse readHoldingRegistersRequest(ReadHoldingRegistersRequest request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected WriteMultipleRegistersResponse writeMultipleRegistersRequest(WriteMultipleRegistersRequest request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected WriteMultipleCoilsResponse writeMultipleCoilsRequest(WriteMultipleCoilsRequest request) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        } catch (ConnectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_btListenActionPerformed

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        setupClient(null);
    }//GEN-LAST:event_btConnectActionPerformed

    private void btReadCoilsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReadCoilsActionPerformed
        callModbusFunction(ModbusFunction.READ_COILS);
    }//GEN-LAST:event_btReadCoilsActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (modbusServer != null) {
            modbusServer.close();
        }
    }//GEN-LAST:event_formWindowClosing

    private void btReadDiscreteInputsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReadDiscreteInputsActionPerformed
        callModbusFunction(ModbusFunction.READ_DISCRETE_INPUTS);
    }//GEN-LAST:event_btReadDiscreteInputsActionPerformed

    private void btReadInputRegistersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReadInputRegistersActionPerformed
        callModbusFunction(ModbusFunction.READ_INPUT_REGISTERS);
    }//GEN-LAST:event_btReadInputRegistersActionPerformed

    private void btReadHoldingRegistersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReadHoldingRegistersActionPerformed
        callModbusFunction(ModbusFunction.READ_HOLDING_REGISTERS);
    }//GEN-LAST:event_btReadHoldingRegistersActionPerformed

    private void btWriteCoilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btWriteCoilActionPerformed
        callModbusFunction(ModbusFunction.WRITE_SINGLE_COIL);
    }//GEN-LAST:event_btWriteCoilActionPerformed

    private void btWriteSingleRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btWriteSingleRegisterActionPerformed
        callModbusFunction(ModbusFunction.WRITE_SINGLE_REGISTER);
    }//GEN-LAST:event_btWriteSingleRegisterActionPerformed

    private void btConnectAsyncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectAsyncActionPerformed
        ModbusResponseHandler handler = new ModbusResponseHandler() {

            @Override
            public void newResponse(ModbusFrame frame) {
                taLog.append(frame.toString() + "\n");
            }
        };
        setupClient(handler);
    }//GEN-LAST:event_btConnectAsyncActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExampleUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btConnect;
    private javax.swing.JButton btConnectAsync;
    private javax.swing.JButton btListen;
    private javax.swing.JButton btReadCoils;
    private javax.swing.JButton btReadDiscreteInputs;
    private javax.swing.JButton btReadHoldingRegisters;
    private javax.swing.JButton btReadInputRegisters;
    private javax.swing.JButton btWriteCoil;
    private javax.swing.JButton btWriteSingleRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbClient;
    private javax.swing.JLabel lbClients;
    private javax.swing.JPanel pConnection;
    private javax.swing.JTextArea taLog;
    private javax.swing.JTextField tfAddr;
    private javax.swing.JTextField tfAddrWrite;
    private javax.swing.JTextField tfHost;
    private javax.swing.JTextField tfPort;
    private javax.swing.JTextField tfQuantity;
    private javax.swing.JTextField tfRemotePort;
    private javax.swing.JTextField tfValueWrite;
    // End of variables declaration//GEN-END:variables
}
