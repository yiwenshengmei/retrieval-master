package com.zj.retrieval.master;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface IDALAction {
	Object doAction(Session sess, Transaction tx) throws Exception;
}
