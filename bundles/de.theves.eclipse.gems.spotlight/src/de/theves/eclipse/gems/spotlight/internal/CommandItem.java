package de.theves.eclipse.gems.spotlight.internal;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.commands.ICommandImageService;

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
		// TODO Auto-generated method stub

	}

}
