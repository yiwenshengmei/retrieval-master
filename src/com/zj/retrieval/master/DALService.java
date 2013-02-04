package com.zj.retrieval.master;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DALService {
	
	private static SessionFactory sessionFactory = null;
	private final static Logger logger = LoggerFactory.getLogger(DALService.class);
	
	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch (Throwable ex) {
			logger.error("Hibernate初始化时发生错误", ex);
		}
	}
	
	private DALService() { }
	
	public static final Object doAction(IDALAction action) throws Exception {
		Transaction tx = null;
		Session sess = null;
		Object ret = null;
		try {
			sess = sessionFactory.openSession();
			tx = sess.beginTransaction();
			ret = action.doAction(sess, tx);
			if (null != tx)
				tx.commit();
			tx = null;
		}
		catch (Throwable ex) {
			logger.error("操作数据库时发生错误", ex);
			if (null != tx) {
				try {
					tx.rollback();
				}
				catch (Throwable ignore) {
					logger.warn("Error while rolling back tranaction", ignore);
				}
			}
			throw new Exception(ex);
		}
		finally {
			if (null != sess) {
				try {
					sess.close();
				}
				catch (Throwable ignore) {
					logger.warn("Error while closing data session", ignore);
				}
			}
		}
		return ret;
	}
}
