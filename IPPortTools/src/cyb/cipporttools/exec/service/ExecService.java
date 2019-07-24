/**
* Copyright © 1998-2019, Glodon Inc. All Rights Reserved.
*/
package cyb.cipporttools.exec.service;

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

public interface ExecService {

	public boolean ping(String ip);
	
	public boolean ping(String ip, Integer timeOut);
	
	public boolean telnet(String ip, int port);
}
