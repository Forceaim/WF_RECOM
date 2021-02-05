/**
 * 
 */
package com.sample.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.openkm.api.OKMAuth;
import com.openkm.api.OKMDocument;




/**
 * @author openkm
 *
 */
public class RefusedDocumentHandler implements ActionHandler {
	private static final long serialVersionUID = 1L;
	String uuid;
	String docPath;

	@Override
	public void execute(ExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		/*if (Main.get().mainPanel.desktop.browser.fileBrowser.isDocumentSelected()) {
			Main.get().mainPanel.desktop.browser.fileBrowser.getDocument().setLocked(true);	
		}*/
		String uuid = (String)context.getContextInstance().getVariable("uuid");
		if (uuid !=""){
			String docPath = OKMDocument.getInstance().getPath(null, uuid);
			OKMDocument.getInstance().forceUnlock(null, docPath);
			OKMDocument.getInstance().refused(null, docPath);
			//OKMAuth.getInstance().login();
			context.getToken().signal();
		}	
	}
}
