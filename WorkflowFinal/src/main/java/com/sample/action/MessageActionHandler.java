package com.sample.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.openkm.api.OKMDocument;
import com.openkm.automation.AutomationUtils;
import com.openkm.bean.Document;

public class MessageActionHandler implements ActionHandler {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The message member gets its value from the configuration in the 
	 * processdefinition. The value is injected directly by the engine. 
	 */
	String message;
	public static String author ="";

	/**
	 * A message process variable is assigned the value of the message
	 * member. The process variable is created if it doesn't exist yet.
	 */
	public void execute(ExecutionContext context) throws Exception {
		//context.getContextInstance().setVariable("message", message);
		//context.getToken().signal();
		
		String uuid = (String)context.getContextInstance().getVariable("uuid");
		Document doc = OKMDocument.getInstance().getProperties(null,uuid);
		//String author = doc.getActualVersion().getAuthor();
		author = doc.getActualVersion().getAuthor();
	}

}
