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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.github.n_i_e.deepfolderview.messages"; //$NON-NLS-1$
	public static String mntmFile_text;
	public static String SwtConfigure_lblWindowsIdleTime_text;
	public static String SwtConfigure_tbtmParameters_text;
	public static String SwtConfigure_lblSeconds_text;
	public static String SwtTaskTrayIcon_mntmRootMenu_text;
	public static String mntmEdit_text;
	public static String mntmCopyTo_text;
	public static String mntmVisibility_text;
	public static String mntmHelp_text;
	public static String mntmOpen_text;
	public static String mntmOpenInNewWindow_text;
	public static String mntmOpenDuplicateDetails_text;
	public static String mntmClose_text;
	public static String mntmQuit_text;
	public static String mntmRun_text;
	public static String mntmCopyAsString_text;
	public static String mntmFoldersVisible_text;
	public static String mntmFilesVisible_text;
	public static String mntmCompressedFoldersVisible_text;
	public static String mntmCompressedFilesVisible_text;
	public static String mntmOpenSourceLicenses_text;
	public static String tblclmnPath_text;
	public static String tblclmnDateLastModified_text;
	public static String tblclmnSize_text;
	public static String tblclmnCompressedesize_text;
	public static String tblclmnDuplicate_text;
	public static String tblclmnDedupablesize_text;
	public static String SwtRootMenu_mntmNewRoot_text;
	public static String SwtConfigure_shlConfiguration_text;
	public static String SwtConfigure_lblDatabaseFilePath_text;
	public static String tblclmnMaximumsize_text;
	public static String tblclmnPercentusage_text;
	public static String SwtRootMenu_shlRootFolders_text;
	public static String SwtTaskTrayIcon_mntmSearch_text;
	public static String SwtTaskTrayIcon_mntmRun_text;
	public static String SwtTaskTrayIcon_mntmConfigure_text;
	public static String SwtRootMenu_mntmDelete_text;
	public static String btnRefresh_toolTipText;
	public static String SwtConfigure_lblNewLabel_text;
	public static String SwtConfigure_tabItemExtensions_text;
	public static String SwtConfigure_tblclmnExtension_text;
	public static String SwtConfigure_tblclmnAvailability_text;
	public static String SwtConfigure_lblZipCharset_text;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Class initialization
	//
	////////////////////////////////////////////////////////////////////////////
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
