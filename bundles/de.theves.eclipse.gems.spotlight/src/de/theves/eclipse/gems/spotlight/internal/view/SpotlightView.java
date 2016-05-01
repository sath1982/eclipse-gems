package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import de.theves.eclipse.gems.spotlight.internal.Activator;
import de.theves.eclipse.gems.spotlight.internal.providers.ActionsProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.CommandProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.JavaTypesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.PerspectivesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.ResourcesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.ViewProvider;

public class SpotlightView extends FilteredItemsSelectionDialog {
	private IWorkbenchWindow activeWindow;

	public SpotlightView(IWorkbenchWindow activeWindow, Shell shell, boolean multi) {
		super(shell, multi);
		setListLabelProvider(new SpotlightItemLabelProvider());
		setDetailsLabelProvider(new SpotlightItemDetailsLabelProvider());
		this.activeWindow = activeWindow;
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
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
		return new SpotlightItemsFilter();
	}

	@Override
	protected Comparator<SpotlightItem> getItemsComparator() {
		return new Comparator<SpotlightItem>() {
			@Override
			public int compare(SpotlightItem o1, SpotlightItem o2) {
				return o1.compareTo(o2);
			}
		};
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException {
		IProgressMonitor safeMonitor = progressMonitor == null ? new NullProgressMonitor() : progressMonitor;
		try {
			SpotlightItemProvider[] providers = new SpotlightItemProvider[] { new ViewProvider(),
					new ResourcesProvider(), new PerspectivesProvider(), new ActionsProvider(this.activeWindow),
					new CommandProvider(this.activeWindow), new JavaTypesProvider() };
			safeMonitor.beginTask(null, providers.length);
			for (int i = 0; i < providers.length; i++) {
				try {
					// add a ruler for each provider
					contentProvider.add(new Ruler(providers[i].getLabel()), itemsFilter);
					// add the items
					List<? extends SpotlightItem> items = providers[i].getItems((SpotlightItemsFilter) itemsFilter,
							new SubProgressMonitor(safeMonitor, 1));
					for (SpotlightItem searchItem : items) {
						contentProvider.add(searchItem, itemsFilter);
					}
				} catch (Exception e) {
					// catch all exceptions thrown from a provider and go on
					// TODO logging
					e.printStackTrace();
				}
			}
		} finally {
			safeMonitor.done();
		}
	}

	public final class SpotlightItemsFilter extends ItemsFilter {

		@Override
		public boolean matchItem(Object item) {
			return matches(((SpotlightItem) item).getElementName());
		}

		@Override
		public boolean isConsistentItem(Object item) {
			// out items are always consistent
			return true;
		}

	}

	@Override
	public String getElementName(Object item) {
		return ((SpotlightItem) item).getLabel();
	}

}
