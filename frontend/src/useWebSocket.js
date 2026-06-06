import { useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';

/**
 * Hook that connects to the backend STOMP WebSocket and invokes callbacks
 * when real-time events arrive on /topic/usage, /topic/alerts, /topic/goals.
 *
 * Uses the native browser WebSocket (no SockJS dependency needed).
 * The backend SockJS endpoint exposes a raw WebSocket at /ws/dashboard/websocket.
 *
 * @param {{ onUsage, onAlert, onGoal }} handlers - optional callbacks
 */
export default function useWebSocket({ onUsage, onAlert, onGoal } = {}) {
  const clientRef = useRef(null);

  useEffect(() => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;

    const client = new Client({
      brokerURL: `${protocol}//${host}/ws/dashboard/websocket`,
      reconnectDelay: 5000,
      onConnect: () => {
        if (onUsage) {
          client.subscribe('/topic/usage', (msg) => {
            onUsage(JSON.parse(msg.body));
          });
        }
        if (onAlert) {
          client.subscribe('/topic/alerts', (msg) => {
            onAlert(JSON.parse(msg.body));
          });
        }
        if (onGoal) {
          client.subscribe('/topic/goals', (msg) => {
            onGoal(JSON.parse(msg.body));
          });
        }
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
    };
  }, []);

  return clientRef;
}
