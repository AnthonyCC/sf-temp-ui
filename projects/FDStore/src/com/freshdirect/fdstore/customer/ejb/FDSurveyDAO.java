/*
 * FDSurveySessionBean.java
 *
 * Created on March 11, 2002, 7:39 PM
 */

package com.freshdirect.fdstore.customer.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.fdstore.survey.FDSurveyResponse;

public class FDSurveyDAO {

	public static void storeSurvey(Connection conn, String id, FDSurveyResponse survey) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SURVEY(ID,CUSTOMER_ID,SURVEY_NAME,SALE_ID,CREATE_DATE) VALUES(?, ?, ?, ?, SYSDATE)");
		ps.setString(1, id);
		
		if (survey.getIdentity() == null) {
			ps.setNull(2, Types.VARCHAR);
		} else {
			ps.setString(2, survey.getIdentity().getFDCustomerPK());
		}
		
		ps.setString(3, survey.getName());
		
		if (survey.getSalePk() == null) {
			ps.setNull(4, Types.VARCHAR);
		} else {
			ps.setString(4, survey.getSalePk().getId());
		}
		
		ps.executeUpdate();
		ps.close();

		ps = conn.prepareStatement("INSERT INTO CUST.SURVEYDATA (SURVEY_ID, QUESTION, ANSWER) VALUES (?, ?, ?)");
		for (Iterator i = survey.getAnswers().entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String question = (String) entry.getKey();
			String[] values = (String[]) entry.getValue();

			for (int j = 0; j < values.length; j++) {
				String answer = values[j];
				ps.setString(1, id);
				ps.setString(2, question);
				ps.setString(3, answer);
				ps.addBatch();
			}
		}
		ps.executeBatch();
		ps.close();
	}
}