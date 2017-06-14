import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.sevenkt.reader.ui.views.ReaderDialog;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TetsReadData {
	 private static SWTBot bot;

     @BeforeClass
     public static void beforeClass() throws Exception {
             // don't use SWTWorkbenchBot here which relies on Platform 3.x
             bot = new SWTBot();
     }
     
	@Test
	public void testOpen(){
		SWTBotMenu menu = bot.menu("Связь").menu("Считать архив");
	}

}
