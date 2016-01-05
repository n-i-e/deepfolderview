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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.prefs.Preferences;

import com.github.n_i_e.dirtreedb.ArchiveListerFactory;
import com.github.n_i_e.dirtreedb.Assertion;
import com.github.n_i_e.dirtreedb.IsWin32Idle;
import com.github.n_i_e.dirtreedb.LazyProxyDirTreeDb;
import com.github.n_i_e.dirtreedb.ZipLister;

public class PreferenceBox {

	private static PreferenceBox instance = null;
	private Preferences prefs;

	private PreferenceBox() {
		// "instance" parameter is not yet set when this function is called
		prefs = Preferences.userNodeForPackage(this.getClass());
		ZipLister.setCharset(getZipListerCharsetLocally());
		LazyAccessorThreadRunningConfigSingleton.getInstance().setDbFilePath(getDbFilePathLocally());
		LazyAccessorThreadRunningConfigSingleton.getInstance().setExtensionAvailabilityMap(getArchiveListerExtensionAvailabilityListLocally());
		LazyProxyDirTreeDb.setNumCrawlingThreads(getNumCrawlingThreadsLocally());
		IsWin32Idle.setWindowsIdleSeconds(getWindowsIdleSecondsLocally());
	}

	public static PreferenceBox getInstance() {
		if (instance == null) {
			instance = new PreferenceBox();
		}
		return instance;
	}

	// DbFilePath

	private final static String DbFilePath_KEY = "DbFilePath";

	private String getDbFilePathLocally() {
		String def = System.getProperty("user.home") + "\\.dirtreedb\\dirtreedb.sqlite";
		String result = prefs.get(DbFilePath_KEY, def);
		File p = new File(result).getParentFile();
		p.mkdirs();
		return result;
	}

	public static String getDbFilePath() {
		return getInstance().getDbFilePathLocally();
	}

	public static void setDbFilePath(String newvalue) {
		getInstance().prefs.put(DbFilePath_KEY, newvalue);
		LazyAccessorThreadRunningConfigSingleton.getInstance().setDbFilePath(newvalue);
	}

	// WindowsIdleSeconds

	private final static String WindowsIdleSeconds_KEY = "WindowsIdleSeconds";

	private int getWindowsIdleSecondsLocally() {
		return Integer.parseInt(prefs.get(WindowsIdleSeconds_KEY, "300"));
	}

	public static int getWindowsIdleSeconds() {
		return getInstance().getWindowsIdleSecondsLocally();
	}

	public static void setWindowsIdleSeconds(int windowsIdleSeconds) {
		getInstance().prefs.put(WindowsIdleSeconds_KEY, String.valueOf(windowsIdleSeconds));
		IsWin32Idle.setWindowsIdleSeconds(windowsIdleSeconds);
	}

	// NumCrawlingThreads

	private final static String NumCrawlingThreads_KEY = "NumCrawlingThreads";

	private int getNumCrawlingThreadsLocally() {
		return Integer.parseInt(prefs.get(NumCrawlingThreads_KEY, "4"));
	}

	public static int getNumCrawlingThreads() {
		return getInstance().getNumCrawlingThreadsLocally();
	}

	public static void setNumCrawlingThreads(int numCrawlingThreads) {
		getInstance().prefs.put(NumCrawlingThreads_KEY, String.valueOf(numCrawlingThreads));
		LazyProxyDirTreeDb.setNumCrawlingThreads(numCrawlingThreads);
	}

	// ZipListerCharset

	private final static String ZipListerCharset_KEY = "ZipListerCharset";

	private String getZipListerCharsetLocally() {
		return prefs.get(ZipListerCharset_KEY, "windows-31j");
	}

	public static String getZipListerCharset() {
		return getInstance().getZipListerCharsetLocally();
	}

	public static void setZipListerCharset(String newvalue) {
		getInstance().prefs.put(ZipListerCharset_KEY, newvalue);
		ZipLister.setCharset(newvalue);
	}

	// SwtFileFolderMenuSortOrder

	private final static String SwtFileFolderMenuSortOrder_KEY = "SwtFileFolderMenuSortOrder";

