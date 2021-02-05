/**
 * 
 */
package com.sample.action;

import java.util.List;
import java.util.ListIterator;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.TaskMgmtDefinition;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.openkm.api.OKMDocument;
import com.openkm.api.OKMWorkflow;
import com.openkm.bean.Document;
import com.openkm.bean.workflow.Comment;
import com.openkm.dao.AuthDAO;
import com.openkm.module.db.stuff.DbSessionManager;
import com.openkm.util.MailUtils;
import com.openkm.module.common.CommonNotificationModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * @author openkm
 *
 */
public class RefusedDocumentHandler implements ActionHandler {
	private static Logger log = LoggerFactory.getLogger(AuthDAO.class);
	private static final long serialVersionUID = 1L;
	String uuid;
	String docPath;
	SwimlaneInstance swim;


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
			
			Document doc = OKMDocument.getInstance().getProperties(null,uuid);
			
			//String author = doc.getActualVersion().getAuthor();
			
			
			//Get the author of document
			ProcessDefinition procDef = context.getProcessDefinition();
			TaskMgmtDefinition taskMgmtDef = procDef.getTaskMgmtDefinition();
			TaskMgmtInstance taskMgmtIns = context.getTaskMgmtInstance();
			Swimlane swim = taskMgmtDef.getSwimlane("initiator");
			SwimlaneInstance swimlaneIns = taskMgmtIns.getInitializedSwimlaneInstance(context, swim);
			String author =swimlaneIns.getActorId();
			log.info("author");
			log.info(author);
			//
			
			//Get the comment
			Long processInstanceId = context.getProcessInstance().getId();
			log.info(processInstanceId.toString());
			String token = DbSessionManager.getInstance().getSystemToken();
			List<Comment> comment = OKMWorkflow.getInstance().getProcessInstance(token,processInstanceId).getRootToken().getComments();
			ListIterator<Comment> test =  comment.listIterator();
			log.info("attoooo zao");
			
			StringBuilder com = new StringBuilder();
			
			while(test.hasNext()){
					com.append(test.next().getMessage());
					com.append("<br>");
			}
			
			String commentaires = com.toString();
			//
			
			log.info("tonga ato ve zao");
			
						
			String docName = docPath.substring(docPath.lastIndexOf('/') + 1);
			
			//CommonNotificationModule.getInstance().sendMailRefusedDocument(author,docName,Corpse);
			
			
			//OKMAuth.getInstance().login();
			context.getToken().signal();
			StringBuilder subj = new StringBuilder();
			subj.append("Refus du rapport ").append(docName);
			String subject = subj.toString();
			
			log.info("mba tafiditra ato ve ela");

			//char typereport = DelayDAO.findTypeReport(docName,sfdname, period);
			

			StringBuilder sb = new StringBuilder();
			sb.append("<h2><Rapport en retard :</h2><br><div>Votre rapport ").append(docName).append(" à été refusé.").append("<br>Voici les commentaires:").append(commentaires);
			String corpse = sb.toString();
			log.info(corpse);
			//String recipients = AuthDAO.getMailWithName(author);
			log.info("recipient ve:");
			String recipients = AuthDAO.getMailOfUser(author);
			log.info(author);
			CommonNotificationModule.getInstance().sendMailRefusedDocument(author,docName,corpse);
			try{	
				MailUtils.sendMessage(recipients, subject , corpse);
				log.info("tokony lasa any aminy antsa");
			}catch(Exception e){
				 throw new Exception(e.getMessage(), e);
			}	
			
		}	
	}
}
