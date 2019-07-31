package com.pmark.ticketingtool.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.pmark.ticketingtool.model.abstractmodel.JSONBuilder;
import lombok.Data;
import org.json.JSONObject;

@Entity
@Table(name="STATUS")
@NamedQuery(name="Status.findAll", query="SELECT c FROM Status c")
@Data
public class Status extends JSONBuilder {
	
	
	public static final int OVERDUE = -2;
	public static final int FAILED = -1;
	public static final int WAITING = 0;
	public static final int APPROVED = 1;
	public static final int IN_PROGRESS = 2;
	public static final int ON_HOLD = 3;
	public static final int DONE = 100;
	public static final int NOT_APPROVED = 11;
	public static final int APPROVED_CHANGE = 12;
	public static final int DECLINED = 13;
	public static final int ON_REVISION = 14;


	@Id
	@Column(name="CODE")
	private int id;
	
	
	@Column(name="NAME")
	private String name;
	
	
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.id);
		jo.put("name", this.name);
		
		return jo;	
		
	}
	

}
