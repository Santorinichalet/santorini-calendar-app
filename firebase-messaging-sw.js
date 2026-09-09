/* Service worker خاص بإشعارات Firebase
   يجب أن يكون هذا الملف في نفس مجلد index.html داخل المستودع
   أي: santorini-calendar-app/firebase-messaging-sw.js */

importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-messaging-compat.js');

firebase.initializeApp({
  apiKey: "AIzaSyAs1Qz1_8Bge6LvIZ61CsjQV4P8RSa8t4A",
  authDomain: "santorini-chalet.firebaseapp.com",
  projectId: "santorini-chalet",
  storageBucket: "santorini-chalet.firebasestorage.app",
  messagingSenderId: "280049242908",
  appId: "1:280049242908:web:2e6e7d9d88506c764760d9"
});

firebase.messaging();

// فتح التطبيق عند الضغط على الإشعار بدل فتح تبويب جديد كل مرة
self.addEventListener('notificationclick', function (event) {
  event.notification.close();
  const target = (event.notification.data && event.notification.data.FCM_MSG &&
                  event.notification.data.FCM_MSG.webpush &&
                  event.notification.data.FCM_MSG.webpush.fcmOptions &&
                  event.notification.data.FCM_MSG.webpush.fcmOptions.link) ||
                 'https://santorinichalet.github.io/santorini-calendar-app/';

  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then(function (list) {
      for (const c of list) {
        if (c.url.indexOf('santorini-calendar-app') !== -1 && 'focus' in c) return c.focus();
      }
      if (clients.openWindow) return clients.openWindow(target);
    })
  );
});
