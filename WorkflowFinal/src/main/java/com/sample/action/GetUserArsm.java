package com.sample.action;

import java.util.ArrayList;
import java.util.List;

import com.openkm.api.OKMAuth;
import com.openkm.bean.form.Option;
import com.openkm.principal.PrincipalAdapterException;
import com.openkm.select.values.OptionSelectValues;

public class GetUserArsm implements OptionSelectValues {


	@Override
	public List<Option> getOptions() {
		// TODO Auto-generated method stub
		
		List<Option> options = new ArrayList<>();
		try {
			List<String> UserList = OKMAuth.getInstance().getUsersByRole(null, "ROLE_ADMIN");
			for (String user : UserList){
				if(OKMAuth.getInstance().getName(null, user).contains("Admin")){
					
				}else{
				Option option = new Option();
				option.setLabel(OKMAuth.getInstance().getName(null, user));
				option.setValue(user);
				options.add(option);
				}
			}
			
		} catch (PrincipalAdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return options;
	}

}
