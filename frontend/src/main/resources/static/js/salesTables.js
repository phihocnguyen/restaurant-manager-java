let currentTableId = null;
let currentPaymentMethod = null;
let currentPaymentTableId = null;

//
// ===== CÁC HÀM TRÙNG LẶP ĐÃ ĐƯỢC XÓA =====
//
// 1. openModal(modalId) - Đã có trong bookingList.js
// 2. closeModal(modalId) - Đã có trong bookingList.js
// 3. Tạo spinnerOverlay - Đã có trong bookingList.js
// 4. showGlobalSpinner() - Đã có trong bookingList.js
// 5. hideGlobalSpinner() - Đã có trong bookingList.js
//

// Helper function to show spinner on a button
function showSpinner(buttonElement) {
    buttonElement.disabled = true;
    // Store original content to restore later
    buttonElement.setAttribute('data-original-html', buttonElement.innerHTML);
    buttonElement.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Đang xử lý...';
}

// Helper function to hide spinner on a button
function hideSpinner(buttonElement) {
    buttonElement.disabled = false;
    // Restore original content
    if (buttonElement.hasAttribute('data-original-html')) {
        buttonElement.innerHTML = buttonElement.getAttribute('data-original-html');
        buttonElement.removeAttribute('data-original-html');
    }
}

// Hàm lưu thông tin bàn và món ăn vào localStorage
function saveTableData(tableId, items) {
    const tableData = {
        tableId: tableId,
        items: items,
        timestamp: new Date().toISOString()
    };
    localStorage.setItem(`table_${tableId}`, JSON.stringify(tableData));
}

// Hàm lấy thông tin bàn từ localStorage
function getTableData(tableId) {
    const data = localStorage.getItem(`table_${tableId}`);
    return data ? JSON.parse(data) : null;
}

// Hàm xóa thông tin bàn khỏi localStorage sau khi thanh toán
function resetTableData(tableId) {
    localStorage.removeItem(`table_${tableId}`);
    console.log(`Table ${tableId} data reset.`);
}

// Helper function to format currency to VND
function formatCurrency(value) {
    // Using Intl.NumberFormat for robust localization
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
}

// Hàm xóa một item khỏi bàn
function removeItemFromTable(tableId, itemId) {
    const tableData = getTableData(tableId);
    if (!tableData) return;

    // Tìm index của item cần xóa
    const itemIndex = tableData.items.findIndex(item => item.id === itemId);
    if (itemIndex === -1) return;

    // Xóa item khỏi mảng
    tableData.items.splice(itemIndex, 1);

    // Lưu lại dữ liệu vào localStorage
    saveTableData(tableId, tableData.items);

    // Cập nhật lại giao diện
    viewTableDetails(tableId);
}

// Hàm cập nhật số lượng item
function updateItemQuantity(tableId, itemId, newQuantity) {
    const tableData = getTableData(tableId);
    if (!tableData) return;
    const item = tableData.items.find(item => item.id === itemId);
    if (!item) return;
    if (newQuantity < 1) {
        if (confirm('Bạn có muốn xóa món này khỏi bàn không?')) {
            removeItemFromTable(tableId, itemId);
            return;
        } else {
            return;
        }
    }
    item.quantity = newQuantity;
    item.totalPrice = item.price * newQuantity;
    saveTableData(tableId, tableData.items);
    viewTableDetails(tableId);
}

