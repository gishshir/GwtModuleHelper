package fr.tsadeo.app.gwt.modulehelper.server.util;

import java.util.HashMap;
import java.util.Map;

public class StoreExtraSession {

	private static final Map<String, ExtraSession> mapExtraSessions = new HashMap<String, ExtraSession>();
	
	public static final ExtraSession getExtraSession(final String sessionId) {
		return getExtraSession(sessionId, true);
	}
	public static final ExtraSession getExtraSession(final String sessionId, final boolean create) {
		 ExtraSession extraSession = mapExtraSessions.get(sessionId);
		 if (extraSession == null && create) {
			 extraSession = new ExtraSession(sessionId);
			 mapExtraSessions.put(sessionId, extraSession);
		 }
		 return extraSession;
	}
	public static final void addExtraSession(final ExtraSession extraSession, final String sessionId) {
		if (mapExtraSessions.containsKey(sessionId)) {
			throw new IllegalArgumentException("StoreExtraSession addExtraSession - duplicate sessionId! : " + sessionId);
		}
		mapExtraSessions.put(sessionId, extraSession);
	}
	// TODO etudier cycle de vie
	public static final void removeExtraSession (final String sessionId) {
		mapExtraSessions.remove(sessionId);
	}
}
