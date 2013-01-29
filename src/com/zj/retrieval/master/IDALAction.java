package com.zj.retrieval.master;

import org.hibernate.Session;

public interface IDALAction {
	Object doAction(Session sess) throws Exception;
}
