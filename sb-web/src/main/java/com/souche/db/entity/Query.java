package com.souche.db.entity;

import com.souche.db.constant.Constant;
import lombok.Data;



/**
 * 翻页查询 参数		
 * 
 */	
@Data
public class Query {

	/**
	 * 跳转
	 */
	private int pageNo = 1;

	/**
	 * 每页显示记录数
	 */
	private int pageSize = 10;

	/**
	 * 是否可分页
	 */
	private String pageable = "true";

	/**
	 * 起始行
	 */
	private int rowIndex;

	/**
	 * 查询条件
	 */
	private Object q;
	
	public Query() {
		this.pageable = "false";
	}

	public Query(Object q) {
		this.q = q;
		this.pageable = "false";
	}

	public Query(int pageNo) {
		this(pageNo, Constant.DEFAULT_PAGE_SIZE);
	}

	public Query(int pageNo, Object q) {
		this(pageNo, Constant.DEFAULT_PAGE_SIZE, q);
	}

	public Query(int pageNo, int pageSize) {
		this.pageNo = pageNo <= 0 ? 1 : pageNo;
		this.pageSize = pageSize < 0 ? 0 : pageSize;
		this.rowIndex = (this.pageNo - 1) * this.pageSize;
	}

	public Query(int pageNo, int pageSize, Object q) {
		this.pageNo = pageNo <= 0 ? 1 : pageNo;
		this.pageSize = pageSize < 0 ? 0 : pageSize;
		this.rowIndex = (this.pageNo - 1) * this.pageSize;
		this.q = q;
	}
}
