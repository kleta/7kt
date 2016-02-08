import ru.sevenkt.archive.domain.Archive
import ru.sevenkt.archive.domain.Length;
import ru.sevenkt.archive.domain.Settings;
import ru.sevenkt.archive.services.impl.ArchiveServiceImpl
import spock.lang.Specification


class ArchiveServiceTest extends  Specification{
	
	
	def "тестируем правильность чтения настроек прибора"(){
		
		given:
		def service=new ArchiveServiceImpl();
		def file=new File("7kt/02016_2016-02-04_13-00.bin")
		when:
		Archive archive=service.readArchiveFromFile(file);
		
		then:
		def size=Settings.class.getAnnotation(Length.class).value()
		Settings s=archive.settings;
		s.data.length==size
		s.archiveLength==8
		s.archiveVersion==3
		s.deviceVersion==4
		s.formulaNum==12
		s.tempColdWaterSetting==7
		s.volumeByImpulsSetting1==10
		s.volumeByImpulsSetting2==10
		s.volumeByImpulsSetting3==10
		s.volumeByImpulsSetting4==10
		
		
	}

}
