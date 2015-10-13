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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class SwtOpenSourceLicenses extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SwtOpenSourceLicenses(Shell parent, int style) {
		super(parent, style);
		setText("Open Source Licenses");
	}

	public SwtOpenSourceLicenses(Shell parent) {
		this(parent, SWT.TITLE|SWT.MIN|SWT.MAX|SWT.CLOSE);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmDirTreeDb = new TabItem(tabFolder, SWT.NONE);
		tbtmDirTreeDb.setText("DirTreeDB (This Program)");
		
		StyledText styledTextDirTreeDb = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextDirTreeDb.setText("Copyright 2015 Namihiko Matsumura (https://github.com/n-i-e/)\r\n\r\nLicensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at\r\n\r\n    http://www.apache.org/licenses/LICENSE-2.0\r\n\r\nUnless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.");
		tbtmDirTreeDb.setControl(styledTextDirTreeDb);

		TabItem tbtmApacheCommons = new TabItem(tabFolder, SWT.NONE);
		tbtmApacheCommons.setText("Apache Commons (Compress / Lang / Logging)");
		
		StyledText styledTextApacheCommons = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextApacheCommons.setText("Copyright 2015 The Apache Software Foundation.\r\n\r\nLicensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at\r\n\r\n    http://www.apache.org/licenses/LICENSE-2.0\r\n\r\nUnless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.");
		tbtmApacheCommons.setControl(styledTextApacheCommons);
		
		TabItem tbtmSwt = new TabItem(tabFolder, SWT.NONE);
		tbtmSwt.setText("SWT");
		
		StyledText styledTextSwt = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		tbtmSwt.setControl(styledTextSwt);
		
		TabItem tbtmGnomeIconTheme = new TabItem(tabFolder, SWT.NONE);
		tbtmGnomeIconTheme.setText("GNOME icon theme");
		
		StyledText styledTextGnomeIconTheme = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextGnomeIconTheme.setText("GNOME icon theme is distributed under the terms of either GNU LGPL v.3 or Creative Commons BY-SA 3.0 license.");
		tbtmGnomeIconTheme.setControl(styledTextGnomeIconTheme);

		TabItem tbtmUCanAccess = new TabItem(tabFolder, SWT.NONE);
		tbtmUCanAccess.setText("UCanAccess");
		
		StyledText styledTextUCanAccess = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextUCanAccess.setText("Copyright 2015 Marco Amadei\r\n\r\nLicensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at\r\n\r\n    http://www.apache.org/licenses/LICENSE-2.0\r\n\r\nUnless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.");
		tbtmUCanAccess.setControl(styledTextUCanAccess);

		TabItem tbtmJackcess = new TabItem(tabFolder, SWT.NONE);
		tbtmJackcess.setText("Jackcess");
		
		StyledText styledTextJackcess = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextJackcess.setText("Copyright 2015  Health Market Science, Inc.\r\n\r\nLicensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at\r\n\r\n    http://www.apache.org/licenses/LICENSE-2.0\r\n\r\nUnless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.");
		tbtmJackcess.setControl(styledTextJackcess);

		TabItem tbtmHSqlDb = new TabItem(tabFolder, SWT.NONE);
		tbtmHSqlDb.setText("HSQLDB");
		
		StyledText styledTextHSqlDb = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextHSqlDb.setText("Copyright (c) 1995-2000 by the Hypersonic SQL Group.\r\nAll rights reserved. \r\nRedistribution and use in source and binary forms, with or without\r\nmodification, are permitted provided that the following conditions are met:\r\n\r\nRedistributions of source code must retain the above copyright notice, this\r\nlist of conditions and the following disclaimer.\r\n\r\nRedistributions in binary form must reproduce the above copyright notice,\r\nthis list of conditions and the following disclaimer in the documentation\r\nand/or other materials provided with the distribution.\r\n\r\nNeither the name of the Hypersonic SQL Group nor the names of its\r\ncontributors may be used to endorse or promote products derived from this\r\nsoftware without specific prior written permission.\r\n\r\nTHIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\"\r\nAND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE\r\nIMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE\r\nARE DISCLAIMED. IN NO EVENT SHALL THE HYPERSONIC SQL GROUP, \r\nOR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, \r\nEXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, \r\nPROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;\r\nLOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND\r\nON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\r\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\r\nSOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\r\n\r\nThis software consists of voluntary contributions made by many individuals on behalf of the\r\nHypersonic SQL Group.");
		tbtmHSqlDb.setControl(styledTextHSqlDb);
		
		TabItem tbtmLhaLibraryForJava = new TabItem(tabFolder, SWT.NONE);
		tbtmLhaLibraryForJava.setText("LHA Library for Java");
		
		StyledText styledTextLhaLibraryForJava = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextLhaLibraryForJava.setText("Copyright (C) 2002  Michel Ishizuka  All rights reserved.\r\n\r\n以下の条件に同意するならばソースとバイナリ形式の再配布と使用を変更の有無にかかわらず許可する。\r\n\r\n１．ソースコードの再配布において著作権表示と この条件のリストおよび下記の声明文を保持しなくてはならない。\r\n\r\n２．バイナリ形式の再配布において著作権表示と この条件のリストおよび下記の声明文を使用説明書もしくは その他の配布物内に含む資料に記述しなければならない。\r\n\r\nこのソフトウェアは石塚美珠瑠によって無保証で提供され、特定の目的を達成できるという保証、商品価値が有るという保証にとどまらず、いかなる明示的および暗示的な保証もしない。\r\n石塚美珠瑠は このソフトウェアの使用による直接的、間接的、偶発的、特殊な、典型的な、あるいは必然的な損害(使用によるデータの損失、業務の中断や見込まれていた利益の遺失、代替製品もしくはサービスの導入費等が考えられるが、決してそれだけに限定されない損害)に対して、いかなる事態の原因となったとしても、契約上の責任や無過失責任を含む いかなる責任があろうとも、たとえそれが不\r\n正行為のためであったとしても、またはそのような損害の可能性が報告されていたとしても一切の責任を負わないものとする。");
		tbtmLhaLibraryForJava.setControl(styledTextLhaLibraryForJava);
		
		TabItem tbtmH2Database = new TabItem(tabFolder, SWT.NONE);
		tbtmH2Database.setText("H2 Database");
		
		StyledText styledTextH2Database = new StyledText(tabFolder, SWT.BORDER | SWT.WRAP);
		styledTextH2Database.setText("This software contains unmodified binary redistributions for H2 database engine (http://www.h2database.com/), which is dual licensed and available under the MPL 2.0 (Mozilla Public License) or under the EPL 1.0 (Eclipse Public License).  An original copy of the license agreement can be found at: http://www.h2database.com/html/license.html");
		tbtmH2Database.setControl(styledTextH2Database);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 1, 1));
		
		Button btnOk = new Button(composite, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnOk.setText("OK");

	}
}
