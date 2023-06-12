/*
 * @(#)AwardPackage.java 2020-07-01
 * 
 * 代码生成: award_package 表的数据模型类  AwardPackage
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
 * award_package 表的数据模型类  AwardPackage
 * </pre>
 * 
 * @版本更新
 * <pre>
 * 修改版本: 1.0.0 / 2020-07-01 / cgc
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */
@SuppressWarnings("serial") 
public class AwardPackage implements java.io.Serializable , Cloneable {
	private static final Logger log = LoggerFactory.getLogger(AwardPackage.class);
	
	public static String TABLE_NAME = "award_package";
	
	// java 属性字段 和 库表字段对应关系， key-java属性， val-表字段名
	public static Map<String, String> TABLE_FIELDS = new HashMap<String, String>();
	static {
		TABLE_FIELDS.put("packageId", "package_id");
		TABLE_FIELDS.put("packageType", "package_type");
		TABLE_FIELDS.put("packageName", "package_name");
		TABLE_FIELDS.put("status", "status");
		TABLE_FIELDS.put("packageImage", "package_image");
		TABLE_FIELDS.put("choiceImage", "choice_image");
		TABLE_FIELDS.put("mouseoverTips", "mouseover_tips");
		TABLE_FIELDS.put("skipUrl", "skip_url");
		TABLE_FIELDS.put("remark", "remark");
		TABLE_FIELDS.put("extjson", "extjson");
		TABLE_FIELDS.put("ctime", "ctime");
		TABLE_FIELDS.put("utime", "utime");
	};	
	
	public static RowMapper<AwardPackage> ROW_MAPPER = new RowMapper<AwardPackage>() {
		@Override
		public AwardPackage mapRow(ResultSet rs, int rowNum) throws SQLException {
			AwardPackage entity = new AwardPackage();
			entity.setPackageId(rs.getLong("package_id"));
			entity.setPackageType(rs.getLong("package_type"));
			entity.setPackageName(rs.getString("package_name"));
			entity.setStatus(rs.getLong("status"));
			entity.setPackageImage(rs.getString("package_image"));
			entity.setChoiceImage(rs.getString("choice_image"));
			entity.setMouseoverTips(rs.getString("mouseover_tips"));
			entity.setSkipUrl(rs.getString("skip_url"));
			entity.setRemark(rs.getString("remark"));
			entity.setExtjson(rs.getString("extjson"));
			entity.setCtime(rs.getTimestamp("ctime"));
			entity.setUtime(rs.getTimestamp("utime"));
			return entity;
		}
	};
	
	// 表主键属性集合
	public static final String[] TABLE_PKS = new String[]{ 
		 "package_id" 		
	};	

	private Long packageId;
	private Long packageType;
	private String packageName;
	private Long status;
	private String packageImage;
	private String choiceImage;
	private String mouseoverTips;
	private String skipUrl;
	private String remark;
	private String extjson;
	private Date ctime;
	private Date utime;

	/** 无参数构造函数  */
	public AwardPackage(){
	}
	
	/** 主键属性构造函数 */
	public AwardPackage(Long packageId){
		this.packageId = packageId;
	}	
	
	/** 全属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardPackage(Long packageId, Long packageType, String packageName, Long status, String packageImage, String choiceImage, String mouseoverTips, String skipUrl, String remark, String extjson, Date ctime, Date utime){
		this.packageId = packageId;
		this.packageType = packageType;
		this.packageName = packageName;
		this.status = status;
		this.packageImage = packageImage;
		this.choiceImage = choiceImage;
		this.mouseoverTips = mouseoverTips;
		this.skipUrl = skipUrl;
		this.remark = remark;
		this.extjson = extjson;
		this.ctime = ctime;
		this.utime = utime;
	}*/	


	public void setPackageId(Long packageId){
		this.packageId = packageId;
	}

	public Long getPackageId(){
		return packageId;
	}

	public void setPackageType(Long packageType){
		this.packageType = packageType;
	}

	public Long getPackageType(){
		return packageType;
	}

	public void setPackageName(String packageName){
		this.packageName = packageName;
	}

	public String getPackageName(){
		return packageName;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public void setPackageImage(String packageImage){
		this.packageImage = packageImage;
	}

	public String getPackageImage(){
		return packageImage;
	}

	public void setChoiceImage(String choiceImage){
		this.choiceImage = choiceImage;
	}

	public String getChoiceImage(){
		return choiceImage;
	}

	public void setMouseoverTips(String mouseoverTips){
		this.mouseoverTips = mouseoverTips;
	}

	public String getMouseoverTips(){
		return mouseoverTips;
	}

	public void setSkipUrl(String skipUrl){
		this.skipUrl = skipUrl;
	}

	public String getSkipUrl(){
		return skipUrl;
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

	public AwardPackage clone() {
		try {
			return (AwardPackage) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}	
	
}