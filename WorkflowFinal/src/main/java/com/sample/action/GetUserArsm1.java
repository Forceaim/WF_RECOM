package com.sample.action;

import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.openkm.api.OKMAuth;
import com.openkm.dao.AuthDAO;
import com.openkm.principal.PrincipalAdapterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserArsm1 implements ActionHandler {
	private static Logger log = LoggerFactory.getLogger(AuthDAO.class);
	private static final long serialVersionUID = 1L;

	/**
	* The message member gets its value from the configuration in the
	* processdefinition. The value is injected directly by the engine.
	*/
	
	String ListArsm;
	/**
	* A message process variable is assigned the value of the message
	* member. The process variable is created if it doesn't exist yet.
	*/
	public void execute(ExecutionContext context) throws Exception {
		//ADD CUSTOM ACTION CODE HERE
		//context.getContextInstance().setVariable("message", message);
		
		StringBuilder st = new StringBuilder();
		try {
			List<String> UserList = OKMAuth.getInstance().getUsersByRole(null, "ROLE_ADMIN");
			for (String user : UserList){
				if(OKMAuth.getInstance().getName(null, user).contains("Admin")){
					
				}else{
					st.append(OKMAuth.getInstance().getName(null, user));
					st.append(",");
				}
			}
		ListArsm = st.toString();
		log.info(ListArsm);
			
		} catch (PrincipalAdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
