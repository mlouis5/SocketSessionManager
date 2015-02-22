/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.socketsessionmanager;

import java.io.IOException;
import javax.websocket.Session;

/**
 *
 * @author MacDerson
 */
public interface SocketSessionManager {
    
    void addSession(Session peer);

    void clearAllSessions();

    boolean hasLiveSessions();

    boolean isEmpty();

    void removeClosedSessions();

    void removeSession(Session peer);

    void sendBasicMessageToLiveSessions(String message) throws IOException;
    
    void sendAsyncMessageToLiveSessions(String message) throws IOException;

    boolean sessionExists(Session peer);
    
    int size();
    
}
