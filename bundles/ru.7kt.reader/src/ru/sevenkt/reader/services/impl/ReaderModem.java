package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;

import org.apache.commons.lang3.ArrayUtils;

import gnu.io.NRSerialPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.utils.HexUtils;

public class ReaderModem extends ReaderCOM {

	private Connection connection;
	
	

	public ReaderModem(Connection con, int timeOut) {
		super(con.getPort(), 9600, timeOut);
		this.connection = con;
	}

	@Override
	public boolean connect() throws IOException, InterruptedException, UnsupportedCommOperationException {
		serial = new NRSerialPort(portName, 9600);
		boolean connect = serial.connect();

		if (connect && ath0()) {
			boolean b;
			if (atd(connection.getPhone())) {
				return true;
			}

		}
		return false;
	}

	private boolean ath0() throws IOException, InterruptedException {
		byte[] command = new String("ATH0\r").getBytes();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());

		byte[] bytes = new byte[20];
		int count = 0;
		while (count < 3) {
			logWrite.info("Length: " + command.length + ", Data: " + new String(command).replace("\r", "."));
			outs.write(command);
			Thread.sleep(500);
			int b = ins.read(bytes);
			if (b > 0) {
				String string = new String(bytes);
				logRead.info("Length: " + b + ", Data: " + string.replaceAll("\\r|\\n", "."));
				if (string.contains("OK"))
					return true;
			}
			count++;
		}
		log.info("Модем не отвечает");
		return false;
	}

	private boolean atd(String phone) throws IOException, InterruptedException {
		byte[] command = new String("ATD" + phone + "\r").getBytes();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] bytes = new byte[194];
		int count = 0;
		while (count < 3) {
			logWrite.info("Length: " + command.length + ", Data: " + new String(command).replace("\r", "."));
			outs.write(command);
			Thread.sleep(20000);
			int b = ins.read(bytes);
			if (b > 0) {
				String string = new String(bytes);
				logRead.info("Length: " + b + ", Data: " + string.replaceAll("\\r|\\n", "."));
				if (string.contains("CONNECT"))
					return true;
			}
			count++;
		}
		return false;
	}

	private void escape() throws IOException, InterruptedException {
		byte[] command = new String("+++\r").getBytes();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		outs.write(command);
		Thread.sleep(2000);
		byte[] bytes = new byte[10];
		int b = ins.read(bytes);
	}

	@Override
	public void disconnect() throws IOException, InterruptedException {
		escape();
		ath0();
		super.disconnect();
	}

	

	

}
