package com.github.n_i_e.deepfolderview;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import com.github.n_i_e.dirtreedb.IPreferenceObserver;

public class PreferenceRW {

	private static Preferences prefs = Preferences.userNodeForPackage(com.github.n_i_e.dirtreedb.PreferenceRW.class);

	// SwtFileFolderMenuWindowWidth

	private final static String SwtFileFolderMenuWindowWidth_KEY = "SwtFileFolderMenuWindowWidth";

	public static int getSwtFileFolderMenuWindowWidth() {
		int def = 640;
		return prefs.getInt(SwtFileFolderMenuWindowWidth_KEY, def);
	}

	public static void setSwtFileFolderMenuWindowWidth(int newvalue) {
		prefs.putInt(SwtFileFolderMenuWindowWidth_KEY, newvalue);
	}

	// SwtFileFolderMenuWindowHeight

	private final static String SwtFileFolderMenuWindowHeight_KEY = "SwtFileFolderMenuWindowHeight";

	public static int getSwtFileFolderMenuWindowHeight() {
		int def = 480;
		return prefs.getInt(SwtFileFolderMenuWindowHeight_KEY, def);
	}

	public static void setSwtFileFolderMenuWindowHeight(int newvalue) {
		prefs.putInt(SwtFileFolderMenuWindowHeight_KEY, newvalue);
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
		String result = prefs.get(SwtFileFolderMenuSortOrder_KEY, def);
		if (Arrays.asList(candidates).contains(result)) {
			return result;
		} else {
			return def;
		}
	}

	public static void setSwtFileFolderMenuSortOrder(String newvalue) {
		prefs.put(SwtFileFolderMenuSortOrder_KEY, newvalue);
	}

	// SwtDuplicateMenuWindowWidth

	private final static String SwtDuplicateMenuWindowWidth_KEY = "SwtDuplicateMenuWindowWidth";

	public static int getSwtDuplicateMenuWindowWidth() {
		int def = 640;
		return prefs.getInt(SwtDuplicateMenuWindowWidth_KEY, def);
	}

	public static void setSwtDuplicateMenuWindowWidth(int newvalue) {
		prefs.putInt(SwtDuplicateMenuWindowWidth_KEY, newvalue);
	}

	// SwtDuplicateMenuWindowHeight

	private final static String SwtDuplicateMenuWindowHeight_KEY = "SwtDuplicateMenuWindowHeight";

	public static int getSwtDuplicateMenuWindowHeight() {
		int def = 480;
		return prefs.getInt(SwtDuplicateMenuWindowHeight_KEY, def);
	}

	public static void setSwtDuplicateMenuWindowHeight(int newvalue) {
		prefs.putInt(SwtDuplicateMenuWindowHeight_KEY, newvalue);
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
		String result = prefs.get(SwtDuplicateMenuSortOrderR_KEY, def);
		if (Arrays.asList(candidates).contains(result)) {
			return result;
		} else {
			return def;
		}
	}

	public static void setSwtDuplicateMenuSortOrderR(String newvalue) {
		prefs.put(SwtDuplicateMenuSortOrderR_KEY, newvalue);
	}

	// SwtRootMenuWindowWidth

	private final static String SwtRootMenuWindowWidth_KEY = "SwtRootMenuWindowWidth";

	public static int getSwtRootMenuWindowWidth() {
		int def = 640;
		return prefs.getInt(SwtRootMenuWindowWidth_KEY, def);
	}

	public static void setSwtRootMenuWindowWidth(int newvalue) {
		prefs.putInt(SwtRootMenuWindowWidth_KEY, newvalue);
	}

	// SwtRootMenuWindowHeight

	private final static String SwtRootMenuWindowHeight_KEY = "SwtRootMenuWindowHeight";

	public static int getSwtRootMenuWindowHeight() {
		int def = 480;
		return prefs.getInt(SwtRootMenuWindowHeight_KEY, def);
	}

	public static void setSwtRootMenuWindowHeight(int newvalue) {
		prefs.putInt(SwtRootMenuWindowHeight_KEY, newvalue);
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
		String result = prefs.get(SwtRootMenuSortOrder_KEY, def);
		if (Arrays.asList(candidates).contains(result)) {
			return result;
		} else {
			return def;
		}
	}

	public static void setSwtRootMenuSortOrder(String newvalue) {
		prefs.put(SwtRootMenuSortOrder_KEY, newvalue);
	}

	// SwtRootMenuPathColumnWidth

	private final static String SwtRootMenuPathColumnWidth_KEY = "SwtRootMenuPathColumnWidth";

	public static int getSwtRootMenuPathColumnWidth() {
		int def = 230;
		return prefs.getInt(SwtRootMenuPathColumnWidth_KEY, def);
	}

