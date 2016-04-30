package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OpenSearchAction extends Action implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow fWindow;

	public OpenSearchAction() {
		setActionDefinitionId("de.theves.eclipse.gems.metasearch.openSearchDialogId"); //$NON-NLS-1$
	}

	@Override
	public void run(IAction action) {
		SearchDialog dialog = new SearchDialog(fWindow.getShell(), false);
		dialog.setTitle("Search Everywhere");
		if (dialog.open() == Window.OK) {
			// TODO open selected item
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// not interested
	}

	@Override
	public void dispose() {
		// nothing to do here
	}

	@Override
	public void init(IWorkbenchWindow window) {
		fWindow = window;
	}

}
