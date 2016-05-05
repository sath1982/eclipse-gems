package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchWindow;

import de.theves.eclipse.gems.spotlight.internal.providers.ActionsProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.CommandProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.JavaTypesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.PerspectivesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.ResourcesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.ViewProvider;

public class SpotlightPopupDialog extends PopupDialog {

	private Composite composite;
	private ResourceManager resourceManager;
	private TreeViewer treeViewer;
	private IWorkbenchWindow window;
	private SpotlightItemsFilter filter;
	private SpotlightItemViewerFilter viewerFilter;
	private SpotlightItemTreeContentProvider contentProvider;

	public SpotlightPopupDialog(Shell parent, IWorkbenchWindow window) {
		super(parent, SWT.NONE, true, true, true, false, false, null, null);
		this.window = window;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.composite = (Composite) super.createDialogArea(parent);

		final Text text = new Text(this.composite, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
		text.setMessage("Spotlight Search");
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 500;
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateViewer(text);
			}

			private void updateViewer(final Text text) {
				treeViewer.getTree().setRedraw(false);
				filter = new SpotlightItemsFilter(text.getText());
				viewerFilter.setFilter(filter);
				contentProvider.setFilter(filter);
				treeViewer.refresh();
				treeViewer.expandAll();
				treeViewer.getTree().setRedraw(true);
			}
		});

		Tree tree = new Tree(this.composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = tree.getItemHeight() * 12;
		tree.setLayoutData(data);

		treeViewer = new TreeViewer(tree);
		contentProvider = new SpotlightItemTreeContentProvider(filter);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new SpotlightItemDetailsLabelProvider());
		filter = new SpotlightItemsFilter(text.getText());
		viewerFilter = new SpotlightItemViewerFilter(filter);
		treeViewer.addFilter(viewerFilter);

		treeViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		treeViewer.setUseHashlookup(true);

		SpotlightItemProvider[] providers = new SpotlightItemProvider[] { new ViewProvider(), new ResourcesProvider(),
				new PerspectivesProvider(), new ActionsProvider(this.window), new CommandProvider(this.window),
				new JavaTypesProvider() };
		// SpotlightItemProvider[] providers = new SpotlightItemProvider[] { new
		// ResourcesProvider() };
		treeViewer.setInput(new SpotlightDialogInput(providers));

		this.composite.pack();

		return composite;
	}

	@Override
	public boolean close() {
		if (resourceManager != null) {
			resourceManager.dispose();
		}
		return super.close();
	}

	ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}

}
