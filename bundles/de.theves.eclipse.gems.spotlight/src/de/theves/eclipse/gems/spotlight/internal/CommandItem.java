package de.theves.eclipse.gems.spotlight.internal;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.handlers.IHandlerService;

public class CommandItem extends SpotlightItem {
	private ParameterizedCommand command;

	public CommandItem(CommandProvider provider, ParameterizedCommand cmd) {
		super(provider);
		this.command = cmd;
	}

	@Override
	public String getLabel() {
		try {
			return this.command.getName();
		} catch (NotDefinedException e) {
			return null;
		}
	}

	@Override
	public ImageDescriptor getImage() {
		CommandProvider cp = (CommandProvider) this.provider;
		ICommandImageService service = cp.getCommandImageService();
		return service.getImageDescriptor(this.command.getId());
	}

	@Override
	public void show() {
		IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
		if (null != handlerService) {
			try {
				handlerService.executeCommand(this.command, null);
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
