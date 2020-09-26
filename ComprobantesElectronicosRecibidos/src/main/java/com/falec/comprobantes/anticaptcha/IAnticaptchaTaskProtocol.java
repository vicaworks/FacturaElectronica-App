package com.falec.comprobantes.anticaptcha;

import org.json.JSONObject;

public interface IAnticaptchaTaskProtocol
{
	JSONObject getPostData();

    TaskResultResponse.SolutionData getTaskSolution();
}