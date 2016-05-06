package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.progress.UIJob;

import de.theves.eclipse.gems.spotlight.internal.providers.ActionsProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.CommandProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.JavaTypesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.PerspectivesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.ResourcesProvider;
import de.theves.eclipse.gems.spotlight.internal.providers.ViewProvider;

public class SpotlightPopupDialog extends PopupDialog {
	public static final int COL_PROVIDER = 0;
	public static final int COL_ITEMS = 1;

	private Composite composite;
	private ResourceManager resourceManager;
	private IWorkbenchWindow window;
	private SpotlightItemsFilter filter;
	private SpotlightItemViewerFilter viewerFilter;
	private TableViewer tableViewer;
	private SpotlightItemContentProvider contentProvider;
	private Text text;
	private UpdateJob job;

	public SpotlightPopupDialog(Shell parent, IWorkbenchWindow window) {
		super(parent, SWT.NONE, true, true, true, false, false, null, null);
		this.window = window;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.composite = (Composite) super.createDialogArea(parent);

		text = new Text(this.composite, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
		text.setMessage("Spotlight Search");
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 500;
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateViewer(text);
			}
		});
		this.filter = new SpotlightItemsFilter(text.getText());
		this.viewerFilter = new SpotlightItemViewerFilter(filter);

		tableViewer = new TableViewer(this.composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		contentProvider = new SpotlightItemContentProvider(filter);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.addFilter(viewerFilter);

		TableViewerColumn providerCol = new TableViewerColumn(tableViewer, SWT.NONE);
		providerCol.getColumn().setWidth(200);
		providerCol.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SpotlightItem item = (SpotlightItem) cell.getElement();
				cell.setText(item.getProvider().getLabel());
			}
		});
		TableViewerColumn itemsCol = new TableViewerColumn(tableViewer, SWT.NONE);
		itemsCol.getColumn().setWidth(200);
		itemsCol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				SpotlightItem item = (SpotlightItem) cell.getElement();
				cell.setText(item.getDetailsLabel());
				cell.setImage((Image) getResourceManager().get(item.getImage()));
			}
		});

		SpotlightItemProvider[] providers = new SpotlightItemProvider[] { new ViewProvider(), new ResourcesProvider(),
				new PerspectivesProvider(), new ActionsProvider(this.window), new CommandProvider(this.window),
				new JavaTypesProvider() };
		tableViewer.setInput(new SpotlightDialogInput(providers));

		this.composite.pack();

		return composite;
	}

	private void updateViewer(final Text text) {
		if (job != null) {
			job.cancel();
		}
		job = new UpdateJob();
		job.setSystem(true);
		job.schedule();

	}

	private class UpdateJob extends UIJob {

		public UpdateJob() {
			super("Spotlight-Search");
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			tableViewer.getTable().setRedraw(false);
			filter = new SpotlightItemsFilter(text.getText());
			viewerFilter.setFilter(filter);
			contentProvider.setFilter(filter);
			contentProvider.setMonitor(monitor);
			tableViewer.refresh();
			tableViewer.getTable().setRedraw(true);
			return Status.OK_STATUS;
		}

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

	public SpotlightItem<Object> getSelectedElement() {
		return (SpotlightItem<Object>) tableViewer.getStructuredSelection().getFirstElement();
	}

}
