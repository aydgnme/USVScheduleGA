// Minimal, valid service worker served at /sw.js to satisfy PWA registration.
// No caching logic; extend if offline support is needed.

self.addEventListener('install', () => self.skipWaiting());

self.addEventListener('activate', (event) => {
  event.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', () => {
  // Let the network handle all requests.
});
