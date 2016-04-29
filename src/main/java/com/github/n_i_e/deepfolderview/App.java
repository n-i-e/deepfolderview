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

import java.util.Date;

import org.eclipse.swt.widgets.Display;

import com.github.n_i_e.dirtreedb.StandardCrawler;

public class App
{
	public static void main(String[] args) {
		// SWT Initialize (TrayIcon)
		final Display display = Display.getDefault();
		final SwtTaskTrayIcon swtTrayIcon = new SwtTaskTrayIcon(display);
		LazyAccessorThreadRunningConfigSingleton.getInstance().setMessageWriter(swtTrayIcon);

		writelog("--- start ---");
		StandardCrawler scenario = new StandardCrawler(LazyAccessorThreadRunningConfigSingleton.getInstance());
		scenario.start();

		// SWT Event Loop (TrayIcon)
		swtTrayIcon.swtEventLoop(display);
    }

	static void writelog(final String message) {
		Date now = new Date();
		System.out.print(now);
		System.out.print(" ");
		System.out.println(message);
	}

	protected static void writeMessage(String title, String message) {
		LazyAccessorThreadRunningConfigSingleton.getInstance().getMessageWriter().writeMessage(title, message);
	}

	protected static void writeInformation(String title, String message) {
		LazyAccessorThreadRunningConfigSingleton.getInstance().getMessageWriter().writeInformation(title, message);
	}

	protected static void writeWarning(String title, String message) {
		LazyAccessorThreadRunningConfigSingleton.getInstance().getMessageWriter().writeWarning(title, message);
	}

	protected static void writeError(String title, String message) {
		LazyAccessorThreadRunningConfigSingleton.getInstance().getMessageWriter().writeError(title, message);
	}
}
