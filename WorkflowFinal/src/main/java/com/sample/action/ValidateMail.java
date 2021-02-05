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
import com.openkm.bean.workflow.Comment;
import com.openkm.dao.AuthDAO;
import com.openkm.module.common.CommonNotificationModule;
import com.openkm.module.db.stuff.DbSessionManager;
import com.openkm.util.MailUtils;

public class ValidateMail implements ActionHandler {

	private static final long serialVersionUID = 1L;

	/**
	* The message member gets its value from the configuration in the
	* processdefinition. The value is injected directly by the engine.
	*/
	String message;

	/**
	* A message process variable is assigned the value of the message
	* member. The process variable is created if it doesn't exist yet.
	*/
	public void execute(ExecutionContext context) throws Exception {
		//ADD CUSTOM ACTION CODE HERE
		//context.getContextInstance().setVariable("message", message);
		
		
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
		
		while(test.hasNext()){
				com.append(test.next().getMessage());
				com.append("<br>");
		}
		
		String commentaires = com.toString();
		//
		String uuid = (String)context.getContextInstance().getVariable("uuid");
		String docPath = OKMDocument.getInstance().getPath(null, uuid);
		String docName = docPath.substring(docPath.lastIndexOf('/') + 1);
		
		//CommonNotificationModule.getInstance().sendMailRefusedDocument(author,docName,Corpse);
		
		
		//OKMAuth.getInstance().login();
		context.getToken().signal();
		StringBuilder subj = new StringBuilder();
		subj.append("Refus du rapport ").append(docName);
		String subject = subj.toString();
		

		//char typereport = DelayDAO.findTypeReport(docName,sfdname, period);
		

		StringBuilder sb = new StringBuilder();
		sb.append("<h2><Rapport en retard :</h2><br><div>Votre rapport ").append(docName).append(" à été validé.").append("<br>Voici les commentaires:").append(commentaires);
		String corpse = sb.toString();
		//String recipients = AuthDAO.getMailWithName(author);
		String recipients = AuthDAO.getMailOfUser(author);
		CommonNotificationModule.getInstance().sendMailRefusedDocument(author,docName,corpse);
		try{	
			MailUtils.sendMessage(recipients, subject , corpse);
		}catch(Exception e){
			 throw new Exception(e.getMessage(), e);
		}
	}

}
