package com.powzyapp.powzy.network;

import java.util.List;

import com.powzyapp.powzy.model.BusinessView;


public interface AsyncResponse {
	void processFinish(int responseMessage, BusinessView[] entities);
}