// Hàm xem chi tiết bàn
async function viewTableDetails(tableId) {
    showGlobalSpinner(); // Show spinner
    const cartArea = document.querySelector('.cart-area');
    const defaultCartContent = `
            <div class="bg-gradient-to-r from-gray-50 to-gray-100 rounded-2xl p-6 mb-6">
                <div class="text-center">
                    <div class="bg-gradient-to-br from-orange-400 to-pink-500 w-16 h-16 rounded-2xl flex items-center justify-center mx-auto mb-4">
                        <i class="fas fa-table text-white text-2xl"></i>
                    </div>
                    <h3 class="text-lg font-bold text-gray-900 mb-2">Chưa chọn bàn nào</h3>
                    <p class="text-sm text-gray-600">Vui lòng chọn bàn để bắt đầu phục vụ</p>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3 mb-6">
                <div class="text-center bg-green-50 rounded-xl p-4">
                    <div class="text-xs text-green-600 font-medium mb-1">Bàn đang hoạt động</div>
                    <div class="text-2xl font-bold text-green-700">2</div>
                </div>
                <div class="text-center bg-gray-50 rounded-xl p-4">
                    <div class="text-xs text-gray-600 font-medium mb-1">Bàn trống</div>
                    <div class="text-2xl font-bold text-gray-700">3</div>
                </div>
                <div class="text-center bg-orange-50 rounded-xl p-4">
                    <div class="text-xs text-orange-600 font-medium mb-1">Bàn đã đặt</div>
                    <div class="2xl font-bold text-orange-700">1</div>
                </div>
                <div class="text-center bg-blue-50 rounded-xl p-4">
                    <div class="text-xs text-blue-600 font-medium mb-1">Tổng doanh thu</div>
                    <div class="lg font-bold text-blue-700">735K</div>
                </div>
            </div>
        `;

    try {
        // Fetch table details from backend
        const response = await fetch(`/sales/api/tables/${tableId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch table details');
        }
        const tableDetails = await response.json();

        // Get order items from localStorage
        const tableData = getTableData(tableId);
        const orderItems = tableData ? tableData.items : [];
        const timestamp = tableData ? new Date(tableData.timestamp).toLocaleString() : 'N/A';

        // Get customer info from localStorage, default to 'Khách vãng lai' if not found
        let customerName = 'Khách vãng lai';
        const customerInfoJSON = localStorage.getItem(`table_${tableId}_customer`);
        if (customerInfoJSON) {
            try {
                const customerInfo = JSON.parse(customerInfoJSON);
                if (customerInfo && customerInfo.name) {
                    customerName = customerInfo.name;
                }
            } catch (e) {
                console.error('Error parsing customer info for table', tableId, e);
            }
        }

        let orderDetailsHtml = '';
        let totalAmount = 0;

        if (orderItems.length === 0) {
            orderDetailsHtml = '<p class="text-center text-gray-500">Không có món ăn nào trong bàn này.</p>';
        } else {
            orderDetailsHtml = orderItems.map(item => `
                <div class="flex items-center justify-between p-4 bg-gray-50 rounded-xl">
                    <div class="flex items-center space-x-4">
                        <img src="${item.image}" alt="${item.name}" class="w-16 h-16 object-cover rounded-lg">
                        <div>
                            <h4 class="font-medium text-gray-900">${item.name}</h4>
                            <div class="flex items-center mt-1">
                                <button onclick="updateItemQuantity(${tableId}, ${item.id}, ${item.quantity - 1})" class="px-2 py-1 bg-gray-200 text-gray-700 rounded-l hover:bg-gray-300">-</button>
                                <input type="number" min="1" value="${item.quantity}" onchange="updateItemQuantity(${tableId}, ${item.id}, this.valueAsNumber)" class="w-12 text-center border border-gray-300 mx-1 rounded" style="height:32px;" />
                                <button onclick="updateItemQuantity(${tableId}, ${item.id}, ${item.quantity + 1})" class="px-2 py-1 bg-gray-200 text-gray-700 rounded-r hover:bg-gray-300">+</button>
                            </div>
                        </div>
                    </div>
                    <div class="flex items-center space-x-4">
                        <p class="font-medium text-gray-900">${formatCurrency(item.totalPrice)}</p>
                        <button onclick="removeItemFromTable(${tableId}, ${item.id})" class="p-2 text-red-500 hover:bg-red-100 rounded-full transition-colors">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `).join('');
            totalAmount = orderItems.reduce((sum, item) => sum + item.totalPrice, 0);
        }

        // Populate the cart area with fetched table details and localStorage items
        cartArea.innerHTML = `
            <div class="p-5 border-b border-gray-200">
                 <h3 class="text-xl font-bold text-gray-900 mb-2">Bàn: ${tableDetails.id}</h3>
                 <p class="text-sm text-gray-600 mb-2">Số chỗ: ${tableDetails.tabNum} chỗ</p>
                 <p class="text-sm text-gray-600">Thời gian bắt đầu: ${timestamp}</p>
                 <p class="text-sm text-gray-600">Tên khách hàng: ${customerName}</p>
             </div>
             <div class="flex-1 p-5 overflow-y-auto">
                 <h4 class="font-bold text-gray-800 mb-3">Chi tiết đơn hàng</h4>
                 <div class="space-y-4">
                     ${orderDetailsHtml}
                 </div>
            </div>
            <div class="p-5 border-t border-gray-200 bg-white">
                <div class="flex justify-between items-center mb-4">
                     <span class="text-lg font-medium text-gray-900">Tổng cộng:</span>
                     <span class="text-xl font-bold text-orange-500">
                          ${formatCurrency(totalAmount)}
                     </span>
                </div>
                 <div class="grid grid-cols-1 gap-3">
                     <button onclick="checkout(${tableId})" class="flex justify-center items-center px-6 py-3 bg-indigo-600 text-white rounded-xl hover:bg-indigo-700 transition-all font-medium shadow-lg">
                         <i class="fas fa-dollar-sign mr-2"></i>Thanh toán
                    </button>
                 </div>
            </div>
        `;

    } catch (error) {
        console.error('Error viewing table details:', error);
        cartArea.innerHTML = defaultCartContent;
    } finally {
        hideGlobalSpinner(); // Hide spinner
    }
}

function synchronizeStateOnLoad() {
    console.log("Running state synchronization on page load...");
    const tableCards = document.querySelectorAll('.table-card');

    tableCards.forEach(card => {
        const tableId = card.getAttribute('data-table');
        const serverStatus = card.getAttribute('data-status');
        
        // Tìm phần tử để hiển thị tên khách hàng trong card này
        const customerDisplayElement = card.querySelector('.customer-name'); // Tìm thẻ p theo class

        // Lấy dữ liệu từ localStorage
        const localTableData = getTableData(tableId);
        const customerInfoJSON = localStorage.getItem(`table_${tableId}_customer`);

        // Cập nhật giao diện tên khách hàng trên thẻ bàn
        if (customerDisplayElement) {
            customerDisplayElement.innerHTML = ''; // Mặc định là trống
            if (customerInfoJSON) {
                try {
                    const customerInfo = JSON.parse(customerInfoJSON);
                    if (customerInfo && customerInfo.name) {
                        // Nếu có thông tin, hiển thị nó
                        customerDisplayElement.textContent = customerInfo.name; // Chỉ hiển thị tên
                    }
                } catch (e) {
                    console.error(`Error parsing customer info for table ${tableId}`, e);
                }
            }
        }

        // Dọn dẹp dữ liệu không nhất quán
        if (serverStatus === 'EMPTY' && (localTableData || customerInfoJSON)) {
            console.warn(`Inconsistency Found: Table ${tableId} is EMPTY on server but has local data. Cleaning up.`);
            resetTableData(tableId);
            localStorage.removeItem(`table_${tableId}_customer`);
            if (customerDisplayElement) customerDisplayElement.innerHTML = '';
        }

        if (serverStatus === 'OCCUPIED' && !localTableData) {
            console.warn(`Inconsistency Found: Table ${tableId} is OCCUPIED on server but has no local items. Creating empty cart.`);
            saveTableData(tableId, []);
        }
    });

    // Tự động mở lại chi tiết bàn cuối cùng được chọn trước khi reload
    const lastSelectedTableId = sessionStorage.getItem('lastSelectedTableId');
    if (lastSelectedTableId) {
        const correspondingCard = document.querySelector(`.table-card[data-table="${lastSelectedTableId}"]`);
        // Chỉ mở lại nếu bàn đó vẫn còn tồn tại và không trống
        if (correspondingCard && correspondingCard.getAttribute('data-status') !== 'EMPTY') {
             viewTableDetails(lastSelectedTableId);
        } else {
            sessionStorage.removeItem('lastSelectedTableId');
        }
    }

    console.log("State synchronization finished.");
}

// Hàm bắt đầu phục vụ bàn
async function startServing(tableId) {
    // Kiểm tra xem bàn đã có dữ liệu chưa
    const tableData = getTableData(tableId);
    if (tableData && tableData.items.length > 0) {
         // If table has items in localStorage, just view details
         viewTableDetails(tableId);
         synchronizeStateOnLoad();
         return;
    } else if (tableData && tableData.items.length === 0) {
         // If table exists but no items, maybe it was started but not used
         // Proceed to update status and view empty details panel
         // No alert needed, just update UI and show empty panel
    } else {
         // If table doesn't exist in localStorage, create it and update status
         saveTableData(tableId, []);
         // No alert needed
    }

    // Lưu thông tin khách hàng mặc định
    const defaultCustomerInfo = {
        name: "Khách vãng lai"
    };
    localStorage.setItem(`table_${tableId}_customer`, JSON.stringify(defaultCustomerInfo));

     // Fetch current table data to get tabNum
     showGlobalSpinner();
     try {
         const currentTableResponse = await fetch(`/sales/api/tables/${tableId}`);
         if (!currentTableResponse.ok) {
             throw new Error('Failed to fetch current table data');
         }
         const currentTableData = await currentTableResponse.json();

         // Update status to OCCUPIED via backend API
         const response = await fetch(`/sales/api/tables/${tableId}`, {
             method: 'PUT',
             headers: {
                 'Content-Type': 'application/json',
             },
             body: JSON.stringify({
                 id: tableId,
                 tabNum: currentTableData.tabNum, // Include tabNum
                 tabStatus: 'OCCUPIED',
             })
         });

         if (!response.ok) {
              if (response.status === 404) {
                 throw new Error('Bàn không tồn tại');
              } else if (response.status === 409) {
                  throw new Error('Bàn đang có khách hoặc đã đặt');
              }
             throw new Error('Lỗi khi cập nhật trạng thái bàn.');
         }

         // Backend updated successfully, now update frontend UI
         // Find the table card and update its status display
         const tableCard = document.querySelector(`[data-table="${tableId}"]`);
         if (tableCard) {
             tableCard.setAttribute('data-status', 'OCCUPIED');
             // Update status text and color
             const statusSpan = tableCard.querySelector('.table-status');
             statusSpan.innerHTML = '<i class="fas fa-circle text-green-500 mr-1"></i>Đang phục vụ';
             statusSpan.className = 'px-2 py-1 bg-green-100 text-green-800 text-xs font-medium rounded-full table-status';

             // Update background and icon
             const iconDiv = tableCard.querySelector('.h-48');
             iconDiv.className = 'h-48 table-active flex items-center justify-center';
             iconDiv.innerHTML = '<i class="fas fa-utensils text-white text-4xl"></i>';

             // Update actions
             const actionsDiv = tableCard.querySelector('.table-actions');
             actionsDiv.innerHTML = `
                 <button class="px-4 py-2 bg-orange-500 text-white text-sm font-medium rounded-xl hover:opacity-90 transition-all shadow-md" onclick="viewTableDetails('${tableId}')">
                     <i class="fas fa-eye mr-1"></i>Xem chi tiết
                 </button>
             `;
         }
         window.location.reload()

     } catch (error) {
         console.error('Error starting service:', error);
         alert(error.message || 'Không thể bắt đầu phục vụ. Vui lòng thử lại.');
     } finally {
         hideGlobalSpinner();
         // Re-apply filtering after status change might be needed
         refreshTableVisibility();
     }
}

// Booking modal functionality
function openBookingModal(tableNumber) {
    document.getElementById('bookingModal').classList.remove('hidden');
    document.getElementById('bookingModal').classList.add('flex');
    document.getElementById('tableNumber').value = tableNumber;
}

function closeBookingModal() {
    document.getElementById('bookingModal').classList.add('hidden');
    document.getElementById('bookingModal').classList.remove('flex');
     // Hide spinner on booking modal submit button if it was showing
     const submitButton = document.querySelector('#bookingForm button[type="submit"]');
     if (submitButton.disabled) {
         hideSpinner(submitButton);
     }
}

// Handle booking form submission
document.getElementById('bookingForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const tableId = document.getElementById('tableNumber').value; // Use tableId, not tableNumber
    const formData = new FormData(this);
    const bookingData = Object.fromEntries(formData.entries());
    bookingData.tableId = parseInt(tableId); // Ensure tableId is integer
    bookingData.status = 'RESERVED'; // Set status to RESERVED

    // Need to get customer ID here. Assuming customer search is implemented separately.
    // For now, this form doesn't capture customer ID, will need backend update.

    showSpinner(this.querySelector('button[type="submit"]')); // Show spinner on submit button

    try {
        // Assuming backend endpoint to create booking/update table status to RESERVED
        const response = await fetch('/backend/api/bookings', { // Or directly update table status
             method: 'POST', // Or PUT to /tables/{id}/status
             headers: {
                 'Content-Type': 'application/json',
             },
             body: JSON.stringify(bookingData) // Adjust payload as per backend API
         });

        if (!response.ok) {
             if (response.status === 404) {
                throw new Error('Bàn không tồn tại ở Backend.');
             } else if (response.status === 409) {
                 throw new Error('Bàn đang có khách hoặc đã đặt (Backend).');
             }
             throw new Error('Lỗi khi đặt bàn ở Backend.');
        }

         // Backend updated successfully, now update frontend UI
        // Find the table card and update its status display
        const tableCard = document.querySelector(`[data-table="${tableId}"]`);
        if (tableCard) {
            tableCard.setAttribute('data-status', 'RESERVED');
            // Update status text and color
            const statusSpan = tableCard.querySelector('.table-status');
            statusSpan.innerHTML = '<i class="fas fa-circle text-orange-500 mr-1"></i>Đã đặt';
            statusSpan.className = 'px-2 py-1 bg-orange-100 text-orange-800 text-xs font-medium rounded-full table-status';

            // Update background and icon
            const iconDiv = tableCard.querySelector('.h-48');
            iconDiv.className = 'h-48 table-booking flex items-center justify-center';
            iconDiv.innerHTML = '<i class="fas fa-calendar-alt text-white text-4xl"></i>';

             // Update actions - Assuming confirm booking button appears after booking
             const actionsDiv = tableCard.querySelector('.table-actions');
             actionsDiv.innerHTML = `
                  <button class="px-4 py-2 bg-orange-500 text-white text-sm font-medium rounded-xl hover:opacity-90 transition-all shadow-md" onclick="confirmBooking1('${tableId}')">
                      <i class="fas fa-check mr-1"></i>Xác nhận
                  </button>
              `;
         }

         closeBookingModal();

    } catch (error) {
        console.error('Error booking table:', error);
         alert(error.message || 'Không thể đặt bàn. Vui lòng thử lại.');
    } finally {
         hideSpinner(this.querySelector('button[type="submit"]')); // Hide spinner
         // Re-apply filtering after status change might be needed
         refreshTableVisibility();
    }
});

// Confirm booking functionality - This should also call backend API
async function confirmBooking1(tableId) {
    showGlobalSpinner();
    try {
        // 1. Lấy thông tin bàn hiện tại, bao gồm cả thông tin booking và khách hàng
        const currentTableResponse = await fetch(`/sales/api/tables/${tableId}`);
        if (!currentTableResponse.ok) {
            throw new Error('Failed to fetch current table data');
        }
        const currentTableData = await currentTableResponse.json();

        saveTableData(tableId, []);

        // 4. Cập nhật trạng thái bàn thành OCCUPIED
        const response = await fetch(`http://localhost:8080/tables/${tableId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                tabStatus: 'OCCUPIED',
            })
        });

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Bàn hoặc đặt bàn không tồn tại ở Backend.');
            } else if (response.status === 409) {
                throw new Error('Bàn đang có khách hoặc đã đặt (Backend).');
            }
            throw new Error('Lỗi khi xác nhận đặt bàn ở Backend.');
        }
        
        window.location.reload()

    } catch (error) {
        console.error('Error confirming booking:', error);
        alert(error.message || 'Không thể xác nhận đặt bàn. Vui lòng thử lại.');
    } finally {
        hideGlobalSpinner();
        // Cập nhật lại bộ lọc sau khi thay đổi trạng thái
        refreshTableVisibility();
    }
}

