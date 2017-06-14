package ru.sevenkt.reader.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NRSerialPort;

public abstract class Reader {

	protected String portName;
	protected CRC16 crc = new CRC16();
	protected Logger logWrite = LoggerFactory.getLogger("TX");
	protected Logger logRead = LoggerFactory.getLogger("RX");
	protected Logger log = LoggerFactory.getLogger("Reader");
	protected NRSerialPort serial;

	public Reader(String port) {
		portName=port;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public CRC16 getCrc() {
		return crc;
	}

	public void setCrc(CRC16 crc) {
		this.crc = crc;
	}

	public Logger getLogWrite() {
		return logWrite;
	}

	public void setLogWrite(Logger logWrite) {
		this.logWrite = logWrite;
	}

	public Logger getLogRead() {
		return logRead;
	}

	public void setLogRead(Logger logRead) {
		this.logRead = logRead;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public NRSerialPort getSerial() {
		return serial;
	}

	public void setSerial(NRSerialPort serial) {
		this.serial = serial;
	}
	
	public abstract byte[] readMonhData(int devAdr, int blokSize) throws Exception;
	public abstract byte[] readDayData(int devAdr, int blokSize) throws Exception;
	public abstract byte[] readHourData(int devAdr, int blokSize)  throws Exception;
	public abstract byte[] readJournalData(int devAdr, int blokSize)  throws Exception;
	public abstract byte[] readCurrentData(int devAdr, int blokSize)  throws Exception;
	public abstract byte[] readEEPROM(int devAdr, int blokSize) throws Exception;
	
}