package com.tensquare.article.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.StringJoiner;

/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_channel")
public class Channel implements Serializable{

	@Id
	private String id;//ID
	private String name;//频道名称
	private String state;//状态
    @Column(name = "create_date")
	private Date createDate;//创建时间
	@Column(name = "str_date")
	private String strDate;
	@Column(name = "is_hot")
	private String isHot;//是否热门,0-热门,1-非热门
	@Column(name = "thumb_up")
	private String thumbUp;//点赞
    private BigDecimal times;


	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public String getThumbUp() {
		return thumbUp;
	}

	public void setThumbUp(String thumbUp) {
		this.thumbUp = thumbUp;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getTimes() {
		return times;
	}

	public void setTimes(BigDecimal times) {
		this.times = times;
	}

	public Channel() {
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Channel.class.getSimpleName() + "[", "]")
				.add("id='" + id + "'")
				.add("name='" + name + "'")
				.add("state='" + state + "'")
				.add("createDate=" + createDate)
				.add("strDate='" + strDate + "'")
				.add("isHot='" + isHot + "'")
				.add("thumbUp='" + thumbUp + "'")
				.add("times=" + times)
				.toString();
	}
}
