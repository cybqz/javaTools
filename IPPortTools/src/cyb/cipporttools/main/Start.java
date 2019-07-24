/**
* Copyright © 1998-2019, Glodon Inc. All Rights Reserved.
*/
package cyb.cipporttools.main;

import cyb.cipporttools.exec.proxy.DynamicProxyHandler;
import cyb.cipporttools.exec.proxy.ProxyStatic;
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
 * 2019年7月22日
 *  
 */

public class Start {

	public static void main(String[] args) {
		/*ExecService proxy = new ProxyStatic();
		proxy.ping("www.baidu.com");*/
		ExecService dynamicProxy = (ExecService) new DynamicProxyHandler().getInstance(new ExecServiceImpl());
		dynamicProxy.ping("www.baidu.com");
	}
}
