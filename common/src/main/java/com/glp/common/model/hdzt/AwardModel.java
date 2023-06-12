/*
 * @(#)AwardModel.java 2020-08-19
 * 
 * 代码生成: award_model 表的数据模型类  AwardModel
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
 * award_model 表的数据模型类  AwardModel
 * </pre>
 * 
 * @版本更新
 * <pre>
 * 修改版本: 1.0.0 / 2020-08-19 / cgc
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */
@SuppressWarnings("serial") 
public class AwardModel implements java.io.Serializable , Cloneable {
	private static final Logger log = LoggerFactory.getLogger(AwardModel.class);
	
	public static String TABLE_NAME = "award_model";
	
	// java 属性字段 和 库表字段对应关系， key-java属性， val-表字段名
	public static Map<String, String> TABLE_FIELDS = new HashMap<String, String>();
	static {
		TABLE_FIELDS.put("taskId", "task_id");
		TABLE_FIELDS.put("packageId", "package_id");
		TABLE_FIELDS.put("ctime", "ctime");
		TABLE_FIELDS.put("extjson", "extjson");
		TABLE_FIELDS.put("remark", "remark");
		TABLE_FIELDS.put("status", "status");
		TABLE_FIELDS.put("consumed", "consumed");
		TABLE_FIELDS.put("dailyLimitGroup", "daily_limit_group");
		TABLE_FIELDS.put("dailyHitLimit", "daily_hit_limit");
		TABLE_FIELDS.put("userHitLimit", "user_hit_limit");
		TABLE_FIELDS.put("packageTotal", "package_total");
		TABLE_FIELDS.put("probability", "probability");
		TABLE_FIELDS.put("position", "position");
		TABLE_FIELDS.put("utime", "utime");
	};	
	
	public static RowMapper<AwardModel> ROW_MAPPER = new RowMapper<AwardModel>() {
		@Override
		public AwardModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			AwardModel entity = new AwardModel();
			entity.setTaskId(rs.getLong("task_id"));
			entity.setPackageId(rs.getLong("package_id"));
			entity.setCtime(rs.getTimestamp("ctime"));
			entity.setExtjson(rs.getString("extjson"));
			entity.setRemark(rs.getString("remark"));
			entity.setStatus(rs.getLong("status"));
			entity.setConsumed(rs.getLong("consumed"));
			entity.setDailyLimitGroup(rs.getString("daily_limit_group"));
			entity.setDailyHitLimit(rs.getLong("daily_hit_limit"));
			entity.setUserHitLimit(rs.getLong("user_hit_limit"));
			entity.setPackageTotal(rs.getLong("package_total"));
			entity.setProbability(rs.getLong("probability"));
			entity.setPosition(rs.getLong("position"));
			entity.setUtime(rs.getTimestamp("utime"));
			return entity;
		}
	};
	
	// 表主键属性集合
	public static final String[] TABLE_PKS = new String[]{ 
		 "task_id",  "package_id" 		
	};	

	private Long taskId;
	private Long packageId;
	private Date ctime;
	private String extjson;
	private String remark;
	private Long status;
	private Long consumed;
	private String dailyLimitGroup;
	private Long dailyHitLimit;
	private Long userHitLimit;
	private Long packageTotal;
	private Long probability;
	private Long position;
	private Date utime;

	/** 无参数构造函数  */
	public AwardModel(){
	}
	
	/** 主键属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardModel(Long taskId, Long packageId){
		this.taskId = taskId;
		this.packageId = packageId;
	}*/
	
	/** 全属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardModel(Long taskId, Long packageId, Date ctime, String extjson, String remark, Long status, Long consumed, String dailyLimitGroup, Long dailyHitLimit, Long userHitLimit, Long packageTotal, Long probability, Long position, Date utime){
		this.taskId = taskId;
		this.packageId = packageId;
		this.ctime = ctime;
		this.extjson = extjson;
		this.remark = remark;
		this.status = status;
		this.consumed = consumed;
		this.dailyLimitGroup = dailyLimitGroup;
		this.dailyHitLimit = dailyHitLimit;
		this.userHitLimit = userHitLimit;
		this.packageTotal = packageTotal;
		this.probability = probability;
		this.position = position;
		this.utime = utime;
	}*/	


	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}

	public Long getTaskId(){
		return taskId;
	}

	public void setPackageId(Long packageId){
		this.packageId = packageId;
	}

	public Long getPackageId(){
		return packageId;
	}

	public void setCtime(Date ctime){
		this.ctime = ctime;
	}

	public Date getCtime(){
		return ctime;
	}

	public void setExtjson(String extjson){
		this.extjson = extjson;
	}

	public String getExtjson(){
		return extjson;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public void setConsumed(Long consumed){
		this.consumed = consumed;
	}

	public Long getConsumed(){
		return consumed;
	}

	public void setDailyLimitGroup(String dailyLimitGroup){
		this.dailyLimitGroup = dailyLimitGroup;
	}

	public String getDailyLimitGroup(){
		return dailyLimitGroup;
	}

	public void setDailyHitLimit(Long dailyHitLimit){
		this.dailyHitLimit = dailyHitLimit;
	}

	public Long getDailyHitLimit(){
		return dailyHitLimit;
	}

	public void setUserHitLimit(Long userHitLimit){
		this.userHitLimit = userHitLimit;
	}

	public Long getUserHitLimit(){
		return userHitLimit;
	}

	public void setPackageTotal(Long packageTotal){
		this.packageTotal = packageTotal;
	}

	public Long getPackageTotal(){
		return packageTotal;
	}

	public void setProbability(Long probability){
		this.probability = probability;
	}

	public Long getProbability(){
		return probability;
	}

	public void setPosition(Long position){
		this.position = position;
	}

	public Long getPosition(){
		return position;
	}

	public void setUtime(Date utime){
		this.utime = utime;
	}

	public Date getUtime(){
		return utime;
	}

	public AwardModel clone() {
		try {
			return (AwardModel) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}	
	
}