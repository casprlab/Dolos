package com.mizan.dsg.dataclass;

import java.util.ArrayList;
import java.util.List;

public class AllDSGInfo {
	private String appId;
	
	private List<DSGInfo> DSGInfos;
	
	public AllDSGInfo() {
		setDSGInfos(new ArrayList<DSGInfo>());
	}
	
	public String getAppId() {
		return appId;
	}
	
	public void addDSGInfo(DSGInfo dsgInfo){ 
		DSGInfos.add(dsgInfo); 
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public List<DSGInfo> getDSGInfos() {
		return DSGInfos;
	}

	public void setDSGInfos(List<DSGInfo> dSGInfos) {
		DSGInfos = dSGInfos;
	}
}
