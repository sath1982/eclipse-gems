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
package de.theves.eclipse.gems.selectproject.internal.dialogs;

import java.util.Comparator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import de.theves.eclipse.gems.selectproject.internal.Activator;
import de.theves.eclipse.gems.selectproject.internal.messages.Messages;

/**
 * FilteredProjectsSelectionDialog shows a dialog where you can enter a search
 * string.
 * 
 * @author Sascha Theves
 * @date: 04.04.2009 09:36:07
 */
public class FilteredProjectsSelectionDialog extends
		FilteredItemsSelectionDialog {

	private static final String SETTING_KEY_SELECTED_COMBO_IDX = "de.theves.selectedComboIndex"; //$NON-NLS-1$

	private int selectionIndex;

	public FilteredProjectsSelectionDialog(Shell shell, boolean multi) {
		super(shell, multi);
		setListLabelProvider(new WorkbenchLabelProvider());
		setDetailsLabelProvider(new WorkbenchLabelProvider());
		setDefaultDialogSettings();
	}

	/**
	 * 
	 */
	private void setDefaultDialogSettings() {
		if (getDialogSettings().get(SETTING_KEY_SELECTED_COMBO_IDX) == null) {
			getDialogSettings().put(SETTING_KEY_SELECTED_COMBO_IDX,
					Integer.toString(0));
		}
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData());

		Label l = new Label(comp, SWT.NONE);
		l.setText(Messages.OpenProjectAction_SelectViewLabel);

		final Combo combo = new Combo(comp, SWT.DROP_DOWN | SWT.READ_ONLY);
		// index of entries are important!
		combo.add(Messages.OpenProjectAction_PackageExplorerCombo);
		combo.add(Messages.OpenProjectAction_NavigatorExplorerCombo);
		combo.add(Messages.OpenProjectAction_ProjectExplorerCombo);
		combo.select(Integer.parseInt(getDialogSettings().get(
				SETTING_KEY_SELECTED_COMBO_IDX)));
		combo.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				int selectionIndex = combo.getSelectionIndex();
				setSelectionIndex(selectionIndex);
				getDialogSettings().put(SETTING_KEY_SELECTED_COMBO_IDX,
						Integer.toString(selectionIndex));
			}
		});

		return comp;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {

			@Override
			public boolean matchItem(Object item) {
				IProject p = (IProject) item;
				return matches(p.getName());
			}

			@Override
			public boolean isConsistentItem(Object item) {
				return true;
			}
		};
	}

	@Override
	protected void fillContentProvider(
			final AbstractContentProvider contentProvider,
			final ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {
		IProgressMonitor safeMonitor = progressMonitor == null ? new NullProgressMonitor()
				: progressMonitor;
		SubProgressMonitor subProgressMonitor = null;
		try {
			safeMonitor.beginTask(null, 2);
			IResource[] wsProjects = ResourcesPlugin.getWorkspace().getRoot()
					.members();
			safeMonitor.worked(1);
			subProgressMonitor = new SubProgressMonitor(safeMonitor,
					wsProjects.length);
			for (IResource project : wsProjects) {
				contentProvider.add(project, itemsFilter);
				subProgressMonitor.worked(1);
			}
		} finally {
			safeMonitor.done();
			if (subProgressMonitor != null) {
				subProgressMonitor.done();
			}
		}
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		return Activator.getDefault().getDialogSettings();
	}

	@Override
	public String getElementName(Object item) {
		return ((IProject) item).getName();
	}

	@Override
	protected Comparator<IProject> getItemsComparator() {
		return new Comparator<IProject>() {
			public int compare(IProject p1, IProject p2) {
				return p1.getFullPath().toString().compareToIgnoreCase(
						p2.getFullPath().toString());
			}
		};
	}

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	public String getSelectedViewId() {
		switch (getSelectionIndex()) {
		case 0:
			return "org.eclipse.jdt.ui.PackageExplorer"; //$NON-NLS-1$
		case 1:
			return "org.eclipse.ui.views.ResourceNavigator"; //$NON-NLS-1$
		case 2:
			return "org.eclipse.ui.navigator.ProjectExplorer"; //$NON-NLS-1$
		default:
			throw new IllegalArgumentException("Unknown selection index: " //$NON-NLS-1$
					+ getSelectionIndex());
		}
	}

	/**
	 * @param selectionIndex
	 *            the selectionIndex to set
	 */
	protected void setSelectionIndex(int selectionIndex) {
		this.selectionIndex = selectionIndex;
	}

	/**
	 * @return the selectionIndex
	 */
	protected int getSelectionIndex() {
		return selectionIndex;
	}
}
