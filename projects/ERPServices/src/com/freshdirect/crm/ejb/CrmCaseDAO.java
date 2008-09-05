package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.ObjectNotFoundException;

import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmDepartment;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.framework.core.EntityDAOI;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.NVL;

public class CrmCaseDAO implements EntityDAOI {
	private static  final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	public PrimaryKey findByPrimaryKey(Connection conn, PrimaryKey pk) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CASE WHERE ID=?");
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new ObjectNotFoundException("Unable to find CrmCase with PK " + pk);
		}

		String id = rs.getString(1);

		rs.close();
		ps.close();

		return new PrimaryKey(id);
	}

	private final static String QUERY_CASEINFO =
		"SELECT /*+ USE_NL (ci) */ rownum y, c.id, c.customer_id, c.assigned_agent_id, c. sale_id,"
			+ " c.locked_agent_id, c.summary, c.case_origin, c.case_subject,"
			+ "c.MEDIA,c.MORETHENONE_ISSUE,c.FIRSTCONTACT,c.FisrtContact_Resolved,c.ReasonNotToResolve,c.SatisfiedWithResolution,c.CustomerTone,"
			+ " c.case_priority as case_priority, c.case_state, a.user_id as assigned, cs.name as subject_name,"
			+ " ci.first_name, ci.last_name, c.create_date, c.last_action_date, c.projected_quantity,"
			+ " c.actual_quantity "
			+ " FROM CUST.CASE c, CUST.AGENT a, CUST.CUSTOMERINFO ci, CUST.CASE_SUBJECT cs "
			+ " WHERE c.customer_id=ci.customer_id(+) AND c.assigned_agent_id = a.id AND c.case_subject = cs.code";
	
	private final static String QUERY_TOTALCASEINFO = 
		"SELECT /*+ USE_NL (ci) */ count(*) AS TOTAL"
		+ " FROM CUST.CASE c, CUST.CUSTOMERINFO ci"
		+ " WHERE c.customer_id=ci.customer_id(+)";

	/** @return List of CrmCaseModel */
	public List loadCaseInfoByTemplate(Connection conn, CrmCaseTemplate ct) throws SQLException {
		
		CriteriaBuilder builder = new CriteriaBuilder();
		builder.addPK("c.ID", ct.getPK());
		builder.addEnum("c.CASE_ORIGIN", ct.getOrigin());
		builder.addEnum("c.CASE_PRIORITY", ct.getPriority());
		builder.addEnum("c.CASE_SUBJECT", ct.getSubject());
		builder.addString("c.SUMMARY", ct.getSummary());
		builder.addPK("c.CUSTOMER_ID", ct.getCustomerPK());
		builder.addPK("c.SALE_ID", ct.getSalePK());
		builder.addPK("c.ASSIGNED_AGENT_ID", ct.getAssignedAgentPK());
		builder.addPK("c.LOCKED_AGENT_ID", ct.getLockedAgentPK());

		builder.addInEnum("CASE_STATE", (CrmCaseState[]) ct.getStates().toArray(new CrmCaseState[0]));
		
		if (ct.getQueue() != null) {
			builder.addSql(
				"CASE_SUBJECT IN (select code from cust.case_subject where case_queue=?)",
				new Object[] { ct.getQueue().getCode()});
		}
		Date startDate = ct.getStartDate();
		if(startDate !=null && !"".equals(startDate) ) {
			String strStartDate = dateFormat.format(startDate);
			builder.addSql("c.CREATE_DATE >= to_date(?,'MM/DD/YYYY HH:MI:SS AM')", new Object[] {strStartDate });
			//builder.addSql(
					//"c.CREATE_DATE >= ? ", new java.sql.Date (ct.getStartDate().getTime()));
		}
		Date endDate = ct.getEndDate();
		if(endDate != null && !"".equals(endDate)) {
			String strEndDate = dateFormat.format(endDate);
			builder.addSql("c.CREATE_DATE <= to_date(?,'MM/DD/YYYY HH:MI:SS AM')", new Object[] {strEndDate });
			//builder.addSql("c.CREATE_DATE <= ?", new java.sql.Date (ct.getEndDate().getTime()));
		}
		PreparedStatement totalPS = conn.prepareStatement(QUERY_TOTALCASEINFO + " AND "+ builder.getCriteria());
		
		Object[] totalPar = builder.getParams();
		
		for (int i = 0; i < totalPar.length; i++) {
			if(totalPar[i] instanceof Date){
				totalPS.setDate(i+1, (java.sql.Date) totalPar[i]);
			}
			else {
				totalPS.setObject(i + 1, totalPar[i]);
			}
		}
		ResultSet totalRs = totalPS.executeQuery();
		if(totalRs.next()){
			ct.setTotalRows(totalRs.getInt("TOTAL"));
		}
		String finalQuery = "SELECT * FROM (SELECT rownum NUM, x.* FROM ("+QUERY_CASEINFO + " AND "
							+ builder.getCriteria()+
							" ORDER BY "+ct.getSortBy()+" "+ct.getSortOrder()+" )x )Y where Y.NUM between "
							+ct.getStartRecord()+" and "+ct.getEndRecord();
		PreparedStatement ps = conn.prepareStatement(finalQuery);
		Object[] par = builder.getParams();
		for (int i = 0; i < par.length; i++) {
			if(par[i] instanceof Date){
				ps.setDate(i+1, (java.sql.Date) par[i]);
			}
			else {
				ps.setObject(i + 1, par[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		List models = new ArrayList();
		while (rs.next()) {
			CrmCaseModel c = new CrmCaseModel(this.loadFromResultSet(rs));
			models.add(c);
		}
		totalRs.close();
		totalPS.close();
		rs.close();
		ps.close();

		return models;
	}

	private final static String QUERY_QUEUE_OVERVIEW = 
		"select cq.code as queue, "
			+ "(select count(1) from cust.case c, cust.case_subject cs where c.case_subject = cs.code and c.case_state='"+CrmCaseState.CODE_OPEN+"' "  
			  		 + "and cs.case_queue=cq.code ) as open, " 
			+ "(select count(1) from cust.case c, cust.case_subject cs where c.case_subject = cs.code and c.case_state='"+CrmCaseState.CODE_OPEN+"' and cs.case_queue=cq.code " 
			  	 	 + "and c.assigned_agent_id=(select id from cust.agent where user_id='system') ) as unassigned, "
			+ "(select min(ca.timestamp) from cust.case c, cust.case_subject cs, cust.caseaction ca where c.case_subject = cs.code and c.case_state='"+CrmCaseState.CODE_OPEN+"' "
			  		 + "and ca.case_id=c.id and cs.case_queue=cq.code) as oldest " 
			+ "from cust.case_queue cq ";

	public List loadQueueOverview(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_QUEUE_OVERVIEW);
		ResultSet rs = ps.executeQuery();

		List results = new ArrayList();
		while (rs.next()) {

			CrmCaseQueue queue = CrmCaseQueue.getEnum(rs.getString("queue"));
			int open = rs.getInt("open");
			int unassigned = rs.getInt("unassigned");
			Timestamp oldest = rs.getTimestamp("oldest");

			results.add(new CrmQueueInfo(queue, open, unassigned, oldest));
		}

		rs.close();
		ps.close();

		return results;
	}
	
	private final static String QUERY_CSR_OVERVIEW = 
	"select " 
	 + " a.id as agent_id, a.first_name, a.last_name, a.active, a.role, a.user_id, "
	 + " (select count(1) from cust.case c where c.assigned_agent_id = a.id ) as assigned, "
	 + " (select count(1) from cust.case c where c.assigned_agent_id = a.id and c.case_state='OPEN' ) as open, "
	 + " (select count(1) from cust.case c where c.assigned_agent_id = a.id and c.case_state='REVW' ) as review, "
	 + " (select count(1) from cust.case c where c.assigned_agent_id = a.id and c.case_state='CLSD' ) as closed, "
	 + " (select min(timestamp) from cust.case c, cust.caseaction ca where ca.case_id = c.id and c.case_state='OPEN' and c.assigned_agent_id = a.id ) as oldest " 
	+ " from cust.agent a ";
	
	public List loadCSROverview(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_CSR_OVERVIEW);
		ResultSet rs = ps.executeQuery();

		List results = new ArrayList();
		while (rs.next()) {

			PrimaryKey agentPK = new PrimaryKey(rs.getString("agent_id"));
			int assigned = rs.getInt("assigned");
			int open = rs.getInt("open");
			int review = rs.getInt("review");
			int closed = rs.getInt("closed");
			Timestamp oldest = rs.getTimestamp("oldest");
			String firstName = rs.getString("FIRST_NAME");
			String lastName = rs.getString("LAST_NAME");
			boolean active = "X".equalsIgnoreCase(rs.getString("ACTIVE"));
			CrmAgentRole role = CrmAgentRole.getEnum(rs.getString("ROLE"));
			String userId = rs.getString("USER_ID");

			results.add(new CrmAgentInfo(agentPK, assigned, open, review, closed, oldest, firstName, lastName, active, role, userId));
		}

		rs.close();
		ps.close();

		return results;
	}

	public void unlockAll(Connection conn, PrimaryKey agentPK) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CASE SET LOCKED_AGENT_ID=NULL WHERE LOCKED_AGENT_ID=?");
		ps.setString(1, agentPK.getId());
		ps.executeUpdate();
		ps.close();
	}

	public void unlock(Connection conn, PrimaryKey casePK) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CASE SET LOCKED_AGENT_ID=NULL WHERE ID=?");
		ps.setString(1, casePK.getId());
		ps.executeUpdate();
		ps.close();
	}

	public boolean lock(Connection conn, PrimaryKey agentPK, PrimaryKey casePK) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.CASE SET LOCKED_AGENT_ID=? WHERE (LOCKED_AGENT_ID IS NULL OR LOCKED_AGENT_ID=?) AND ID=?");
		ps.setString(1, agentPK.getId());
		ps.setString(2, agentPK.getId());
		ps.setString(3, casePK.getId());
		boolean locked = ps.executeUpdate() == 1;
		ps.close();
		return locked;
	}

	public void create(Connection conn, PrimaryKey pk, ModelI model) throws SQLException {
		CrmCaseModel c = (CrmCaseModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.CASE(ID, CASE_ORIGIN, CASE_STATE, CASE_PRIORITY, CASE_SUBJECT, SUMMARY, CUSTOMER_ID, SALE_ID, ASSIGNED_AGENT_ID, LOCKED_AGENT_ID, CREATE_DATE, LAST_ACTION_DATE,PROJECTED_QUANTITY,ACTUAL_QUANTITY,MEDIA,MORETHENONE_ISSUE,FIRSTCONTACT,FisrtContact_Resolved,ReasonNotToResolve,SatisfiedWithResolution,CustomerTone) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, pk.getId());
		ps.setString(2, c.getOrigin().getCode());
		ps.setString(3, c.getState().getCode());
		ps.setString(4, c.getPriority().getCode());
		ps.setString(5, c.getSubject().getCode());
		ps.setString(6, c.getSummary().substring(0, Math.min(255, c.getSummary().length())));

		setNullable(ps, 7, c.getCustomerPK());
		setNullable(ps, 8, c.getSalePK());
		setNullable(ps, 9, c.getAssignedAgentPK());
		setNullable(ps, 10, c.getLockedAgentPK());

		ps.setTimestamp(11, new Timestamp(getActionDate(c.getActions(), false).getTime()));
		ps.setTimestamp(12, new Timestamp(getActionDate(c.getActions(), true).getTime()));

		ps.setInt(13,c.getProjectedQuantity());
		ps.setInt(14,c.getActualQuantity());
		
		if(c.getCrmCaseMedia()!=null)
		{
			ps.setString(15, c.getCrmCaseMedia());	
		}else{
			ps.setNull(15, Types.VARCHAR);
		}
		
		if(c.isMoreThenOneIssue())
		{
			ps.setString(16, "X" );	
		}else{
			ps.setNull(16, Types.VARCHAR);
		}

		if(c.isFirstContactForIssue())
		{
			ps.setString(17, "X" );	
		}else{
			ps.setNull(17, Types.VARCHAR);
		}

		if(c.isFirstContactResolved())
		{
			ps.setString(18, "X" );	
		}else{
			ps.setNull(18, Types.VARCHAR);
		}

		if(c.getResonForNotResolve()!=null)
		{
			ps.setString(19, c.getResonForNotResolve());	
		}else{
			ps.setNull(19, Types.VARCHAR);
		}						
		
		if(c.isSatisfiedWithResolution())
		{
			ps.setString(20, "X" );	
		}else{
			ps.setNull(20, Types.VARCHAR);
		}
		
		if(c.getCustomerTone()!=null)
		{
			ps.setString(21, c.getCustomerTone());	
		}else{
			ps.setNull(21, Types.VARCHAR);
		}

		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}

		ps.close();

		this.insertCaseDepartments(conn, pk, c.getDepartments());
		this.insertCaseActions(conn, pk, c.getActions());
	}

	private void setNullable(PreparedStatement ps, int field, PrimaryKey pk) throws SQLException {
		if (pk == null) {
			ps.setNull(field, Types.VARCHAR);
		} else {
			ps.setString(field, pk.getId());
		}
	}

	private PrimaryKey getNullable(ResultSet rs, String fieldName) throws SQLException {
		String id = rs.getString(fieldName);
		return id == null ? null : new PrimaryKey(id);
	}

	String LOAD_QUERY = QUERY_CASEINFO + " and c.id=?";

	public ModelI load(Connection conn, PrimaryKey pk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_QUERY);
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			throw new SQLException("No such CrmCase: " + pk);
		}

		CrmCaseModel c = new CrmCaseModel(this.loadFromResultSet(rs));

		rs.close();
		ps.close();

		c.setDepartments(this.selectCaseDepartments(conn, pk));
		c.setActions(this.selectCaseActions(conn, pk));

		return c;
	}

	private CrmCaseInfo loadFromResultSet(ResultSet rs) throws SQLException {
		CrmCaseInfo ci = new CrmCaseInfo(new PrimaryKey(rs.getString("ID")));
		ci.setCustomerPK(getNullable(rs, "CUSTOMER_ID"));
		ci.setAssignedAgentPK(getNullable(rs, "ASSIGNED_AGENT_ID"));
		ci.setSalePK(getNullable(rs, "SALE_ID"));
		ci.setLockedAgentPK(getNullable(rs, "LOCKED_AGENT_ID"));

		ci.setSummary(rs.getString("SUMMARY"));
		ci.setOrigin(CrmCaseOrigin.getEnum(rs.getString("CASE_ORIGIN")));
		ci.setSubject(CrmCaseSubject.getEnum(rs.getString("CASE_SUBJECT")));
		ci.setSubjectName(rs.getString("SUBJECT_NAME"));
		ci.setPriority(CrmCasePriority.getEnum(rs.getString("CASE_PRIORITY")));
		ci.setState(CrmCaseState.getEnum(rs.getString("CASE_STATE")));

		ci.setCustomerFirstName(NVL.apply(rs.getString("FIRST_NAME"), ""));
		ci.setCustomerLastName(NVL.apply(rs.getString("LAST_NAME"), ""));
		ci.setCreateDate(rs.getTimestamp("CREATE_DATE"));
		ci.setLastModDate(rs.getTimestamp("LAST_ACTION_DATE"));
		
		ci.setProjectedQuantity(rs.getInt("projected_quantity"));
		ci.setActualQuantity(rs.getInt("actual_quantity"));
		
		ci.setCrmCaseMedia(NVL.apply(rs.getString("MEDIA"), ""));		
		ci.setResonForNotResolve(NVL.apply(rs.getString("ReasonNotToResolve"), ""));						
		ci.setFirstContactResolved(NVL.apply(rs.getString("FisrtContact_Resolved"), "").equalsIgnoreCase("X")?true:false);
		ci.setFirstContactForIssue(NVL.apply(rs.getString("FIRSTCONTACT"), "").equalsIgnoreCase("X")?true:false);
		ci.setMoreThenOneIssue(NVL.apply(rs.getString("MORETHENONE_ISSUE"), "").equalsIgnoreCase("X")?true:false);
		ci.setSatisfiedWithResolution(NVL.apply(rs.getString("SatisfiedWithResolution"), "").equalsIgnoreCase("X")?true:false);
		ci.setCustomerTone(NVL.apply(rs.getString("CustomerTone"), ""));
		
		return ci;
	}

	private final static String UPDATE_CASE =
		"UPDATE CUST.CASE SET CASE_ORIGIN=?, CASE_STATE=?, CASE_PRIORITY=?, CASE_SUBJECT=?, SUMMARY=?, CUSTOMER_ID=?, SALE_ID=?, ASSIGNED_AGENT_ID=?, LOCKED_AGENT_ID=?, LAST_ACTION_DATE=?,  PROJECTED_QUANTITY=?, ACTUAL_QUANTITY=?,MEDIA=?,MORETHENONE_ISSUE=?,FIRSTCONTACT=?,FisrtContact_Resolved=?,ReasonNotToResolve=?,SatisfiedWithResolution=?,CustomerTone=?   WHERE ID=?";

	public void store(Connection conn, ModelI model) throws SQLException {
		CrmCaseModel c = (CrmCaseModel) model;

		PreparedStatement ps = conn.prepareStatement(UPDATE_CASE);

		ps.setString(1, c.getOrigin().getCode());
		ps.setString(2, c.getState().getCode());
		ps.setString(3, c.getPriority().getCode());
		ps.setString(4, c.getSubject().getCode());
		ps.setString(5, c.getSummary());

		setNullable(ps, 6, c.getCustomerPK());
		setNullable(ps, 7, c.getSalePK());
		setNullable(ps, 8, c.getAssignedAgentPK());
		setNullable(ps, 9, c.getLockedAgentPK());

		ps.setTimestamp(10, new Timestamp(getActionDate(c.getActions(), true).getTime()));
		ps.setInt(11,c.getProjectedQuantity());
		ps.setInt(12,c.getActualQuantity());
		
		
		
		if(c.getCrmCaseMedia()!=null)
		{
			ps.setString(13, c.getCrmCaseMedia());	
		}else{
			ps.setNull(13, Types.VARCHAR);
		}
		
		if(c.isMoreThenOneIssue())
		{
			ps.setString(14, "X" );	
		}else{
			ps.setNull(14, Types.VARCHAR);
		}

		if(c.isFirstContactForIssue())
		{
			ps.setString(15, "X" );	
		}else{
			ps.setNull(15, Types.VARCHAR);
		}

		if(c.isFirstContactResolved())
		{
			ps.setString(16, "X" );	
		}else{
			ps.setNull(16, Types.VARCHAR);
		}

		if(c.getResonForNotResolve()!=null)
		{
			ps.setString(17, c.getResonForNotResolve());	
		}else{
			ps.setNull(17, Types.VARCHAR);
		}
		
		if(c.isSatisfiedWithResolution())
		{
			ps.setString(18, "X" );	
		}else{
			ps.setNull(18, Types.VARCHAR);
		}
		
		if(c.getCustomerTone()!=null)
		{
			ps.setString(19, c.getCustomerTone());	
		}else{
			ps.setNull(19, Types.VARCHAR);
		}

		ps.setString(20, c.getPK().getId());

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}

		ps.close();

		this.deleteCaseDepartments(conn, c.getPK());
		this.insertCaseDepartments(conn, c.getPK(), c.getDepartments());

		List anonActions = new ArrayList();
		for (Iterator i = c.getActions().iterator(); i.hasNext();) {
			CrmCaseAction act = (CrmCaseAction) i.next();
			if (act.isAnonymous()) {
				anonActions.add(act);
			}
		}

		this.insertCaseActions(conn, c.getPK(), anonActions);

	}

	public void remove(Connection conn, PrimaryKey pk) throws SQLException {
		this.deleteCaseDepartments(conn, pk);
		this.deleteCaseActions(conn, pk);
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CASE WHERE ID=?");
		ps.setString(1, pk.getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
	}

	public void downloadCases(Connection conn, PrimaryKey agentPK, String queue, String subject, int numberToDownload) throws SQLException {
		
		String sql = 
			"update cust.case set assigned_agent_id = ? "
			+ "where case_subject in (select code from cust.case_subject where case_queue = ?"
			+ (subject != null && !"".equals(subject) ? " AND code = ? " : "") 
			+ " ) " 
			+ "and assigned_agent_id = (select id from cust.agent where user_id = 'system') "
			+ "and case_state = 'OPEN' and locked_agent_id is null and rownum <= ? ";
		
		PreparedStatement ps = conn.prepareStatement(sql);

		int index = 1; 
		ps.setString(index++, agentPK.getId());
		ps.setString(index++, queue);
		if (subject != null && !"".equals(subject)) {
			ps.setString(index++, subject);
		}
		ps.setInt(index++, numberToDownload);

		ps.executeUpdate();
		ps.close();
	}

	private void deleteCaseDepartments(Connection conn, PrimaryKey casePK) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CASE_DEPARTMENT WHERE CASE_ID=?");
		ps.setString(1, casePK.getId());
		ps.executeUpdate();
		ps.close();
	}

	private void insertCaseDepartments(Connection conn, PrimaryKey casePK, Set departments) throws SQLException {
		if (departments.isEmpty()) {
			return;
		}

		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.CASE_DEPARTMENT(CASE_ID, DEPARTMENT) VALUES (?,?)");
		for (Iterator i = departments.iterator(); i.hasNext();) {
			CrmDepartment dept = (CrmDepartment) i.next();
			ps.setString(1, casePK.getId());
			ps.setString(2, dept.getCode());
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}

	private Set selectCaseDepartments(Connection conn, PrimaryKey casePK) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT DEPARTMENT FROM CUST.CASE_DEPARTMENT WHERE CASE_ID=?");
		ps.setString(1, casePK.getId());

		Set departments = new HashSet();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			departments.add(CrmDepartment.getEnum(rs.getString("DEPARTMENT")));
		}

		rs.close();
		ps.close();

		return departments;
	}

	private void deleteCaseActions(Connection conn, PrimaryKey casePK) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CASEACTION WHERE CASE_ID=?");
		ps.setString(1, casePK.getId());
		ps.executeUpdate();
		ps.close();
	}

	private void insertCaseActions(Connection conn, PrimaryKey casePK, List actions) throws SQLException {
		if (actions.isEmpty()) {
			return;
		}

		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.CASEACTION(CASE_ID, ID, CASEACTION_TYPE, AGENT_ID, TIMESTAMP, NOTE) VALUES (?,?,?,?,?,?)");
		for (Iterator i = actions.iterator(); i.hasNext();) {
			CrmCaseAction act = (CrmCaseAction) i.next();
			act.setPK(new PrimaryKey(this.getNextId(conn)));

			ps.setString(1, casePK.getId());
			ps.setString(2, act.getPK().getId());
			ps.setString(3, act.getType().getCode());
			ps.setString(4, act.getAgentPK().getId());
			ps.setTimestamp(5, new Timestamp(act.getTimestamp().getTime()));
			ps.setString(6, act.getNote());
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}

	protected String getNextId(Connection conn) throws SQLException {
		return SequenceGenerator.getNextId(conn, "CUST");
	}

	private Date getActionDate(List caseActions, boolean maximum) {
		Date lastActionDate = null;
		for (Iterator i = caseActions.iterator(); i.hasNext();) {
			CrmCaseAction act = (CrmCaseAction) i.next();
			if (lastActionDate == null
				|| (maximum ? act.getTimestamp().after(lastActionDate) : act.getTimestamp().before(lastActionDate))) {
				lastActionDate = act.getTimestamp();
			}
		}
		return lastActionDate;
	}

	private List selectCaseActions(Connection conn, PrimaryKey casePK) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT ID, CASEACTION_TYPE, AGENT_ID, TIMESTAMP, NOTE FROM CUST.CASEACTION WHERE CASE_ID=? ORDER BY TIMESTAMP");
		ps.setString(1, casePK.getId());

		List actions = new ArrayList();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			CrmCaseAction ca = new CrmCaseAction();
			ca.setPK(new PrimaryKey(rs.getString("ID")));
			ca.setType(CrmCaseActionType.getEnum(rs.getString("CASEACTION_TYPE")));
			ca.setAgentPK(new PrimaryKey(rs.getString("AGENT_ID")));
			ca.setTimestamp(rs.getTimestamp("TIMESTAMP"));
			ca.setNote(rs.getString("NOTE"));
			actions.add(ca);
		}

		rs.close();
		ps.close();

		return actions;
	}

}
