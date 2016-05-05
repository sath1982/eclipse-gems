package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.handlers.IHandlerService;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class CommandItem extends SpotlightItem<ParameterizedCommand> {

	public CommandItem(CommandProvider provider, ParameterizedCommand cmd) {
		super(provider, cmd);
	}

	@Override
	public String getLabel() {
		try {
			return getItem().getName();
		} catch (NotDefinedException e) {
			return null;
		}
	}

	@Override
	public String getDetailsLabel() {
		try {
			return getItem().getCommand().getDescription();
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.getDetailsLabel();
	}

	@Override
	public ImageDescriptor doGetImage() {
		CommandProvider cp = (CommandProvider) this.provider;
		ICommandImageService service = cp.getCommandImageService();
		ImageDescriptor imageDescriptor = service.getImageDescriptor(getItem().getId());
		if (null == imageDescriptor) {
			// fallback to default command image
			return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
		}
		return imageDescriptor;
	}

	@Override
	public void show() {
		IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
		if (null != handlerService) {
			try {
				handlerService.executeCommand(getItem(), null);
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
