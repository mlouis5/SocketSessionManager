/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.socketsessionmanager.impl;

import com.mac.socketsessionmanager.SocketSessionManager;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.ejb.Stateless;
import javax.websocket.Session;

/**
 *
 * @author Mac
 */
@Stateless
public class SocketSessionManagerImpl implements SocketSessionManager {

    private final Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private final Set<Session> closedSessions = Collections.checkedSet(new HashSet<Session>(), Session.class);

    @Override
    public void addSession(Session peer) {
        synchronized (peers) {
            peers.add(peer);
        }
    }

    @Override
    public void removeSession(Session peer) {
        synchronized (peers) {
            peers.remove(peer);
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (peers) {
            return peers.isEmpty();
        }
    }
    
    @Override
    public int size(){
        synchronized (peers){
            return peers.size();
        }
    }

    @Override
    public boolean hasLiveSessions() {
        synchronized (peers) {
            for (Session peer : peers) {
                if (Objects.nonNull(peer) && peer.isOpen()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void clearAllSessions() {
        synchronized (peers) {
            peers.clear();
        }
    }

    @Override
    public void removeClosedSessions() {
        synchronized (peers) {
            for (Session peer : peers) {
                if (!peer.isOpen()) {
                    closedSessions.add(peer);
                }
            }
        }
        for (Session peer : closedSessions) {
            peers.remove(peer);
        }
        emptyClosedSessions();

    }

    @Override
    public void sendBasicMessageToLiveSessions(String message) throws IOException {
        synchronized (peers) {
            for (Session peer : peers) {
                if (peer.isOpen()) {
                    peer.getBasicRemote().sendText(message);
                } else {
                    closedSessions.add(peer);
                }
            }
        }
        for (Session peer : closedSessions) {
            peers.remove(peer);
        }
        emptyClosedSessions();
    }
    
    @Override
    public void sendAsyncMessageToLiveSessions(String message) throws IOException {
        synchronized (peers) {
            for (Session peer : peers) {
                if (peer.isOpen()) {
                    peer.getAsyncRemote().sendText(message);
                } else {
                    closedSessions.add(peer);
                }
            }
        }
        for (Session peer : closedSessions) {
            peers.remove(peer);
        }
        emptyClosedSessions();
    }

    private void emptyClosedSessions() {
        closedSessions.clear();
    }

    @Override
    public boolean sessionExists(Session peer) {
        synchronized (peers) {
            return peers.contains(peer);
        }
    }
}