function refreshTableVisibility() {
    const activeTabStatus = document.querySelector('.active-tab').getAttribute('data-tab-status');
    // Mapping tab status to backend enum status for filtering
    let backendStatus = null;
    if (activeTabStatus === 'active') {
        backendStatus = 'OCCUPIED';
    } else if (activeTabStatus === 'empty') {
        backendStatus = 'EMPTY';
    } else if (activeTabStatus === 'booking') {
        backendStatus = 'RESERVED';
    }

    document.querySelectorAll('.table-card').forEach(card => {
        const cardStatus = card.getAttribute('data-status');
        const searchTermInput = document.querySelector('input[placeholder*="Tìm tên bàn"]');
        const searchTerm = searchTermInput ? searchTermInput.value.toLowerCase() : '';
        const title = card.querySelector('h3').textContent.toLowerCase();

         // Check if card matches the active status filter OR 'all' tab
        const matchesStatus = (activeTabStatus === 'all' || cardStatus === backendStatus);

         // Check if card matches the search term
         const matchesSearch = title.includes(searchTerm);

        // Show card only if it matches both status filter and search term
        if (matchesStatus && matchesSearch) {
            card.style.display = 'flex';
        } else {
            card.style.display = 'none';
        }
    });
}

// Add a checkout function placeholder
function checkout(tableId) {
    openPaymentModal(tableId);
}

