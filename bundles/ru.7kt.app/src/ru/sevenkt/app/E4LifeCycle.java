/*******************************************************************************
 * Copyright (c) 2014 TwelveTone LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Steven Spungin <steven@spungin.tv> - initial API and implementation
 *******************************************************************************/
package ru.sevenkt.app;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;

import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.reports.services.IReportService;

/**
 * This is a stub implementation containing e4 LifeCycle annotated
 * methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/
@SuppressWarnings("restriction")
public class E4LifeCycle {

	@PostContextCreate
	void postContextCreate(IEclipseContext eclipseContext) throws InterruptedException, IllegalStateException, IOException {
		IDBService dbService = null;
		IArchiveService archiveService = null;
		IReportService reportService = null;
		int time = 0;
		while ((dbService == null || archiveService == null || reportService == null) && time < 20) {
			dbService = eclipseContext.get(IDBService.class);
			archiveService = eclipseContext.get(IArchiveService.class);
			reportService = eclipseContext.get(IReportService.class);
			System.out.println(dbService + " " + archiveService + " " + reportService + " time=" + time);
			Thread.sleep(1000);
			time++;
		}
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
	}
}
