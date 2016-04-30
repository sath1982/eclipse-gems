package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenSearchDialogCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		SearchDialog dialog = new SearchDialog(window.getShell(), false);
		dialog.setTitle("Eclipse Gems Spotlight");
		if (dialog.open() == Window.OK) {
			SearchItem searchItem = (SearchItem) dialog.getFirstResult();
			if (searchItem != null) {
				searchItem.show();
			}
		}
		return null;
	}

}
