package org.javlo.service.event;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.javlo.component.web2.EventRegistration;
import org.javlo.context.ContentContext;
import org.javlo.service.ContentService;
import org.javlo.user.AdminUserFactory;
import org.javlo.user.IUserFactory;
import org.javlo.user.User;

public class Event implements Serializable {
	
	public static final Event NO_EVENT = new Event(null,null,null,"no event",null);

	private static final long serialVersionUID = 1L;

	private String id;
	private URL url;
	private Date start;
	private Date end;
	private String summary;
	private String location;
	private String category;
	private String status;
	private String description;
	private int sequence;
	private String user;

	public Event(String id, Date start, Date end, String summary, String description) {		
		this.id = id;
		this.start = start;
		this.end = end;
		this.summary = summary;
		this.description = description;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String getProdID() {
		return "-//ImmanenceSPRL//NONSGML Javlo//EN";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public List<User> getParticipants(ContentContext ctx) throws Exception {
		List<User> outUsers = new LinkedList<User>();
		ContentService content = ContentService.getInstance(ctx.getRequest());
		EventRegistration comp = (EventRegistration)content.getComponent(ctx, getId());		
		return comp.getParticipants(ctx);
	}

}