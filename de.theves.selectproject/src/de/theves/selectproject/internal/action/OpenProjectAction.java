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
package de.theves.selectproject.internal.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ISetSelectionTarget;

import de.theves.selectproject.internal.dialogs.FilteredProjectsSelectionDialog;
import de.theves.selectproject.internal.messages.Messages;

/**
 * OpenProjectAction opens a Eclipse Project.
 * 
 * @author Sascha Theves
 * @date: 04.04.2009 09:34:42
 */
public class OpenProjectAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow fWindow;

	public OpenProjectAction() {
		setActionDefinitionId("de.theves.selectproject.OpenProjectCommand"); //$NON-NLS-1$
	}

	public void dispose() {
		// nothing to dispose
	}

	public void init(IWorkbenchWindow window) {
		fWindow = window;
	}

	public void run(IAction action) {
		FilteredProjectsSelectionDialog dialog = new FilteredProjectsSelectionDialog(
				fWindow.getShell(), false);
		dialog.setTitle(Messages.OpenProjectAction_Title);
		if (dialog.open() == Window.OK) {
			try {
				IViewPart view = fWindow.getActivePage().showView(
						dialog.getSelectedViewId());
				if (view instanceof ISetSelectionTarget) {
					ISetSelectionTarget setSelectionTarget = (ISetSelectionTarget) view;
					setSelectionTarget.selectReveal(new StructuredSelection(
							dialog.getFirstResult()));
				} else {
					view.getSite().getSelectionProvider().setSelection(
							new StructuredSelection(dialog.getFirstResult()));
				}
			} catch (PartInitException e) {
				// give up
				return;
			}

		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// not used
	}
}
