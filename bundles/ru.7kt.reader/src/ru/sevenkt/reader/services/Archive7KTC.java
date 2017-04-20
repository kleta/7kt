package ru.sevenkt.reader.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Archive7KTC {
	private byte[] data;

	private int firstBlockNum;

	private int pagesInBlock;

	private int sizePage;

	private int numberOfBlock;

	private String serialNum;

	private LocalDateTime datetime;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	public Archive7KTC(int firstBlockNum, int sizePage, int numberOfBlock, int pagesInBlock) {
		this.firstBlockNum = firstBlockNum;
		this.pagesInBlock = pagesInBlock;
		this.numberOfBlock = numberOfBlock;
		this.sizePage = sizePage;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getFirstBlockNum() {
		return firstBlockNum;
	}

	public void setFirstBlockNum(int firstBlockNum) {
		this.firstBlockNum = firstBlockNum;
	}

	public int getPagesInBlock() {
		return pagesInBlock;
	}

	public void setPagesInBlock(int pagesInBlock) {
		this.pagesInBlock = pagesInBlock;
	}

	public int getSizePage() {
		return sizePage;
	}

	public void setSizePage(int sizePage) {
		this.sizePage = sizePage;
	}

	public int getNumberOfBlock() {
		return numberOfBlock;
	}

	public void setNumberOfBlock(int numberOfBlock) {
		this.numberOfBlock = numberOfBlock;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "0"+serialNum+"-"+formatter.format(datetime);
	}

	public String getName() {
		String name = serialNum;
		return "0"+name+"-"+formatter.format(datetime);
	}

}
