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
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.github.n_i_e.dirtreedb.Assertion;
import com.github.n_i_e.dirtreedb.DBPathEntry;
import com.github.n_i_e.dirtreedb.PathEntry;
import com.ibm.icu.text.NumberFormat;


public class SwtRootMenu extends SwtCommonFileFolderRootMenu {
	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtSearch;
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
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					SwtRootMenu window = new SwtRootMenu();
					window.open();
					// test demonstration

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

	public SwtRootMenu() {
		createContents();
		shell.open();
		shell.layout();
	}

	/**
	 * Create contents of the window.
	 */
	private void createContents() {
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				Point p = shell.getSize();
				PreferenceRW.setSwtRootMenuWindowWidth(p.x);
				PreferenceRW.setSwtRootMenuWindowHeight(p.y);
				PreferenceRW.setSwtRootMenuSizeColumnWidth(tblclmnSize.getWidth());
				PreferenceRW.setSwtRootMenuCompressedsizeColumnWidth(tblclmnCompressedsize.getWidth());
				PreferenceRW.setSwtRootMenuMaximumsizeColumnWidth(tblclmnMaximumsize.getWidth());
				PreferenceRW.setSwtRootMenuPercentusageColumnWidth(tblclmnPercentusage.getWidth());
			}
		});
		shell.setImage(SWTResourceManager.getImage(SwtRootMenu.class, "/com/github/n_i_e/deepfolderview/icon/drive-harddisk.png"));
		shell.setMinimumSize(new Point(300, 200));
		shell.setSize(PreferenceRW.getSwtRootMenuWindowWidth(),PreferenceRW.getSwtDuplicateMenuWindowHeight());
		shell.setText(Messages.SwtRootMenu_shlRootFolders_text);
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

		MenuItem mntmOpenInNewWindow_1 = new MenuItem(menuFile, SWT.NONE);
		mntmOpenInNewWindow_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenInNewWindowSelected(e);
			}
		});
		mntmOpenInNewWindow_1.setText(Messages.mntmOpenInNewWindow_text);

		MenuItem mntmClose = new MenuItem(menuFile, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCloseSelected(e);
			}
		});
		mntmClose.setText(Messages.mntmClose_text);

		MenuItem mntmNewRoot_1 = new MenuItem(menuFile, SWT.NONE);
		mntmNewRoot_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onNewRootSelected();
			}
		});
		mntmNewRoot_1.setText(Messages.SwtRootMenu_mntmNewRoot_text);

		MenuItem mntmQuit = new MenuItem(menuFile, SWT.NONE);
		mntmQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onQuitSelected(e);
			}
		});
		mntmQuit.setText(Messages.mntmQuit_text);

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText(Messages.mntmEdit_text);

		Menu menuEdit = new Menu(mntmEdit);
		mntmEdit.setMenu(menuEdit);

		MenuItem mntmCopyAsString_1 = new MenuItem(menuEdit, SWT.NONE);
		mntmCopyAsString_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyAsStringSelected(e);
			}
		});
		mntmCopyAsString_1.setText(Messages.mntmCopyAsString_text);

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
		compositeToolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeToolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		compositeToolBar.setFont(SWTResourceManager.getFont("Meiryo UI", 12, SWT.NORMAL));
		GridLayout gl_compositeToolBar = new GridLayout(4, false);
		gl_compositeToolBar.horizontalSpacing = 0;
		gl_compositeToolBar.verticalSpacing = 0;
		gl_compositeToolBar.marginWidth = 0;
		gl_compositeToolBar.marginHeight = 0;
		compositeToolBar.setLayout(gl_compositeToolBar);
		formToolkit.adapt(compositeToolBar);
		formToolkit.paintBordersFor(compositeToolBar);

		txtSearch = new Text(compositeToolBar, SWT.BORDER);
		txtSearch.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				onSearchStringModified();
			}
		});
		GridData gd_txtSearch = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txtSearch.widthHint = 200;
		txtSearch.setLayoutData(gd_txtSearch);
		txtSearch.setFont(SWTResourceManager.getFont("Meiryo UI", 11, SWT.NORMAL));
		formToolkit.adapt(txtSearch, true, true);

		Button btnRefresh = new Button(compositeToolBar, SWT.NONE);
		btnRefresh.setToolTipText(Messages.btnRefresh_toolTipText);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setImage(SWTResourceManager.getImage(SwtRootMenu.class, "/com/github/n_i_e/deepfolderview/icon/view-refresh.png"));
		formToolkit.adapt(btnRefresh, true, true);

		Button btnAddNewRoot = new Button(compositeToolBar, SWT.NONE);
		btnAddNewRoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onNewRootSelected();
			}
		});
		btnAddNewRoot.setImage(SWTResourceManager.getImage(SwtRootMenu.class, "/com/github/n_i_e/deepfolderview/icon/list-add.png"));
		formToolkit.adapt(btnAddNewRoot, true, true);

		table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onTableSelected(e);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				onOpenInNewWindowSelected(e);
			}
		});
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);

		tblclmnPath = new TableColumn(table, SWT.LEFT);
		tblclmnPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferenceRW.setSwtRootMenuPathColumnWidth(tblclmnPath.getWidth());
				table.setSortColumn(tblclmnPath);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnPathSelected(tblclmnPath, e);
			}
		});
		tblclmnPath.setWidth(230);
		tblclmnPath.setWidth(PreferenceRW.getSwtRootMenuPathColumnWidth());
		tblclmnPath.setText(Messages.tblclmnPath_text);
		setTableSortDirection(tblclmnPath, "path", order);

		tblclmnSize = new TableColumn(table, SWT.RIGHT);
		tblclmnSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnSize);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnSizeSelected(tblclmnSize, e);
			}
		});
		tblclmnSize.setWidth(110);
		tblclmnSize.setWidth(PreferenceRW.getSwtRootMenuSizeColumnWidth());
		tblclmnSize.setText(Messages.tblclmnSize_text);
		setTableSortDirection(tblclmnSize, "size", order);

		tblclmnCompressedsize = new TableColumn(table, SWT.RIGHT);
		tblclmnCompressedsize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(tblclmnCompressedsize);
				if (table.getSortDirection() == SWT.UP) {
					table.setSortDirection(SWT.DOWN);
				} else {
					table.setSortDirection(SWT.UP);
				}
				onTblclmnCompressedsizeSelected(tblclmnCompressedsize, e);
			}
		});
		tblclmnCompressedsize.setWidth(110);
		tblclmnCompressedsize.setWidth(PreferenceRW.getSwtRootMenuCompressedsizeColumnWidth());
		tblclmnCompressedsize.setText(Messages.tblclmnCompressedesize_text);
		setTableSortDirection(tblclmnCompressedsize, "compressedsize", order);

		tblclmnMaximumsize = new TableColumn(table, SWT.RIGHT);
		tblclmnMaximumsize.setWidth(110);
		tblclmnMaximumsize.setWidth(PreferenceRW.getSwtRootMenuMaximumsizeColumnWidth());
		tblclmnMaximumsize.setText(Messages.tblclmnMaximumsize_text);

		tblclmnPercentusage = new TableColumn(table, SWT.RIGHT);
		tblclmnPercentusage.setWidth(35);
		tblclmnPercentusage.setWidth(PreferenceRW.getSwtRootMenuPercentusageColumnWidth());
		tblclmnPercentusage.setText(Messages.tblclmnPercentusage_text);

		Menu menu_1 = new Menu(table);
		table.setMenu(menu_1);

		MenuItem mntmOpenInNewWindow = new MenuItem(menu_1, SWT.NONE);
		mntmOpenInNewWindow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenInNewWindowSelected(e);
			}
		});
		mntmOpenInNewWindow.setText(Messages.mntmOpenInNewWindow_text);

		MenuItem mntmCopyAsString = new MenuItem(menu_1, SWT.NONE);
		mntmCopyAsString.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCopyAsStringSelected(e);
			}
		});
		mntmCopyAsString.setText(Messages.mntmCopyAsString_text);

		MenuItem mntmDelete = new MenuItem(menu_1, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onDeleteSelected(e);
			}
		});
		mntmDelete.setText(Messages.SwtRootMenu_mntmDelete_text);

		MenuItem mntmNewRoot = new MenuItem(menu_1, SWT.NONE);
		mntmNewRoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onNewRootSelected();
			}
		});
		mntmNewRoot.setText(Messages.SwtRootMenu_mntmNewRoot_text);

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

	public void writeStatusBar(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				lblStatusBar.setText(text);
			}
		});
	}

	protected void writeProgress(final int percent) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				getProgressBar().setSelection(percent);
			}
		});
	}

	protected void writeMessageBox(final String text) {
		MessageBox msgbox = new MessageBox(shell, SWT.OK);
		msgbox.setMessage(text);
		msgbox.open();
	}

	protected void onCloseSelected(SelectionEvent e) {
		shell.dispose();
	}

	protected void onQuitSelected(SelectionEvent e) {
		System.exit(0);
	}

	protected void onOpenInNewWindowSelected(SelectionEvent e) {
		DBPathEntry p = getSelectedPathEntry();
		if (p == null) {
			new SwtRootMenu().setSearchStringAndRefresh("");
		} else {
			new SwtFileFolderMenu().setLocationAndRefresh(p);
		}
	}

	protected void onDeleteSelected(SelectionEvent e) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				DBPathEntry p = getSelectedPathEntry();
				MessageBox err = new MessageBox(shell,SWT.OK|SWT.CANCEL);
				err.setMessage("ルートフォルダ " + p.getPath() + " をデータベースから削除します。よろしいですか？");
				int okCancel = err.open();
				if (okCancel == SWT.OK) {
					deleteRootFolderAndRefresh(p);
				}
			}
		});
	}

	protected void onNewRootSelected() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				InputDialog d = new InputDialog(shell,
						"New Root",
						"追加するルートフォルダを入力して下さい。\n例） C:\\ , \\\\host\\directory\\",
						"",
						new IInputValidator () {
					public String isValid(String newText) {
						return null;
						//File f = new File(newText);
						//if (f.isAbsolute() && f.isDirectory()) {
						//	return null;
						//} else {
						//	return newText;
						//}
					}
				});
				if (d.open() == Window.OK) {
					String newroot = d.getValue();
					if (! "\\".equals(newroot.substring(newroot.length()-1))) {
						newroot += "\\";
					}
					for (DBPathEntry p: pathentrylist) {
						if (p.getPath().equals(newroot)) {
							writeMessageBox("フォルダ " + newroot + " は既に存在します。");
							return;
						}
					}
					File fileobj = new File(newroot);
					if (!fileobj.exists() || !fileobj.isDirectory() || !fileobj.isAbsolute()) {
						writeMessageBox("フォルダ " + newroot + " が見つかりません。");
					} else {
						addRootFolderAndRefresh(newroot);
					}
				}
			}
		});
	}

	String searchString = null;

	protected void onSearchStringModified() {
		searchString = txtSearch.getText();
		refresh();
	}

	public void setSearchStringAndRefresh(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				txtSearch.setText(text); // onSearchStringModified() is automatically called here.
			}
		});
	}

	public String getSearchString() {
		return searchString;
	}

	private String order = PreferenceRW.getSwtRootMenuSortOrder();

	private void setSortOrderAndRefresh(String neworder) {
		order = neworder;
		refresh();
	}

	protected void onCopyAsStringSelected(SelectionEvent e) {
		ArrayList<String> s = new ArrayList<String>();
		for (PathEntry p: getSelectedPathEntries()) {
			s.add(p.getPath());
		}
		StringSelection ss = new StringSelection(String.join("\n", s));
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(ss, ss);
	}

	protected void onTableSelected(SelectionEvent e) {}

	protected void onTblclmnPathSelected(TableColumn tblclmnPath, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			setSortOrderAndRefresh("path");
		} else {
			setSortOrderAndRefresh("path DESC");
		}
		PreferenceRW.setSwtRootMenuSortOrder(order);
	}

	protected void onTblclmnSizeSelected(TableColumn tblclmnSize, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			setSortOrderAndRefresh("size");
		} else {
			setSortOrderAndRefresh("size DESC");
		}
		PreferenceRW.setSwtRootMenuSortOrder(order);
	}

	protected void onTblclmnCompressedsizeSelected(TableColumn tblclmnCompressedSize, SelectionEvent e) {
		if (table.getSortDirection() == SWT.UP) {
			setSortOrderAndRefresh("compressedsize");
		} else {
			setSortOrderAndRefresh("compressedsize DESC");
		}
		PreferenceRW.setSwtRootMenuSortOrder(order);
	}


	private List<DBPathEntry> pathentrylist = new ArrayList<DBPathEntry>();

	private TableColumn tblclmnPath;

	private TableColumn tblclmnSize;

	private TableColumn tblclmnCompressedsize;

	private TableColumn tblclmnMaximumsize;

	private TableColumn tblclmnPercentusage;

	private DBPathEntry getSelectedPathEntry() {
		int row = table.getSelectionIndex();
		if (row >= 0) {
			return pathentrylist.get(row);
		} else {
			return null;
		}
	}

	private List<PathEntry> getSelectedPathEntries() {
		int[] rows = table.getSelectionIndices();

		ArrayList<PathEntry> result = new ArrayList<PathEntry>();
		for (int row: rows) {
			result.add(pathentrylist.get(row));
		}
		return result;
	}

	private void refresh() {
		refresh(null, null);
	}

	private void refresh(String newroot, DBPathEntry deleteroot) {
		refresh(new Scenario(newroot, deleteroot));
	}

	private void deleteRootFolderAndRefresh(DBPathEntry deleteroot) {
		refresh(null, deleteroot);
	}

	private void addRootFolderAndRefresh(String newroot) {
		refresh(newroot, null);
	}

	class Scenario extends SwtCommonFileFolderRootMenu.Scenario {

		private String _newroot = null;
		private DBPathEntry _deleteroot = null;

		Scenario(String newroot, DBPathEntry deleteroot) {
			_newroot = newroot;
			_deleteroot = deleteroot;
		}

		@Override
		public void run() throws SQLException, InterruptedException {
			try {
				writeProgress(10);
				cleanupTable();
				if (_newroot != null) {
					try {
						getDB().insert(null, new PathEntry(new File(_newroot)));
						getDB().consumeUpdateQueue(0);
					} catch (IOException e) {
						String msg = String.format("Error: insert root failed for IOException: %s", _newroot);
						Debug.writelog(msg);
						writeStatusBar(msg);
					} catch (SQLException e) {
						String msg = String.format("Error: insert root failed for SQLException: %s", _newroot);
						Debug.writelog(msg);
						writeStatusBar(msg);
						throw e;
					}
				}

				if (_deleteroot != null) {
					Assertion.assertAssertionError(_deleteroot.getPathId() == _deleteroot.getRootId());
					Assertion.assertAssertionError(_deleteroot.getParentId() == 0L);
					getDB().delete(_deleteroot);
					getDB().consumeUpdateQueue(0);
				}

				writeStatusBar("Querying...");
				writeProgress(70);

				PreparedStatement ps;
				if (getSearchString() == null || "".equals(getSearchString())) {
					String sql = "SELECT * FROM directory WHERE type=0 AND parentid=0 ORDER BY " + order;
					Debug.writelog(sql);
					ps = getDB().prepareStatement(sql);
				} else {
					ArrayList<String> p = new ArrayList<String> ();
					for (String s: getSearchString().split(" ")) {
						if (! "".equals(s)) {
							p.add("path LIKE ?");
						}
					}
					String sql = "SELECT * FROM directory WHERE type=0 AND parentid=0 AND (" + String.join(" OR ", p) + ") ORDER BY " + order;
					Debug.writelog(sql);
					ps = getDB().prepareStatement(sql);
					int c = 1;
					for (String s: getSearchString().split(" ")) {
						if (! "".equals(s)) {
							ps.setString(c, "%" + s + "%");
							Debug.writelog(c + " %" + s + "%");
							c++;
						}
					}
				}

				try {
					com.github.n_i_e.dirtreedb.lazy.LazyUpdater.Dispatcher disp = getDB().getDispatcher();
					disp.setList(com.github.n_i_e.dirtreedb.lazy.LazyUpdater.Dispatcher.NONE);
					disp.setCsum(com.github.n_i_e.dirtreedb.lazy.LazyUpdater.Dispatcher.NONE);
					disp.setNoReturn(true);
					ResultSet rs = ps.executeQuery();
					writeStatusBar("Listing...");
					writeProgress(90);
					int count = 0;
					try {
						while (rs.next()) {
							final DBPathEntry p1 = getDB().rsToPathEntry(rs);
							final long maximumsize = new File(p1.getPath()).getTotalSpace();
							addRow(p1, maximumsize);
							try {
								disp.dispatch(p1);
							} catch (IOException e) {}
							count ++;
						}
						writeStatusBar(String.format("%d root folders", count));
					} finally {
						rs.close();
					}
				} finally {
					ps.close();
				}
				writeProgress(0);
			} catch (WindowDisposedException e1) {}
		}

		protected void cleanupTable() throws WindowDisposedException {
			if (table.isDisposed()) {
				throw new WindowDisposedException("!! Window disposed at cleanupTable");
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					pathentrylist.clear();
					table.removeAll();
				}
			});
		}

		protected void addRow(final DBPathEntry entry, final long maximumsize) throws WindowDisposedException {
			if (table.isDisposed()) {
				throw new WindowDisposedException("!! Window disposed at addRow");
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					pathentrylist.add(entry);

					final long percentusage = maximumsize != 0 ? 100 * entry.getCompressedSize() / maximumsize : 0;

					final NumberFormat numf = NumberFormat.getNumberInstance();
					String[] row = {
							entry.getPath(),
							numf.format(entry.getSize()),
							numf.format(entry.getCompressedSize()),
							(maximumsize != 0 ? numf.format(maximumsize) : null),
							(maximumsize != 0 ? numf.format(percentusage) : null),
							};

					TableItem tableItem = new TableItem(table, SWT.NONE);
					tableItem.setText(row);
				}
			});
		}
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
