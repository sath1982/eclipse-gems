package de.theves.eclipse.gems.metasearch;

import java.util.Comparator;
import java.util.List;

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

import de.theves.eclipse.gems.metasearch.model.PerspectivesProvider;
import de.theves.eclipse.gems.metasearch.model.ResourcesProvider;
import de.theves.eclipse.gems.metasearch.model.Ruler;
import de.theves.eclipse.gems.metasearch.model.SearchItem;
import de.theves.eclipse.gems.metasearch.model.SearchItemProvider;
import de.theves.eclipse.gems.metasearch.model.ViewProvider;

public class SearchDialog extends FilteredItemsSelectionDialog {

	public SearchDialog(Shell shell, boolean multi) {
		super(shell, multi);
		setListLabelProvider(new SearchItemLabelProvider());
		setDetailsLabelProvider(new SearchItemLabelProvider());
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
		return new ItemsFilter() {

			@Override
			public boolean matchItem(Object item) {
				return matches(((SearchItem) item).getLabel());
			}

			@Override
			public boolean isConsistentItem(Object item) {
				// out items are always consistent
				return true;
			}
		};
	}

	@Override
	protected Comparator<SearchItem> getItemsComparator() {
		return new Comparator<SearchItem>() {
			@Override
			public int compare(SearchItem o1, SearchItem o2) {
				return o1.compareTo(o2);
			}
		};
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException {
		IProgressMonitor safeMonitor = progressMonitor == null ? new NullProgressMonitor() : progressMonitor;
		SubProgressMonitor subProgressMonitor = null;
		try {
			SearchItemProvider[] providers = new SearchItemProvider[] { new ViewProvider(), new ResourcesProvider(),
					new PerspectivesProvider() };
			safeMonitor.beginTask(null, providers.length);
			for (int i = 0; i < providers.length; i++) {
				// add a ruler for each provider
				contentProvider.add(new Ruler(providers[i].getLabel()), itemsFilter);
				// add the items
				List<SearchItem> items = providers[i].getItems();
				for (SearchItem searchItem : items) {
					contentProvider.add(searchItem, itemsFilter);
				}
				safeMonitor.worked(1);

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
		return ((SearchItem) item).getLabel();
	}

}
