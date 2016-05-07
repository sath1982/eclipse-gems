package de.theves.eclipse.gems.spotlight.internal.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightPopupDialog;

public class SpotlightCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		SpotlightPopupDialog dialog = new SpotlightPopupDialog(window.getShell(), window);
		dialog.open();
		return null;
	}

}
