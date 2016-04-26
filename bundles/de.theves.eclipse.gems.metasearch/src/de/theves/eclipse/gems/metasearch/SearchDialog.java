package de.theves.eclipse.gems.metasearch;

import java.util.Comparator;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SearchDialog extends FilteredItemsSelectionDialog {

	public SearchDialog(Shell shell, boolean multi) {
		super(shell, multi);
		setListLabelProvider(new WorkbenchLabelProvider());
		setDetailsLabelProvider(new WorkbenchLabelProvider());
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		return comp;
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		return Activator.getDefault().getDialogSettings();
	}

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Comparator getItemsComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException {
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
	public String getElementName(Object item) {
		// TODO Auto-generated method stub
		return item.toString();
	}

}
