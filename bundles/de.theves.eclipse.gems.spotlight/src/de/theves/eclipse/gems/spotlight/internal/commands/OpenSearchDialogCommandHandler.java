package de.theves.eclipse.gems.spotlight.internal.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightView;

public class OpenSearchDialogCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		SpotlightView dialog = new SpotlightView(window, window.getShell(), false);
		dialog.setTitle("Eclipse Gems Spotlight");
		if (dialog.open() == Window.OK) {
			SpotlightItem searchItem = (SpotlightItem) dialog.getFirstResult();
			if (searchItem != null) {
				searchItem.show();
			}
		}
		return null;
	}

}
