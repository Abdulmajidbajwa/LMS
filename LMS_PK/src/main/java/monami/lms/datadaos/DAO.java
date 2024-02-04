package monami.lms.datadaos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
@Repository
@Scope("prototype")
public class DAO {
	@Autowired
	private SessionFactory sessionFactory;
	Logger logger = LoggerFactory.getLogger(DAO.class);
	private Session session;
	Transaction transaction;



	public  DAO(){
		
	}

	public void startTask() {
		
		//Statistics objStatistics=sessionFactory.getStatistics();	
		
		if (session == null || !session.isConnected()) {
			
			session = createSession();
			

			//logger.info("getConnectCount "+objStatistics.getConnectCount());
			//logger.info("getSessionOpenCount "+objStatistics.getSessionOpenCount());
		}
		try {
			transaction = getTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	public Session getCreatedSession() {
		if(session==null){
			logger.error("FATAL THIS SHOULD NOT BE NULL ");
		}
		return session;
	}
	private Session createSession() {
		while(true){
			try {
				Thread.sleep(5);
				Session temo=sessionFactory.openSession();
				if(temo!=null){
					return temo;
				}
			} catch (Exception ex){
				logger.error("FATAL ",ex);
			}

		}

	}

	public Transaction getTransaction() {
		return session.beginTransaction();
	}

	public void endTask() throws Exception {
		try {
			if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
					&& session.getTransaction().getStatus() != TransactionStatus.MARKED_ROLLBACK
					&& session.getTransaction().getStatus() != TransactionStatus.COMMITTED) {
				session.flush();
				transaction.commit();

			}

			if (session.isConnected()) {

				// session.clear();
				session.close();
				session = null;
			}
		} finally {
			if(session!=null){
				logger.error("Clearing DB Session  ");
				session.close();
				session = null;
			}
		}
		



	}

	public void rollbackTransaction() {
		try {
			transaction.rollback();
		} catch (Exception e) {
			System.out.println("exception in transaction");
		}

	}

	/**
	 * Generic function for saving any entity. The provided entity must be a
	 * hibernate mapped java POJO. This method returns the id of the immediately
	 * saved/persisted object.
	 * 
	 * @param entity
	 *            - any Java POJO that has been mapped to your hibernate
	 * @return - id of the saved/persisted object.
	 * @throws Exception
	 */
	protected int saveObject(Object entity) throws Exception {
		return (int) session.save(entity);
	}

	protected void persistObject(Object entity) throws Exception {
		session.persist(entity);
	}

	/**
	 * Function to update any entity. The provided entity must be a hibernate
	 * mapped java POJO. This entity must contains true value for unique
	 * identifier(ID) to update correctly.
	 * 
	 * @param entity
	 *            - any Java POJO that has been mapped to your hibernate
	 * @return - true if update runs correctly, false otherwise.
	 * @throws Exception
	 */
	protected boolean updateObject(Object entity) throws Exception {
		session.update(entity);
		return true;
	}

	/**
	 * Function to delete any persisted entity. The provided entity must be a
	 * hibernate mapped java POJO. This entity must contains true value for
	 * unique identifier(ID) to delete correctly.
	 * 
	 * @param entity
	 *            - any Java POJO that has been mapped to your hibernate
	 * @return - true if deletion runs correctly, false otherwise.
	 * @throws Exception
	 */
	protected boolean deleteObject(Object entity) throws Exception {
		session.delete(entity);
		return true;
	}

	/**
	 * This method get all the entries of the specified class/table.
	 * 
	 * @param className
	 *            - is the Object.class value given to this function e.g
	 *            Entity.class
	 * @return - the list of all the entries of this table. null if exception
	 *         occurs.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected List getAll(Class className) throws Exception {
		return session.createCriteria(className).addOrder(Order.asc(("id"))).list();
	}
	
	@SuppressWarnings("rawtypes")
	protected List getAllByGroup(Class className, String columnName) throws Exception {
		return session.createCriteria(className)
				.setProjection(Projections.groupProperty(columnName)).list();
	}

	/**
	 * This method is used to get row of an entity on specific criteria.
	 * Criteria is based on attribute. You can pass class (Table/POJO) who's
	 * data has to fetch, its attribute name on which you want to search and
	 * value to that attribute which you want to compare. Note that the
	 * attribute selected should be a unique column in table/POJO or it will not
	 * work properly otherwise.
	 * 
	 * @param entity
	 *            - is the Class of any mapped POJO
	 * @param attribute
	 *            - is the attribute on which you want to search any row
	 * @param value
	 *            - is the required value of that attribute who's value you want
	 *            to compare in attribute column.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Object getRowByAttribute(Class entity, String attribute, Object value) throws Exception {
		Criteria criteria = session.createCriteria(entity);
		criteria.add(Restrictions.eq(attribute, value));
		return criteria.uniqueResult();
	}
	
	@SuppressWarnings("rawtypes")
	protected Object getRowByAttributeAndOrder(Class entity, String attribute, Object value, Order order) throws Exception {
		Criteria criteria = session.createCriteria(entity);
		criteria.add(Restrictions.eq(attribute, value));
		criteria.addOrder(order);
		criteria.setMaxResults(1);
		return criteria.uniqueResult();
	}
	
	@SuppressWarnings("rawtypes")
	protected String getColumnByAttribute(Class entity, String attribute, Object value) throws Exception {
		Criteria criteria = session.createCriteria(entity);
		criteria.add(Restrictions.eq(attribute, value));
		criteria.setProjection(Projections.property("assignToken"));		
		return criteria.uniqueResult().toString();
	}
	
	

	/**
	 * This method is used to catch exception of all types, it checks the type
	 * of the exception and return the integer related to that particular
	 * exception. Further logic can be created with time. For the time being
	 * only ConstraintViolationException has been catched and in this case -2 is
	 * returned to tell that violation of constraint has occurred and -1 for all
	 * other exceptions.
	 * 
	 * @param e
	 *            - is the coming parameter of type Exception, which is then
	 *            used to get the real exception type using instanceOf
	 * @param transaction
	 *            - is the transaction instance for the currently opened
	 *            session.
	 * @return - integers, based on specific exception.
	 */
	protected int catchException(Exception e) {
		rollbackTransaction();
		if (e instanceof ConstraintViolationException) {
			String errorMsg = e.getCause().getMessage();
			if (errorMsg.contains("Duplicate entry")) {
				/*
				 * Pattern p = Pattern.compile("'(.*?)'"); Matcher m =
				 * p.matcher(errorMsg); m.find(); String value = m.group(1);
				 * m.find(); String attribute = m.group(1); //return
				 * "Duplicate entry for " + attribute;
				 */ return -2;
			} else if (errorMsg.contains("a foreign key constraint fails")) {
				return -3;
			}
			return -1;
		} else if (e instanceof DataException) {
			return -4;
		}
		return -1;
	}

	protected String catchExceptionWithMessage(Exception e) {
		rollbackTransaction();
		if (e instanceof ConstraintViolationException) {
			String errorMsg = e.getCause().getMessage();
			if (errorMsg.contains("Duplicate entry")) {
				/*
				 * Pattern p = Pattern.compile("'(.*?)'"); Matcher m =
				 * p.matcher(errorMsg); m.find(); String value = m.group(1);
				 * m.find(); String attribute = m.group(1); //return
				 * "Duplicate entry for " + attribute;
				 */
				return "DuplicateEntry";
			} else if (errorMsg.contains("a foreign key constraint fails")) {
				return errorMsg.split("\\(")[1].split(",")[0].split("\\.")[1].replaceAll("`", "");
			}
		} else if (e instanceof DataException) {
			return "DataException";
		}
		return "databaseError";
	}

	/**
	 * This method is used to get any mapped POJO on the basis of its unique
	 * identifier(Id). This will return the unique row of a specific Table/POJO
	 * 
	 * @param objClass
	 *            - Class which you want to get e.g Entity.class
	 * @param id
	 *            - unique identifier to get the object
	 * @return - the POJO's unique object null if wrong id or any other
	 *         exception occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object getObjectById(Class objClass, int id) throws Exception {
		/*
		 * Object obj = session.load(objClass, id); if(obj == null){ return
		 * session.get(objClass, id); }else{ return obj; }
		 */
		return session.get(objClass, id);
	}

