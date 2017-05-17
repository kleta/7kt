package ru.sevenkt.reader.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import gnu.io.NRSerialPort;
import ru.sevenkt.reader.services.impl.Reader7KTC;

public class Reader7KTCTest {

	Reader7KTC reader = new Reader7KTC("COM4");

	@Test
	public void readAttributes() {
		for (String s : NRSerialPort.getAvailableSerialPorts()) {
			System.out.println("Availible port: " + s);
		}
		String port = "COM3";
		int baudRate = 115200;
		NRSerialPort serial = new NRSerialPort(port, baudRate);
		serial.connect();

		reader.connect();
		try {
			byte[] bytes = reader.readAttributes();
			assertNotNull(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.disconnect();
		}

	}

	@Test
	public void readPic() {
		reader.connect();
		try {
			byte[] bytes = reader.readPic();
			assertNotNull(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.disconnect();
		}
	}

	@Test
	public void readBlock() {
		reader.connect();
		try {
			byte[] bytes = reader.readPage(0);
			assertNotNull(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.disconnect();
		}

	}

	@Test
	public void getArchivesList() {
		reader.connect();
		try {
			byte[] pic = reader.readPic();
			byte[] attributes = reader.readAttributes();
			Map<Integer, byte[]> map = reader.getArchivesList(true, attributes, pic);
			assertNotNull(map);
			for (Integer key : map.keySet()) {
				byte[] bytes = map.get(key);
				assertTrue(bytes[0] == 4 || bytes[0] == 8);
				assertTrue(bytes[1] == 1 || bytes[1] == 3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.disconnect();
		}

	}

}
