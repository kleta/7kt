package ru.sevenkt.app.it.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestMenus {

        private static SWTBot bot;

        @BeforeClass
        public static void beforeClass() throws Exception {
                // don't use SWTWorkbenchBot here which relies on Platform 3.x
                bot = new SWTBot();
        }

//        @Test
//        public void ensureSaveIsDisabledWhenNothingIsDirty() {
//                SWTBotMenu menu = bot.menu("Файл").menu("Выход");
//                assertThat("Save command in menu is not enabled", not(menu.isEnabled()));
//        }
        
        @Test
        public void readData() {
        	SWTBotMenu menu = bot.menu("Связь").menu("Считать архив");
        	assertThat("Save command in menu is not enabled", menu.isEnabled());
        }

}
