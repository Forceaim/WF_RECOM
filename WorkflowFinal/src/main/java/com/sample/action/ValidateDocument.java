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
import com.openkm.module.common.CommonNotificationModule;
import com.openkm.module.db.stuff.DbSessionManager;
import com.openkm.util.MailUtils;

public class ValidateDocument implements ActionHandler {
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
				//OKMDocument.getInstance().forceCancelCheckout(null, docPath);
				OKMDocument.getInstance().forceUnlock(null, docPath);
				OKMDocument.getInstance().checkout(null, docPath);
			//	context.getToken().signal();
				

				//Get the author of document
				ProcessDefinition procDef = context.getProcessDefinition();
				TaskMgmtDefinition taskMgmtDef = procDef.getTaskMgmtDefinition();
				TaskMgmtInstance taskMgmtIns = context.getTaskMgmtInstance();
				Swimlane swim = taskMgmtDef.getSwimlane("initiator");
				SwimlaneInstance swimlaneIns = taskMgmtIns.getInitializedSwimlaneInstance(context, swim);
				String author =swimlaneIns.getActorId();
				//
				
				//Get the comment
				Long processInstanceId = context.getProcessInstance().getId();
				String token = DbSessionManager.getInstance().getSystemToken();
				List<Comment> comment = OKMWorkflow.getInstance().getProcessInstance(token,processInstanceId).getRootToken().getComments();
				ListIterator<Comment> test =  comment.listIterator();
				
				StringBuilder com = new StringBuilder();
				
				/*while(test.hasNext()){
					if(test.next().getActorId().contains("Arsm") || (test.next().getActorId().contains("arsm"))){
						com.append(test.next().getMessage());
						com.append("<br>");
					}else{
						
					}
					
				}*/
				
				for(Comment user : comment){
					if(user.getActorId().contains("Arsm") || user.getActorId().contains("arsm") || user.getActorId().contains("ARSM")){
						com.append(user.getMessage());
					}	
				}
				
				String commentaires = com.toString();
				//

				String docName = docPath.substring(docPath.lastIndexOf('/') + 1);
				
				//CommonNotificationModule.getInstance().sendMailRefusedDocument(author,docName,Corpse);
				
				
				//OKMAuth.getInstance().login();
				context.getToken().signal();
				StringBuilder subj = new StringBuilder();
				subj.append("Validation du rapport ").append(docName);
				String subject = subj.toString();
				

				//char typereport = DelayDAO.findTypeReport(docName,sfdname, period);
				

				StringBuilder sb = new StringBuilder();
				sb.append("<h2><Rapport en retard :</h2><br><div>Votre rapport ").append(docName).append(" à été validé.").append("<br>Voici les commentaires:").append(commentaires);
				String corpse = sb.toString();
				//String recipients = AuthDAO.getMailWithName(author);
				String recipients = AuthDAO.getMailOfUser(author);
				//CommonNotificationModule.getInstance().sendMailValidateDocument(author,docName,corpse);
				try{	
					MailUtils.sendMessage(recipients, subject , corpse);
				}catch(Exception e){
					 throw new Exception(e.getMessage(), e);
				}
		}	
	}
}
