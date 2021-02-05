package com.sample.action;

import java.util.List;
import java.util.ListIterator;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.TaskMgmtDefinition;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.openkm.api.OKMDocument;
import com.openkm.api.OKMWorkflow;
import com.openkm.bean.form.Select;
import com.openkm.bean.workflow.Comment;
import com.openkm.dao.AuthDAO;
import com.openkm.module.db.stuff.DbSessionManager;
import com.openkm.util.MailUtils;

public class LockDocumentHandler implements ActionHandler{
	
	/**
	 * 
	 */
	
	Assignable test;
	private static final long serialVersionUID = 1L;

	public void execute(ExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		/*if (Main.get().mainPanel.desktop.browser.fileBrowser.isDocumentSelected()) {
			Main.get().mainPanel.desktop.browser.fileBrowser.getDocument().setLocked(true);	
		}*/
		String uuid = (String)context.getContextInstance().getVariable("uuid");
		if (uuid !=""){
			String docPath = OKMDocument.getInstance().getPath(null, uuid);
			if(OKMDocument.getInstance().isRefused(null, uuid)){
				OKMDocument.getInstance().unrefused(null, docPath);
				OKMDocument.getInstance().lock(null, docPath);
				//OKMDocument.getInstance().refused(null, docPath);
				//OKMAuth.getInstance().login();
			}
			else{
				OKMDocument.getInstance().lock(null, docPath);
				//OKMDocument.getInstance().refused(null, docPath);
				//OKMAuth.getInstance().login();
				
			}
			
			String docName = docPath.substring(docPath.lastIndexOf('/') + 1);
			
			StringBuilder subj = new StringBuilder();
			subj.append("Envoi du rapport ").append(docName).append(" pour validation");
			String subject = subj.toString();
			
			Select Arsm = (Select) context.getContextInstance().getVariable("user");
			
			ProcessDefinition procDef = context.getProcessDefinition();
			TaskMgmtDefinition taskMgmtDef = procDef.getTaskMgmtDefinition();
			TaskMgmtInstance taskMgmtIns = context.getTaskMgmtInstance();
			Swimlane swim = taskMgmtDef.getSwimlane("initiator");
			SwimlaneInstance swimlaneIns = taskMgmtIns.getInitializedSwimlaneInstance(context, swim);
			String Docauthor =swimlaneIns.getActorId();
			
			//Get the comment
			Long processInstanceId = context.getProcessInstance().getId();
			String token = DbSessionManager.getInstance().getSystemToken();
			List<Comment> comment = OKMWorkflow.getInstance().getProcessInstance(token,processInstanceId).getRootToken().getComments();
			ListIterator<Comment> test =  comment.listIterator();
			
			StringBuilder com = new StringBuilder();
			
			/*while(test.hasNext()){
					if(test.next().getActorId().contains("Arsm")){
						com.append(test.);
						com.append("<br>");
					}

			}*/
			
			for(Comment user : comment){
				if(user.getActorId().contains("SFD") || user.getActorId().contains("sfd") || user.getActorId().contains("Sfd")){
					com.append(user.getMessage());
				}	
			}
			
			
			String commentaires = com.toString();
			//

			String author = Arsm.getValue();
			StringBuilder sb = new StringBuilder();
			sb.append("<b>User:</b>").append(Docauthor).append("<br>").append("<b>Document:</b>").append(docName).append("<br>").append("<b>Commentaires:</b>").append(commentaires);
			String corpse = sb.toString();
			//String recipients = AuthDAO.getMailWithName(author);
			String recipients = AuthDAO.getMailOfUser(author);
			//CommonNotificationModule.getInstance().sendMailValidateDocument(author,docName,corpse);
			try{	
				MailUtils.sendMessage(recipients, subject , corpse);
			}catch(Exception e){
				 throw new Exception(e.getMessage(), e);
			}
			context.getToken().signal();
		}	
	}



}
