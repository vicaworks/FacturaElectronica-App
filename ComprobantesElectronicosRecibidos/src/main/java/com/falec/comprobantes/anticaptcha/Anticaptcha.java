package com.falec.comprobantes.anticaptcha;

import java.net.MalformedURLException;
import java.net.URL;

public class Anticaptcha
{
	public static String resolverNoCaptchaProxyless(String direccionActual, String sitekey) throws MalformedURLException, InterruptedException
	{
		String result = null;
        DebugHelper.setVerboseMode(true);

        NoCaptchaProxyless api = new NoCaptchaProxyless();
        api.setClientKey("36ef7e56dad1b9644b6696d3c02f54ff"); // Clave proporcionada por anti-captcha.com
        api.setWebsiteUrl(new URL(direccionActual));
        api.setWebsiteKey(sitekey);

        if(!api.createTask())
        {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        }
        else if(!api.waitForResult())
        {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        }
        else
        {
        	result = api.getTaskSolution().getGRecaptchaResponse();
            DebugHelper.out("Result: " + result, DebugHelper.Type.SUCCESS);
        }
        return result;
    }
}