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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.github.n_i_e.dirtreedb.DBPathEntry;
import com.github.n_i_e.dirtreedb.MessageWriter;
import com.github.n_i_e.dirtreedb.lazy.RunnableWithLazyProxyDirTreeDBProvider;

public class SwtTaskTrayIcon extends SwtCommonFileFolderRootTaskTrayIconMenu implements MessageWriter {
	private final TrayItem icon;
	private final Shell shell;

	@Override protected Shell getShell() { return shell; }

	public SwtTaskTrayIcon(Display display) {
		icon = new TrayItem(display.getSystemTray(), SWT.NONE);

		icon.setImage(SWTResourceManager.getImage(DeepFolderView.class, "/com/github/n_i_e/deepfolderview/icon/drive-harddisk.png"));
		icon.setToolTipText("DirTreeDB for Windows");
		icon.setText("DirTreeDB");

		shell = new Shell(display);
		final Menu menu = new Menu(shell, SWT.POP_UP);

		icon.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event e) {
				menu.setVisible(true);
			}
		});

		icon.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				menu.setVisible(true);
			}
		});

		MenuItem mntmRootMenu = new MenuItem(menu, SWT.PUSH);
		mntmRootMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onMntmRootMenuSelected(e);
			}
		});
		mntmRootMenu.setText(Messages.SwtTaskTrayIcon_mntmRootMenu_text);

		MenuItem mntmSearch = new MenuItem(menu, SWT.PUSH);
		mntmSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onMntmSearchSelected(e);
			}
		});
		mntmSearch.setText(Messages.SwtTaskTrayIcon_mntmSearch_text);

		MenuItem mntmRun = new MenuItem(menu, SWT.PUSH);
		mntmRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onMntmRunSelected(e);
			}
		});
		mntmRun.setText(Messages.SwtTaskTrayIcon_mntmRun_text);

		MenuItem mntmConfigure = new MenuItem(menu, SWT.PUSH);
		mntmConfigure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onMntmConfigureSelected(e);
			}
		});
		mntmConfigure.setText(Messages.SwtTaskTrayIcon_mntmConfigure_text);

		MenuItem mntmQuit = new MenuItem(menu, SWT.PUSH);
		mntmQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onMntmExitSelected(e);
			}
		});
		mntmQuit.setText(Messages.mntmQuit_text);

		MenuItem mntmOpenSourceLicenses = new MenuItem(menu, SWT.PUSH);
		mntmOpenSourceLicenses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOpenSourceLicensesSelected(e);
			}
		});
		mntmOpenSourceLicenses.setText(Messages.mntmOpenSourceLicenses_text);
}

	protected void onMntmRootMenuSelected(SelectionEvent e) {
		new SwtRootMenu().setSearchStringAndRefresh("");
	}

	protected void onMntmSearchSelected(SelectionEvent e) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				InputDialog d = new InputDialog(shell,
						"Search",
						"検索文字列を入力して下さい。",
						"",
						new IInputValidator () {
					public String isValid(String newText) {
						return null;
					}
				});
				if (d.open() == Window.OK) {
					String searchString = d.getValue();
					new SwtFileFolderMenu().setLocationAndRefresh(searchString);
				}
			}
		});
	}

	protected void onMntmRunSelected(SelectionEvent e) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				InputDialog d = new InputDialog(shell,
						"Run",
						"パス名を入力して下さい。",
						"",
						new IInputValidator () {
					public String isValid(String newText) {
						return null;
					}
				});
				if (d.open() == Window.OK) {
					final String runString = d.getValue();
					DeepFolderView.getProv().getThread(new RunnableWithLazyProxyDirTreeDBProvider() {

						@Override
						public void run() throws SQLException, InterruptedException {
							Desktop desktop = Desktop.getDesktop();
							DBPathEntry entry = getDB().getDBPathEntryByPath(runString);
							if (entry == null) {
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										writeMessageBox("No such file or directory: " + runString);
									}
								});
								return;
							}
							if (entry.isCompressedFolder()) {
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										writeMessageBox(entry.getPath() + " is a compressed folder.");
									}
								});
								return;
							}

							if (entry.isFolder() || entry.isFile()) {
								try {
									desktop.open(new File(entry.getPath()));
								} catch (IOException e1) {}
							} else if (entry.isCompressedFile()) {
								String pt = entry.getPath();
								pt = replaceToWindowsSafeFileName(pt);

								try {
									final File toFile = File.createTempFile("DTDB", pt);

									if (toFile == null || !toFile.canWrite()) {
									} else {
										toFile.deleteOnExit();
										final String toPath = toFile.getAbsolutePath();
										InputStream inf = getDB().getInputStream(entry);
										Files.copy(inf , toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
										inf.close();
										toFile.setLastModified(entry.getDateLastModified());
										desktop.open(new File(toPath));
									}
								} catch (IOException e) {
									Display.getDefault().asyncExec(new Runnable() {
										public void run() {
											String msg = "Failed creating temporary file.";
											writeMessageBox(msg);
										}
									});
								}
							}
						}
					}).start();
				}
			}
		});
	}

	protected void onMntmConfigureSelected(SelectionEvent e) {
		new SwtConfigure(shell).open();
	}

	protected void onMntmExitSelected(SelectionEvent e) {
		icon.dispose();
	}

	protected void onOpenSourceLicensesSelected(SelectionEvent e) {
		new SwtOpenSourceLicenses(shell).open();
	}

	private void writeTaskTrayMessage(final String title, final String message, final int icontype) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				ToolTip tip = new ToolTip(shell, SWT.BALLOON | icontype);
				tip.setText(title);
				tip.setMessage(message);
				icon.setToolTip(tip);
				tip.setVisible(true);
			}
		});
	}

	public void writeMessage(String title, String message) {
		writeTaskTrayMessage(title, message, 0);
	}

	public void writeInformation(String title, String message) {
		writeTaskTrayMessage(title, message, SWT.ICON_INFORMATION);
	}

	public void writeWarning(String title, String message) {
		writeTaskTrayMessage(title, message, SWT.ICON_WARNING);
	}

	public void writeError(String title, String message) {
		writeTaskTrayMessage(title, message, SWT.ICON_ERROR);
	}

	public void swtEventLoop(final Display display) {
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				while (!icon.isDisposed()) {
					try {
						if (!display.readAndDispatch()) {
							display.sleep();
						}
					} catch (SWTException e) {
					} catch (Exception e) {
						writeError("Exception at SwtTaskTrayIcon bottom", e.toString());
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});

	}
}
