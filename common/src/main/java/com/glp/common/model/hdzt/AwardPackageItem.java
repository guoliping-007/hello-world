/*
 * @(#)AwardPackageItem.java 2020-07-01
 * 
 * 代码生成: award_package_item 表的数据模型类  AwardPackageItem
 */
 
package com.glp.common.model.hdzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @功能说明
 * <pre>
 * award_package_item 表的数据模型类  AwardPackageItem
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
public class AwardPackageItem implements java.io.Serializable , Cloneable {
	private static final Logger log = LoggerFactory.getLogger(AwardPackageItem.class);
	
	public static String TABLE_NAME = "award_package_item";
	
	// java 属性字段 和 库表字段对应关系， key-java属性， val-表字段名
	public static Map<String, String> TABLE_FIELDS = new HashMap<String, String>();
	static {
		TABLE_FIELDS.put("itemId", "item_id");
		TABLE_FIELDS.put("busiId", "busi_id");
		TABLE_FIELDS.put("packageId", "package_id");
		TABLE_FIELDS.put("giftCode", "gift_code");
		TABLE_FIELDS.put("giftName", "gift_name");
		TABLE_FIELDS.put("giftNum", "gift_num");
		TABLE_FIELDS.put("giftImage", "gift_image");
		TABLE_FIELDS.put("issueConfig", "issue_config");
		TABLE_FIELDS.put("position", "position");
		TABLE_FIELDS.put("choiceImage", "choice_image");
		TABLE_FIELDS.put("mouseoverTips", "mouseover_tips");
		TABLE_FIELDS.put("skipUrl", "skip_url");
		TABLE_FIELDS.put("remark", "remark");
		TABLE_FIELDS.put("extjson", "extjson");
		TABLE_FIELDS.put("ctime", "ctime");
		TABLE_FIELDS.put("giftType", "gift_type");
		TABLE_FIELDS.put("status", "status");
		TABLE_FIELDS.put("issueType", "issue_type");
		TABLE_FIELDS.put("utime", "utime");
	};	
	
	public static RowMapper<AwardPackageItem> ROW_MAPPER = new RowMapper<AwardPackageItem>() {
		@Override
		public AwardPackageItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			AwardPackageItem entity = new AwardPackageItem();
			entity.setItemId(rs.getLong("item_id"));
			entity.setBusiId(rs.getLong("busi_id"));
			entity.setPackageId(rs.getLong("package_id"));
			entity.setGiftCode(rs.getString("gift_code"));
			entity.setGiftName(rs.getString("gift_name"));
			entity.setGiftNum(rs.getLong("gift_num"));
			entity.setGiftImage(rs.getString("gift_image"));
			entity.setIssueConfig(rs.getString("issue_config"));
			entity.setPosition(rs.getLong("position"));
			entity.setChoiceImage(rs.getString("choice_image"));
			entity.setMouseoverTips(rs.getString("mouseover_tips"));
			entity.setSkipUrl(rs.getString("skip_url"));
			entity.setRemark(rs.getString("remark"));
			entity.setExtjson(rs.getString("extjson"));
			entity.setCtime(rs.getTimestamp("ctime"));
			entity.setGiftType(rs.getLong("gift_type"));
			entity.setStatus(rs.getLong("status"));
			entity.setIssueType(rs.getLong("issue_type"));
			entity.setUtime(rs.getTimestamp("utime"));
			return entity;
		}
	};
	
	// 表主键属性集合
	public static final String[] TABLE_PKS = new String[]{ 
		 "package_id", "item_id"
	};	

	private Long itemId;
	private Long busiId;
	private Long packageId;
	private String giftCode;
	private String giftName;
	private Long giftNum;
	private String giftImage;
	private String issueConfig;
	private Long position;
	private String choiceImage;
	private String mouseoverTips;
	private String skipUrl;
	private String remark;
	private String extjson;
	private Date ctime;
	private Long giftType;
	private Long status;
	private Long issueType;
	private Date utime;

	/** 无参数构造函数  */
	public AwardPackageItem(){
	}
	
	/** 构造函数 */
	public AwardPackageItem(Long packageId, Long itemId){
        this.packageId = packageId;
		this.itemId = itemId;
	}	
	
	/** 全属性构造函数 - 自动生成参数顺序可能会变而函数签名不变，使用时可能参数错乱，若确定不再重新生成，可去掉注释！ */
	/*public AwardPackageItem(Long itemId, Long busiId, Long packageId, String giftCode, String giftName, Long giftNum, String giftImage, String issueConfig, Long position, String choiceImage, String mouseoverTips, String skipUrl, String remark, String extjson, Date ctime, Long giftType, Long status, Long issueType, Date utime){
		this.itemId = itemId;
		this.busiId = busiId;
		this.packageId = packageId;
		this.giftCode = giftCode;
		this.giftName = giftName;
		this.giftNum = giftNum;
		this.giftImage = giftImage;
		this.issueConfig = issueConfig;
		this.position = position;
		this.choiceImage = choiceImage;
		this.mouseoverTips = mouseoverTips;
		this.skipUrl = skipUrl;
		this.remark = remark;
		this.extjson = extjson;
		this.ctime = ctime;
		this.giftType = giftType;
		this.status = status;
		this.issueType = issueType;
		this.utime = utime;
	}*/	


	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	public Long getItemId(){
		return itemId;
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

	public void setGiftImage(String giftImage){
		this.giftImage = giftImage;
	}

	public String getGiftImage(){
		return giftImage;
	}

	public void setIssueConfig(String issueConfig){
		this.issueConfig = issueConfig;
	}

	public String getIssueConfig(){
		return issueConfig;
	}

	public void setPosition(Long position){
		this.position = position;
	}

	public Long getPosition(){
		return position;
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

	public void setGiftType(Long giftType){
		this.giftType = giftType;
	}

	public Long getGiftType(){
		return giftType;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public void setIssueType(Long issueType){
		this.issueType = issueType;
	}

	public Long getIssueType(){
		return issueType;
	}

	public void setUtime(Date utime){
		this.utime = utime;
	}

	public Date getUtime(){
		return utime;
	}

	public AwardPackageItem clone() {
		try {
			return (AwardPackageItem) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}	
	
}