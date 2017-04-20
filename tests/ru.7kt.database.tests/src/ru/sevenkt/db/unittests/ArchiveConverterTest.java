package ru.sevenkt.db.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.services.ArchiveConverter;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.Parameters;

public class ArchiveConverterTest {

	@Test
	public void testGetMonthData() throws Exception {
		File file = new File("resources/V3/07041_2016-03-24_08-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Measuring> md = ac.getMonthData();
		assertTrue(md.size() == 50);
	}

	@Test
	public void testGetDayData() throws Exception {
		File file = new File("resources/V3/07041_2016-03-24_08-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Measuring> md = ac.getDayData();
		assertTrue(md.size() > 1000);
	}

	@Test
	public void testGetHourData() throws Exception {
		File file = new File("resources/V3/07041_2016-03-24_08-00.bin");
		byte[] data = FileUtils.readFileToByteArray(file);
		IArchive arc = ArchiveFactory.createArhive(Arrays.copyOfRange(data, 64, data.length));
		ArchiveConverter ac = new ArchiveConverter(arc);
		List<Measuring> hd = ac.getHourData();
		List<Measuring> dd = ac.getDayData();
		hd = hd.stream().filter(m -> m.getParameter().equals(Parameters.V2)).collect(Collectors.toList());
		hd.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		Map<LocalDateTime, BigDecimal> mdd = dd.stream().filter(m -> m.getParameter().equals(Parameters.V2))
				.collect(Collectors.toMap(Measuring::getDateTime, Measuring::getValue));
		dd.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		assertTrue(hd.size() > 59 * 24);
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
			if (dayVal != null) {
				BigDecimal sub = dayVal.subtract(prevDayVal);
				assertEquals(date+"", sub.setScale(4, BigDecimal.ROUND_HALF_UP), h.setScale(4, BigDecimal.ROUND_HALF_UP));
			}

		}

	}

}
