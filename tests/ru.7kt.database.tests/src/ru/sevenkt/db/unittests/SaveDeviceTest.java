package ru.sevenkt.db.unittests;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveDeviceTest {
	
	
	private static IDBService service;
	
	
	
	
	private static Device device;

	private static SchedulerGroup scheduler;
	
	@BeforeClass
	public static void init() throws Exception{
		BundleContext context = FrameworkUtil.getBundle(IDBService.class).getBundleContext();
		ServiceReference<IDBService> sr = context.getServiceReference(IDBService.class);
		service=context.getService(sr);
		device = new Device();
		device.setSerialNum("5117");
		device.setDeviceName("test1478");
		service.saveDevice(device);
		scheduler=new SchedulerGroup();
		scheduler.setName("SchTest1478");
		scheduler.setArchiveTypes(new ArrayList<>());
		scheduler.getArchiveTypes().add(new ArchiveType(ArchiveTypes.DAY));
		scheduler.getArchiveTypes().add(new ArchiveType(ArchiveTypes.HOUR));
		scheduler.getArchiveTypes().add(new ArchiveType(ArchiveTypes.MONTH));
		scheduler.setDevices(new ArrayList<>());
		scheduler.getDevices().add(device);
		scheduler.setDeepDay(3);
		scheduler.setCronString("0 0 0 * * ? *");
		device.setGroups(new HashSet<>());
		device.getGroups().add(scheduler);
		service.saveSchedulerGroup(scheduler);
	}
	
	@Test
	public void a_UpdateDeviceName() throws Exception{
		device.setDeviceName("newTestName");
		service.saveDevice(device);
	}
	
	@Test
	public void b_AddConnectionToDevice() throws Exception{
		Connection con=new Connection("Прямое", "COM4", null, null);
		Device d = service.findDeviceBySerialNum(Integer.parseInt(device.getSerialNum()));
		d.setConnection(con);
		service.saveDevice(d);
	}
	
//	@Test
//	public void c_AddConnectionToDevice() throws Exception{
//		scheduler.setEnabled(true);
//		service.saveSchedulerGroup(scheduler);
//		device.setDeviceName("c1");
//		service.saveDevice(device);
//	}

}
