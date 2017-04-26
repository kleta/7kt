/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package ru.sevenkt.app.ui.handlers;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * TestJobRule is a scheduling rules that makes all jobs sequential.
 *
 */
public class ImportJobRule implements ISchedulingRule {
	private int jobOrder;

	public ImportJobRule(int order) {
		jobOrder = order;
	}

	@Override
	public boolean contains(ISchedulingRule rule) {
		if (rule instanceof IResource || rule instanceof ImportJobRule)
			return true;
		return false;
	}

	@Override
	public boolean isConflicting(ISchedulingRule rule) {
		if (!(rule instanceof ImportJobRule))
			return false;
		return ((ImportJobRule) rule).getJobOrder() >= jobOrder;
	}

	/**
	 * Return the order of this rule.
	 * @return
	 */
	public int getJobOrder() {
		return jobOrder;
	}

}
