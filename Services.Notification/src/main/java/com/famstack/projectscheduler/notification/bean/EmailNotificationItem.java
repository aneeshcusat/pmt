package com.famstack.projectscheduler.notification.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.famstack.email.contants.Templates;

public class EmailNotificationItem extends NotificationItem
{

    private Set<String> toList;
    
    private Set<String> ccList;

    private Map<String, Object> data;

    private Templates templates;

    private final boolean emailEnabled = true;

    public EmailNotificationItem()
    {
        setCreatedTme(new Date());
    }

    public Set<String> getToList()
    {
        if (toList == null) {
            toList = new HashSet<>();
        }
        return toList;
    }

    public void setToList(Set<String> toList)
    {
        this.toList = toList;
    }

    public Map<String, Object> getData()
    {
        if (data == null) {
            data = new HashMap<>();
        }
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }

    public boolean isEmailEnabled()
    {
        return emailEnabled;
    }

    public Templates getTemplates()
    {
        return templates;
    }

    public void setTemplates(Templates templates)
    {
        this.templates = templates;
    }

	public Set<String> getCcList() {
	  if (ccList == null) {
		  ccList = new HashSet<>();
      }
      return ccList;
	}

	public void setCcList(Set<String> ccList) {
		this.ccList = ccList;
	}

}
