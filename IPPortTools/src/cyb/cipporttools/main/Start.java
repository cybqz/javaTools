/**
* Copyright © 1998-2019, Glodon Inc. All Rights Reserved.
*/
package cyb.cipporttools.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

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
		if(initLogger(args)) {
			/*ExecService proxy = new ProxyStatic();
			proxy.ping("www.baidu.com");*/
			ExecService dynamicProxy = (ExecService) new DynamicProxyHandler().getInstance(new ExecServiceImpl());
			dynamicProxy.ping("www.baidu.com");
		}
	}

	private static boolean initLogger(String[] args) {
		try {
			if(args != null && args.length > 0 && args[0].equals("log4j2")) {
				String logPath = Start.class.getResource("/").toString() + "log4j2.xml";
				System.out.println(logPath);
				File file = new File(logPath);
				if(file.exists()) {
					try {
						ConfigurationSource source = new ConfigurationSource(new FileInputStream(file));
						Configurator.initialize(null, source);
						return true;
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
