<%@page import="com.freshdirect.transadmin.model.Dispatch"%>
<% 
          	  	int ucount = 0;
          	  	List dispatchList = (List)request.getAttribute("dispatchList");
          	  	if(dispatchList != null) {
          	  	Dispatch _command = null;
          	  		ucount = dispatchList.size(); 
          	  		Iterator _itr = dispatchList.iterator();
          	  	out.println("Below are the list of changes maded after the truck refresh");
          	  out.println();
          	  		out.println("dispatch_id \t truck_number \t first delivery time");
          	  		while(_itr.hasNext()) {
          	  			_command = (Dispatch)_itr.next();
          	  			out.print(_command.getDispatchId()+"\t");
          	  			out.print(_command.getTruck()+"\t");
          	  			out.print(_command.getFirstDlvTime()+"\t\n");
          	  		}
          	  	}
          	  %>