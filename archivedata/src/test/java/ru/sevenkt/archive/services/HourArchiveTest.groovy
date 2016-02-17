package ru.sevenkt.archive.services



import java.time.LocalDate;

import ru.sevenkt.archive.services.impl.ArchiveServiceImpl
import spock.lang.Shared;
import spock.lang.Specification;

class HourArchiveTest extends Specification {

	@Shared
	Archive archive;


	def setupSpec(){
		def service=new ArchiveServiceImpl();
		def file=new File("7kt/02016_2016-02-04_13-00.bin")
		archive=service.readArchiveFromFile(file);
	}

	def "��������� ������������ ������� �� ������ �� ��������� ������"() {
		given:
		def currentData=archive.currentData
		def hourArchive=archive.hourArchive
		def date=LocalDate.of(2015, 12, 23)

		def dayArchive
		def dayArchivePrev
		def calcDay
		while(date.isBefore(LocalDate.of(2016, 02, 03))){

			dayArchive=archive.dayArchive.getDayRecord(date.plusDays(1),currentData.currentDateTime)
			dayArchivePrev=archive.dayArchive.getDayRecord(date, currentData.currentDateTime)
			calcDay=hourArchive.getDayConsumption(date, currentData.currentDateTime)
			def calc = String.format("Date %s p1=%d p2=%d t1=%d t2=%d t3=%d t4=%d e1=%f e2=%f v1=%f v2=%f v3=%f v4=%f", calcDay.getDate(),calcDay.getAvgPressure1(),
					calcDay.getAvgPressure2(), calcDay.getAvgTemp1(), calcDay.getAvgTemp2(), calcDay.getAvgTemp3(), calcDay.getAvgTemp4(), calcDay.energy1, calcDay.energy2, calcDay.volume1/100, calcDay.volume2/100, calcDay.volume3/100, calcDay.volume4/100);

			String archiveDay = String.format("Date %s p1=%d p2=%d t1=%d t2=%d t3=%d t4=%d e1=%f e2=%f v1=%f v2=%f v3=%f v4=%f", dayArchive.getDate(),dayArchive.getAvgPressure1(),
					dayArchive.getAvgPressure2(), dayArchive.getAvgTemp1(), dayArchive.getAvgTemp2(), dayArchive.getAvgTemp3(), dayArchive.getAvgTemp4(), dayArchive.energy1-dayArchivePrev.energy1, dayArchive.energy2-dayArchivePrev.energy2, dayArchive.volume1-dayArchivePrev.volume1, dayArchive.volume2-dayArchivePrev.volume2, dayArchive.volume3-dayArchivePrev.volume3, dayArchive.volume4-dayArchivePrev.volume4);
			println "========================================"
			println calc
			println archiveDay
			println "========================================"
			date=date.plusDays(1)
		}
	}
}
