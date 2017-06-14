 
package ru.sevenkt.reader.ui.handlers;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.reader.services.IDeviceReaderService;
import ru.sevenkt.reader.ui.views.ConnectionSettingsDialog;

public class ConnectionSettingHandler {
	
	@Inject
	private IDeviceReaderService reader;
	
	@Inject
	private IDBService dbService;
	
	@Execute
	public void execute(Shell shell, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) throws Exception {
		Device device = (Device)object;
		Connection connection=device.getConnection();
		Set<String> availableSerialPorts = reader.getAvailableSerialPorts();
		if(connection==null)
			connection=new Connection("Прямое", "COM1", "","");
		ConnectionSettingsDialog dialog = new ConnectionSettingsDialog(shell, new ArrayList<>(availableSerialPorts), connection);
		if (dialog.open() == Window.OK) {
			Connection con = dialog.getConnection();
			device.setConnection(con);
			if(con.getType().equals("Прямое")){
				con.setPhone("");
				con.setInitString("");
			}
			dbService.saveDevice(device);
		}
	}
		
}