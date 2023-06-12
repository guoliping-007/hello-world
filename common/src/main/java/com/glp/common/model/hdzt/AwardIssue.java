/*
 * @(#)AwardIssue.java 2020-08-21
 * 
 * 代码生成: award_issue 表的数据模型类  AwardIssue
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
 * award_issue 表的数据模型类  AwardIssue
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
public class AwardIssue implements java.io.Serializable , Cloneable {
	private static final Logger log = LoggerFactory.getLogger(AwardIssue.class);
	
	public static String TABLE_NAME = "award_issue";
	
	// java 属性字段 和 库表字段对应关系， key-java属性， val-表字段名
	public static Map<String, String> TABLE_FIELDS = new HashMap<String, String>();
	static {
		TABLE_FIELDS.put("issueId", "issue_id");
		TABLE_FIELDS.put("ctime", "ctime");
		TABLE_FIELDS.put("recordId", "record_id");
		TABLE_FIELDS.put("uid", "uid");
		TABLE_FIELDS.put("giftType", "gift_type");
		TABLE_FIELDS.put("busiId", "busi_id");
		TABLE_FIELDS.put("packageId", "package_id");
		TABLE_FIELDS.put("giftCode", "gift_code");
		TABLE_FIELDS.put("giftName", "gift_name");
		TABLE_FIELDS.put("giftNum", "gift_num");
		TABLE_FIELDS.put("status", "status");
		TABLE_FIELDS.put("issueConfig", "issue_config");
		TABLE_FIELDS.put("issueSeq", "issue_seq");
		TABLE_FIELDS.put("taskId", "task_id");
		TABLE_FIELDS.put("receipt", "receipt");
		TABLE_FIELDS.put("exint", "exint");
		TABLE_FIELDS.put("extjson", "extjson");
		TABLE_FIELDS.put("itemId", "item_id");
		TABLE_FIELDS.put("packageNum", "package_num");
		TABLE_FIELDS.put("utime", "utime");
		TABLE_FIELDS.put("remark", "remark");
	};	
	
	public static RowMapper<AwardIssue> ROW_MAPPER = new RowMapper<AwardIssue>() {
		@Override
		public AwardIssue mapRow(ResultSet rs, int rowNum) throws SQLException {
			AwardIssue entity = new AwardIssue();
			entity.setIssueId(rs.getLong("issue_id"));
			entity.setCtime(rs.getTimestamp("ctime"));
			entity.setRecordId(rs.getLong("record_id"));
			entity.setUid(rs.getLong("uid"));
			entity.setGiftType(rs.getLong("gift_type"));
			entity.setBusiId(rs.getLong("busi_id"));
			entity.setPackageId(rs.getLong("package_id"));
			entity.setGiftCode(rs.getString("gift_code"));
			entity.setGiftName(rs.getString("gift_name"));
			entity.setGiftNum(rs.getLong("gift_num"));
			entity.setStatus(rs.getLong("status"));
			entity.setIssueConfig(rs.getString("issue_config"));
			entity.setIssueSeq(rs.getString("issue_seq"));
			entity.setTaskId(rs.getLong("task_id"));
			entity.setReceipt(rs.getString("receipt"));
			entity.setExint(rs.getLong("exint"));
			entity.setExtjson(rs.getString("extjson"));
			entity.setItemId(rs.getLong("item_id"));
			entity.setPackageNum(rs.getLong("package_num"));
			entity.setUtime(rs.getTimestamp("utime"));
			entity.setRemark(rs.getString("remark"));
			return entity;
		}
	};
	
	// 表主键属性集合
	public static final String[] TABLE_PKS = new String[]{ 
		 "issue_id" 		
	};	

	private Long issueId;
	private Date ctime;
	private Long recordId;
	private Long uid;
	private Long giftType;
	private Long busiId;
	private Long packageId;
	private String giftCode;
	private String giftName;
	private Long giftNum;
	private Long status;
	private String issueConfig;
	private String issueSeq;
	private Long taskId;
	private String receipt;
	private Long exint;
	private String extjson;
	private Long itemId;
	private Long packageNum;
	private Date utime;
	private String remark;

	/** 无参数构造函数  */
	public AwardIssue(){
	}
	
	/** 主键属性构造函数 */
	public AwardIssue(Long issueId){
		this.issueId = issueId;
	}	
	
	/** 全属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardIssue(Long issueId, Date ctime, Long recordId, Long uid, Long giftType, Long busiId, Long packageId, String giftCode, String giftName, Long giftNum, Long status, String issueConfig, String issueSeq, Long taskId, String receipt, Long exint, String extjson, Long itemId, Long packageNum, Date utime, String remark){
		this.issueId = issueId;
		this.ctime = ctime;
		this.recordId = recordId;
		this.uid = uid;
		this.giftType = giftType;
		this.busiId = busiId;
		this.packageId = packageId;
		this.giftCode = giftCode;
		this.giftName = giftName;
		this.giftNum = giftNum;
		this.status = status;
		this.issueConfig = issueConfig;
		this.issueSeq = issueSeq;
		this.taskId = taskId;
		this.receipt = receipt;
		this.exint = exint;
		this.extjson = extjson;
		this.itemId = itemId;
		this.packageNum = packageNum;
		this.utime = utime;
		this.remark = remark;
	}*/	


	public void setIssueId(Long issueId){
		this.issueId = issueId;
	}

	public Long getIssueId(){
		return issueId;
	}

	public void setCtime(Date ctime){
		this.ctime = ctime;
	}

	public Date getCtime(){
		return ctime;
	}

	public void setRecordId(Long recordId){
		this.recordId = recordId;
	}

	public Long getRecordId(){
		return recordId;
	}

	public void setUid(Long uid){
		this.uid = uid;
	}

	public Long getUid(){
		return uid;
	}

	public void setGiftType(Long giftType){
		this.giftType = giftType;
	}

	public Long getGiftType(){
		return giftType;
	}

	public void setBusiId(Long busiId){
		this.busiId = busiId;
	}

	public Long getBusiId(){
		return busiId;
	}

	public void setPackageId(Long packageId){
		this.packageId = packageId;
	}

	public Long getPackageId(){
		return packageId;
	}

	public void setGiftCode(String giftCode){
		this.giftCode = giftCode;
	}

	public String getGiftCode(){
		return giftCode;
	}

	public void setGiftName(String giftName){
		this.giftName = giftName;
	}

	public String getGiftName(){
		return giftName;
	}

	public void setGiftNum(Long giftNum){
		this.giftNum = giftNum;
	}

	public Long getGiftNum(){
		return giftNum;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public void setIssueConfig(String issueConfig){
		this.issueConfig = issueConfig;
	}

	public String getIssueConfig(){
		return issueConfig;
	}

	public void setIssueSeq(String issueSeq){
		this.issueSeq = issueSeq;
	}

	public String getIssueSeq(){
		return issueSeq;
	}

	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}

	public Long getTaskId(){
		return taskId;
	}

	public void setReceipt(String receipt){
		this.receipt = receipt;
	}

	public String getReceipt(){
		return receipt;
	}

	public void setExint(Long exint){
		this.exint = exint;
	}

	public Long getExint(){
		return exint;
	}

	public void setExtjson(String extjson){
		this.extjson = extjson;
	}

	public String getExtjson(){
		return extjson;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	public Long getItemId(){
		return itemId;
	}

	public void setPackageNum(Long packageNum){
		this.packageNum = packageNum;
	}

	public Long getPackageNum(){
		return packageNum;
	}

	public void setUtime(Date utime){
		this.utime = utime;
	}

	public Date getUtime(){
		return utime;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public AwardIssue clone() {
		try {
			return (AwardIssue) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}	
}