// Table filtering functionality
document.addEventListener('DOMContentLoaded', function() {
    // Get all filter buttons and table cards
    const filterButtons = document.querySelectorAll('[data-tab-status]');
    const tableCards = document.querySelectorAll('.table-card');
    
    // Add click event listeners to filter buttons
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Remove active class from all buttons
            filterButtons.forEach(btn => {
                btn.classList.remove('bg-orange-500', 'text-white');
                btn.classList.add('bg-gray-200', 'text-gray-700');
            });

            // Add active class to clicked button
            this.classList.remove('bg-gray-200', 'text-gray-700');
            this.classList.add('bg-orange-500', 'text-white');
            
            const selectedStatus = this.getAttribute('data-tab-status');
            
            // Filter tables based on selected status
            tableCards.forEach(card => {
                const tableStatus = card.getAttribute('data-status');
                
                if (selectedStatus === 'all') {
                    card.style.display = 'flex';
                } else if (selectedStatus === 'active' && tableStatus === 'OCCUPIED') {
                    card.style.display = 'flex';
                } else if (selectedStatus === 'empty' && tableStatus === 'EMPTY') {
                    card.style.display = 'flex';
                } else if (selectedStatus === 'booking' && tableStatus === 'RESERVED') {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    });
    synchronizeStateOnLoad();
});

let selectedPaymentMethod = null;


function generateVietQR() {
    const totalAmount = parseFloat(document.getElementById('paymentTotalAmount').textContent.replace(/[^0-9.]/g, ''));
    const tableNumber = document.getElementById('paymentTableNumber').textContent;
    const bankId = '970403';
    const accountNo = '070126475657';
    const template = 'print';
    const accountName = encodeURIComponent('KHONG HUYNH NGOC HAN');
    const addInfo = encodeURIComponent(`Thanh Toan Ban ${tableNumber} - G15 Kitchen`);

    const vietQRUrl = `https://img.vietqr.io/image/${bankId}-${accountNo}-${template}.png?amount=${totalAmount}&addInfo=${addInfo}&accountName=${accountName}`;
    document.getElementById('vietQrImage').src = vietQRUrl;
    console.log('Generated VietQR URL for table payment:', vietQRUrl);
}


function openPaymentModal(tableId) {
    currentPaymentTableId = tableId;
    const tableData = getTableData(tableId);
    if (!tableData || !tableData.items || tableData.items.length === 0) {
        alert('Không có món ăn nào để thanh toán!');
        return;
    }

    console.log('Items from localStorage for table', tableId, ':', tableData.items);
    const totalAmount = tableData.items.reduce((sum, item) => sum + item.totalPrice, 0);
    console.log('Calculated totalAmount:', totalAmount);
    document.getElementById('paymentTotalAmount').textContent = formatCurrency(totalAmount);
    document.getElementById('paymentTableNumber').textContent = `Bàn ${tableId}`;
    
    // Reset payment method selection
    document.querySelectorAll('.payment-method-btn').forEach(btn => {
        btn.classList.remove('border-orange-500', 'bg-orange-50');
        btn.classList.add('border-gray-200');
    });
    currentPaymentMethod = null;

    document.getElementById('paymentModal').classList.remove('hidden');
    document.getElementById('paymentModal').classList.add('flex');
    document.getElementById('vietQrSection').classList.add('hidden');
}

function closePaymentModal() {
    document.getElementById('paymentModal').classList.add('hidden');
    document.getElementById('paymentModal').classList.remove('flex');
    currentPaymentTableId = null;
    currentPaymentMethod = null;
}

function selectPaymentMethod(method) {
    currentPaymentMethod = method;
    document.querySelectorAll('.payment-method-btn').forEach(btn => {
        if (btn.getAttribute('data-method') === method) {
            btn.classList.add('border-orange-500', 'bg-orange-500/10', 'text-orange-800');
            btn.classList.remove('border-gray-200', 'text-gray-600');
        } else {
            btn.classList.remove('border-orange-500', 'bg-orange-500/10', 'text-orange-800');
            btn.classList.add('border-gray-200', 'text-gray-600');
        }
    });

    const vietQrSection = document.getElementById('vietQrSection');
    if (method === 'Internet Banking') {
        vietQrSection.classList.remove('hidden');
        generateVietQR();
    } else {
        vietQrSection.classList.add('hidden');
    }
}

async function processPayment() {
    if (!currentPaymentMethod) {
        alert('Vui lòng chọn phương thức thanh toán!');
        return;
    }

    showGlobalSpinner();
    try {
        const tableData = getTableData(currentPaymentTableId);
        console.log('Table Data:', tableData);

        let customerId = null;
        const customerStorageKey = `table_${currentPaymentTableId}_customer`;
        const customerInfoJSON = localStorage.getItem(customerStorageKey);

        if (customerInfoJSON) {
            try {
                const customerInfo = JSON.parse(customerInfoJSON);
                if (customerInfo && customerInfo.id) {
                    customerId = customerInfo.id;
                    console.log(`Retrieved customerId: ${customerId} from key: ${customerStorageKey}`);
                }
            } catch (error) {
                console.error('Error parsing customer info from localStorage:', error);
            }
        } else {
             console.log(`No customer info found in localStorage for key: ${customerStorageKey}`);
        }

        const totalAmount = tableData.items.reduce((sum, item) => sum + item.totalPrice, 0);

        const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));
        let employeeId = null;

        if (loggedInUser && loggedInUser.accEmail) {
            try {
                const employeeResponse = await fetch(`http://localhost:8080/employees/email/${loggedInUser.accEmail}`);
                if (employeeResponse.ok) {
                    const employee = await employeeResponse.json();
                    if (employee) {
                        employeeId = employee.id;
                        console.log(`Retrieved employeeId for payment: ${employeeId} for email ${loggedInUser.accEmail}`);
                    }
                } else {
                    console.error('Failed to fetch employee info by email:', employeeResponse.statusText);
                }
            } catch (error) {
                console.error('Error fetching employee info:', error);
            }
        } else {
            console.warn('loggedInUser or accEmail not found in localStorage. Employee ID will be null.');
        }

        // Chuẩn bị dữ liệu hóa đơn
        const receiptData = {
            receipt: {
                tabId: parseInt(currentPaymentTableId),
                recTime: new Date().toISOString(),
                isdeleted: false,
                paymentMethod: currentPaymentMethod,
                cusId: customerId ? parseInt(customerId) : null,
                empId: employeeId
            },
            details: tableData.items.map(item => ({
                itemId: parseInt(item.itemId),
                quantity: parseInt(item.quantity)
            }))
        };
        const response = await fetch('http://localhost:8080/receipt-details/many', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(receiptData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        if (customerInfoJSON) {
            localStorage.removeItem(customerStorageKey);
            console.log(`Removed customer info for key: ${customerStorageKey}`);
        }

        // In hóa đơn PDF các món ăn
        if (window.jspdf && window.jspdf.jsPDF) {
            const { jsPDF } = window.jspdf;
            const doc = new jsPDF();
            doc.setFont('Arial', 'normal');
            doc.setFontSize(18);
            doc.text('HÓA ĐƠN THANH TOÁN', 105, 18, { align: 'center' });
            doc.setFontSize(12);
            doc.text(`Bàn: ${currentPaymentTableId}`, 14, 30);
            doc.text(`Thời gian: ${new Date().toLocaleString('vi-VN')}`, 14, 38);
            let employeeName = '';
            if (loggedInUser && (loggedInUser.accDisplayName || loggedInUser.name)) {
                employeeName = loggedInUser.accDisplayName || loggedInUser.name;
            }
            doc.text(`Nhân viên: ${employeeName}`, 14, 46);
            let customerName = '';
            if (customerInfoJSON) {
                try {
                    const customerInfo = JSON.parse(customerInfoJSON);
                    customerName = customerInfo.name || 'Khách vãng lai';
                } catch (e) { customerName = 'Khách vãng lai'; }
            } else {
                customerName = 'Khách vãng lai';
            }
            if (customerName) doc.text(`Khách hàng: ${customerName}`, 14, 54);
            doc.autoTable({
                startY: 60,
                head: [['STT', 'Tên món', 'Số lượng', 'Đơn giá', 'Thành tiền']],
                body: tableData.items.map((item, idx) => [
                    idx + 1,
                    item.name,
                    item.quantity,
                    formatCurrency(item.price),
                    formatCurrency(item.totalPrice)
                ]),
                theme: 'grid',
                headStyles: { fillColor: [255, 107, 53] },
                styles: { font: 'Arial' }
            });
            const finalY = doc.lastAutoTable.finalY;
            doc.setFontSize(13);
            doc.text('Tổng cộng:', 150, finalY + 10, { align: 'right' });
            doc.text(formatCurrency(totalAmount), 200, finalY + 10, { align: 'right' });
            doc.save(`receipt-table-${currentPaymentTableId}-${Date.now()}.pdf`);
        }

        const updateTableResponse = await fetch(`http://localhost:8080/tables/${currentPaymentTableId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                tabStatus: 'EMPTY',
            })
        });

        if (!updateTableResponse.ok) {
            throw new Error(`HTTP error! status: ${updateTableResponse.status}`);
        }

        alert('Thanh toán thành công!');
        resetTableData(currentPaymentTableId);
        closePaymentModal();
        refreshTableVisibility();

        // Sau khi thanh toán thành công:
        location.reload();

    } catch (error) {
        console.error('Error processing payment:', error);
        alert('Có lỗi xảy ra khi thanh toán: ' + error.message);
    } finally {
        hideGlobalSpinner();
    }
}

// Add logout function to salesTables.js
function logout() {
    localStorage.removeItem('loggedInUser');
    window.location.href = '/login';
}