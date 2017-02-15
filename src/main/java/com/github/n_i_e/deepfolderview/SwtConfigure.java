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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SwtConfigure extends Dialog {

	protected Object result;
	protected Shell shlConfiguration;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtDatabaseFilePath;
	private Text textWindowsIdleTime;

	private String newDatabaseFilePath = null;
	private int newWindowsIdleTime = -1;
	private int newNumCrawlingThreads = -1;
	Map<String, Boolean> newArchiveListerExtensionAvailabilityList = PreferenceRW.getExtensionAvailabilityMap();

	private Text textNumCrawlingThreads;
	private Combo comboZipListerCharset;
	private final String[] comboZipListerCharsetItems = new String[] {"windows-31j"};

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SwtConfigure(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public SwtConfigure(Shell parent) {
		this(parent, SWT.TITLE|SWT.CLOSE);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlConfiguration.open();
		shlConfiguration.layout();
		Display display = getParent().getDisplay();
		while (!shlConfiguration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlConfiguration = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE);
		shlConfiguration.setSize(450, 300);
		shlConfiguration.setText(Messages.SwtConfigure_shlConfiguration_text);
		shlConfiguration.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(shlConfiguration, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);

		TabItem tbtmFile = new TabItem(tabFolder, SWT.NONE);
		tbtmFile.setText(Messages.mntmFile_text);

		Composite compositeFile = new Composite(tabFolder, SWT.NONE);
		tbtmFile.setControl(compositeFile);
		compositeFile.setLayout(new GridLayout(2, false));
		new Label(compositeFile, SWT.NONE);
		new Label(compositeFile, SWT.NONE);

		@SuppressWarnings("unused") Label lblDatabaseFilePath = formToolkit.createLabel(compositeFile, Messages.SwtConfigure_lblDatabaseFilePath_text, SWT.NONE);
		new Label(compositeFile, SWT.NONE);

		txtDatabaseFilePath = formToolkit.createText(compositeFile, PreferenceRW.getDBFilePath(), SWT.NONE);
		txtDatabaseFilePath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				newDatabaseFilePath = txtDatabaseFilePath.getText();
			}
		});
		txtDatabaseFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(compositeFile, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] ext = {"*.sqlite", "*.mv.db", "*.mdb", "*.script", "*.*"};
				String pt = txtDatabaseFilePath.getText();
				FileDialog d = new FileDialog(shlConfiguration, SWT.SAVE);
				d.setText("New File Path");
				d.setFileName(pt);
				d.setFilterExtensions(ext);
				d.setOverwrite(true);
				String newPath = d.open();
				if (newPath != null) {
					txtDatabaseFilePath.setText(newPath);
				}
			}
		});
		btnNewButton.setText("Browse");

		TabItem tbtmParameters = new TabItem(tabFolder, SWT.NONE);
		tbtmParameters.setText(Messages.SwtConfigure_tbtmParameters_text);

		Composite compositeParameters = new Composite(tabFolder, SWT.NONE);
		tbtmParameters.setControl(compositeParameters);
		formToolkit.paintBordersFor(compositeParameters);
		GridLayout gl_compositeParameters = new GridLayout(3, false);
		gl_compositeParameters.marginHeight = 20;
		gl_compositeParameters.verticalSpacing = 20;
		compositeParameters.setLayout(gl_compositeParameters);

		Label lblWindowsIdleTime = new Label(compositeParameters, SWT.NONE);
		lblWindowsIdleTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWindowsIdleTime.setSize(116, 15);
		formToolkit.adapt(lblWindowsIdleTime, true, true);
		lblWindowsIdleTime.setText(Messages.SwtConfigure_lblWindowsIdleTime_text);

		textWindowsIdleTime = new Text(compositeParameters, SWT.BORDER);
		textWindowsIdleTime.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				Integer t;
				try {
					t = Integer.parseInt(textWindowsIdleTime.getText());
				} catch (NumberFormatException e) {
					t = null;
				}
				if (t != null) {
					newWindowsIdleTime = t;
				}
			}
		});
		textWindowsIdleTime.setText(String.valueOf(PreferenceRW.getWindowsIdleSeconds()));
		textWindowsIdleTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(textWindowsIdleTime, true, true);

		Label lblSeconds = new Label(compositeParameters, SWT.NONE);
		formToolkit.adapt(lblSeconds, true, true);
		lblSeconds.setText(Messages.SwtConfigure_lblSeconds_text);

		Label lblNumCrawlingThreads = new Label(compositeParameters, SWT.NONE);
		lblNumCrawlingThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblNumCrawlingThreads, true, true);
		lblNumCrawlingThreads.setText(Messages.SwtConfigure_lblNewLabel_text);

		textNumCrawlingThreads = new Text(compositeParameters, SWT.BORDER);
		textNumCrawlingThreads.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				Integer t;
				try {
					t = Integer.parseInt(textNumCrawlingThreads.getText());
				} catch (NumberFormatException e) {
					t = null;
				}
				if (t != null) {
					newNumCrawlingThreads = t;
				}
			}
		});
		textNumCrawlingThreads.setText(String.valueOf(PreferenceRW.getNumCrawlingThreads()));
		textNumCrawlingThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(textNumCrawlingThreads, true, true);
		new Label(compositeParameters, SWT.NONE);

		Label lblZipCharset = new Label(compositeParameters, SWT.NONE);
		lblZipCharset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblZipCharset, true, true);
		lblZipCharset.setText(Messages.SwtConfigure_lblZipCharset_text);

		comboZipListerCharset = new Combo(compositeParameters, SWT.NONE);
		comboZipListerCharset.setItems(comboZipListerCharsetItems);
		for (int i=0; i<comboZipListerCharsetItems.length; i++) {
			if (comboZipListerCharsetItems[i].equals(PreferenceRW.getZipListerCharset())) {
				comboZipListerCharset.select(i);
			}
		}
		comboZipListerCharset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(comboZipListerCharset);
		formToolkit.paintBordersFor(comboZipListerCharset);
		new Label(compositeParameters, SWT.NONE);

		TabItem tabItemExtensions = new TabItem(tabFolder, SWT.NONE);
		tabItemExtensions.setText(Messages.SwtConfigure_tabItemExtensions_text);

		Composite compositeExtensions = new Composite(tabFolder, SWT.NONE);
		tabItemExtensions.setControl(compositeExtensions);
		formToolkit.paintBordersFor(compositeExtensions);
		FillLayout fl_compositeExtensions = new FillLayout(SWT.VERTICAL);
		fl_compositeExtensions.marginHeight = 20;
		fl_compositeExtensions.marginWidth = 5;
		fl_compositeExtensions.spacing = 20;
		compositeExtensions.setLayout(fl_compositeExtensions);

		Composite compositeAvailability = new Composite(compositeExtensions, SWT.NONE);
		formToolkit.adapt(compositeAvailability);
		formToolkit.paintBordersFor(compositeAvailability);
		compositeAvailability.setLayout(new GridLayout(6, false));

		ArrayList<String> kl = new ArrayList<String>(newArchiveListerExtensionAvailabilityList.keySet());
		Collections.sort(kl);
		for (final String key: kl) {
			final boolean value = newArchiveListerExtensionAvailabilityList.get(key);
			final Button btnExt = new Button(compositeAvailability, SWT.CHECK);
			btnExt.setSelection(value);
			btnExt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean newValue = btnExt.getSelection();
					newArchiveListerExtensionAvailabilityList.put(key, newValue);
				}
			});
			btnExt.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			formToolkit.adapt(btnExt, true, true);
			btnExt.setText(key);
		}

		Composite compositeOkCancel = new Composite(shlConfiguration, SWT.NONE);
		compositeOkCancel.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeOkCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

		Button btnOk = new Button(compositeOkCancel, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOkSelected();
				shlConfiguration.dispose();
			}
		});
		btnOk.setText("OK");

		Button btnCancel = new Button(compositeOkCancel, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlConfiguration.dispose();
			}
		});
		btnCancel.setText("Cancel");

	}

	protected void onOkSelected() {
		if (newDatabaseFilePath != null) {
			PreferenceRW.setDBFilePath(newDatabaseFilePath);
		}
		if (newWindowsIdleTime >= 0) {
			PreferenceRW.setWindowsIdleSeconds(newWindowsIdleTime);
		}
		if (newNumCrawlingThreads >= 0) {
			PreferenceRW.setNumCrawlingThreads(newNumCrawlingThreads);
		}
		if (newArchiveListerExtensionAvailabilityList != null) {
			PreferenceRW.setExtensionAvailabilityMap(newArchiveListerExtensionAvailabilityList);
		}
		if (comboZipListerCharsetItems[comboZipListerCharset.getSelectionIndex()] != null) {
			PreferenceRW.setZipListerCharset(comboZipListerCharsetItems[comboZipListerCharset.getSelectionIndex()]);
		}
	}
}
