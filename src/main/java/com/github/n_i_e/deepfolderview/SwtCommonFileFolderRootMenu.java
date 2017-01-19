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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.github.n_i_e.dirtreedb.lazy.LazyThread;
import com.github.n_i_e.dirtreedb.lazy.RunnableWithLazyUpdaterProvider;
import com.github.n_i_e.dirtreedb.lazy.RunnableWithLazyUpdaterProviderPlusOneAhead;

public abstract class SwtCommonFileFolderRootMenu extends SwtCommonFileFolderRootTaskTrayIconMenu {

	private LazyThread thread = null;
	private RunnableWithLazyUpdaterProviderPlusOneAhead scenariolist =
			new RunnableWithLazyUpdaterProviderPlusOneAhead();

	protected void refresh(RunnableWithLazyUpdaterProvider scenario) {
		scenariolist.add(scenario);
		if (scenariolist.size() == 1) {
			Debug.writelog("refresh mode 1 size=" + scenariolist.size());
			thread = DeepFolderView.getProv().getThread(scenariolist);
			thread.start();
		} else {
			if (thread != null && thread.isAlive()) {
				Debug.writelog("refresh mode 2 size=" + scenariolist.size());
				thread.interrupt();
			} else {
				Debug.writelog("refresh mode 3 size=" + scenariolist.size());
				thread = DeepFolderView.getProv().getThread(scenariolist);
				thread.start();
			}
		}
	}

	protected abstract Table getTable();
	protected abstract Label getLblStatusBar();
	protected abstract ProgressBar getProgressBar();

	protected void setTableSortDirection(TableColumn tblclmn, String columnName, String order) {
		if (columnName.equals(order)) {
			getTable().setSortColumn(tblclmn);
			getTable().setSortDirection(SWT.UP);
		} else if ((columnName + " DESC").equals(order)) {
			getTable().setSortColumn(tblclmn);
			getTable().setSortDirection(SWT.DOWN);
		}
	}

	public void writeStatusBar(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				getLblStatusBar().setText(text);
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

	public abstract class Scenario extends RunnableWithLazyUpdaterProvider {

		@Override
		public void openingHook() {
			writeProgress(10);
		}

		@Override
		public void closingHook() {
			writeProgress(0);
		}

		public void openingHook2() {
			writeProgress(20);
		}

		public void closingHook2() {
			writeProgress(100);
		}
	}

	public class WindowDisposedException extends Exception {
		WindowDisposedException(String s) {
			super(s);
		}
	}

}
