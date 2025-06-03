let currentTableId = null;
let currentPaymentMethod = null;
let currentPaymentTableId = null;

function openModal(modalId) {
    document.getElementById(modalId).classList.remove('hidden');
    document.body.classList.add('modal-open'); // Prevent scrolling
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
    document.body.classList.remove('modal-open'); // Re-enable scrolling
}

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

// Global spinner overlay functions
const spinnerOverlay = document.createElement('div');
spinnerOverlay.id = 'globalSpinnerOverlay';
spinnerOverlay.className = 'fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[100] hidden';
spinnerOverlay.innerHTML = '<div class="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>';
document.body.appendChild(spinnerOverlay);

function showGlobalSpinner() {
    spinnerOverlay.classList.remove('hidden');
}

function hideGlobalSpinner() {
    spinnerOverlay.classList.add('hidden');
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

            <!-- Quick Stats -->
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
                            <p class="text-sm text-gray-500">Số lượng: ${item.quantity}</p>
                        </div>
                    </div>
                    <div class="text-right">
                        <p class="font-medium text-gray-900">${formatCurrency(item.totalPrice)}</p>
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
                 <!-- Placeholder for customer info if available -->
                 <p class="text-sm text-gray-600">Thời gian bắt đầu: ${timestamp}</p>
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
        // Display error message or default content if fetching fails
         cartArea.innerHTML = `
             <div class="p-5 border-b border-gray-200">
                <label class="block text-sm font-medium text-gray-700 mb-2">Thông tin khách hàng</label>
                <div class="relative">
                    <div class="absolute left-3 top-1/2 transform -translate-y-1/2 bg-blue-100 p-1.5 rounded-lg">
                        <i class="fas fa-user text-blue-600 text-sm"></i>
                    </div>
                    <input type="text" class="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all" placeholder="Tìm tên khách hàng...">
                </div>
            </div>
            <div class="flex-1 p-5">
               <p class="text-center text-red-500">Không thể tải chi tiết bàn: ${error.message}</p>
               ${defaultCartContent} // Optionally include quick stats
            </div>
            <div class="p-5 border-t border-gray-200 bg-white">
                 <div class="grid grid-cols-1 gap-3">
                    <button class="flex justify-center items-center px-6 py-3 bg-orange-500 text-white rounded-xl hover:opacity-90 transition-all font-medium shadow-lg">
                        <i class="fas fa-plus mr-2"></i>Thêm bàn mới
                    </button>
                    <div class="grid grid-cols-2 gap-3">
                        <button class="flex justify-center items-center px-4 py-3 bg-gray-500 text-white rounded-xl hover:opacity-90 transition-all font-medium">
                            <i class="fas fa-sync mr-2"></i>Làm mới
                        </button>
                        <button class="flex justify-center items-center px-4 py-3 bg-green-500 text-white rounded-xl hover:opacity-90 transition-all font-medium shadow-lg">
                            <i class="fas fa-chart-bar mr-2"></i>Báo cáo
                        </button>
                    </div>
                </div>
            </div>
         `;
    } finally {
        hideGlobalSpinner(); // Hide spinner
    }
}

// Hàm bắt đầu phục vụ bàn
async function startServing(tableId) {
    // Kiểm tra xem bàn đã có dữ liệu chưa
    const tableData = getTableData(tableId);
    if (tableData && tableData.items.length > 0) {
         // If table has items in localStorage, just view details
         viewTableDetails(tableId);
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
         // Show empty details panel after starting service
         viewTableDetails(tableId);

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
                  <button class="px-4 py-2 bg-orange-500 text-white text-sm font-medium rounded-xl hover:opacity-90 transition-all shadow-md" onclick="confirmBooking('${tableId}')">
                      <i class="fas fa-check mr-1"></i>Xác nhận
                  </button>
              `;
         }

         closeBookingModal();
         // No alert needed

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
async function confirmBooking(tableId) {
     showGlobalSpinner();
     try {
          // Fetch current table data to get tabNum
         const currentTableResponse = await fetch(`/sales/api/tables/${tableId}`);
         if (!currentTableResponse.ok) {
             throw new Error('Failed to fetch current table data');
         }
         const currentTableData = await currentTableResponse.json();

         // Assuming backend endpoint to update booking status or table status to OCCUPIED
         const response = await fetch(`/sales/api/tables/${tableId}`, { // Or booking update API
              method: 'PUT', // Or PUT/PATCH on booking API
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
                 throw new Error('Bàn hoặc đặt bàn không tồn tại ở Backend.');
               } else if (response.status === 409) {
                   throw new Error('Bàn đang có khách hoặc đã đặt (Backend).');
               }
              throw new Error('Lỗi khi xác nhận đặt bàn ở Backend.');
          }

         // Backend updated successfully, now update frontend UI
         const tableCard = document.querySelector(`[data-table="${tableId}"]`);
         if (tableCard) {
             // Update status
             tableCard.setAttribute('data-status', 'OCCUPIED');
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
         // Show empty details panel after confirming booking and starting service
         viewTableDetails(tableId);

     } catch (error) {
         console.error('Error confirming booking:', error);
         alert(error.message || 'Không thể xác nhận đặt bàn. Vui lòng thử lại.');
     } finally {
         hideGlobalSpinner();
         // Re-apply filtering after status change might be needed
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
});

function openPaymentModal(tableId) {
    currentPaymentTableId = tableId;
    const tableData = getTableData(tableId);
    if (!tableData || !tableData.items || tableData.items.length === 0) {
        alert('Không có món ăn nào để thanh toán!');
        return;
    }

    const totalAmount = tableData.items.reduce((sum, item) => sum + item.totalPrice, 0);
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
            btn.classList.remove('border-gray-200');
            btn.classList.add('border-orange-500', 'bg-orange-50');
        } else {
            btn.classList.remove('border-orange-500', 'bg-orange-50');
            btn.classList.add('border-gray-200');
        }
    });
}

async function processPayment() {
    if (!currentPaymentMethod) {
        alert('Vui lòng chọn phương thức thanh toán!');
        return;
    }

    showGlobalSpinner();
    try {
        const tableData = getTableData(currentPaymentTableId);
        console.log('Table Data:', tableData); // Debug log

        const totalAmount = tableData.items.reduce((sum, item) => sum + item.totalPrice, 0);

        // Prepare receipt data with details as an array
        const receiptData = {
            receipt: {
                tabId: parseInt(currentPaymentTableId),
                recTime: new Date().toISOString(),
                isdeleted: false,
                paymentMethod: currentPaymentMethod
            },
            details: tableData.items.map(item => {
                console.log('Item before mapping:', item); // Debug log
                return {
                    itemId: parseInt(item.itemId),
                    quantity: parseInt(item.quantity)
                };
            })
        };
        console.log('Receipt Data to send:', receiptData); // Debug log

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

        // Update table status to EMPTY
        const updateTableResponse = await fetch(`http://localhost:8080/tables/${currentPaymentTableId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                tabNum: tableData.tabNum,
                tabStatus: 'EMPTY',
                isdeleted: false
            })
        });

        if (!updateTableResponse.ok) {
            throw new Error(`HTTP error! status: ${updateTableResponse.status}`);
        }

        alert('Thanh toán thành công!');
        // Reset table data
        resetTableData(currentPaymentTableId);
        // Close payment modal
        closePaymentModal();
        // Refresh the page to show updated table status
        window.location.reload();

    } catch (error) {
        console.error('Error processing payment:', error);
        alert('Có lỗi xảy ra khi thanh toán: ' + error.message);
    } finally {
        hideGlobalSpinner();
    }
} 