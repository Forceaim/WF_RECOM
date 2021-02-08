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

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

public class NotifierHandler implements ActionHandler {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(AuthDAO.class);

	String uuid;
	String docPath;
	SwimlaneInstance swim;
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
		log.info("author");
		log.info(author);
		
		String docName = docPath.substring(docPath.lastIndexOf('/') + 1);
		
		
		
		//OKMAuth.getInstance().login();
		context.getToken().signal();
		StringBuilder subj = new StringBuilder();
		subj.append("Accusé de réception").append(docName);
		String subject = subj.toString();
		
		log.info("mba tafiditra ato ve ela");

		//char typereport = DelayDAO.findTypeReport(docName,sfdname, period);
		

		StringBuilder sb = new StringBuilder();
		sb.append("<h2><Rapport en retard :</h2><br><div>Accusé de réception du document").append(docName).append(" à été envoyé.");
		String corpse = sb.toString();
		log.info(corpse);
		//String recipients = AuthDAO.getMailWithName(author);
		log.info("recipient ve:");
		String recipients = AuthDAO.getMailOfUser(author);
		log.info(author);
		try{
			
			MailUtils.sendMessage(recipients, subject , corpse);
			log.info("tokony lasa any aminy antsa");
		}catch(Exception e){
			 throw new Exception(e.getMessage(), e);
		}	
	}

}
