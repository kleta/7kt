package ru.sevenkt.reader.services.impl;

import ru.sevenkt.db.entities.Connection;

public class ReaderFactory {

	public static ReaderCOM createReader(Connection con) {
		if (con.getType().equals("Прямое"))
			return new ReaderCOM(con.getPort(), 2400, 300);
		else
			return new ReaderModem(con, 1300);
	}

}
