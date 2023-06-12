/*
 * @(#)AwardRecord.java 2020-08-21
 * 
 * 代码生成: award_record 表的数据模型类  AwardRecord
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
 * award_record 表的数据模型类  AwardRecord
 * </pre>
 * 
 * @版本更新
 * <pre>
 * 修改版本: 1.0.0 / 2020-08-21 / cgc
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */
@SuppressWarnings("serial") 
public class AwardRecord implements java.io.Serializable , Cloneable {
	private static final Logger log = LoggerFactory.getLogger(AwardRecord.class);
	
	public static String TABLE_NAME = "award_record";
	
	// java 属性字段 和 库表字段对应关系， key-java属性， val-表字段名
	public static Map<String, String> TABLE_FIELDS = new HashMap<String, String>();
	static {
		TABLE_FIELDS.put("seq", "seq");
		TABLE_FIELDS.put("recordId", "record_id");
		TABLE_FIELDS.put("ctime", "ctime");
		TABLE_FIELDS.put("uid", "uid");
		TABLE_FIELDS.put("utime", "utime");
		TABLE_FIELDS.put("taskId", "task_id");
		TABLE_FIELDS.put("packageId", "package_id");
		TABLE_FIELDS.put("packageName", "package_name");
		TABLE_FIELDS.put("packageNum", "package_num");
		TABLE_FIELDS.put("status", "status");
		TABLE_FIELDS.put("platform", "platform");
		TABLE_FIELDS.put("ip", "ip");
		TABLE_FIELDS.put("mac", "mac");
		TABLE_FIELDS.put("address", "address");
		TABLE_FIELDS.put("extint", "extint");
		TABLE_FIELDS.put("extjson", "extjson");
		TABLE_FIELDS.put("remark", "remark");
	};	
	
	public static RowMapper<AwardRecord> ROW_MAPPER = new RowMapper<AwardRecord>() {
		@Override
		public AwardRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			AwardRecord entity = new AwardRecord();
			entity.setSeq(rs.getString("seq"));
			entity.setRecordId(rs.getLong("record_id"));
			entity.setCtime(rs.getTimestamp("ctime"));
			entity.setUid(rs.getLong("uid"));
			entity.setUtime(rs.getTimestamp("utime"));
			entity.setTaskId(rs.getLong("task_id"));
			entity.setPackageId(rs.getLong("package_id"));
			entity.setPackageName(rs.getString("package_name"));
			entity.setPackageNum(rs.getLong("package_num"));
			entity.setStatus(rs.getLong("status"));
			entity.setPlatform(rs.getLong("platform"));
			entity.setIp(rs.getString("ip"));
			entity.setMac(rs.getString("mac"));
			entity.setAddress(rs.getString("address"));
			entity.setExtint(rs.getLong("extint"));
			entity.setExtjson(rs.getString("extjson"));
			entity.setRemark(rs.getString("remark"));
			return entity;
		}
	};
	
	// 表主键属性集合
	public static final String[] TABLE_PKS = new String[]{ 
		 "record_id" 		
	};	

	private String seq;
	private Long recordId;
	private Date ctime;
	private Long uid;
	private Date utime;
	private Long taskId;
	private Long packageId;
	private String packageName;
	private Long packageNum;
	private Long status;
	private Long platform;
	private String ip;
	private String mac;
	private String address;
	private Long extint;
	private String extjson;
	private String remark;

	/** 无参数构造函数  */
	public AwardRecord(){
	}
	
	/** 主键属性构造函数 */
	public AwardRecord(Long recordId){
		this.recordId = recordId;
	}	
	
	/** 全属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardRecord(String seq, Long recordId, Date ctime, Long uid, Date utime, Long taskId, Long packageId, String packageName, Long packageNum, Long status, Long platform, String ip, String mac, String address, Long extint, String extjson, String remark){
		this.seq = seq;
		this.recordId = recordId;
		this.ctime = ctime;
		this.uid = uid;
		this.utime = utime;
		this.taskId = taskId;
		this.packageId = packageId;
		this.packageName = packageName;
		this.packageNum = packageNum;
		this.status = status;
		this.platform = platform;
		this.ip = ip;
		this.mac = mac;
		this.address = address;
		this.extint = extint;
		this.extjson = extjson;
		this.remark = remark;
	}*/	


	public void setSeq(String seq){
		this.seq = seq;
	}

	public String getSeq(){
		return seq;
	}

	public void setRecordId(Long recordId){
		this.recordId = recordId;
	}

	public Long getRecordId(){
		return recordId;
	}

	public void setCtime(Date ctime){
		this.ctime = ctime;
	}

	public Date getCtime(){
		return ctime;
	}

	public void setUid(Long uid){
		this.uid = uid;
	}

	public Long getUid(){
		return uid;
	}

	public void setUtime(Date utime){
		this.utime = utime;
	}

	public Date getUtime(){
		return utime;
	}

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

	public void setPackageName(String packageName){
		this.packageName = packageName;
	}

	public String getPackageName(){
		return packageName;
	}

	public void setPackageNum(Long packageNum){
		this.packageNum = packageNum;
	}

	public Long getPackageNum(){
		return packageNum;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public void setPlatform(Long platform){
		this.platform = platform;
	}

	public Long getPlatform(){
		return platform;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getIp(){
		return ip;
	}

	public void setMac(String mac){
		this.mac = mac;
	}

	public String getMac(){
		return mac;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setExtint(Long extint){
		this.extint = extint;
	}

	public Long getExtint(){
		return extint;
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

	public AwardRecord clone() {
		try {
			return (AwardRecord) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}	

}