	/**
	 * This method returns the list of all the objects on the basis of attribute
	 * name / column of the object/table and given its value to check in that
	 * attribute. It will get all the rows having value int attributeName
	 * 
	 * @param className
	 *            - Table/POJO to get List of
	 * @param attributeName
	 *            - is the specific attribute in which the value has to be
	 *            matched.
	 * @param value
	 *            - is the exact value to search in the above attributeName
	 * @return - list of rows/objects according to criteria
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected List getAllOnCriteria(Class className, String attributeName, Object value) throws Exception {
		return session.createCriteria(className).add(Restrictions.eq(attributeName, value)).list();
	}

	@SuppressWarnings("rawtypes")
	protected List getAllOnCriteria(Class className, String attribute1, String value1, String attribute2, String value2)
			throws Exception {
		
		return session.createCriteria(className).add(Restrictions.and(Restrictions.eq(attribute1, value1), Restrictions.eq(attribute2, value2))).list();
	}
	
	
	@SuppressWarnings("rawtypes")
	protected List getAllOnCriteria(Class className, String attribute1, Object value1, String attribute2, Object value2)
			throws Exception {
		
		return session.createCriteria(className).add(Restrictions.and(Restrictions.eq(attribute1, value1), Restrictions.eq(attribute2, value2))).list();
	}
	
	@SuppressWarnings("rawtypes")
	protected List getAllOnCriteria(Class className, String attribute1, Object value1, String attribute2, Object value2, String attribute3, Object value3)
			throws Exception {
		
		return session.createCriteria(className).add(Restrictions.and(Restrictions.eq(attribute1, value1), Restrictions.eq(attribute2, value2), Restrictions.eq(attribute3, value3))).list();
	}

	protected int getUpdateSQLQueryResult(String sql, List<Object> paramList) {
		Query query = session.createSQLQuery(sql);
		if (paramList != null) {
			for (int i = 0; i < paramList.size(); i++) {
				query.setParameter(i, paramList.get(i));
			}
		}
		return query.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	protected List getSQLQueryResult(String sql, List<Object> paramList, Class dto) {
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(dto));
		;
		if (paramList != null) {
			for (int i = 0; i < paramList.size(); i++) {
				query.setParameter(i, paramList.get(i));
			}
		}
		return query.list();
	}

	public void saveOrUpdateObject(Object entity) {
		session.saveOrUpdate(entity);
	}
}
