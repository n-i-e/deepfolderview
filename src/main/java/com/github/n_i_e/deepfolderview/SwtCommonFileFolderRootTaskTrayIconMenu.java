package com.github.n_i_e.deepfolderview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class SwtCommonFileFolderRootTaskTrayIconMenu {

	protected abstract Shell getShell();

	protected void writeMessageBox(final String text) {
		MessageBox msgbox = new MessageBox(getShell(), SWT.OK);
		msgbox.setMessage(text);
		msgbox.open();
	}

	protected String replaceToWindowsSafeFileName(String pt) {
		int i1 = pt.lastIndexOf("\\")+1;
		int i2 = pt.lastIndexOf("/")+1;
		if (i2>i1) {
			i1 = i2;
		}
		pt = pt.substring(i1);

		pt = pt.replace("\\", "");
		pt = pt.replace("/", "");
		pt = pt.replace(":", "");
		pt = pt.replace("*", "");
		pt = pt.replace("?", "");
		pt = pt.replace("\"", "");
		pt = pt.replace("<", "");
		pt = pt.replace(">", "");
		pt = pt.replace("|", "");
		return pt;
	}

}