	public static void setSwtRootMenuPathColumnWidth(int newvalue) {
		prefs.putInt(SwtRootMenuPathColumnWidth_KEY, newvalue);
	}

	// SwtRootMenuSizeColumnWidth

	private final static String SwtRootMenuSizeColumnWidth_KEY = "SwtRootMenuSizeColumnWidth";

	public static int getSwtRootMenuSizeColumnWidth() {
		int def = 110;
		return prefs.getInt(SwtRootMenuSizeColumnWidth_KEY, def);
	}

	public static void setSwtRootMenuSizeColumnWidth(int newvalue) {
		prefs.putInt(SwtRootMenuSizeColumnWidth_KEY, newvalue);
	}

	// SwtRootMenuCompressedsizeColumnWidth

	private final static String SwtRootMenuCompressedsizeColumnWidth_KEY = "SwtRootMenuCompressedsizeColumnWidth";

	public static int getSwtRootMenuCompressedsizeColumnWidth() {
		int def = 110;
		return prefs.getInt(SwtRootMenuCompressedsizeColumnWidth_KEY, def);
	}

	public static void setSwtRootMenuCompressedsizeColumnWidth(int newvalue) {
		prefs.putInt(SwtRootMenuCompressedsizeColumnWidth_KEY, newvalue);
	}

	// SwtRootMenuMaximumsizeColumnWidth

	private final static String SwtRootMenuMaximumsizeColumnWidth_KEY = "SwtRootMenuMaximumsizeColumnWidth";

	public static int getSwtRootMenuMaximumsizeColumnWidth() {
		int def = 110;
		return prefs.getInt(SwtRootMenuMaximumsizeColumnWidth_KEY, def);
	}

	public static void setSwtRootMenuMaximumsizeColumnWidth(int newvalue) {
		prefs.putInt(SwtRootMenuMaximumsizeColumnWidth_KEY, newvalue);
	}

	// SwtRootMenuPercentusageColumnWidth

	private final static String SwtRootMenuPercentusageColumnWidth_KEY = "SwtRootMenuPercentusageColumnWidth";

	public static int getSwtRootMenuPercentusageColumnWidth() {
		int def = 35;
		return prefs.getInt(SwtRootMenuPercentusageColumnWidth_KEY, def);
	}

	public static void setSwtRootMenuPercentusageColumnWidth(int newvalue) {
		prefs.putInt(SwtRootMenuPercentusageColumnWidth_KEY, newvalue);
	}

	// com.github.n_i_e.dirtreedb.PreferenceRW compatibility methods

	public static void addObserver(IPreferenceObserver updater) {
		com.github.n_i_e.dirtreedb.PreferenceRW.addObserver(updater);
	}

	public static boolean unregist(IPreferenceObserver updater) {
		return com.github.n_i_e.dirtreedb.PreferenceRW.unregist(updater);
	}

	public static String getDBFilePath() {
		return com.github.n_i_e.dirtreedb.PreferenceRW.getDBFilePath();
	}

	public static void setDBFilePath(String newvalue) {
		com.github.n_i_e.dirtreedb.PreferenceRW.setDBFilePath(newvalue);
	}

	public static int getWindowsIdleSeconds() {
		return com.github.n_i_e.dirtreedb.PreferenceRW.getWindowsIdleSeconds();
	}

	public static void setWindowsIdleSeconds(int windowsIdleSeconds) {
		com.github.n_i_e.dirtreedb.PreferenceRW.setWindowsIdleSeconds(windowsIdleSeconds);
	}

	public static int getNumCrawlingThreads() {
		return com.github.n_i_e.dirtreedb.PreferenceRW.getNumCrawlingThreads();
	}

	public static void setNumCrawlingThreads(int numCrawlingThreads) {
		com.github.n_i_e.dirtreedb.PreferenceRW.setNumCrawlingThreads(numCrawlingThreads);
	}

	public static String getZipListerCharset() {
		return com.github.n_i_e.dirtreedb.PreferenceRW.getZipListerCharset();
	}

	public static void setZipListerCharset(String newvalue) {
		com.github.n_i_e.dirtreedb.PreferenceRW.setZipListerCharset(newvalue);
	}

	public static HashMap<String, Boolean> getExtensionAvailabilityMap() {
		return com.github.n_i_e.dirtreedb.PreferenceRW.getExtensionAvailabilityMap();
	}

	public static void setExtensionAvailabilityMap(Map<String, Boolean> newvalue) {
		com.github.n_i_e.dirtreedb.PreferenceRW.setExtensionAvailabilityMap(newvalue);
	}

}
