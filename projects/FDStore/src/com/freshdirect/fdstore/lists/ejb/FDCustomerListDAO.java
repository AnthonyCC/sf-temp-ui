/**
 * @author ekracoff
 * Created on Sep 29, 2004*/

package com.freshdirect.fdstore.lists.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.ejb.ErpOrderLineUtil;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerCreatedListInfo;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerRecipeListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

class FDCustomerListDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(FDCustomerListDAO.class);
		
	private static final String CREATE_LIST = "INSERT INTO CUST.CUSTOMERLIST (ID,CUSTOMER_ID,TYPE,NAME,CREATE_DATE,MODIFICATION_DATE) VALUES (?,?,?,?,?,?)";
	
	private static final String UPDATE_LIST = "UPDATE CUST.CUSTOMERLIST SET CUSTOMER_ID=? ,TYPE =? ,NAME=?, MODIFICATION_DATE=? WHERE ID=?";

	public void updateCustomerList(Connection conn, FDCustomerList list) throws SQLException {			
	  PreparedStatement ps=null;
		try	{
			ps = conn.prepareStatement(UPDATE_LIST);
			ps.setString(1, list.getCustomerPk().getId());		
			ps.setString(2, list.getType().getName());
			ps.setString(3, list.getName());		
			Timestamp ts = list.getModificationDate() == null
			             ? null
			             : new Timestamp(list.getModificationDate().getTime());
			ps.setTimestamp(4, ts);
			
			ps.setString(5, list.getId());

			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not updated");
			}
		} catch(SQLException e)	{
			if (ps != null) {
			    ps.close();
			}
			throw e;
		}						
	}

	private FDCustomerList createList(Connection conn, PrimaryKey customerPk, EnumCustomerListType type, String name, Date createDate, Date modificationDate) throws SQLException {
		String id = getNextId(conn);
		PreparedStatement ps = conn.prepareStatement(CREATE_LIST);
		ps.setString(1, id);
		ps.setString(2, customerPk.getId());
		ps.setString(3, type.getName());
		ps.setString(4, name);
		createDate = (createDate == null ? getCurrentDate() : createDate);
		modificationDate = (modificationDate == null || createDate.after(modificationDate) ? createDate : modificationDate); 
		ps.setTimestamp(5, new Timestamp(createDate.getTime()));
		ps.setTimestamp(6, new Timestamp(modificationDate.getTime()));
	
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
	
		ps.close();
		
		FDCustomerList list = createListByType(type);
		
		list.setId(id);
		list.setCustomerPk(customerPk);
		list.setCreateDate(createDate);
		list.setName(name);
		list.setModificationDate(modificationDate);
		
		return list;
	}	
				
	// CCL
	private FDCustomerCreatedList createCustomerCreatedList(Connection conn, PrimaryKey customerPk, String name, Date createDate, Date modificationDate) 
	throws SQLException {
		return (FDCustomerCreatedList) createList(conn, customerPk, EnumCustomerListType.CC_LIST,  name, createDate, modificationDate);
	}
		
	private FDCustomerRecipeList createRecipeList(Connection conn, PrimaryKey customerPk, String name, Date createDate, Date modificationDate)
																	throws SQLException {	
		return (FDCustomerRecipeList) createList(conn, customerPk, EnumCustomerListType.RECIPE_LIST, name, createDate, modificationDate);
	}
	
	private static final String LOAD_CUSTOMER_LIST_DETAILS = "SELECT * from CUST.CUSTOMERLIST_DETAILS cld WHERE LIST_ID = ? and DELETE_DATE is null";
	
	private static final String LOAD_RECIPE_LIST_DETAILS = "SELECT * from CUST.CUSTOMERLIST_RECIPES clr WHERE LIST_ID = ? and DELETE_DATE is null";

	private static final String LOAD_CUSTOMER_LIST = "SELECT * from CUST.CUSTOMERLIST cl WHERE cl.customer_id = ? AND cl.name = ? AND cl.type = ?";

	/* 2 */
	public FDCustomerList load(Connection conn, FDIdentity identity, EnumCustomerListType type, String name) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_CUSTOMER_LIST);
		ps.setString(1, identity.getErpCustomerPK());
		ps.setString(2, name);
		ps.setString(3, type.getName());
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			LOGGER.debug("Attempted to load list of type "+ type.getName()+" with name " + name + " for customer " + identity.getErpCustomerPK() + ": no list found.");
			rs.close();
			ps.close();
			return null;
		}

		PrimaryKey pk               = new PrimaryKey(rs.getString("ID"));
		Timestamp  modificationDate = rs.getTimestamp("MODIFICATION_DATE");
		
		rs.close();
		ps.close();
		
		
		PrimaryKey customerPk = new PrimaryKey(identity.getErpCustomerPK());
		
		if (EnumCustomerListType.RECIPE_LIST.equals(type)) {
			return loadRecipeList(conn, customerPk, name, pk);
		} else if (EnumCustomerListType.CC_LIST.equals(type)) {
			return loadProductList(conn, (FDCustomerProductList) createListByType(EnumCustomerListType.CC_LIST), customerPk, name, modificationDate, pk);
		} else if (EnumCustomerListType.SHOPPING_LIST.equals(type)) {
			return loadProductList(conn, (FDCustomerProductList) createListByType(EnumCustomerListType.SHOPPING_LIST), customerPk, name, modificationDate, pk);
		}
		return null;
	}

	private Date getTimestamp(ResultSet rs, String field) throws SQLException {
		Timestamp ts = rs.getTimestamp(field);
		return ts == null ? null : new Date(ts.getTime());
	}
	
	
	private FDCustomerProductList loadProductList(Connection conn,
			                                      FDCustomerProductList list,
		                                          PrimaryKey            customerPk,
		                                          String                name,
		                                          Timestamp             modificationDate,
		                                          PrimaryKey            pk) throws SQLException {
		list.setPK(pk);
		list.setCustomerPk(customerPk);
		list.setName(name);

		PreparedStatement ps = conn.prepareStatement(LOAD_CUSTOMER_LIST_DETAILS);
		ps.setString(1, list.getPK().getId());
		ResultSet rs = ps.executeQuery();

		List lineItems = new ArrayList();
		while (rs.next()) {
			PrimaryKey itemPK = new PrimaryKey(rs.getString("ID"));
			FDCustomerProductListLineItem item = new FDCustomerProductListLineItem(
				rs.getString("SKU_CODE"),
				new FDConfiguration(rs.getDouble("QUANTITY"), rs.getString("SALES_UNIT"), ErpOrderLineUtil.convertStringToHashMap(rs.getString("CONFIGURATION"))),
				rs.getString("RECIPE_SOURCE_ID")	
			);
			item.setPK(itemPK);
			item.setFrequency(rs.getInt("FREQUENCY"));
			item.setFirstPurchase(getTimestamp(rs, "CREATE_DATE"));
			item.setLastPurchase(getTimestamp(rs, "RECENT_DATE"));
			item.setDeleted(getTimestamp(rs, "DELETE_DATE"));
			lineItems.add(item);
		}

		list.setLineItems(lineItems);


		// set this last - as the above setters update the modification date
		list.setModificationDate(new Date(modificationDate.getTime()));
		
		rs.close();
		ps.close();

		return list;
	}

	private FDCustomerRecipeList loadRecipeList(Connection conn, PrimaryKey customerPk, String name, PrimaryKey pk) throws SQLException {
		FDCustomerRecipeList list = new FDCustomerRecipeList(pk, customerPk, name);

		PreparedStatement ps = conn.prepareStatement(LOAD_RECIPE_LIST_DETAILS);
		ps.setString(1, list.getPK().getId());
		ResultSet rs = ps.executeQuery();

		List lineItems = new ArrayList();
		while (rs.next()) {
			FDCustomerRecipeListLineItem item = new FDCustomerRecipeListLineItem();
			PrimaryKey itemPK = new PrimaryKey(rs.getString("ID"));
			item.setPK(itemPK);
			item.setRecipeId(rs.getString("RECIPE_ID"));
			item.setFrequency(rs.getInt("FREQUENCY"));
			item.setFirstPurchase(getTimestamp(rs, "CREATE_DATE"));
			item.setLastPurchase(getTimestamp(rs, "RECENT_DATE"));
			item.setDeleted(getTimestamp(rs, "DELETE_DATE"));

			lineItems.add(item);
		}

		list.setLineItems(lineItems);

		rs.close();
		ps.close();

		return list;
	}

	private static final String STORE_DETAIL = "INSERT INTO CUST.CUSTOMERLIST_DETAILS (ID,LIST_ID,SKU_CODE,QUANTITY,SALES_UNIT,CONFIGURATION,FREQUENCY,CREATE_DATE,RECENT_DATE,DELETE_DATE,RECIPE_SOURCE_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

	private static final String DELETE_DETAILS = "DELETE FROM CUST.CUSTOMERLIST_DETAILS WHERE LIST_ID = ?";
	
	
	private FDCustomerList createListByType(EnumCustomerListType type) {
	    if (EnumCustomerListType.SHOPPING_LIST.equals(type)) {
	    	return new FDCustomerShoppingList();
	    } else if (EnumCustomerListType.CC_LIST.equals(type)) {
	    	return new FDCustomerCreatedList();
	    } else if (EnumCustomerListType.RECIPE_LIST.equals(type)) {
	    	return new FDCustomerRecipeList();
	    }
	    return null;
	}

	private void store(Connection conn, FDCustomerProductList list) throws SQLException {
		PrimaryKey listId = list.getPK();

		assert (listId != null);
		{
			//delete existing leafs from table
			PreparedStatement ps = conn.prepareStatement(DELETE_DETAILS);
			ps.setString(1, listId.getId());
			ps.execute();
			ps.close();
		}

		PreparedStatement ps = conn.prepareStatement(STORE_DETAIL);
		for (Iterator i = list.getLineItems().iterator(); i.hasNext();) {
			FDCustomerProductListLineItem item = (FDCustomerProductListLineItem) i.next();
			String id = getNextId(conn);
			item.setId(id);
			ps.setString(1, id);
			ps.setString(2, listId.getId());
			ps.setString(3, item.getSkuCode());
			ps.setDouble(4, item.getConfiguration().getQuantity());
			ps.setString(5, item.getConfiguration().getSalesUnit());
			ps.setString(6, ErpOrderLineUtil.convertHashMapToString(item.getConfiguration().getOptions()));
			ps.setInt(7, item.getFrequency());
			ps.setTimestamp(8, item.getFirstPurchase() != null ? new Timestamp(item.getFirstPurchase().getTime()) : 
				new Timestamp(getCurrentDate().getTime()));
			ps.setTimestamp(9, item.getLastPurchase() != null ? new Timestamp(item.getLastPurchase().getTime()) : 
				new Timestamp(getCurrentDate().getTime()));
			ps.setTimestamp(10, item.getDeleted() == null ? null : new Timestamp(item.getDeleted().getTime()));
			ps.setString(11, item.getRecipeSourceId());
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}

	private static final String STORE_RECIPE = "INSERT INTO CUST.CUSTOMERLIST_RECIPES (ID,LIST_ID,RECIPE_ID,FREQUENCY,CREATE_DATE,RECENT_DATE,DELETE_DATE) VALUES(?,?,?,?,?,?,?)";

	private static final String DELETE_RECIPE_DETAILS = "DELETE FROM CUST.CUSTOMERLIST_RECIPES WHERE LIST_ID = ?";

	private void store(Connection conn, FDCustomerRecipeList list) throws SQLException {
		PrimaryKey listId = list.getPK();
		
		assert (listId != null);
		{
			//delete existing leafs from table
			PreparedStatement ps = conn.prepareStatement(DELETE_RECIPE_DETAILS);
			ps.setString(1, listId.getId());
			ps.execute();
			ps.close();
		}

		PreparedStatement ps = conn.prepareStatement(STORE_RECIPE);
		for (Iterator i = list.getLineItems().iterator(); i.hasNext();) {
			FDCustomerRecipeListLineItem item = (FDCustomerRecipeListLineItem) i.next();
			String id = getNextId(conn);
			item.setId(id);
			ps.setString(1, id);
			ps.setString(2, listId.getId());
			ps.setString(3, item.getRecipeId());
			ps.setInt(4, item.getFrequency());
			ps.setTimestamp(5, new Timestamp(item.getFirstPurchase().getTime()));
			ps.setTimestamp(6, new Timestamp(item.getLastPurchase().getTime()));
			ps.setTimestamp(7, item.getDeleted() == null ? null : new Timestamp(item.getDeleted().getTime()));
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}
	
	// CCL
	public void store(Connection conn, FDCustomerList list) throws SQLException {
		if (list.getId() != null) {
			// Update the list if it already exists
			updateCustomerList(conn, list);
		} else {
			persistList(conn, list);
		}

		if (list instanceof FDCustomerProductList) {
			store(conn, (FDCustomerProductList) list);
		} else if (list instanceof FDCustomerRecipeList) {
			store(conn, (FDCustomerRecipeList) list);
		} else {
			throw new IllegalArgumentException("Unknown list type");
		}
	}
	
	private void persistList(Connection conn, FDCustomerList list) throws SQLException {
		FDCustomerList persistedList = createList(conn, list.getCustomerPk(), list.getType(), list.getName(), list.getCreateDate(), list.getModificationDate());
		
		list.setPK(persistedList.getPK());
		list.setCreateDate(persistedList.getCreateDate());
		list.setModificationDate(persistedList.getModificationDate());
	}

	// ensure list item is on a list owned by logged in user
	private static final String LIST_ITEM_OWNED = "SELECT count(*) AS CNT FROM CUST.CUSTOMERLIST_DETAILS D JOIN CUST.CUSTOMERLIST L ON(D.LIST_ID = L.ID) WHERE D.ID = ? AND L.CUSTOMER_ID = ?";
	private static final String REMOVE_DETAIL = "UPDATE CUST.CUSTOMERLIST_DETAILS SET DELETE_DATE = ?, RECENT_DATE = ? WHERE ID = ?";
	private static final String UPDATE_LIST_MODIFICATION_DATE_BY_ITEM_ID = 		
		"UPDATE cust.customerlist SET modification_date = ? WHERE id in (SELECT cl.id FROM cust.customerlist cl, cust.customerlist_details cld WHERE cl.id = cld.list_id AND cld.id = ?)";
	


	/**
	 * Removes a list item from its list
	 * 
	 * @param conn DB connection
	 * @param identity Identity of current user
	 * @param id item ID
	 * @throws SQLException
	 */
	/* 2 */
	public boolean removeItem(Connection conn, FDIdentity identity, PrimaryKey id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LIST_ITEM_OWNED);
		ps.setString(1, id.getId()); // list item ID
		ps.setString(2, identity.getErpCustomerPK()); // customer ID
		ResultSet rs = ps.executeQuery();
		boolean itemOwnedByCurrentUser = (rs.next() && rs.getInt("CNT") == 1);
		ps.close();

		if (!itemOwnedByCurrentUser) {
			// list item is not owned by current user
			return false;
		}


		ps = conn.prepareStatement(REMOVE_DETAIL);
		Timestamp ts = new Timestamp(getCurrentDate().getTime());
		ps.setTimestamp(1, ts);
		ps.setTimestamp(2, ts);
		ps.setString(3, id.getId());
		ps.execute();
		ps.close();
		
		ps = conn.prepareStatement(UPDATE_LIST_MODIFICATION_DATE_BY_ITEM_ID);
		ps.setTimestamp(1,ts);
		ps.setString(2,id.getId());
		ps.execute();
		ps.close();
		
		return true;
	}

	private String QUERY_EVERY_ITEM = "SELECT ol.sku_code, ol.sales_unit, ol.configuration, "
		+ "count(sa.action_date) as frequency, max(sa.action_date) as last_purchase, "
		+ "min (sa.action_date) as first_purchase, ol.recipe_source_id, ol.description "
		+ "FROM cust.sale s, cust.salesaction sa, cust.orderline ol "
		+ "WHERE s.id=sa.sale_id AND sa.id=ol.salesaction_id AND sa.action_type IN ('CRO','MOD') "
		+ "and s.type = 'REG' "
		+ "AND sa.action_date=(SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id=s.id AND action_type IN ('CRO','MOD')) "
		+ "AND s.customer_id=? "
		+ "AND NVL(ol.promotion_type,0)<> 3 "
		+ "AND NVL(ol.delivery_grp, 0) = 0 "
		+ "group by ol.sku_code, ol.sales_unit, ol.configuration, ol.recipe_source_id, ol.description";

	public FDCustomerShoppingList generateEveryItemEverOrderedList(Connection conn, FDIdentity identity) throws SQLException {
		FDCustomerShoppingList list = new FDCustomerShoppingList();
		list.setCustomerPk(new PrimaryKey(identity.getErpCustomerPK()));
		list.setName(FDCustomerShoppingList.EVERY_ITEM_LIST);

		LOGGER.info("generateEveryItemEverOrderedList for " + identity.getErpCustomerPK());

		PreparedStatement ps = conn.prepareStatement(QUERY_EVERY_ITEM);
		ps.setString(1, list.getCustomerPk().getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			
			FDCustomerProductListLineItem item = new FDCustomerProductListLineItem(
					rs.getString("SKU_CODE"),
					new FDConfiguration(1, rs.getString("SALES_UNIT"), ErpOrderLineUtil.convertStringToHashMap(rs.getString("CONFIGURATION"))),
					rs.getString("recipe_source_id")
			);
			
			item.setFrequency(rs.getInt("frequency"));
			item.setFirstPurchase(getTimestamp(rs, "first_purchase"));
			item.setLastPurchase(getTimestamp(rs, "last_purchase"));
			list.mergeLineItem(item);
		}

		rs.close();
		ps.close();

		return list;
	}

	protected String getNextId(Connection conn) throws SQLException {
		return SequenceGenerator.getNextId(conn, "CUST");
	}
	
	// CCL
	/**
	 * 
	 * @param listName new CCL list name
	 */
	public void createCustomerCreatedList(Connection conn, FDIdentity identity, String listName)
	throws SQLException {
		createCustomerCreatedList(conn,new PrimaryKey(identity.getErpCustomerPK()),listName,null ,null);
	}
	
	
	private static final String LOAD_CUSTOMER_CREATED_LISTS = 
		"SELECT * from CUST.CUSTOMERLIST cl WHERE cl.customer_id = ? AND cl.type = '" + EnumCustomerListType.CC_LIST.getName() + "'";
	
	// CCL, ?? maybe sorted by last accessed, it could be faster in SQL? This is the default order
	/**
	 * @return List<FDCustomerCreatedList> 
	 */
	public List getCustomerCreatedLists(Connection conn, FDIdentity identity) 
	throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_CUSTOMER_CREATED_LISTS);
		ps.setString(1, identity.getErpCustomerPK());
		ResultSet rs = ps.executeQuery();
		
		
		List CCLists = new ArrayList(rs.getFetchSize());

		while (rs.next()) {
			FDCustomerCreatedList CCList = new FDCustomerCreatedList();
			CCList.setCreateDate(getTimestamp(rs, "CREATE_DATE"));
			
			PrimaryKey  pk              = new PrimaryKey(rs.getString("ID"));
			String      name            = rs.getString("NAME");
			Timestamp   modificationDate = rs.getTimestamp("MODIFICATION_DATE");
			PrimaryKey  customerPk = new PrimaryKey(identity.getErpCustomerPK());
			loadProductList(conn, CCList, customerPk, name, modificationDate, pk);

			CCLists.add(CCList);
		}
		
		rs.close();
		ps.close();
		
		return CCLists;
	 }

	private static final String LOAD_CUSTOMER_CREATED_LIST_INFOS = 
		"select cl.id as list_id, cl.name as name, cl.create_date as create_date," + 
		" cl.modification_date as modification_date, count(cld.list_id) as elem_count " + 
		" from cust.customerlist cl LEFT JOIN cust.customerlist_details cld ON cl.id = cld.list_id and cld.delete_date is null " + 
		" where cl.customer_id = ? and cl.type = '"+ EnumCustomerListType.CC_LIST.getName() + "'" +
		" group by cl.id, cl.name, cl.create_date, cl.modification_date";
	
	public List getCustomerCreatedListInfos(Connection conn, FDIdentity identity)	throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_CUSTOMER_CREATED_LIST_INFOS);
		ps.setString(1, identity.getErpCustomerPK());
		ResultSet rs = ps.executeQuery();
		
		
		List cclLists = new ArrayList(rs.getFetchSize());

		while (rs.next()) {
			FDCustomerCreatedListInfo list = new FDCustomerCreatedListInfo();
			list.setCreateDate(getTimestamp(rs, "CREATE_DATE"));
			
			list.setPK(new PrimaryKey(rs.getString("LIST_ID")));
			list.setCustomerPk(new PrimaryKey(identity.getErpCustomerPK()));
			list.setName(rs.getString("NAME"));
			list.setCount(rs.getInt("ELEM_COUNT"));
			list.setModificationDate(rs.getTimestamp("MODIFICATION_DATE"));

			cclLists.add(list);
		}
		
		rs.close();
		ps.close();
		
		return cclLists;
	 }
	
	// CCL
	private static final String GET_LIST_ID = "SELECT id FROM CUST.CUSTOMERLIST cl WHERE cl.name = ? AND cl.type = ? AND cl.customer_id = ?";
    private static final String DELETE_LIST = "DELETE FROM CUST.CUSTOMERLIST cl WHERE cl.id = ?";
    
	// CCL, 
	public void deleteCustomerCreatedList(Connection conn, FDIdentity identity, String listName) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GET_LIST_ID);
		ps.setString(1, listName);
		ps.setString(2, EnumCustomerListType.CC_LIST.getName());
		ps.setString(3, identity.getErpCustomerPK());
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			LOGGER.debug("No CCL list for " + identity.getErpCustomerPK());
			return;
		}
		
		String ID = rs.getString("ID");
		
		rs.close();
		ps.close();
		
	    ps = conn.prepareStatement(DELETE_DETAILS);
		ps.setString(1, ID);
		ps.execute();
		ps.close();
		
		ps = conn.prepareStatement(DELETE_LIST);
		ps.setString(1, ID);
		ps.execute();
		ps.close();
	}
	
	
	private static final String CUSTOMER_CREATED_LIST_EXISTS = 
		"SELECT COUNT(*) from CUST.CUSTOMERLIST cl WHERE cl.customer_id = ? AND cl.name = ? AND cl.type = '" + EnumCustomerListType.CC_LIST.getName() + "'";
	
	// CCL
	public boolean isCustomerCreatedList(Connection conn, FDIdentity identity, String listName ) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(CUSTOMER_CREATED_LIST_EXISTS);
		ps.setString(1, identity.getErpCustomerPK());
		ps.setString(2, listName);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		ps.close();
		return count > 0;
	}
	
	
	private static final String LOAD_CUSTOMER_CREATED_LIST = 
		"SELECT * from CUST.CUSTOMERLIST cl WHERE cl.customer_id = ? AND cl.type = '" + EnumCustomerListType.CC_LIST.getName() + "' AND cl.id=? ";



