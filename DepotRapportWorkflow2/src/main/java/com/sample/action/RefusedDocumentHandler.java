/**
 * 
 */
package com.sample.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

import com.openkm.api.OKMDocument;
import com.openkm.dao.NodeBaseDAO;
import com.openkm.extractor.TextExtractorWork;
import com.openkm.frontend.client.Main;



/**
 * @author openkm
 *
 */
public class RefusedDocumentHandler implements ActionHandler {
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(ExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		if (Main.get().mainPanel.desktop.browser.fileBrowser.isDocumentSelected()) {
			Main.get().mainPanel.desktop.browser.fileBrowser.getDocument().setLocked(true);	
		}	
	}
}
