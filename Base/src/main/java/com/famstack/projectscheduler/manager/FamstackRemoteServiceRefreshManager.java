package com.famstack.projectscheduler.manager;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.datatransferobject.RemoteItemRefreshItem;

@Component
public class FamstackRemoteServiceRefreshManager extends BaseFamstackManager
{
    public Map<String, Map<String, RemoteItemRefreshItem>> getAllRemoteItemMap()
    {
    	List<RemoteItemRefreshItem> remoteItemRefreshItems = (List<RemoteItemRefreshItem>) famstackDataAccessObjectManager.getAllGroupItems("RemoteItemRefreshItem");
    	Map<String, Map<String, RemoteItemRefreshItem>> remoteRefreshItemsServiceMap = new HashMap<>();
    	if (remoteItemRefreshItems != null) {
    		for(RemoteItemRefreshItem remoteItemRefreshItem  : remoteItemRefreshItems) {
    			Map<String, RemoteItemRefreshItem> remoteRefreshItemsMap = remoteRefreshItemsServiceMap.get(remoteItemRefreshItem.getServiceName());
    			if (remoteRefreshItemsMap == null) {
    				remoteRefreshItemsMap = new HashMap<>();
    				remoteRefreshItemsServiceMap.put(remoteItemRefreshItem.getServiceName(), remoteRefreshItemsMap);
    			}
    			remoteRefreshItemsMap.put(remoteItemRefreshItem.getInstanceIdentifier(), remoteItemRefreshItem);
    		}
    	}
    	return remoteRefreshItemsServiceMap;
    }
    
    public void createOrUpdateRemoteRefreshItem(String instanceIdentifier, String serviceName, Boolean selfUpdate)
    {
    	  try {
		    	Map<String, Map<String, RemoteItemRefreshItem>> remoteRefreshItemsMap = getAllRemoteItemMap();
		    	Map<String, RemoteItemRefreshItem> remoteItemRefreshItems = remoteRefreshItemsMap.get(serviceName);
		    	RemoteItemRefreshItem remoteItemRefreshItem = null;
		    	if(remoteItemRefreshItems != null && remoteItemRefreshItems.get(instanceIdentifier) != null) {
		    		remoteItemRefreshItem = remoteItemRefreshItems.get(instanceIdentifier);
		    	}
		    	if (remoteItemRefreshItem == null) {
		    		remoteItemRefreshItem = new RemoteItemRefreshItem();
		    		remoteItemRefreshItem.setInstanceIdentifier(instanceIdentifier);
		        	remoteItemRefreshItem.setServiceName(serviceName);
		    	}
		    	remoteItemRefreshItem.setSelfUpdate(selfUpdate);
		    	famstackDataAccessObjectManager.saveOrUpdateItemNoGroup(remoteItemRefreshItem);
    	   } catch (Exception e) {
 	    	  logError("createOrUpdateRemoteRefreshItem failed instanceIdentifier :" + instanceIdentifier + ", serviceName :" + serviceName, e);
 	      }
    	
    }
    
    
    public void triggerRemoteItemRefresh(){
    	Map<String, Map<String, RemoteItemRefreshItem>> remoteRefreshItemsMap = getAllRemoteItemMap();
    	Map<String, List<String>> refreshServiceList = new HashMap<>();
    	if (remoteRefreshItemsMap != null) {
    		for (String serviceName : remoteRefreshItemsMap.keySet()) {
    			Map<String, RemoteItemRefreshItem> remoteItemRefreshItems = remoteRefreshItemsMap.get(serviceName);
    			if (remoteItemRefreshItems != null) {
    				RemoteItemRefreshItem lastUpdatedRemoteItemRefreshItem = getHighetTimeSelfUpdateItem(remoteItemRefreshItems);
    				if (lastUpdatedRemoteItemRefreshItem != null){
    					logDebug("Last updated item on " + lastUpdatedRemoteItemRefreshItem);
    					for(String hostIdentifer : remoteItemRefreshItems.keySet()) {
    						RemoteItemRefreshItem remoteItem = remoteItemRefreshItems.get(hostIdentifer);
    						
    						if (lastUpdatedRemoteItemRefreshItem.getLastModifiedDate().after(remoteItem.getLastModifiedDate())){
    							List<String> listOfHosts = refreshServiceList.get(serviceName);
    							if (listOfHosts == null) {
    								listOfHosts = new ArrayList<>();
    								refreshServiceList.put(serviceName, listOfHosts);
    							}
    							logDebug("Cached item to be refreshed on " + remoteItem);
    							listOfHosts.add(remoteItem.getInstanceIdentifier());
    						}
    						
    					}
    				}
    			}
    		}
    	}
    	
    	for (String serviceName : refreshServiceList.keySet()){
    		List<String> listOfHosts = refreshServiceList.get(serviceName);
    		if (listOfHosts != null) {
    			for(String hostIdentifer : listOfHosts){
    				invokeRefreshServiceUrl(hostIdentifer, serviceName);
    			}
    		}
    	}
    	
    }
    
    
    private void invokeRefreshServiceUrl(String hostIdentifier, String serviceName){
	      String httpUrl = "http://"+hostIdentifier+"/bops/dashboard/initialize/"+serviceName;
	      logInfo("Remote service initializing service : "  + httpUrl);
	      URL url;
	      try {
		     url = new URL(httpUrl);
		     HttpURLConnection con = (HttpURLConnection)url.openConnection();
		     con.setRequestMethod("GET");
		     con.setDoOutput(false);
		     int responseCode = con.getResponseCode();
		     if (responseCode != 200){
		    	 logError("Refresh serive failed with status code :" +  responseCode +", hostname :" + httpUrl);
		     }
	      } catch (Exception e) {
	    	  logError("Refresh serive failed, hostname :" + httpUrl,  e);
	      }
    }

	private RemoteItemRefreshItem getHighetTimeSelfUpdateItem(
			Map<String, RemoteItemRefreshItem> remoteItemRefreshItems) {
		RemoteItemRefreshItem preRemoteItem = null;
		RemoteItemRefreshItem lastUpdatedRemoteItem = null;
		for(String hostIdentifer : remoteItemRefreshItems.keySet()) {
			RemoteItemRefreshItem remoteItem = remoteItemRefreshItems.get(hostIdentifer);
			if (preRemoteItem != null) {
				if (remoteItem.getLastModifiedDate().after(preRemoteItem.getLastModifiedDate()) && remoteItem.getSelfUpdate()){
					lastUpdatedRemoteItem = remoteItem;
				} else if (preRemoteItem.getSelfUpdate()) {
					lastUpdatedRemoteItem = preRemoteItem;
				}
			}
			preRemoteItem = remoteItem;
		}
		return lastUpdatedRemoteItem;
	}

}