// CCL 	
	/**
	 * @return List<FDCustomerCreatedList> 
	 */
	public FDCustomerCreatedList getCustomerCreatedList(Connection conn, FDIdentity identity,String ccListId) 
	throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_CUSTOMER_CREATED_LIST);
		ps.setString(1, identity.getErpCustomerPK());
		ps.setString(2, ccListId );
		ResultSet rs = ps.executeQuery();
		FDCustomerCreatedList CCList=null;			

		if (rs.next()) {
			CCList = new FDCustomerCreatedList();
			CCList.setCreateDate(getTimestamp(rs, "CREATE_DATE"));
			
			PrimaryKey pk               = new PrimaryKey(rs.getString("ID"));
			String     name             = rs.getString("NAME");
			Timestamp  modificationDate = rs.getTimestamp("MODIFICATION_DATE");
			PrimaryKey customerPk = new PrimaryKey(identity.getErpCustomerPK());
			loadProductList(conn, CCList, customerPk, name, modificationDate, pk);			
		}
		
		rs.close();
		ps.close();
		
		return CCList;
	 }
	
	private static final String GET_LIST_NAME = "SELECT name FROM cust.customerlist cl WHERE cl.id= ? AND cl.type= ? AND cl.customer_id = ?";
	
	/* 2 */
	public String getListName(Connection conn, FDIdentity identity, EnumCustomerListType type, String ccListId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GET_LIST_NAME);
		ps.setString(1, ccListId );
		ps.setString(2, type.getName());
		ps.setString(3, identity.getErpCustomerPK());
		ResultSet rs = ps.executeQuery();
		String name = null;

		if (rs.next()) name = rs.getString("NAME");
			
		rs.close();
		ps.close();
		
		return name;
	 }
	
	private static final String RENAME_LIST = "UPDATE cust.customerlist cl SET cl.name = ?, cl.modification_date = ?  WHERE cl.customer_id = ? AND cl.name = ? AND cl.type = '" + EnumCustomerListType.CC_LIST.getName() + "'";
	
	public void renameCustomerCreatedList(Connection conn, FDIdentity identity, String oldName, String newName) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(RENAME_LIST);
		ps.setString(1, newName);
		ps.setTimestamp(2, new Timestamp(getCurrentDate().getTime()));
		ps.setString(3, identity.getErpCustomerPK());
		ps.setString(4, oldName);
		ResultSet rs = ps.executeQuery();
		rs.close();
		ps.close();
	}
	
	protected Date getCurrentDate() {
		return new Date();
	}
	
	/**
	 * Get the order details for the selected skus for the customer.
	 * @param conn
	 * @param erpCustomerId 
	 * @param skus selected skus
	 * @return the line items as a product list
	 * @throws SQLException
	 */
	public FDCustomerProductList getOrderDetails(Connection conn, String erpCustomerId, List skus) throws SQLException {
		if (skus.size() == 0) throw new FDRuntimeException("Empty sku list");
	
		StringBuffer query = 
			new StringBuffer(256).
				append("SELECT ld.ID, QUANTITY, SKU_CODE, SALES_UNIT, CONFIGURATION, RECIPE_SOURCE_ID, ").
				append("ld.CREATE_DATE, ld.RECENT_DATE, ld.DELETE_DATE, FREQUENCY ").
				append("FROM cust.customerlist l, cust.customerlist_details ld WHERE ").
				append("ld.list_id = l.id and l.customer_id = ? AND ld.sku_code in (");
		for(int i = 0; i < skus.size(); ++i) {
			if (i > 0) query.append(',');
			query.append('?');
		}
		query.append(')');
		
		PreparedStatement ps = conn.prepareStatement(query.toString());
		ps.setString(1,erpCustomerId);
		for(int i = 0; i< skus.size(); ++i) {
			ps.setString(i+2,skus.get(i).toString());
		}
			
		FDCustomerProductList products = new FDCustomerShoppingList();
		
		ResultSet rs = ps.executeQuery();
		
		List lineItems = new ArrayList();
		while (rs.next()) {
			PrimaryKey itemPK = new PrimaryKey(rs.getString("ID"));
			FDCustomerProductListLineItem item = new FDCustomerProductListLineItem(
				rs.getString("SKU_CODE"),
				new FDConfiguration(rs.getDouble("QUANTITY"), 
				rs.getString("SALES_UNIT"), 
				ErpOrderLineUtil.convertStringToHashMap(rs.getString("CONFIGURATION"))),
				rs.getString("RECIPE_SOURCE_ID")	
			);
			item.setPK(itemPK);
			item.setFrequency(rs.getInt("FREQUENCY"));
			item.setFirstPurchase(getTimestamp(rs, "CREATE_DATE"));
			item.setLastPurchase(getTimestamp(rs, "RECENT_DATE"));
			item.setDeleted(getTimestamp(rs, "DELETE_DATE"));
			lineItems.add(item);
		}
		rs.close();
		ps.close();

		products.setLineItems(lineItems);
		
		products.setCustomerPk(new PrimaryKey(erpCustomerId));
		
		return products;
	}

	
}
