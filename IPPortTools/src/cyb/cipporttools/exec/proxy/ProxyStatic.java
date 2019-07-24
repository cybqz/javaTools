/**
* Copyright © 1998-2019, Glodon Inc. All Rights Reserved.
*/
package cyb.cipporttools.exec.proxy;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import cyb.cipporttools.exec.service.ExecService;
import cyb.cipporttools.exec.service.impl.ExecServiceImpl;

/**
 * 
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author admin
 * @since jdk1.6
 * 2019年7月24日
 *  
 */

public class ProxyStatic implements ExecService {
	
	private Logger logger = LogManager.getLogger(ProxyStatic.class);
	
	private ExecService execSerivce = new ExecServiceImpl();

	@Override
	public boolean ping(String ip) {
		logger.info("start ping " + ip + "...");
		boolean result = execSerivce.ping(ip);
		if(result){
			logger.info("ping " + ip + " success...");
		}else{
			logger.info("ping " + ip + " fail...");
		}
		return result;
	}

	@Override
	public boolean ping(String ip, Integer timeOut) {
		logger.info("start ping " + ip + " "+ timeOut +"...");
		boolean result = execSerivce.ping(ip, timeOut);
		if(result){
			logger.info("ping " + ip + " "+ timeOut +" success...");
		}else{
			logger.info("ping " + ip + " "+ timeOut +" fail...");
		}
		return result;	
	}

	@Override
	public boolean telnet(String ip, int port) {
		logger.info("start telnet " + ip + " "+ port +"...");
		boolean result = execSerivce.telnet(ip, port);
		if(result){
			logger.info("telnet " + ip + " "+ port +" success...");
		}else{
			logger.info("telnet " + ip + " "+ port +" fail...");
		}
		return result;
	}
}
