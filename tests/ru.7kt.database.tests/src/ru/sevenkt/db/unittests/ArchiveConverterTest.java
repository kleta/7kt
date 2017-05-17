package ru.sevenkt.db.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.services.ArchiveConverter;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.ErrorCodes;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IHourRecord;
import ru.sevenkt.domain.Parameters;

public class ArchiveConverterTest {

	@Test
	public void testGetMonthData() throws Exception {
		File file = new File("resources/V3/07042_2016-05-20_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Measuring> md = ac.getMonthData();
		assertTrue(md.size() > 50);
	}

	@Test
	public void testGetDayData() throws Exception {
		File file = new File("resources/V3/01932_2016-10-24_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Measuring> md = ac.getDayData();
		assertTrue(md.size() > 500);
	}

	@Test
	public void testGetHourData() throws Exception {
		File file = new File("resources/V3/01932_2016-10-24_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		
		List<IHourRecord> recs = arc.getHourArchive().getRecordsByDay(LocalDate.parse("2016-09-15"));
		for (IHourRecord iHourRecord : recs) {
			assertNotNull(iHourRecord);
			LocalDateTime dateTime = iHourRecord.getDateTime();
			System.out.println(dateTime);
		}
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Measuring> hd = ac.getHourData();
		List<Measuring> dd = ac.getDayData();
		hd = hd.stream().filter(m -> m.getParameter().equals(Parameters.V2)).collect(Collectors.toList());
		hd.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		Map<LocalDateTime, BigDecimal> mdd = dd.stream().filter(m -> m.getParameter().equals(Parameters.V2))
				.collect(Collectors.toMap(Measuring::getDateTime, Measuring::getValue));
		dd.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		assertTrue(hd.size() > 30);
		Map<LocalDate, BigDecimal> sumHour = new HashMap<>();
		for (Measuring measuring : hd) {
			LocalDate localDate = measuring.getDateTime().toLocalDate();
			if (measuring.getDateTime().getHour() == 0)
				localDate = localDate.minusDays(1);
			BigDecimal val = sumHour.get(localDate);
			if (val == null) {
				val = measuring.getValue();
				sumHour.put(localDate, val);
			} else {
				val = val.add(measuring.getValue());
				sumHour.put(localDate, val);
			}
		}
		for (LocalDate date : sumHour.keySet()) {
			BigDecimal dayVal = mdd.get(date.atStartOfDay().plusDays(1));
			BigDecimal prevDayVal = mdd.get(date.atStartOfDay());
			BigDecimal h = sumHour.get(date);
			if (dayVal != null && prevDayVal != null) {
				BigDecimal sub = dayVal.subtract(prevDayVal);
				assertEquals(date + "", sub.setScale(3, BigDecimal.ROUND_HALF_UP),
						h.setScale(3, BigDecimal.ROUND_HALF_UP));
			}

		}

	}

	@Test
	public void testGetAccountParameters() throws Exception {
		File file = new File("resources/V3/01932_2016-10-24_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Parameters> params = ac.getAccountParameters(1);
		List<Parameters> p = new ArrayList<>(Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1,
				Parameters.AVG_TEMP2, Parameters.V1, Parameters.V2, Parameters.AVG_P1, Parameters.E1 }));
		assertEquals(params, p);
		params = ac.getAccountParameters(2);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.AVG_TEMP2, Parameters.V1,
				Parameters.AVG_P1, Parameters.E1 });
		assertEquals(params, p);
		params = ac.getAccountParameters(3);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.AVG_TEMP2, Parameters.V2,
				Parameters.AVG_P1, Parameters.E1 });
		assertEquals(params, p);
		params = ac.getAccountParameters(5);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.V2, Parameters.AVG_P1,
				Parameters.E1 });
		assertEquals(params, p);
		params = ac.getAccountParameters(6);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.AVG_P1, Parameters.E1 });
		assertEquals(params, p);

		params = ac.getAccountParameters(16);
		p = new ArrayList<>(Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.AVG_P1,
				Parameters.E1, Parameters.AVG_TEMP3, Parameters.AVG_TEMP4, Parameters.V3, Parameters.V4,
				Parameters.AVG_P2, Parameters.E2 }));
		assertEquals(params, p);
		params = ac.getAccountParameters(26);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.AVG_P1, Parameters.E1,
				Parameters.AVG_TEMP3, Parameters.AVG_TEMP4, Parameters.V3, Parameters.AVG_P2, Parameters.E2 });
		assertEquals(params, p);
		params = ac.getAccountParameters(36);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.AVG_P1, Parameters.E1,
				Parameters.AVG_TEMP3, Parameters.AVG_TEMP4, Parameters.V4, Parameters.AVG_P2, Parameters.E2 });
		assertEquals(params, p);
		params = ac.getAccountParameters(56);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.AVG_P1, Parameters.E1,
				Parameters.AVG_TEMP3, Parameters.V3, Parameters.V4, Parameters.AVG_P2, Parameters.E2 });
		assertEquals(params, p);
		params = ac.getAccountParameters(66);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.V1, Parameters.AVG_P1, Parameters.E1,
				Parameters.AVG_TEMP3, Parameters.V3, Parameters.AVG_P2, Parameters.E2 });
		assertEquals(params, p);
		params = ac.getAccountParameters(41);
		p = Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1, Parameters.AVG_TEMP2, Parameters.V1, Parameters.V2,
				Parameters.AVG_P1, Parameters.E1, Parameters.AVG_TEMP3, Parameters.V3 });
		assertEquals(params, p);
	}

	@Test
	public void testGetHourErrors() throws Exception {
		File file = new File("resources/V3/07042_2016-05-20_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Error> errors = ac.getHourErrors();
		assertFalse(errors.isEmpty());
	}

	@Test
	public void testGetDayErrors() throws Exception {
		File file = new File("resources/V3/07042_2016-05-20_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Error> errors = ac.getDayErrors();
		for (Error error : errors) {
			if (!error.getErrorCode().equals(ErrorCodes.U))
				System.out.println(error);
		}
		assertFalse(errors.isEmpty());
	}

	@Test
	public void testGeMonthErrors() throws Exception {
		File file = new File("resources/V3/07042_2016-05-20_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Error> errors = ac.getMonthErrors();
		assertFalse(errors.isEmpty());
	}

	@Test
	public void testErrorTime() throws Exception {
		File file = new File("resources/V3/07042_2016-05-20_09-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		ac.getMonthErrors();
		List<Measuring> hourErrorTime = ac.getHourData().stream()
				.filter(m -> m.getParameter().equals(Parameters.ERROR_TIME1)).collect(Collectors.toList());
		List<Measuring> dayErrorTime = ac.getDayData().stream()
				.filter(m -> m.getParameter().equals(Parameters.ERROR_TIME1)).collect(Collectors.toList());

		Map<LocalDateTime, BigDecimal> dayErTimeMap = dayErrorTime.stream()
				.collect(Collectors.toMap(Measuring::getDateTime, Measuring::getValue));
		for (LocalDateTime dt : dayErTimeMap.keySet()) {
			List<Measuring> l = hourErrorTime.stream().filter(
					m -> m.getDateTime().isBefore(dt.plusHours(1)) && m.getDateTime().isAfter(dt))
					.collect(Collectors.toList());
			if (!l.isEmpty()) {
				int hourSum = l.stream().map(Measuring::getValue).mapToInt(BigDecimal::intValue).sum();
				int d = dayErTimeMap.get(dt).intValue();
				assertEquals("dt="+dt+" d="+d+" sum="+hourSum,d, hourSum);
			}
		}

		List<Measuring> monthErrorTime = ac.getMonthData().stream()
				.filter(m -> m.getParameter().equals(Parameters.ERROR_TIME1)).collect(Collectors.toList());

		Map<LocalDateTime, BigDecimal> monthErTimeMap = monthErrorTime.stream()
				.collect(Collectors.toMap(Measuring::getDateTime, Measuring::getValue));
		for (LocalDateTime dt : monthErTimeMap.keySet()) {
			List<Measuring> l = dayErrorTime.stream().filter(
					m -> m.getDateTime().isAfter(dt.minusMonths(1)) && m.getDateTime().isBefore(dt.plusHours(1)))
					.collect(Collectors.toList());
			if (!l.isEmpty()) {
				int daySum = l.stream().map(Measuring::getValue).mapToInt(BigDecimal::intValue).sum();
				int m = monthErTimeMap.get(dt).intValue();
				assertEquals(m, daySum);
			}
		}
	}
}
