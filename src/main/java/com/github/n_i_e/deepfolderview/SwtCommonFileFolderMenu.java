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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.github.n_i_e.dirtreedb.DbPathEntry;
import com.github.n_i_e.dirtreedb.PathEntry;
import com.github.n_i_e.dirtreedb.RunnableWithLazyProxyDirTreeDbProvider;

public abstract class SwtCommonFileFolderMenu extends SwtCommonFileFolderRootMenu {
	protected List<DbPathEntry> pathentrylist = new ArrayList<DbPathEntry>();

	protected class Location {
		private String pathString = "";
		private long pathId = 0L;
		private DbPathEntry pathEntry = null;
		private String searchString = null;

		public synchronized String getPathString() {
			return pathString;
		}
		public synchronized void setPathString(String pathString) {
			this.pathString = pathString;
		}
		public synchronized long getPathId() {
			return pathId;
		}
		public synchronized void setPathId(long pathId) {
			this.pathId = pathId;
		}
		public synchronized DbPathEntry getPathEntry() {
			return pathEntry;
		}
		public synchronized void setPathEntry(DbPathEntry pathEntry) {
			this.pathEntry = pathEntry;
		}
		public synchronized String getSearchString() {
			return searchString;
		}
		public synchronized void setSearchString(String searchString) {
			this.searchString = searchString;
		}
	}

	protected NavigatableArrayList<Location> location;

	public String getLocationPath() {
		return location.get().getPathString();
	}

	public long getLocationPathId() {
		return location.get().getPathId();
	}

	public DbPathEntry getLocationPathEntry() {
		return location.get().getPathEntry();
	}

	public String getLocationSearchString() {
		return location.get().getSearchString();
	}

	protected void onCloseSelected() {
		getShell().dispose();
	}

	protected void onQuitSelected() {
		System.exit(0);
	}

	protected void onCopyToSelected() {
		DbPathEntry p0 = getSelectedPathEntry();
		final DbPathEntry p;
		if (p0 == null) {
			p = getLocationPathEntry();
		} else {
			p = p0;
		}
		if (p == null) {
			writeMessageBox("Copying error! Path not selected");
			return;
		}
		if (p.isFolder() || p.isCompressedFolder()) {
			writeMessageBox(p.getPath() + " is a directory!  Choose a file or a compressed file.");
			return;
		}

		String pt = p.getPath();
		pt = replaceToWindowsSafeFileName(pt);
		FileDialog d = new FileDialog(getShell(), SWT.SAVE);
		d.setText("Copy from " + p.getPath());
		d.setFileName(pt);
		d.setOverwrite(true);
		final String toPath = d.open();
		if (toPath == null) {
			writeStatusBar("Copying cancelled: " + p.getPath());
		} else {
			if (p.isFile()) {
				try {
					writeStatusBar("Start copying: " + p.getPath() + " to " + toPath);
					Files.copy((new File(p.getPath())).toPath(), (new File(toPath)).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					writeStatusBar("Copying finished: " + p.getPath() + " to " + toPath);
				} catch (IOException e1) {
					writeStatusBar("Copying failed: " + p.getPath() + " to " + toPath);
					e1.printStackTrace();
				}
			} else {
				assert(p.isCompressedFile());
				App.getProv().getThread(new RunnableWithLazyProxyDirTreeDbProvider() {

					private boolean isCopySuccessful = false;
					@Override
					public void openingHook() {
						writeStatusBar("Start copying from compressed file: " + p.getPath() + " to " + toPath);
					}

					@Override
					public void run() throws SQLException, InterruptedException {
						File f = new File(toPath);
						try {
							Files.copy(getDb().getInputStream(p) , f.toPath(), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							isCopySuccessful = false;
						}
						f.setLastModified(p.getDateLastModified());
						isCopySuccessful = true;
					}

					@Override
					public void closingHook() {
						if (isCopySuccessful) {
							writeStatusBar("Copying from compressed file finished: " + p.getPath() + " to " + toPath);
						} else {
							String msg = "Copying from compressed file failed: " + p.getPath() + " to " + toPath;
							getMessageWriter().writeWarning("Copy Failed", msg);
							writeStatusBar(msg);
						}
					}

				}).start();
			}
		}
	}

	protected void onRunSelected() {
		final Desktop desktop = Desktop.getDesktop();

		final DbPathEntry entry = getSelectedPathEntry();
		if (entry == null) {
			writeMessageBox("No file selected to run!");
			return;
		}
		if (entry.isCompressedFolder()) {
			writeMessageBox(entry.getPath() + " is a compressed folder.");
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
					writeStatusBar("Copying cancelled: " + entry.getPath());
				} else {
					toFile.deleteOnExit();
					final String toPath = toFile.getAbsolutePath();
					App.getProv().getThread(new RunnableWithLazyProxyDirTreeDbProvider() {
						private boolean isCopySuccessful = false;
						@Override
						public void openingHook() {
							writeStatusBar(String.format("Start copying from compressed file: %s to %s", entry.getPath(), toPath));
						}

						@Override
						public void run() throws SQLException, InterruptedException {
							InputStream inf;
							try {
								inf = getDb().getInputStream(entry);
								Files.copy(inf , toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
								inf.close();
								desktop.open(new File(toPath));
								isCopySuccessful = true;
							} catch (IOException e) {
								isCopySuccessful = false;
							}
						}

						@Override
						public void closingHook() {
							if (isCopySuccessful) {
								writeStatusBar(String.format("Copying from compressed file finished: %s to %s", entry.getPath(), toPath));
							} else {
								String msg = String.format("Copying from compressed file failed: %s to %s", entry.getPath(), toPath);
								getMessageWriter().writeWarning("Copy Failed", msg);
								writeStatusBar(msg);
							}
						}
					}).start();
				}
			} catch (IOException e) {
				String msg = "Failed creating temporary file.";
				writeStatusBar(msg);
				writeMessageBox(msg);
			}
		}
	}

	protected DbPathEntry getSelectedPathEntry() {
		int row = getTable().getSelectionIndex();
		try {
			return pathentrylist.get(row);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	protected List<PathEntry> getSelectedPathEntries() {
		int[] rows = getTable().getSelectionIndices();

		ArrayList<PathEntry> result = new ArrayList<PathEntry>();
		for (int row: rows) {
			result.add(pathentrylist.get(row));
		}
		return result;
	}

	public abstract class Scenario extends SwtCommonFileFolderRootMenu.Scenario {
		Scenario() {
			super();
		}
	}
}
