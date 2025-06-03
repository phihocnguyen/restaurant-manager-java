import { initializeApp } from "https://www.gstatic.com/firebasejs/11.8.1/firebase-app.js";
import { getDatabase, ref, push, serverTimestamp } from "https://www.gstatic.com/firebasejs/11.8.1/firebase-database.js";

const firebaseConfig = {
  apiKey: "AIzaSyD7aNo4XtXJhn8yqCnmGrwQxc2cr4uarl8",
  authDomain: "se330-restaurant.firebaseapp.com",
  databaseURL: "https://se330-restaurant-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "se330-restaurant",
  storageBucket: "se330-restaurant.firebasestorage.app",
  messagingSenderId: "153993493103",
  appId: "1:153993493103:web:53336ab99178c1e7c9eb9a",
  measurementId: "G-MXVERLH7CD"
};

const app = initializeApp(firebaseConfig);
const database = getDatabase(app);


export async function submitOrderToFirebase(orderPayload) {
    if (!orderPayload) {
        console.error("Dữ liệu đơn hàng rỗng.");
        return false;
    }

    try {
        const ordersRef = ref(database, 'orders');

        const finalOrderData = {
            ...orderPayload,
            status: 'pending',
            createdAt: serverTimestamp()
        };

        console.log('Final order data being pushed to Firebase:', finalOrderData);

        await push(ordersRef, finalOrderData);

        console.log('Order created successfully in Firebase!');
        return true;

    } catch (error) {
        console.error('Lỗi khi gửi đơn hàng lên Firebase:', error);
        return false;
    }
}