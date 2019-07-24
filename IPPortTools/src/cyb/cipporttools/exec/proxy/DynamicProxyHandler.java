/**
* Copyright © 1998-2019, Glodon Inc. All Rights Reserved.
*/
package cyb.cipporttools.exec.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cyb.cipporttools.exec.service.ExecService;

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

public class DynamicProxyHandler implements InvocationHandler {
	
	private Logger logger = LogManager.getLogger(DynamicProxyHandler.class);
	private ExecService execService;
	
	public Object getInstance(ExecService execService){
        this.execService = execService;
        Class<? extends ExecService> clazz = execService.getClass();
        Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        return obj;
    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		logger.info(getStartPrintOut(method, args));
		boolean result = (Boolean) method.invoke(execService, args);
		if(result){
			logger.info(getEndPrintOut(method, args) + "success...");
		}else{
			logger.info(getEndPrintOut(method, args) +  "fail...");
		}
		return result;
	}
	
	private String getStartPrintOut(Method method, Object[] args){
		StringBuilder builder = new StringBuilder("start ");
		builder.append(method.getName()).append(" ");
		if(args != null){
			for(Object o : args){
				builder.append(o.toString()).append(" ");
			}
		}
		return builder.toString();
	}
	
	private String getEndPrintOut(Method method, Object[] args){
		StringBuilder builder = new StringBuilder("end ");
		builder.append(method.getName()).append(" ");
		if(args != null){
			for(Object o : args){
				builder.append(o.toString()).append(" ");
			}
		}
		return builder.toString();
	}
}
