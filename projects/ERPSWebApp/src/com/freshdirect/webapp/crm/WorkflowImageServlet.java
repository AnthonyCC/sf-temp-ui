package com.freshdirect.webapp.crm;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;

public class WorkflowImageServlet extends HttpServlet {

	private String operationsToDot(List operations) {
		StringBuffer sb = new StringBuffer();

		sb.append("digraph g {");
		sb.append("rankdir=LR;");
		sb.append("node[shape=box,fontname=Verdana];");
		sb.append("edge[fontsize=8,fontname=Verdana];");

		for (Iterator i = operations.iterator(); i.hasNext();) {
			CrmCaseOperation operation = (CrmCaseOperation) i.next();
			sb.append(operation.getStartState().getName());
			sb.append(" -> ");
			sb.append(operation.getEndState().getName());
			sb.append(" [label=\"");
			sb.append(operation.getActionType().getCode());
			sb.append("\"];");
		}
		sb.append("}");

		return sb.toString();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		CrmAgentRole role = CrmAgentRole.getEnum(request.getParameter("role"));
		CrmCaseSubject subject = CrmCaseSubject.getEnum(request.getParameter("subject"));

		List ops;
		try {
			ops = CrmManager.getInstance().getOperations(role, subject);
		} catch (FDResourceException e) {
			throw new ServletException(e);
		}
		String dot = this.operationsToDot(ops);

		response.setContentType("image/png");
		Graphviz.writePNG(dot, response.getOutputStream());

	}

}
