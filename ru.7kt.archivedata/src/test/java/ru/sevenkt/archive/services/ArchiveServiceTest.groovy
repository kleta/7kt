package ru.sevenkt.archive.services
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime

import ru.sevenkt.archive.domain.Archive
import ru.sevenkt.archive.domain.CurrentData;
import ru.sevenkt.archive.domain.DayArchive
import ru.sevenkt.archive.domain.DayRecord;
import ru.sevenkt.archive.domain.HourArchive
import ru.sevenkt.archive.domain.HourRecord
import ru.sevenkt.archive.domain.Length
import ru.sevenkt.archive.domain.MonthArchive;
import ru.sevenkt.archive.domain.MonthRecord
import ru.sevenkt.archive.domain.Settings;
import ru.sevenkt.archive.services.impl.ArchiveServiceImpl
import ru.sevenkt.archive.utils.DataUtils
import spock.lang.Shared;
import spock.lang.Specification


class ArchiveServiceTest extends  Specification{
	@Shared
	Archive archive

	def setupSpec(){
		def service=new ArchiveServiceImpl();
		def file=new File("7kt/02016_2016-02-04_13-00.bin")
		archive=service.readArchiveFromFile(file);
	}

	def "тестируем правильность чтени€ настроек прибора"(){
		given:
		when:
		def size=Settings.class.getAnnotation(Length.class).value()
		Settings s=archive.settings;
		then:
		s.data.length==size
		s.archiveLength==8
		s.archiveVersion==3
		s.deviceVersion==14
		s.formulaNum==12
		s.serialNumber==2016
		s.tempColdWaterSetting==7
		s.volumeByImpulsSetting1==0.00999999f
		s.volumeByImpulsSetting2==0.00999999f
		s.volumeByImpulsSetting3==0.00999999f
		s.volumeByImpulsSetting4==0.00999999f
	}


	def "тестируем преобразование float24"(){
		given:
		def data1=[0, -32, 64] as byte[]
		def data2=[0x3A, 0x12, 0x42] as byte[]

		when:
		def res1=DataUtils.getFloat24Value(data1)

		def res2=DataUtils.getFloat24Value(data2)

		then:
		res1==7

		res2==36.55664f
	}

	def "тестируем преобразование float32"(){
		given:

		def data2=[0x9B, 0xB6, 0xA6, 0x7D] as byte[]
		//def data2=[0x3A, 0x12, 0x42] as byte[]
		when:

		def res2=DataUtils.getFloat32Value(data2)

		then:
		res2==2.7699999E37f
	}

	def "тестируем правильность чтени€ текущих данных"(){

		given:

		when:
		def size=CurrentData.class.getAnnotation(Length.class).value()

		CurrentData cd=archive.currentData;

		then:


		cd.data.length==size

		cd.day==4

		cd.hour==13

		cd.month==2

		cd.year==16

		cd.energy1==13805.526f
	}

	def "читаем мес€чный архив"(){
		given:
		when:
		MonthArchive ma=archive.monthArchive
		then:
		ma.data.length==2964
		int year=15
		int month=1
		for(Integer i : 0..11){
			MonthRecord record=ma.getMonthRecord(year, month)
			record.data.length==74
			println "${record.getDate()} объЄм ${record.volume1} er1 ${record.errorChannel1} er2 ${record.errorChannel2} t1 ${record.timeError1} t2 ${record.timeError2}"
			if(month%12==0){
				year++
				month=0
			}
			month++
		}
	}

	def "читаем суточный архив"(){
		given:
		when:
		DayArchive da=archive.getDayArchive();
		then:
		da.data.length==13416
		LocalDate endDate=LocalDate.parse("2016-02-05")
		LocalDate startDay=LocalDate.parse("2015-12-23")
		while (startDay.isBefore(endDate)){
			println startDay
			DayRecord record=da.getDayRecord(startDay,archive.currentData.currentDateTime);
			record.data.length==56
			println "${record.getDate()} объЄм ${record.volume1} er1 ${record.errorChannel1} er2 ${record.errorChannel2} t1 ${record.timeError1} t2 ${record.timeError2}"

			startDay=startDay.plusDays(1)
		}
	}

	def "читаем часовой архив"(){
		given:
		when:
		HourArchive ha=archive.getHourArchive();
		then:
		ha.data.length==41664
		LocalDateTime endDate=LocalDateTime.parse("2016-02-05T00:00:00")
		LocalDateTime startDay=LocalDateTime.parse("2015-12-10T00:00:00")
		while (startDay.isBefore(endDate)){
			println startDay
			HourRecord record=ha.getHourRecord(startDay, archive.currentData.currentDateTime);
			record.data.length==28
			def roundVol1=new BigDecimal(record.volume1*10).setScale(2, RoundingMode.UP).floatValue();
			println "${record.getDateTime()} объЄм ${roundVol1} er1 ${record.errorChannel1} er2 ${record.errorChannel2}"
			startDay=startDay.plusHours(1)
		}
	}

	def "провер€ем значени€ в часовом архиве на дату 2016-01-28T14:00:00"(){
		given:		

		when:
		HourArchive ha=archive.getHourArchive();
		def day=LocalDateTime.parse("2016-01-28T14:00:00")
		HourRecord record=ha.getHourRecord(day, archive.currentData.currentDateTime);

		then:
		record.avgPressure1/10==1.9
		record.avgPressure2/10==7.5
		record.avgTemp1/100==65.5
		record.avgTemp2/100==16.56
		record.avgTemp3/100==50.71
		record.avgTemp4/100==16.41
		record.energy1==6.388794f
		record.energy2==6.7424316f
		def roundVol1=new BigDecimal(record.volume1*0.00999999f).setScale(3, RoundingMode.UP).doubleValue();
		roundVol1==133.4
		def roundVol2=new BigDecimal(record.volume2*0.00999999f).setScale(3, RoundingMode.UP).doubleValue();
		roundVol2==86.19
		def roundVol3=new BigDecimal(record.volume3*0.00999999f).setScale(3, RoundingMode.UP).doubleValue();
		roundVol3==185.11
		def roundVol4=new BigDecimal(record.volume4*0.00999999f).setScale(3, RoundingMode.UP).doubleValue();
		roundVol4==131.44
	}
	
	
}
