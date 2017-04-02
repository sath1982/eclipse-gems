/**
 * Copyright (C) 2009 Sascha Theves
 *
 * This file is part of Eclipse Tiny Tools Project.
 *
 * Eclipse Tiny Tools Project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Eclipse Tiny Tools Project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Eclipse Tiny Tools Project.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.theves.eclipse.gems.selectproject.internal.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Messages.
 * 
 * @author Sascha Theves
 * @date: 25.04.2009 09:34:14
 */
public class Messages extends NLS {
	private static final String MESSAGES_NAME = "de.theves.eclipse.gems.selectproject.internal.messages.messages"; //$NON-NLS-1$

	public static String OpenProjectAction_Title;

	public static String OpenProjectAction_SelectViewLabel;

	public static String OpenProjectAction_PackageExplorerCombo;

	public static String OpenProjectAction_NavigatorExplorerCombo;

	public static String OpenProjectAction_ProjectExplorerCombo;

	static {
		// load message values from bundle file
		NLS.initializeMessages(MESSAGES_NAME, Messages.class);
	}
}
