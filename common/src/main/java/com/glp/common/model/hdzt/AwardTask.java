/*
 * @(#)AwardTask.java 2020-06-10
 * 
 * 代码生成: award_task 表的数据模型类  AwardTask
 */
 
package com.glp.common.model.hdzt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


/**
 * @功能说明
 * <pre>
 * award_task 表的数据模型类  AwardTask
 * </pre>
 * 
 * @版本更新
 * <pre>
 * 修改版本: 1.0.0 / 2020-06-10 / cgc
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */
@SuppressWarnings("serial") 
public class AwardTask implements java.io.Serializable , Cloneable {
	private static final Logger log = LoggerFactory.getLogger(AwardTask.class);
	
	public static String TABLE_NAME = "award_task";
	
	// java 属性字段 和 库表字段对应关系， key-java属性， val-表字段名
	public static Map<String, String> TABLE_FIELDS = new HashMap<String, String>();
	static {
		TABLE_FIELDS.put("taskId", "task_id");
		TABLE_FIELDS.put("actId", "act_id");
		TABLE_FIELDS.put("opentime", "opentime");
		TABLE_FIELDS.put("taskName", "task_name");
		TABLE_FIELDS.put("endtime", "endtime");
		TABLE_FIELDS.put("status", "status");
		TABLE_FIELDS.put("model", "model");
		TABLE_FIELDS.put("remark", "remark");
		TABLE_FIELDS.put("extjson", "extjson");
		TABLE_FIELDS.put("ctime", "ctime");
		TABLE_FIELDS.put("utime", "utime");
	};	
	
	public static RowMapper<AwardTask> ROW_MAPPER = new RowMapper<AwardTask>() {
		@Override
		public AwardTask mapRow(ResultSet rs, int rowNum) throws SQLException {
			AwardTask entity = new AwardTask();
			entity.setTaskId(rs.getLong("task_id"));
			entity.setActId(rs.getLong("act_id"));
			entity.setOpentime(rs.getTimestamp("opentime"));
			entity.setTaskName(rs.getString("task_name"));
			entity.setEndtime(rs.getTimestamp("endtime"));
			entity.setStatus(rs.getLong("status"));
			entity.setModel(rs.getLong("model"));
			entity.setRemark(rs.getString("remark"));
			entity.setExtjson(rs.getString("extjson"));
			entity.setCtime(rs.getTimestamp("ctime"));
			entity.setUtime(rs.getTimestamp("utime"));
			return entity;
		}
	};
	
	// 表主键属性集合
	public static final String[] TABLE_PKS = new String[]{ 
		 "task_id" 		
	};	

	private Long taskId;
	private Long actId;
	private Date opentime;
	private String taskName;
	private Date endtime;
	private Long status;
	private Long model;
	private String remark;
	private String extjson;
	private Date ctime;
	private Date utime;

	/** 无参数构造函数  */
	public AwardTask(){
	}
	
	/** 主键属性构造函数 */
	public AwardTask(Long taskId){
		this.taskId = taskId;
	}	
	
	/** 全属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardTask(Long taskId, Date opentime, String taskName, Date endtime, Long status, Long model, String remark, String extjson, Date ctime, Date utime){
		this.taskId = taskId;
		this.opentime = opentime;
		this.taskName = taskName;
		this.endtime = endtime;
		this.status = status;
		this.model = model;
		this.remark = remark;
		this.extjson = extjson;
		this.ctime = ctime;
		this.utime = utime;
	}*/	


	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}

	public Long getTaskId(){
		return taskId;
	}
	public Long getActId() {
		return actId;
	}
	public void setActId(Long actId) {
		this.actId = actId;
	}

	public void setOpentime(Date opentime){
		this.opentime = opentime;
	}

	public Date getOpentime(){
		return opentime;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskName(){
		return taskName;
	}

	public void setEndtime(Date endtime){
		this.endtime = endtime;
	}

	public Date getEndtime(){
		return endtime;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public void setModel(Long model){
		this.model = model;
	}

	public Long getModel(){
		return model;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setExtjson(String extjson){
		this.extjson = extjson;
	}

	public String getExtjson(){
		return extjson;
	}

	public void setCtime(Date ctime){
		this.ctime = ctime;
	}

	public Date getCtime(){
		return ctime;
	}

	public void setUtime(Date utime){
		this.utime = utime;
	}

	public Date getUtime(){
		return utime;
	}

	public AwardTask clone() {
		try {
			return (AwardTask) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}	
}