package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
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

	private final SpotlightItemProvider[] providers;

	private Composite composite;
	private ResourceManager resourceManager;
	private IWorkbenchWindow window;
	private SpotlightItemsFilter filter;
	private TableViewer tableViewer;
	private Text text;
	private SearchItemsJob searchItemsJob;
	private ProgressBar progressbar;
	protected UpdateUIJob uiJob;
	public RefreshProgressBarJob refreshProgressBarJob;
	private CachingContentProvider contentProvider;

	public SpotlightPopupDialog(Shell parent, IWorkbenchWindow window) {
		super(parent, SWT.RESIZE, true, true, true, false, false, null, null);
		this.window = window;
		providers = new SpotlightItemProvider[] { new ViewProvider(), new ResourcesProvider(),
				new PerspectivesProvider(), new ActionsProvider(this.window), new CommandProvider(this.window),
				new JavaTypesProvider() };

		refreshProgressBarJob = new RefreshProgressBarJob(parent.getDisplay());
		uiJob = new UpdateUIJob(parent.getDisplay());
		searchItemsJob = new SearchItemsJob();
		searchItemsJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				IStatus result = event.getResult();

				if (result.getCode() == IStatus.CANCEL) {
					return;
				}

				uiJob.cancel();
				uiJob.schedule(200);
			}
		});
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.composite = (Composite) super.createDialogArea(parent);

		createSearchField();

		progressbar = new ProgressBar(this.composite, SWT.SMOOTH);
		progressbar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		this.filter = new SpotlightItemsFilter(text.getText());
		tableViewer = new TableViewer(this.composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		contentProvider = new CachingContentProvider(tableViewer);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(new LabelProvider());

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

		filter = new SpotlightItemsFilter(text.getText());

		searchItemsJob.cancel();
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
			tableViewer.getTable().setRedraw(false);
			tableViewer.getTable().deselectAll();
			tableViewer.setItemCount(contentProvider.getNumberOfElements());
			tableViewer.refresh();
			tableViewer.setSelection(StructuredSelection.EMPTY);
			tableViewer.getTable().setRedraw(true);
			return Status.OK_STATUS;
		}

	}

	private class SearchItemsJob extends Job {
		public SearchItemsJob() {
			super("Spotlight search");
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			listItems(filter, new ReportingProgressMonitor(monitor));
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			return Status.OK_STATUS;
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
				int maximum = progressbar.getMaximum();
				if (ReportingProgressMonitor.TOTAL_WORK_LOAD != maximum) {
					progressbar.setMaximum(ReportingProgressMonitor.TOTAL_WORK_LOAD);
					progressbar.setSelection(0);
				}
				progressbar.setSelection(this.reportingMonitor.getWorked());
			}

			if (this.reportingMonitor.isCanceled() || this.reportingMonitor.isDone()) {
				return Status.CANCEL_STATUS;
			}

			// schedule periodically with delay
			schedule(500);

			return Status.OK_STATUS;
		}

		public void scheduleProgressBarRefresh(ReportingProgressMonitor monitor) {
			this.reportingMonitor = monitor;
			// schedule with a short delay to avoid flickering
			schedule(200);
		}

	}

	private class ReportingProgressMonitor extends ProgressMonitorWrapper {
		public static final int TOTAL_WORK_LOAD = 10000;
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

	private void listItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		monitor.beginTask(null, providers.length * 100);
		contentProvider.reset();
		try {
			for (SpotlightItemProvider provider : providers) {
				List<SpotlightItem<?>> items = provider.getItems(filter, new SubProgressMonitor(monitor, 100));
				if (monitor.isCanceled()) {
					return;
				}
				for (SpotlightItem<?> spotlightItem : items) {
					contentProvider.addElement(spotlightItem, filter);
				}
			}
			contentProvider.sort();
		} catch (OperationCanceledException e) {
			return;
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
