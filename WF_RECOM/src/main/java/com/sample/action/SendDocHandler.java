package com.sample.action;


import com.openkm.dao.AuthDAO;
import com.openkm.util.MailUtils;

import java.util.List;
import java.util.ListIterator;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.Swimlane;

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


public class SendDocHandler implements ActionHandler {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(AuthDAO.class);

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
		String uuid = (String)context.getContextInstance().getVariable("uuid");
		String docPath = OKMDocument.getInstance().getPath(null, uuid); 
		String docName = docPath.substring(docPath.lastIndexOf('/') + 1);

		Document doc = OKMDocument.getInstance().getProperties(null,uuid);
		String author = doc.getActualVersion().getAuthor();
		
		context.getToken().signal();
		StringBuilder subj = new StringBuilder();
		subj.append("Recommandation ").append(docName);
		String subject = subj.toString();
		
		log.info("mba tafiditra ato ve ela");

		//char typereport = DelayDAO.findTypeReport(docName,sfdname, period);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<h2><Recommandation :</h2><br><div>Des recommandations sur le document ").append(docName).append("ont été envoyé.");
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