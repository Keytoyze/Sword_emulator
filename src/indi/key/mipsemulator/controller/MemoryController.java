package indi.key.mipsemulator.controller;


import java.util.AbstractList;
import java.util.Collections;
import java.util.RandomAccess;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Instruction;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.Memory;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemoryController {

    private TableView<MemoryBean> tableView;
    private MemoryListWrapper memoryListWrapper;
    private Button jump, last, next;
    private ComboBox<String> typeBox;
    private TextField addressText;

    private static final int PAGE_NUM = 6;

    public MemoryController(TableView<MemoryBean> tableView, Machine machine,
                            Button jump, Button last, Button next, ComboBox<String> typeBox,
                            TextField addressText) {
        this.tableView = tableView;
        this.memoryListWrapper = new MemoryListWrapper(machine.getAddressRedirector());
        this.jump = jump;
        this.last = last;
        this.next = next;
        this.typeBox = typeBox;
        this.addressText = addressText;
        initView();
    }

    private void initView() {
        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("address"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("instruction"));
        tableView.setItems(FXCollections.observableList(memoryListWrapper));
        jump.setOnAction(event -> {
            try {
                long address = parseAddress(addressText.getText());
                memoryListWrapper.setAddress(address);
                tableView.setItems(FXCollections.observableList(memoryListWrapper));
                addressText.setText(formatAddress(address));
            } catch (Exception e) {
                Alert information = new Alert(Alert.AlertType.ERROR);
                information.setTitle("错误！");
                information.setHeaderText("输入内容（" + addressText.getText() + "）不是一个有效的十六进制地址。");
                information.showAndWait();
            }
        });
        last.setOnAction(event -> {
            long address = Math.max(
                    memoryListWrapper.getAddress() -
                            getAddressPageRange(memoryListWrapper.getMemoryType()), 0);
            memoryListWrapper.setAddress(address);
            tableView.setItems(FXCollections.observableList(memoryListWrapper));
            addressText.setText(formatAddress(address));
        });
        next.setOnAction(event -> {
            long address = Math.min(
                    memoryListWrapper.getAddress() +
                            getAddressPageRange(memoryListWrapper.getMemoryType()), 0xFFFFFFFL);
            memoryListWrapper.setAddress(address);
            tableView.setItems(FXCollections.observableList(memoryListWrapper));
            addressText.setText(formatAddress(address));
        });
        addressText.setText(formatAddress(0));
        typeBox.setItems(FXCollections.observableArrayList("十六进制", "二进制"));
        typeBox.getSelectionModel().select(0);
        typeBox.setOnAction(event -> {
            boolean binary = typeBox.getSelectionModel().getSelectedIndex() == 1;
            memoryListWrapper.setMemoryType(binary);
            tableView.setItems(FXCollections.observableList(memoryListWrapper));
        });
    }

    private static long parseAddress(String content) {
        long address = Long.valueOf(content, 16);
        if (address < 0 || address > 0xFFFFFFFFL) {
            throw new RuntimeException();
        }
        return address;
    }

    private static String formatAddress(long address) {
        String content = Long.toHexString(address);
        return String.join("", Collections.nCopies(8 - content.length(), "0")) + content;
    }

    private static String formatData(String data) {
        return String.join("", Collections.nCopies(8 - data.length(), "0")) + data;
    }

    private static long getAddressPageRange(boolean binary) {
        return binary ? PAGE_NUM : PAGE_NUM * 4;
    }

    private static class MemoryListWrapper extends AbstractList<MemoryBean> implements RandomAccess {

        private long beginAddress = 0;
        private Memory memory;
        private boolean binary = false;

        MemoryListWrapper(Memory memory) {
            this.memory = memory;
        }

        void setAddress(long address) {
            this.beginAddress = address;
        }

        long getAddress() {
            return beginAddress;
        }

        void setMemoryType(boolean binary) {
            this.binary = binary;
        }

        boolean getMemoryType() {
            return binary;
        }

        @Override
        public MemoryBean get(int index) {
            long address = binary ?
                    beginAddress + index :
                    beginAddress + index * 4;
            try {
                byte[] bytes = memory.load(address, 4);
                BitArray bitArray = BitArray.of(bytes);
                String data = binary ?
                        bitArray.toString().substring(2, 10) :
                        Integer.toHexString(bitArray.value()).toUpperCase();
                Statement statement = Statement.of(bitArray.value());
                Instruction instruction = statement.getInstruction();
                String s;
                if (instruction == Instruction.UNKNOWN || (binary && address % 4 != 0)) {
                    s = "";
                } else {
                    s = statement.toString();
                }
                return new MemoryBean(address, data, s);
            } catch (MemoryOutOfBoundsException e) {
                return new MemoryBean(address, "--------", "");
            }
        }

        @Override
        public int size() {
            return PAGE_NUM;
        }
    }

    @SuppressWarnings("unused")
    public static class MemoryBean {

        private String address, data, instruction;

        MemoryBean(long address, String data, String instruction) {
            this.address = formatAddress(address);
            StringBuilder dataString = new StringBuilder();
            String completeData = formatData(data);
            for (int i = 0; i < completeData.length(); i++) {
                dataString.append(completeData, i, i + 1);
                if (i % 2 == 1) {
                    dataString.append(" ");
                }
            }
            this.data = dataString.toString();
            this.instruction = instruction;
        }

        public String getAddress() {
            return address;
        }

        public String getData() {
            return data;
        }

        public String getInstruction() {
            return instruction;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof MemoryBean)) {
                return false;
            }
            return address.equals(((MemoryBean) obj).address);
        }
    }
}
