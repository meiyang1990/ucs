/**
 * 
 */
package com.netfinworks.basis.inf.ucs.memcached.ns;

import java.util.HashMap;
import java.util.Map;

import tokyotyrant.RDB;
import tokyotyrant.networking.NodeAddress;

/**
 * @author bigknife
 * 
 */
public class TTNamingService implements NamingService {
	private String ttServerAddress;
	private Map<String, String> addressMap = new HashMap<String, String>();

	/**
	 * @param ttServerAddress
	 *            the ttServerAddress to set
	 */
	public void setTtServerAddress(String ttServerAddress) {
		this.ttServerAddress = ttServerAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.netfinworks.basis.inf.ucs.memcached.ns.NamingService#resove(java.lang.
	 * String)
	 */
	public String resove(String name) {
		String serverInfo = addressMap.get(name);
		if (serverInfo == null) {
			RDB db = null;
			try {
				db = new RDB();
				db.open(NodeAddress.addresses(ttServerAddress)[0]);
				serverInfo = (String) db.get(name);
				addressMap.put(name, serverInfo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
		return serverInfo;
	}

}