	public static String getSwtFileFolderMenuSortOrder() {
		String[] candidates = {
				"path", "path DESC",
				"datelastmodified", "datelastmodified DESC",
				"size", "size DESC",
				"compressedsize", "compressedsize DESC",
				"duplicate", "duplicate DESC",
				"dedupablesize", "dedupablesize DESC"
		};

		String def = "compressedsize DESC";
		String result = getInstance().prefs.get(SwtFileFolderMenuSortOrder_KEY, def);
		if (Arrays.asList(candidates).contains(result)) {
			return result;
		} else {
			return def;
		}
	}

	public static void setSwtFileFolderMenuSortOrder(String newvalue) {
		getInstance().prefs.put(SwtFileFolderMenuSortOrder_KEY, newvalue);
	}

	// SwtDuplicateMenuSortOrderR

	private final static String SwtDuplicateMenuSortOrderR_KEY = "SwtDuplicateMenuSortOrderR";

	public static String getSwtDuplicateMenuSortOrderR() {
		String[] candidates = {
				"path", "path DESC",
				"datelastmodified", "datelastmodified DESC",
				"size", "size DESC",
				"compressedsize", "compressedsize DESC",
		};

		String def = "compressedsize DESC";
		String result = getInstance().prefs.get(SwtDuplicateMenuSortOrderR_KEY, def);
		if (Arrays.asList(candidates).contains(result)) {
			return result;
		} else {
			return def;
		}
	}

	public static void setSwtDuplicateMenuSortOrderR(String newvalue) {
		getInstance().prefs.put(SwtDuplicateMenuSortOrderR_KEY, newvalue);
	}

	// SwtRootMenuSortOrder

	private final static String SwtRootMenuSortOrder_KEY = "SwtRootMenuSortOrder";

	public static String getSwtRootMenuSortOrder() {
		String[] candidates = {
				"path", "path DESC",
				"datelastmodified", "datelastmodified DESC",
				"size", "size DESC",
				"compressedsize", "compressedsize DESC",
		};

		String def = "path";
		String result = getInstance().prefs.get(SwtRootMenuSortOrder_KEY, def);
		if (Arrays.asList(candidates).contains(result)) {
			return result;
		} else {
			return def;
		}
	}

	public static void setSwtRootMenuSortOrder(String newvalue) {
		getInstance().prefs.put(SwtRootMenuSortOrder_KEY, newvalue);
	}

	// ArchiveListerExtensionAvailabilityList

	private static final String ArchiveListerExtensionAvailabilityList_KEY = "ArchiveListerExtensionAvailabilityList";

	private static final String[] disabledByDefaultExtensionList = {"jar"};

	private HashMap<String, Boolean> getArchiveListerExtensionAvailabilityListLocally() {
		Set<String> d1 = ArchiveListerFactory.getExtensionList();
		Assertion.assertNullPointerException(d1 != null);
		Assertion.assertNullPointerException(disabledByDefaultExtensionList != null);
		for (String d2: disabledByDefaultExtensionList) {
			d1.remove(d2);
		}
		String def = String.join(",", d1);

		String r1 = prefs.get(ArchiveListerExtensionAvailabilityList_KEY, def);
		ArrayList<String> r2 = new ArrayList<String>();
		for (String k: r1.split(",")) {
			r2.add(k);
		}

		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		for (String k: ArchiveListerFactory.getExtensionList()) {
			if (r2.contains(k)) {
				result.put(k, true);
			} else {
				result.put(k, false);
			}
		}

		return result;
	}

	public static HashMap<String, Boolean> getArchiveListerExtensionAvailabilityList() {
		return getInstance().getArchiveListerExtensionAvailabilityListLocally();
	}

	public static void setArchiveListerExtensionAvailabilityList(HashMap<String, Boolean> newvalue) {
		ArrayList<String> r1 = new ArrayList<String>();
		for (Entry<String, Boolean> k: newvalue.entrySet()) {
			if (k.getValue()) {
				r1.add(k.getKey());
			}
		}
		getInstance().prefs.put(ArchiveListerExtensionAvailabilityList_KEY, String.join(",", r1));
		LazyAccessorThreadRunningConfigSingleton.getInstance().setExtensionAvailabilityMap(getArchiveListerExtensionAvailabilityList());
	}
}
