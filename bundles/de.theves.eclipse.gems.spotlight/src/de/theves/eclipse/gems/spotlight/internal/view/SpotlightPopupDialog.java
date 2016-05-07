package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
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

	private static final Object[] EMPTY_ARRAY = new Object[0];

	private final SpotlightItemProvider[] providers;

	private Composite composite;
	private ResourceManager resourceManager;
	private IWorkbenchWindow window;
	private SpotlightItemsFilter filter;
	private SpotlightItemViewerFilter viewerFilter;
	private TableViewer tableViewer;
	private Text text;
	private UpdateJob job;

	public SpotlightPopupDialog(Shell parent, IWorkbenchWindow window) {
		super(parent, SWT.RESIZE, true, true, true, false, false, null, null);
		this.window = window;
		this.job = new UpdateJob();
		this.job.setSystem(true);
		providers = new SpotlightItemProvider[] { new ViewProvider(), new ResourcesProvider(),
				new PerspectivesProvider(), new ActionsProvider(this.window), new CommandProvider(this.window),
				new JavaTypesProvider() };
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.composite = (Composite) super.createDialogArea(parent);

		createSearchField();

		this.filter = new SpotlightItemsFilter(text.getText());
		this.viewerFilter = new SpotlightItemViewerFilter(filter);
		tableViewer = new TableViewer(this.composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.addFilter(viewerFilter);

		tableViewer.setComparator(new ViewerComparator() {
			@Override
			public int category(Object element) {
				return ((SpotlightItem<?>) element).getProvider().getCategory();
			}

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				int cat1 = category(e1);
				int cat2 = category(e2);

				if (cat1 != cat2) {
					return cat1 - cat2;
				}

				SpotlightItem<?> i1 = (SpotlightItem<?>) e1;
				SpotlightItem<?> i2 = (SpotlightItem<?>) e2;
				return i1.compareTo(i2);
			}
		});

		createColumns();

		tableViewer.getTable().addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// not used
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					closeAndShow();
				}
			}
		});
		tableViewer.getTable().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// not used
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// not used
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				closeAndShow();
			}
		});

		tableViewer.setUseHashlookup(true);
		tableViewer.setInput(EMPTY_ARRAY);

		this.composite.pack();

		return composite;
	}

	private void createSearchField() {
		text = new Text(this.composite, SWT.SINGLE | SWT.SEARCH);
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

		text.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// not used
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					Object elementAt = tableViewer.getElementAt(0);
					tableViewer.getTable().setFocus();
					tableViewer.getTable().setCapture(true);
					tableViewer.setSelection(new StructuredSelection(elementAt), true);
				}
			}
		});
	}

	private void closeAndShow() {
		SpotlightItem<?> item = getSelectedElement();
		SpotlightPopupDialog.this.close();
		if (item != null) {
			item.show();
		}
	}

	@Override
	protected Control getFocusControl() {
		return text;
	}

	private void createColumns() {
		TableViewerColumn providerCol = new TableViewerColumn(tableViewer, SWT.NONE);
		providerCol.getColumn().setWidth(200);
		providerCol.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SpotlightItem<?> item = (SpotlightItem<?>) cell.getElement();
				cell.setText(item.getProvider().getLabel());
			}
		});
		TableViewerColumn itemsCol = new TableViewerColumn(tableViewer, SWT.NONE);
		itemsCol.getColumn().setWidth(200);
		itemsCol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				SpotlightItem<?> item = (SpotlightItem<?>) cell.getElement();
				cell.setText(item.getLabel());
				cell.setImage((Image) getResourceManager().get(item.getImage()));
			}
		});
	}

	private void updateViewer(final Text text) {
		job.cancel();
		job.schedule();
	}

	private class UpdateJob extends UIJob {

		public UpdateJob() {
			super("Spotlight-Search");
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			IProgressMonitor pm = monitor != null ? monitor : new NullProgressMonitor();

			try {
				pm.beginTask(null, 3);
				if (text.getText() != null && !text.getText().isEmpty()) {
					tableViewer.getTable().setRedraw(false);
					filter = new SpotlightItemsFilter(text.getText());
					viewerFilter.setFilter(filter);
					SpotlightItem<?>[] items = listItems(new SubProgressMonitor(pm, 2));
					tableViewer.setInput(items);
					tableViewer.getTable().setRedraw(true);
				} else {
					tableViewer.setInput(EMPTY_ARRAY);
					pm.worked(2);
				}
				pm.worked(1);
				return Status.OK_STATUS;
			} finally {
				pm.done();
			}
		}

	}

	@Override
	public boolean close() {
		if (resourceManager != null) {
			resourceManager.dispose();
		}
		return super.close();
	}

	private SpotlightItem<?>[] listItems(IProgressMonitor monitor) {
		monitor.beginTask(null, providers.length);
		try {
			List<SpotlightItem<?>> result = new ArrayList<>();
			for (SpotlightItemProvider provider : providers) {
				List<SpotlightItem<?>> items = provider.getItems(this.filter, new SubProgressMonitor(monitor, 1));
				result.addAll(items);
			}
			return result.toArray(new SpotlightItem[result.size()]);
		} finally {
			monitor.done();
		}
	}

	private ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}

	public SpotlightItem<?> getSelectedElement() {
		return (SpotlightItem<?>) tableViewer.getStructuredSelection().getFirstElement();
	}

}
