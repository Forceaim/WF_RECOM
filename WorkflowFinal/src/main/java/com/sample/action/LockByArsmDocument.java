/**
 * 
 */
package com.sample.action;

import java.util.List;
import java.util.ListIterator;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.openkm.api.OKMAuth;
import com.openkm.api.OKMDocument;
import com.openkm.api.OKMWorkflow;
import com.openkm.bean.workflow.Comment;
import com.openkm.dao.AuthDAO;
import com.openkm.module.db.stuff.DbSessionManager;
import com.openkm.util.MailUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author openkm
 *
 */
public class LockByArsmDocument implements ActionHandler {
	private static Logger log = LoggerFactory.getLogger(AuthDAO.class);
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
			log.info("attooo zao");
			OKMDocument.getInstance().refused(null, docPath);
			log.info("attooo zao");
			
			//nouvelle modification
			Long processInstanceId = context.getProcessInstance().getId();
			log.info(processInstanceId.toString());
			String token = DbSessionManager.getInstance().getSystemToken();
			List<Comment> comment = OKMWorkflow.getInstance().getProcessInstance(token,processInstanceId).getRootToken().getComments();
			ListIterator<Comment> test =  comment.listIterator();
			log.info("attoo zao");
			
			StringBuilder com = new StringBuilder();
			
			while(test.hasNext()){
				com.append(test.next().getMessage());
				com.append("<br>");
			}
			
			String commentaires = com.toString();
			
			//comments = context.getTaskInstance().getComments();
			
			String docName = docPath.substring(docPath.lastIndexOf('/') + 1);
			
			
			//OKMAuth.getInstance().login();
			context.getToken().signal();
			StringBuilder subj = new StringBuilder();
			subj.append("Refus du rapport ").append(docName);
			String subject = subj.toString();
			
			log.info("mba tafiditra ato ve ela");

			//char typereport = DelayDAO.findTypeReport(docName,sfdname, period);

			StringBuilder sb = new StringBuilder();
			sb.append("<h2><Rapport en retard :</h2><br><div>Votre rapport ").append(docName).append("à été refusé.").append("<br>Voici les commentaires:").append(commentaires);
			String corpse = sb.toString();
			String recipients = AuthDAO.getMailOfUser("okmSfd01");
			log.info("recipient:");
			log.info(recipients);
			try{
				MailUtils.sendMessage(recipients, subject , corpse);
			}catch(Exception e){
				 throw new Exception(e.getMessage(), e);
			}	
		}
	}
}
