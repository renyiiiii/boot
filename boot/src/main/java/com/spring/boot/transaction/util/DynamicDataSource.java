package com.spring.boot.transaction.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{

	    @Override
	    protected Object determineCurrentLookupKey() {
	        // TODO Auto-generated method stub
	        return DynamicDataSourceHolder.getDataSouce();
	    }

	}

	
