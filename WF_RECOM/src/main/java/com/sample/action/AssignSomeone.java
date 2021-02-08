package com.sample.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.bean.form.Select;

public class AssignSomeone implements AssignmentHandler {

	private static Logger log = LoggerFactory.getLogger(AssignSomeone.class);
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
	//public void execute(ExecutionContext context) throws Exception {
		
	//}
	
	public void assign(Assignable assignable, ExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		Select Arsm = (Select) context.getContextInstance().getVariable("user");
		
		if (Arsm != null){
			assignable.setActorId(Arsm.getValue());
			
		} else {
			assignable.setActorId("okmAdmin");
		}
	}

}
