package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
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

	private Composite composite;
	private ResourceManager resourceManager;
	private IWorkbenchWindow window;
	private SpotlightItemsFilter filter;
	private TableViewer tableViewer;
	private Text text;
	private RefreshCacheJob searchItemsJob;
	private ProgressBar progressbar;
	protected UpdateUIJob uiJob;
	public RefreshProgressBarJob refreshProgressBarJob;
	private CachingContentProvider contentProvider;

	public SpotlightPopupDialog(Shell parent, IWorkbenchWindow window) {
		super(parent, SWT.RESIZE, true, true, true, false, false, null, null);
		this.window = window;
		refreshProgressBarJob = new RefreshProgressBarJob(parent.getDisplay());
		uiJob = new UpdateUIJob(parent.getDisplay());
		searchItemsJob = new RefreshCacheJob();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.composite = (Composite) super.createDialogArea(parent);

		createSearchField();

		progressbar = new ProgressBar(this.composite, SWT.SMOOTH);
		progressbar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		progressbar.setMaximum(ReportingProgressMonitor.TOTAL_WORK_LOAD);
		progressbar.setMinimum(0);
		progressbar.setSelection(0);

		SpotlightItemProvider[] providers = new SpotlightItemProvider[] { new ViewProvider(), new ResourcesProvider(),
				new PerspectivesProvider(), new ActionsProvider(this.window), new CommandProvider(this.window),
				new JavaTypesProvider() };

		this.filter = new SpotlightItemsFilter(text.getText());
		tableViewer = new TableViewer(this.composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		tableViewer.setLabelProvider(new LabelProvider());
		contentProvider = new CachingContentProvider(tableViewer, providers);
		tableViewer.setContentProvider(contentProvider);

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
		tableViewer.setItemCount(0);
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
					if (elementAt != null) {
						tableViewer.getTable().setFocus();
						tableViewer.getTable().setCapture(true);
						tableViewer.setSelection(new StructuredSelection(elementAt), true);
					}
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
		progressbar.setSelection(0);

		if (text.getText() == null || text.getText().isEmpty()) {
			tableViewer.setInput(EMPTY_ARRAY);
			return;
		}

		filter.setPattern(text.getText());

		searchItemsJob.cancelAll();
		searchItemsJob.schedule(200);
	}

	private class UpdateUIJob extends UIJob {

		public UpdateUIJob(Display d) {
			super(d, "Spotlight input");
			setSystem(true);
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			if (tableViewer.getTable().isDisposed() || tableViewer.isBusy()) {
				return Status.CANCEL_STATUS;
			}
			// tableViewer.getTable().setRedraw(false);
			tableViewer.getTable().deselectAll();
			tableViewer.setItemCount(contentProvider.getNumberOfElements());
			tableViewer.refresh();
			if (contentProvider.getNumberOfElements() > 0) {
				tableViewer.setSelection(new StructuredSelection(contentProvider.getElement(0)));
			}
			// tableViewer.getTable().setRedraw(true);
			return Status.OK_STATUS;
		}

	}

	private class RefreshCacheJob extends Job {
		public RefreshCacheJob() {
			super("Spotlight search");
			setSystem(true);
		}

		public boolean cancelAll() {
			boolean uiCanceled = uiJob.cancel();
			return cancel() && uiCanceled;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			ReportingProgressMonitor reportingProgressMonitor = new ReportingProgressMonitor(monitor);
			contentProvider.reloadCache(SpotlightPopupDialog.this.filter, reportingProgressMonitor);
			if (reportingProgressMonitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			// schedule UI update for the reload
			uiJob.cancel();
			uiJob.schedule();
			return Status.OK_STATUS;
		}

		@Override
		protected void canceling() {
			super.canceling();
			// stop reloading cache and return asap
			contentProvider.stopReloadProgress();
		}
	}

	private class RefreshProgressBarJob extends UIJob {
		private ReportingProgressMonitor reportingMonitor;

		public RefreshProgressBarJob(Display d) {
			super(d, "Spotlight refresh");
			setSystem(true);
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			if (!progressbar.isDisposed()) {
				progressbar.setSelection(this.reportingMonitor.getWorked());
			}

			if (this.reportingMonitor.isCanceled() || this.reportingMonitor.isDone()) {
				return Status.CANCEL_STATUS;
			}

			// schedule periodically with delay
			schedule(100);

			return Status.OK_STATUS;
		}

		public void scheduleProgressBarRefresh(ReportingProgressMonitor monitor) {
			this.reportingMonitor = monitor;
			// schedule with a short delay to avoid flickering
			schedule(200);
		}

	}

	private class ReportingProgressMonitor extends ProgressMonitorWrapper {
		public static final int TOTAL_WORK_LOAD = 100;
		private int worked = 0;

		protected ReportingProgressMonitor(IProgressMonitor monitor) {
			super(monitor);
		}

		public boolean isDone() {
			return worked == TOTAL_WORK_LOAD;
		}

		@Override
		public void beginTask(String name, int totalWork) {
			super.beginTask(name, totalWork);
			if (refreshProgressBarJob != null) {
				refreshProgressBarJob.scheduleProgressBarRefresh(this);
			}
		}

		@Override
		public void done() {
			super.done();
			this.worked = TOTAL_WORK_LOAD;
		}

		@Override
		public void internalWorked(double work) {
			super.internalWorked(work);
			this.worked = (int) (work * TOTAL_WORK_LOAD);
		}

		public int getWorked() {
			return worked;
		}
	}

	@Override
	public boolean close() {
		if (resourceManager != null) {
			resourceManager.dispose();
		}
		cancelAllJobs();
		uiJob = null;
		searchItemsJob = null;
		refreshProgressBarJob = null;
		return super.close();
	}

	private void cancelAllJobs() {
		Job[] jobs = new Job[] { uiJob, searchItemsJob, refreshProgressBarJob };
		for (Job job : jobs) {
			int retries = 3;
			while (retries > 0) {
				try {
					boolean canceled = job.cancel();
					if (!canceled) {
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					// ignore this one
				} finally {
					retries--;
				}
			}
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
