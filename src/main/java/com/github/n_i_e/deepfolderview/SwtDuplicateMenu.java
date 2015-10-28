/*
 * Copyright 2015 Namihiko Matsumura (https://github.com/n-i-e/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.n_i_e.deepfolderview;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.github.n_i_e.dirtreedb.AbstractDirTreeDb;
import com.github.n_i_e.dirtreedb.Assertion;
import com.github.n_i_e.dirtreedb.DbPathEntry;
import com.github.n_i_e.dirtreedb.LazyAccessorThread;
import com.github.n_i_e.dirtreedb.LazyProxyDirTreeDb;
import com.github.n_i_e.dirtreedb.LazyProxyDirTreeDb.Dispatcher;
import com.github.n_i_e.dirtreedb.PathEntry;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;

public class SwtDuplicateMenu extends SwtCommonFileFolderMenu {
	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtLocation;
	private Composite compositeToolBar;
	private Table table;
	private Label lblStatusBar;
	private Composite compositeStatusBar;
	private ProgressBar progressBar;

	@Override protected Shell getShell() { return shell; }
	@Override protected Table getTable() { return table; }
	@Override protected Label getLblStatusBar() { return lblStatusBar; }
	@Override protected ProgressBar getProgressBar() { return progressBar; }

 	public static void main(String[] args) {
		final Display display = Display.getDefault();

		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					final SwtDuplicateMenu window = new SwtDuplicateMenu();
					window.open();

					display.asyncExec(new Runnable() {
						public void run() {
							TableItem tableItem = new TableItem(window.table, SWT.NONE);
							tableItem.setText(new String[] {"C:\\", "2015-01-01 00:00:00", "1", "2", "3"});
							
							TableItem tableItem_1 = new TableItem(window.table, SWT.NONE);
							tableItem_1.setText(new String[] {"D:\\", "2014-01-01 00:00:00", "100", "200", "1"});
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void open() {
		Display display = Display.getDefault();
		//createContents();
		//shell.open();
		//shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public SwtDuplicateMenu() {
		createContents();
		shell.open();
		shell.layout();

		location = new NavigatableArrayList<Location>();
		location.add(new Location());
	}

	/**
	 * Create contents of the window.
	 */
	private void createContents() {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(SwtDuplicateMenu.class, "/com/github/n_i_e/deepfolderview/icon/drive-harddisk.png"));
		shell.setMinimumSize(new Point(300, 200));
		shell.setSize(640, 480);
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 6;
		gl_shell.marginWidth = 3;
		gl_shell.marginHeight = 3;
		gl_shell.horizontalSpacing = 6;
		shell.setLayout(gl_shell);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText(Messages.mntmFile_text);
		
		Menu menuFile = new Menu(mntmFile);
		mntmFile.setMenu(menuFile);

		MenuItem mntmOpen_1 = new MenuItem(menuFile, SWT.NONE);
		mntmOpen_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenSelected(e);
			}
		});
		mntmOpen_1.setText(Messages.mntmOpen_text);
		
		MenuItem mntmOpenInNew_1 = new MenuItem(menuFile, SWT.NONE);
		mntmOpenInNew_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenInNewWindowSelected(e);
			}
		});
		mntmOpenInNew_1.setText(Messages.mntmOpenInNewWindow_text);
		
		MenuItem mntmCopyTo_2 = new MenuItem(menuFile, SWT.NONE);
		mntmCopyTo_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyToSelected();
			}
		});
		mntmCopyTo_2.setText(Messages.mntmCopyTo_text);
		
		MenuItem mntmClose = new MenuItem(menuFile, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCloseSelected();
			}
		});
		mntmClose.setText(Messages.mntmClose_text);

		MenuItem mntmQuit = new MenuItem(menuFile, SWT.NONE);
		mntmQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onQuitSelected();
			}
		});
		mntmQuit.setText(Messages.mntmQuit_text);

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText(Messages.mntmEdit_text);
		
		Menu menuEdit = new Menu(mntmEdit);
		mntmEdit.setMenu(menuEdit);
		
		MenuItem mntmRun_1 = new MenuItem(menuEdit, SWT.NONE);
		mntmRun_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onRunSelected();
			}
		});
		mntmRun_1.setText(Messages.mntmRun_text);
		
		MenuItem mntmCopyAsString_1 = new MenuItem(menuEdit, SWT.NONE);
		mntmCopyAsString_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyAsStringSelected();
			}
		});
		mntmCopyAsString_1.setText(Messages.mntmCopyAsString_text);
		
		MenuItem mntmCopyTo_1 = new MenuItem(menuEdit, SWT.NONE);
		mntmCopyTo_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyToSelected();
			}
		});
		mntmCopyTo_1.setText(Messages.mntmCopyTo_text);

		MenuItem mntmVisibility = new MenuItem(menu, SWT.CASCADE);
		mntmVisibility.setText(Messages.mntmVisibility_text);
		
		Menu menuVisibility = new Menu(mntmVisibility);
		mntmVisibility.setMenu(menuVisibility);
		
		final MenuItem mntmFoldersVisible = new MenuItem(menuVisibility, SWT.CHECK);
		mntmFoldersVisible.setSelection(folder_is_checked);
		mntmFoldersVisible.setText(Messages.mntmFoldersVisible_text);
		
		final MenuItem mntmFilesVisible = new MenuItem(menuVisibility, SWT.CHECK);
		mntmFilesVisible.setSelection(file_is_checked);
		mntmFilesVisible.setText(Messages.mntmFilesVisible_text);
		
		final MenuItem mntmCompressedFoldersVisible = new MenuItem(menuVisibility, SWT.CHECK);
		mntmCompressedFoldersVisible.setSelection(compressedfolder_is_checked);
		mntmCompressedFoldersVisible.setText(Messages.mntmCompressedFoldersVisible_text);
		
		final MenuItem mntmCompressedFilesVisible = new MenuItem(menuVisibility, SWT.CHECK);
		mntmCompressedFilesVisible.setSelection(compressedfile_is_checked);
		mntmCompressedFilesVisible.setText(Messages.mntmCompressedFilesVisible_text);
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText(Messages.mntmHelp_text);
		
		Menu menuHelp = new Menu(mntmHelp);
		mntmHelp.setMenu(menuHelp);
		
		MenuItem mntmOpenSourceLicenses = new MenuItem(menuHelp, SWT.NONE);
		mntmOpenSourceLicenses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new SwtOpenSourceLicenses(shell, SWT.TITLE|SWT.MIN|SWT.MAX|SWT.CLOSE).open();
			}
		});
		mntmOpenSourceLicenses.setText(Messages.mntmOpenSourceLicenses_text);

		compositeToolBar = new Composite(shell, SWT.NONE);
		compositeToolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeToolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		compositeToolBar.setFont(SWTResourceManager.getFont("Meiryo UI", 12, SWT.NORMAL));
		GridLayout gl_compositeToolBar = new GridLayout(5, false);
		gl_compositeToolBar.horizontalSpacing = 0;
		gl_compositeToolBar.verticalSpacing = 0;
		gl_compositeToolBar.marginWidth = 0;
		gl_compositeToolBar.marginHeight = 0;
		compositeToolBar.setLayout(gl_compositeToolBar);
		formToolkit.adapt(compositeToolBar);
		formToolkit.paintBordersFor(compositeToolBar);
		
		Button btnLeft = new Button(compositeToolBar, SWT.NONE);
		btnLeft.setImage(SWTResourceManager.getImage(SwtDuplicateMenu.class, "/com/github/n_i_e/deepfolderview/icon/go-previous.png"));
		btnLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onNavigatePreviousSelected(e);
			}
		});
		btnLeft.setFont(SWTResourceManager.getFont("Meiryo UI", 11, SWT.NORMAL));
		formToolkit.adapt(btnLeft, true, true);
		
		Button btnRight = new Button(compositeToolBar, SWT.NONE);
		btnRight.setImage(SWTResourceManager.getImage(SwtDuplicateMenu.class, "/com/github/n_i_e/deepfolderview/icon/go-next.png"));
		btnRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onNavigateNextSelected(e);
			}
		});
		btnRight.setFont(SWTResourceManager.getFont("Meiryo UI", 11, SWT.NORMAL));
		formToolkit.adapt(btnRight, true, true);
		
		Button btnUp = new Button(compositeToolBar, SWT.NONE);
		btnUp.setImage(SWTResourceManager.getImage(SwtDuplicateMenu.class, "/com/github/n_i_e/deepfolderview/icon/go-up.png"));
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onUpperFolderSelected(e);
			}
		});
		formToolkit.adapt(btnUp, true, true);
		
		txtLocation = new Text(compositeToolBar, SWT.BORDER);
		txtLocation.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				onLocationModified(arg0);
			}
		});
		GridData gd_txtLocation = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtLocation.widthHint = 200;
		txtLocation.setLayoutData(gd_txtLocation);
		txtLocation.setFont(SWTResourceManager.getFont("Meiryo UI", 11, SWT.NORMAL));
		formToolkit.adapt(txtLocation, true, true);
		
		Button btnRefresh = new Button(compositeToolBar, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setImage(SWTResourceManager.getImage(SwtDuplicateMenu.class, "/com/github/n_i_e/deepfolderview/icon/view-refresh.png"));
		formToolkit.adapt(btnRefresh, true, true);
		
		final TableViewer tableViewer = new TableViewer(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//table = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onTableSelected(e);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				onOpenSelected(e);
			}
		});
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);
		
		final TableColumn tblclmnPathL = new TableColumn(table, SWT.NONE);
		tblclmnPathL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnPathL);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnPathLSelected(tblclmnPathL, e);
			}
		});
		tblclmnPathL.setWidth(230);
		tblclmnPathL.setText(Messages.tblclmnPath_text);
		setTableSortDirection(tblclmnPathL, "path", orderL);
		
		final TableColumn tblclmnDateLastModifiedL = new TableColumn(table, SWT.LEFT);
		tblclmnDateLastModifiedL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnDateLastModifiedL);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnDateLastModifiedLSelected(tblclmnDateLastModifiedL, e);
			}
		});
		tblclmnDateLastModifiedL.setWidth(136);
		tblclmnDateLastModifiedL.setText(Messages.tblclmnDateLastModified_text);
		setTableSortDirection(tblclmnDateLastModifiedL, "datelastmodified", orderL);
		
		final TableColumn tblclmnSizeL = new TableColumn(table, SWT.RIGHT);
		tblclmnSizeL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnSizeL);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnSizeLSelected(tblclmnSizeL, e);
			}
		});
		tblclmnSizeL.setWidth(110);
		tblclmnSizeL.setText(Messages.tblclmnSize_text);
		setTableSortDirection(tblclmnSizeL, "size", orderL);

		final TableColumn tblclmnCompressedsizeL = new TableColumn(table, SWT.RIGHT);
		tblclmnCompressedsizeL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnCompressedsizeL);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnCompressedsizeLSelected(tblclmnCompressedsizeL, e);
			}
		});
		tblclmnCompressedsizeL.setWidth(110);
		tblclmnCompressedsizeL.setText(Messages.tblclmnCompressedesize_text);
		setTableSortDirection(tblclmnCompressedsizeL, "compressedsize", orderL);

		final TableColumn tblclmnPathR = new TableColumn(table, SWT.LEFT);
		tblclmnPathR.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnPathR);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnPathSelected(tblclmnPathR, e);
			}
		});
		tblclmnPathR.setWidth(230);
		tblclmnPathR.setText(Messages.tblclmnPath_text);
		setTableSortDirection(tblclmnPathR, "path", orderR);

		final TableColumn tblclmnDateLastModifiedR = new TableColumn(table, SWT.LEFT);
		tblclmnDateLastModifiedR.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnDateLastModifiedR);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnDateLastModifiedSelected(tblclmnDateLastModifiedR, e);
			}
		});
		tblclmnDateLastModifiedR.setWidth(136);
		tblclmnDateLastModifiedR.setText(Messages.tblclmnDateLastModified_text);
		setTableSortDirection(tblclmnDateLastModifiedR, "datelastmodified", orderR);

		final TableColumn tblclmnSizeR = new TableColumn(table, SWT.RIGHT);
		tblclmnSizeR.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnSizeR);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnSizeSelected(tblclmnSizeR, e);
			}
		});
		tblclmnSizeR.setWidth(110);
		tblclmnSizeR.setText(Messages.tblclmnSize_text);
		setTableSortDirection(tblclmnSizeR, "size", orderR);

		final TableColumn tblclmnCompressedsizeR = new TableColumn(table, SWT.RIGHT);
		tblclmnCompressedsizeR.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnCompressedsizeR);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnCompressedsizeSelected(tblclmnCompressedsizeR, e);
			}
		});
		tblclmnCompressedsizeR.setWidth(110);
		tblclmnCompressedsizeR.setText(Messages.tblclmnCompressedesize_text);
		setTableSortDirection(tblclmnCompressedsizeR, "compressedsize", orderR);

		Menu popupMenu = new Menu(table);
		table.setMenu(popupMenu);

		MenuItem mntmRun = new MenuItem(popupMenu, SWT.NONE);
		mntmRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onRunSelected();
			}
		});
		mntmRun.setText("&Run");

		MenuItem mntmOpen = new MenuItem(popupMenu, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenSelected(e);
			}
		});
		mntmOpen.setText("&Open");

		MenuItem mntmOpenInNew = new MenuItem(popupMenu, SWT.NONE);
		mntmOpenInNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenInNewWindowSelected(e);
			}
		});
		mntmOpenInNew.setText("Open in new window");

		MenuItem mntmCopyAsString = new MenuItem(popupMenu, SWT.NONE);
		mntmCopyAsString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyAsStringSelected();
			}
		});
		mntmCopyAsString.setText("Copy as string");

		MenuItem mntmCopyTo = new MenuItem(popupMenu, SWT.NONE);
		mntmCopyTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyToSelected();
			}
		});
		mntmCopyTo.setText("Copy to");

		MenuItem menuItem = new MenuItem(popupMenu, SWT.SEPARATOR);
		menuItem.setText("Visibility");

		final MenuItem mntmFoldersVisible_1 = new MenuItem(popupMenu, SWT.CHECK);
		mntmFoldersVisible_1.setSelection(folder_is_checked);
		mntmFoldersVisible_1.setText("Folders");
		mntmFoldersVisible_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmFoldersVisible.setSelection(mntmFoldersVisible_1.getSelection());
				onFoldersVisibleChecked(mntmFoldersVisible.getSelection());
			}
		});

		final MenuItem mntmFilesVisible_1 = new MenuItem(popupMenu, SWT.CHECK);
		mntmFilesVisible_1.setSelection(file_is_checked);
		mntmFilesVisible_1.setText("Files");
		mntmFilesVisible_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmFilesVisible.setSelection(mntmFilesVisible_1.getSelection());
				onFilesVisibleChecked(mntmFilesVisible.getSelection());
			}
		});

		final MenuItem mntmCompressedFoldersVisible_1 = new MenuItem(popupMenu, SWT.CHECK);
		mntmCompressedFoldersVisible_1.setSelection(compressedfolder_is_checked);
		mntmCompressedFoldersVisible_1.setText("Compressed Folders");
		mntmCompressedFoldersVisible_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmCompressedFoldersVisible.setSelection(mntmCompressedFoldersVisible_1.getSelection());
				onCompressedFoldersVisibleChecked(mntmCompressedFoldersVisible.getSelection());
			}
		});

		final MenuItem mntmCompressedFilesVisible_1 = new MenuItem(popupMenu, SWT.CHECK);
		mntmCompressedFilesVisible_1.setSelection(compressedfile_is_checked);
		mntmCompressedFilesVisible_1.setText("Compressed Files");
		mntmCompressedFilesVisible_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmCompressedFilesVisible.setSelection(mntmCompressedFilesVisible_1.getSelection());
				onCompressedFilesVisibleSelected(mntmCompressedFilesVisible.getSelection());
			}
		});
		mntmFoldersVisible.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmFoldersVisible_1.setSelection(mntmFoldersVisible.getSelection());
				onFoldersVisibleChecked(mntmFoldersVisible.getSelection());
			}
		});
		mntmFilesVisible.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    mntmFilesVisible_1.setSelection(mntmFilesVisible.getSelection());
				onFilesVisibleChecked(mntmFilesVisible.getSelection());
			}
		});
		mntmCompressedFoldersVisible.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    mntmCompressedFoldersVisible_1.setSelection(mntmCompressedFoldersVisible.getSelection());
				onCompressedFoldersVisibleChecked(mntmCompressedFoldersVisible.getSelection());
			}
		});
		mntmCompressedFilesVisible.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    mntmCompressedFilesVisible_1.setSelection(mntmCompressedFilesVisible.getSelection());
				onCompressedFilesVisibleSelected(mntmCompressedFilesVisible.getSelection());
			}
		});
		
		compositeStatusBar = new Composite(shell, SWT.NONE);
		compositeStatusBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeStatusBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gl_compositeStatusBar = new GridLayout(2, false);
		gl_compositeStatusBar.marginWidth = 0;
		gl_compositeStatusBar.marginHeight = 0;
		compositeStatusBar.setLayout(gl_compositeStatusBar);
		formToolkit.adapt(compositeStatusBar);
		formToolkit.paintBordersFor(compositeStatusBar);
		
		lblStatusBar = new Label(compositeStatusBar, SWT.NONE);
		lblStatusBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblStatusBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		formToolkit.adapt(lblStatusBar, true, true);
		lblStatusBar.setText("");
		
		progressBar = new ProgressBar(compositeStatusBar, SWT.NONE);
		formToolkit.adapt(progressBar, true, true);
		m_bindingContext = initDataBindings();

	}

	protected void onCopyAsStringSelected() {
		ArrayList<String> s = new ArrayList<String>();
		int[] rows = getTable().getSelectionIndices();
		for (int row: rows) {
			DbPathEntry s1 = pathentrylistL.get(row);
			if (s1 != null) {
				s.add(s1.getPath());
			}
			s1 = pathentrylist.get(row);
			if (s1 != null) {
				s.add(s1.getPath());
			}
		}
		StringSelection ss = new StringSelection(String.join("\n", s));
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(ss, ss);
	}

	protected void onOpenSelected(SelectionEvent e) {
		try {
			setLocationAndRefresh(getSelectedPathEntry());
		} catch (ArrayIndexOutOfBoundsException e1) {}
	}

	protected void onOpenInNewWindowSelected(SelectionEvent e) {
		DbPathEntry p = getSelectedPathEntry();
		if (p == null) {
			p = location.get().getPathEntry();
		}
		if (p != null) {
			new SwtDuplicateMenu().setLocationAndRefresh(p);
		} else if (location.get().getPathString() != null) {
			new SwtDuplicateMenu().setLocationAndRefresh(location.get().getPathString());
		} else if (location.get().getSearchString() != null) {
			new SwtDuplicateMenu().setLocationAndRefresh(location.get().getSearchString());
		} else if (location.get().getPathId() != 0L) {
			new SwtDuplicateMenu().setLocationAndRefresh(location.get().getPathId());
		}
	}

	protected void onNavigatePreviousSelected(SelectionEvent e) {
		location.navigatePrevious();
		setLocationAndRefresh(location.get());
	}

	protected void onNavigateNextSelected(SelectionEvent e) {
		location.navigateNext();
		setLocationAndRefresh(location.get());
	}

	protected void onUpperFolderSelected(SelectionEvent e) {
		DbPathEntry p = location.get().getPathEntry();
		if (p != null && p.getParentId() != 0L) {
			setLocationAndRefresh(p.getParentId());
		} else {
			writeStatusBar("Not ready for going up operation; be patient.");
		}
	}

	protected void onLocationModified(ModifyEvent arg0) {
		String newstring = txtLocation.getText();
		assert(newstring != null);
		writeStatusBar(String.format("New path string is: %s", newstring));
		shell.setText(newstring);
		Location oldloc = location.get();
		if (newstring.equals(oldloc.getPathString())) {
			// noop
		} else if (newstring.equals(oldloc.getSearchString())) {
			oldloc.setPathEntry(null);
			oldloc.setPathId(0L);
			oldloc.setPathString(null);
		} else {
			Location newloc = new Location();
			newloc.setPathString(newstring);
			location.add(newloc);
		}
		refresh();
	}

	public void setLocationAndRefresh(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				txtLocation.setText(text); // onLocationModified() is automatically called here.
			}
		});
	}

	public void setLocationAndRefresh(final DbPathEntry entry) {
		assert(entry != null);
		assert(location != null);
		Location oldloc = location.get();
		if (oldloc.getPathEntry() != null && oldloc.getPathEntry().getPathId() == entry.getPathId()) {
			// noop
		} else if (oldloc.getPathString() != null && oldloc.getPathString().equals(entry.getPath())) {
			oldloc.setPathEntry(entry);
			oldloc.setPathId(entry.getPathId());
		} else {
			Location newloc = new Location();
			newloc.setPathEntry(entry);
			newloc.setPathId(entry.getPathId());
			newloc.setPathString(entry.getPath());
			location.add(newloc);
		}
		setLocationAndRefresh(entry.getPath());
	}

	public void setLocationAndRefresh(long id) {
		writeStatusBar(String.format("Starting query; new ID is: %d", id));
		Location oldloc = location.get();
		if (oldloc.getPathId() == id) {
			// null
		} else {
			Location newloc = new Location();
			newloc.setPathId(id);
			location.add(newloc);
		}
		new LazyAccessorThread(LazyAccessorThreadRunningConfigSingleton.getInstance()) {
			@Override
			public void run() throws Exception {
				writelog("-- SwtFileFolderMenu SetLocationAndRefresh LOCAL PATTERN (id based) --");
				Location loc = location.get();
				DbPathEntry p = getDb().getDbPathEntryByPathId(loc.getPathId());
				if (p != null) {
					loc.setPathEntry(p);
					loc.setPathString(p.getPath());
					loc.setSearchString(null);
					setLocationAndRefresh(loc.getPathString());
				}
			}
		}.start();
	}

	public void setLocationAndRefresh(final Location loc) {
		if (loc.getPathString() != null) {
			setLocationAndRefresh(loc.getPathString());
		} else if (loc.getPathEntry() != null) {
			setLocationAndRefresh(loc.getPathEntry().getPath());
		} else if (loc.getSearchString() != null) {
			setLocationAndRefresh(loc.getSearchString());
		} else {
			setLocationAndRefresh("");
		}
	}

	protected void onTableSelected(SelectionEvent e) {}

	private String orderL = PreferenceBox.getSwtFileFolderMenuSortOrder();;
	private String orderR = PreferenceBox.getSwtDuplicateMenuSortOrderR();
	private boolean folder_is_checked = true;
	private boolean file_is_checked = true;
	private boolean compressedfolder_is_checked = true;
	private boolean compressedfile_is_checked = true;

	protected void onTblclmnPathLSelected(TableColumn tblclmnPathL, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderL = "path";
		} else {
			orderL = "path DESC";
		}
		PreferenceBox.setSwtFileFolderMenuSortOrder(orderL);
		refresh();
	}

	protected void onTblclmnDateLastModifiedLSelected(TableColumn tblclmnDateLastModified, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderL = "datelastmodified";
		} else {
			orderL = "datelastmodified DESC";
		}
		PreferenceBox.setSwtFileFolderMenuSortOrder(orderL);
		refresh();
	}

	protected void onTblclmnSizeLSelected(TableColumn tblclmnSize, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderL = "size";
		} else {
			orderL = "size DESC";
		}
		PreferenceBox.setSwtFileFolderMenuSortOrder(orderL);
		refresh();
	}

	protected void onTblclmnCompressedsizeLSelected(TableColumn tblclmnCompressedesize, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderL = "compressedsize";
		} else {
			orderL = "compressedsize DESC";
		}
		PreferenceBox.setSwtFileFolderMenuSortOrder(orderL);
		refresh();
	}

	protected void onTblclmnPathSelected(TableColumn tblclmnPath, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderR = "path";
		} else {
			orderR = "path DESC";
		}
		PreferenceBox.setSwtDuplicateMenuSortOrderR(orderR);
		refresh();
	}

	protected void onTblclmnDateLastModifiedSelected(TableColumn tblclmnDateLastModified, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderR = "datelastmodified";
		} else {
			orderR = "datelastmodified DESC";
		}
		PreferenceBox.setSwtDuplicateMenuSortOrderR(orderR);
		refresh();
	}

	protected void onTblclmnSizeSelected(TableColumn tblclmnSize, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderR = "size";
		} else {
			orderR = "size DESC";
		}
		PreferenceBox.setSwtDuplicateMenuSortOrderR(orderR);
		refresh();
	}

	protected void onTblclmnCompressedsizeSelected(TableColumn tblclmnCompressedesize, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			orderR = "compressedsize";
		} else {
			orderR = "compressedsize DESC";
		}
		PreferenceBox.setSwtDuplicateMenuSortOrderR(orderR);
		refresh();
	}

	protected void onFoldersVisibleChecked(boolean checked) {
		folder_is_checked = checked;
		refresh();
	}

	protected void onFilesVisibleChecked(boolean checked) {
		file_is_checked = checked;
		refresh();
	}

	protected void onCompressedFoldersVisibleChecked(boolean checked) {
		compressedfolder_is_checked = checked;
		refresh();
	}

	protected void onCompressedFilesVisibleSelected(boolean checked) {
		compressedfile_is_checked = checked;
		refresh();
	}

	protected void cleanupTable() throws WindowDisposedException {
		if (table.isDisposed()) {
			throw new WindowDisposedException("!! Window disposed at addRow");
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				pathentrylistL.clear();
				pathentrylist.clear();
				table.removeAll();;
			}
		});
	}

	protected void addRow(final DbPathEntry entry, final DbPathEntry entryR, final boolean grayout)
			throws WindowDisposedException {
		if (table.isDisposed()) {
			throw new WindowDisposedException("!! Window disposed at addRow");
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				pathentrylistL.add(entry);
				pathentrylist.add(entryR);

				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				final NumberFormat numf = NumberFormat.getNumberInstance();
				Date d = (entry == null ? null : new Date(entry.getDateLastModified()));
				Date dR = (entryR == null ? null : new Date(entryR.getDateLastModified()));
				String[] row = {
						(entry == null ? "" : entry.getPath()),
						(d == null ? "" : sdf.format(d)),
						(entry == null ? "" : numf.format(entry.getSize())),
						(entry == null ? "" : numf.format(entry.getCompressedSize())),
						(entryR == null ? "" : entryR.getPath()),
						(dR == null ? "" : sdf.format(dR)),
						(entryR == null ? "" : numf.format(entryR.getSize())),
						(entryR == null ? "" : numf.format(entryR.getCompressedSize())),
				};

				final Display display = Display.getDefault();
				final Color blue = new Color(display, 0, 0, 255);
				final Color red = new Color(display, 255, 0, 0);
				final Color black = new Color(display, 0, 0, 0);
				final Color gray = new Color(display, 127, 127, 127);

				try {
					TableItem tableItem = new TableItem(table, SWT.NONE);
					tableItem.setText(row);
					if (entryR != null) {
						if (grayout) {
							tableItem.setForeground(gray);
						} else if (entryR.isNoAccess()) {
							tableItem.setForeground(red);
						} else if (entryR.isFile() && entryR.getSize() != entryR.getCompressedSize()) {
							tableItem.setForeground(blue);
						} else {
							tableItem.setForeground(black);
						}
					}
				} catch (Exception e) {
					if (!table.isDisposed()) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private List<DbPathEntry> pathentrylistL = new ArrayList<DbPathEntry>();

	class Scenario extends SwtCommonFileFolderMenu.Scenario {

		LazyAccessorThread oldScenarioToKill = null;

		Scenario() {
			super();
		}

		public Scenario(LazyAccessorThread oldScenarioToKill) {
			this();
			this.oldScenarioToKill = oldScenarioToKill;
		}

		@Override
		public void run() throws SQLException, InterruptedException, IOException {
			if (oldScenarioToKill != null) {
				oldScenarioToKill.interrupt();
			}
			try {
				cleanupTable();

				ArrayList<String> typelist = new ArrayList<String> ();
				if (folder_is_checked) {
					typelist.add("type=0");
				}
				if (file_is_checked) {
					typelist.add("type=1");
				}
				if (compressedfolder_is_checked) {
					typelist.add("type=2");
				}
				if (compressedfile_is_checked) {
					typelist.add("type=3");
				}
				String typeWhere = typelist.size() == 0 ? "" : String.join(" OR ", typelist);

				writeStatusBar("Querying...");
				writeProgress(70);

				String searchSubSql;
				ArrayList<String> searchStringElement = new ArrayList<String> ();
				if (getLocationSearchString() == null || "".equals(getLocationSearchString())) {
					searchSubSql = "";
				} else {
					ArrayList<String> p = new ArrayList<String> ();
					for (String s: getLocationSearchString().split(" ")) {
						if (! "".equals(s)) {
							p.add("path LIKE ?");
							searchStringElement.add(s);
						}
					}
					searchSubSql = " AND (" + String.join(" OR ", p) + ")";
				}
				DbPathEntry locationPathEntry = null;
				PreparedStatement psL;
				if (getLocationPath() == null || "".equals(getLocationPath())) {
					String sqlL = "SELECT * FROM directory WHERE (" + typeWhere + ") " + searchSubSql
							+ " ORDER BY " + orderL;
					psL = getDb().prepareStatement(sqlL);
					int c = 1;
					for (String s: searchStringElement) {
						psL.setString(c++, "%" + s + "%");
					}
				} else if ((locationPathEntry = getLocationPathEntry()) != null) {
					String sqlL = "SELECT * FROM directory WHERE (" + typeWhere + ") " + searchSubSql
							+ " AND (pathid=? OR EXISTS (SELECT * FROM upperlower WHERE upper=? AND lower=pathid))"
							+ " ORDER BY " + orderL;
					psL = getDb().prepareStatement(sqlL);
					int c = 1;
					for (String s: searchStringElement) {
						psL.setString(c++, "%" + s + "%");
					}
					psL.setLong(c++, locationPathEntry.getPathId());
					psL.setLong(c++, locationPathEntry.getPathId());
				} else {
					String sqlL = "SELECT * FROM directory WHERE (" + typeWhere + ") " + searchSubSql
							+ " AND path LIKE ? ORDER BY " + orderL;
					psL = getDb().prepareStatement(sqlL);
					int c = 0;
					for (String s: searchStringElement) {
						psL.setString(c++, "%" + s + "%");
					}
					psL.setString(c++, getLocationPath() + "%");
				}

				try {
					ResultSet rsL = psL.executeQuery();
					try {
						writelog("QUERY FINISHED");
						writeStatusBar("Listing...");
						writeProgress(90);

						LazyProxyDirTreeDb.Dispatcher disp = getDb().getDispatcher();
						disp.setList(Dispatcher.NONE);
						disp.setCsum(Dispatcher.NONE);
						disp.setNoReturn(false);

						int countL = 0;
						while (rsL.next()) {
							DbPathEntry p1L = getDb().rsToPathEntry(rsL);
							Assertion.assertAssertionError(p1L != null);
							Assertion.assertAssertionError(p1L.getPath() != null);
							if (locationPathEntry != null) {
								Assertion.assertAssertionError(locationPathEntry.getPath() != null);
								Assertion.assertAssertionError(p1L.getPath().startsWith(locationPathEntry.getPath()),
										p1L.getPath() + " does not start with " + locationPathEntry.getPath()
										);
							}
							PathEntry p2L = disp.dispatch(p1L);
							if (rsL.getInt("duplicate") == 0 || p2L == null || !AbstractDirTreeDb.dscMatch(p1L, p2L)) {
								// no right side fields, only left
								mixOldNewEntriesAndAddRow(p1L, p2L, null, null);
								countL ++;
							} else {
								String sqlR = "SELECT directory.*, datelasttested FROM directory LEFT JOIN equality "
										+ "ON (pathid1=pathid AND pathid2=?) OR (pathid2=pathid AND pathid1=?) "
										+ "WHERE (type=1 OR type=3) "
										+ "AND pathid<>? AND directory.size=? AND directory.csum=? "
										+ "ORDER BY " + orderR;
								PreparedStatement psR = getDb().prepareStatement(sqlR);
								try {
									psR.setLong(1, p1L.getPathId());
									psR.setLong(2, p1L.getPathId());
									psR.setLong(3, p1L.getPathId());
									psR.setLong(4, p1L.getSize());
									psR.setInt(5, p1L.getCsum());
									ResultSet rsR = psR.executeQuery();
									int countR = 0;
									while (rsR.next()) {
										DbPathEntry p1R = getDb().rsToPathEntry(rsR);
										PathEntry p2R = disp.dispatch(p1R);
										boolean addRowFlag = true;
										if (p2R == null) {
											// noop
										} else if (AbstractDirTreeDb.dscMatch(p1R, p2R)) {
											rsR.getLong("datelasttested");
											if (rsR.wasNull()) {
												addRowFlag = getDb().checkEqualityNoLazy(p1L, p1R, true);
											}
										}
										if (addRowFlag) {
											if (countR == 0) {
												mixOldNewEntriesAndAddRow(p1L, p2L, p1R, p2R);
											} else {
												mixOldNewEntriesAndAddRow(null, null, p1R, p2R);
											}
											countL++;
											countR++;
										}
									}
									if (countR == 0) {
										mixOldNewEntriesAndAddRow(p1L, p2L, null, null);
										countL++;
									}
								} finally {
									psR.close();
									getDb().consumeUpdateQueue();
								}
							}
						}
						writeStatusBar(String.format("%d items", countL));
					} finally {
						rsL.close();
					}
				} finally {
					psL.close();
				}
			} catch (WindowDisposedException e) {
			}
		}

		private void mixOldNewEntriesAndAddRow(DbPathEntry entry1L, PathEntry entry2L,
				DbPathEntry entry1R, PathEntry entry2R) throws WindowDisposedException {
			boolean grayout = false;
			if (entry1L != null) {
				if (entry2L == null) {
					grayout = true;
				} else {
					Assertion.assertAssertionError(entry1L.getPath().equals(entry2L.getPath()));
					if (!AbstractDirTreeDb.dscMatch(entry1L, entry2L)) {
						entry1L.setDateLastModified(entry2L.getDateLastModified());
						entry1L.setSize(entry2L.getSize());
						entry1L.setCompressedSize(entry2L.getCompressedSize());
						entry1L.clearCsum();
					}
				}
			} 

			if (entry1R != null) {
				if (entry2R == null) {
					grayout = true;
				} else {
					Assertion.assertAssertionError(entry1R.getPath().equals(entry2R.getPath()));
					if (!AbstractDirTreeDb.dscMatch(entry1R, entry2R)) {
						entry1R.setDateLastModified(entry2R.getDateLastModified());
						entry1R.setSize(entry2R.getSize());
						entry1R.setCompressedSize(entry2R.getCompressedSize());
						entry1R.clearCsum();
					}
				}
			} 

			if (entry1L != null || entry1R != null) {
				addRow(entry1L, entry1R, grayout);
			}
		}

	}

	private SwtCommonFileFolderRootMenu.Scenario scenario = null;

	private void refresh() {
		final SwtCommonFileFolderRootMenu.Scenario oldScenarioToKill = scenario;

		final Location loc = location.get();
		if (loc.getPathEntry() != null || loc.getSearchString() != null ||
				(loc.getPathEntry() == null && loc.getPathId() == 0L
				&& (loc.getPathString() == null || "".equals(loc.getPathString())))) {
			scenario = new Scenario(oldScenarioToKill);
		} else {
			scenario = new SwtCommonFileFolderRootMenu.Scenario() {
				@Override
				public void run() throws Exception {
					if (oldScenarioToKill != null) {
						oldScenarioToKill.setDontResetProgressAtEnding(true);
						oldScenarioToKill.interrupt();
					}
					writeProgress(50);
					if (loc.getPathString() != null) {
						DbPathEntry p = getDb().getDbPathEntryByPath(loc.getPathString());
						if (p != null) {
							writelog("-- SwtDuplicateMenu PREPROCESS PATTERN 1 (path based entry detection) --");
							setLocationAndRefresh(p);
							return;
						} else {
							loc.setSearchString(loc.getPathString());
							loc.setPathString(null);
							loc.setPathId(0L);
							loc.setPathEntry(null);
							writelog("-- SwtDuplicateMenu PREPROCESS PATTERN 2 (searchstring=" + loc.getSearchString() + ") --");
							refresh();
							return;
						}
					} else if (loc.getPathId() != 0L) {
						writelog("-- SwtDuplicateMenu PREPROCESS PATTERN 3 (id based) --");
						DbPathEntry p = getDb().getDbPathEntryByPathId(loc.getPathId());
						assert(p != null);
						setLocationAndRefresh(p);
						return;
					} else {
						writelog("-- SwtDuplicateMenu PREPROCESS PATTERN 4 (show all paths) --");
					}
					scenario = new Scenario();
					scenario.start();
				}
				
			};
		}
		scenario.start();
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeBackgroundCompositeObserveWidget = WidgetProperties.background().observe(compositeToolBar);
		IObservableValue backgroundShellObserveValue = PojoProperties.value("background").observe(shell);
		bindingContext.bindValue(observeBackgroundCompositeObserveWidget, backgroundShellObserveValue, null, null);
		//
		IObservableValue observeBackgroundLblStatusBarObserveWidget = WidgetProperties.background().observe(lblStatusBar);
		bindingContext.bindValue(observeBackgroundLblStatusBarObserveWidget, backgroundShellObserveValue, null, null);
		//
		IObservableValue observeBackgroundCompositeStatusBarObserveWidget = WidgetProperties.background().observe(compositeStatusBar);
		bindingContext.bindValue(observeBackgroundCompositeStatusBarObserveWidget, backgroundShellObserveValue, null, null);
		//
		return bindingContext;
	}
}
