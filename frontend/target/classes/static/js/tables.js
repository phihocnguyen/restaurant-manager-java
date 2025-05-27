// Mock data for orders
const mockOrders = {
    'Bàn 1': {
        items: [
            { name: 'Phở bò', quantity: 2, price: 65000 },
            { name: 'Cơm rang hải sản', quantity: 1, price: 85000 },
            { name: 'Coca Cola', quantity: 3, price: 15000 }
        ],
        customer: 'Nguyễn Văn A',
        time: '14:30'
    },
    'Bàn 3': {
        items: [
            { name: 'Bún chả', quantity: 2, price: 55000 },
            { name: 'Nem rán', quantity: 1, price: 45000 },
            { name: 'Trà đá', quantity: 2, price: 10000 }
        ],
        customer: 'Trần Thị B',
        time: '15:15'
    },
    'Bàn 4': {
        items: [
            { name: 'Lẩu thái', quantity: 1, price: 299000 },
            { name: 'Tôm sú nướng', quantity: 2, price: 159000 },
            { name: 'Bia Heineken', quantity: 4, price: 25000 }
        ],
        customer: 'Lê Văn C',
        time: '18:00',
        isBooking: true
    }
};

// Helper: Reset all 'Đã chọn' buttons to 'Xem chi tiết' (or 'Xác nhận' for booking)
function resetSelectedButtons() {
    document.querySelectorAll('.table-card').forEach(card => {
        const status = card.getAttribute('data-status');
        const actionBtn = card.querySelector('.view-details-btn, .confirm-btn');
        if (actionBtn) {
            if (status === 'active') {
                actionBtn.innerHTML = '<i class="fas fa-eye mr-1.5"></i> Xem chi tiết';
                actionBtn.className = 'w-full px-4 py-2.5 bg-indigo-600 text-white text-sm font-medium rounded-xl hover:bg-indigo-700 transition-all shadow-md flex items-center justify-center view-details-btn';
            } else if (status === 'booking') {
                actionBtn.innerHTML = '<i class="fas fa-check mr-1.5"></i> Xác nhận';
                actionBtn.className = 'w-full px-4 py-2.5 bg-green-500 text-white text-sm font-medium rounded-xl hover:bg-green-600 transition-all shadow-md flex items-center justify-center confirm-btn';
            }
        }
    });
}

// Function to show table details in right panel
function showTableDetails(tableNumber, status) {
    resetSelectedButtons();
    // Highlight the selected button
    document.querySelectorAll('.table-card').forEach(card => {
        const h3 = card.querySelector('h3');
        if (h3 && h3.textContent.trim() === tableNumber) {
            const statusNow = card.getAttribute('data-status');
            let btn;
            if (statusNow === 'active') {
                btn = card.querySelector('.view-details-btn');
                if (btn) {
                    btn.innerHTML = '<i class="fas fa-check mr-1.5"></i> Đã chọn';
                    btn.className = 'w-full px-4 py-2.5 bg-green-500 text-white text-sm font-medium rounded-xl transition-all shadow-md flex items-center justify-center view-details-btn';
                }
            } else if (statusNow === 'booking') {
                btn = card.querySelector('.confirm-btn');
                if (btn) {
                    btn.innerHTML = '<i class="fas fa-check mr-1.5"></i> Xác nhận';
                    btn.className = 'w-full px-4 py-2.5 bg-green-500 text-white text-sm font-medium rounded-xl hover:bg-green-600 transition-all shadow-md flex items-center justify-center confirm-btn';
                }
            }
        }
    });
    const noTableSelected = document.getElementById('noTableSelected');
    const tableDetails = document.getElementById('tableDetails');
    const selectedTableTitle = document.getElementById('selectedTableTitle');
    const selectedTableStatus = document.getElementById('selectedTableStatus');
    const customerInfo = document.getElementById('customerInfo');
    const bookingInfo = document.getElementById('bookingInfo');
    const orderItems = document.getElementById('orderItems');
    noTableSelected.classList.add('hidden');
    tableDetails.classList.remove('hidden');
    selectedTableTitle.textContent = tableNumber;
    const orderData = mockOrders[tableNumber];
    if (orderData) {
        customerInfo.textContent = `Khách hàng: ${orderData.customer}`;
        bookingInfo.textContent = `Thời gian: ${orderData.time}`;
        orderItems.innerHTML = '';
        orderData.items.forEach(item => {
            const itemElement = document.createElement('div');
            itemElement.className = 'flex items-center justify-between p-3 bg-white rounded-xl shadow-sm';
            itemElement.innerHTML = `
                <div class="flex items-center">
                    <div class="bg-blue-100 p-2 rounded-lg mr-3">
                        <i class="fas fa-utensils text-blue-600"></i>
                    </div>
                    <div>
                        <h4 class="font-medium text-gray-900">${item.name}</h4>
                        <p class="text-sm text-gray-500">Số lượng: ${item.quantity}</p>
                    </div>
                </div>
                <span class="font-medium text-gray-900">${(item.price * item.quantity).toLocaleString()}đ</span>
            `;
            orderItems.appendChild(itemElement);
        });
        const total = orderData.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        const totalElement = document.createElement('div');
        totalElement.className = 'flex items-center justify-between p-3 bg-gradient-to-r from-indigo-50 to-purple-50 rounded-xl mt-4';
        totalElement.innerHTML = `
            <span class="font-bold text-gray-900">Tổng cộng:</span>
            <span class="font-bold text-lg bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                ${total.toLocaleString()}đ
            </span>
        `;
        orderItems.appendChild(totalElement);
    } else {
        customerInfo.textContent = 'Khách hàng: Chưa có thông tin';
        bookingInfo.textContent = 'Thời gian: --:--';
        orderItems.innerHTML = '<p class="text-center text-gray-500 py-4">Chưa có món ăn nào được đặt</p>';
    }
    if (status === 'active') {
        selectedTableStatus.textContent = 'Đang phục vụ';
        selectedTableStatus.parentElement.className = 'px-3 py-1 bg-green-100 text-green-800 text-sm font-medium rounded-full';
    } else if (status === 'booking') {
        selectedTableStatus.textContent = 'Đã đặt';
        selectedTableStatus.parentElement.className = 'px-3 py-1 bg-orange-100 text-orange-800 text-sm font-medium rounded-full';
    }
}

// Function to start serving a table
function startServing(tableNumber) {
    const tableCard = Array.from(document.querySelectorAll('.table-card')).find(card => 
        card.querySelector('h3').textContent.trim() === tableNumber
    );
    
    if (tableCard) {
        const ellipsisButton = tableCard.querySelector('.relative button i.fa-ellipsis-v');
        changeTableStatus(ellipsisButton, 'active');
        showTableDetails(tableNumber, 'active');
    }
}

// Function to change table status
function changeTableStatus(elementWithinCard, newStatus) {
    const tableCard = elementWithinCard.closest('.table-card');
    if (!tableCard) return;
    const statusBadge = tableCard.querySelector('.rounded-full');
    const statusIcon = statusBadge.querySelector('i');
    const statusText = statusBadge.childNodes[1];
    const actionButtonContainer = tableCard.querySelector('.flex.items-center.justify-between');
    const ellipsisButton = tableCard.querySelector('.relative button i.fa-ellipsis-v');
    const tableNumber = tableCard.querySelector('h3').textContent.trim();
    tableCard.setAttribute('data-status', newStatus);
    if (newStatus === 'active') {
        statusBadge.classList.remove('bg-gray-100', 'text-gray-800', 'bg-orange-100', 'text-orange-800');
        statusBadge.classList.add('bg-green-100', 'text-green-800');
        statusIcon.classList.remove('text-gray-500', 'text-orange-500', 'fas fa-calendar-alt');
        statusIcon.classList.add('text-green-500', 'fas fa-utensils');
        statusText.textContent = 'Đang phục vụ';
        tableCard.querySelector('.h-48').classList.remove('table-empty', 'table-booking');
        tableCard.querySelector('.h-48').classList.add('table-active');
        if (ellipsisButton) ellipsisButton.closest('button').style.display = 'none';
        if (actionButtonContainer) {
            actionButtonContainer.innerHTML = `
                <button class="w-full px-4 py-2.5 bg-indigo-600 text-white text-sm font-medium rounded-xl hover:bg-indigo-700 transition-all shadow-md flex items-center justify-center view-details-btn">
                    <i class="fas fa-eye mr-1.5"></i>
                    Xem chi tiết
                </button>
            `;
            actionButtonContainer.querySelector('.view-details-btn').onclick = () => showTableDetails(tableNumber, 'active');
        }
        showTableDetails(tableNumber, 'active');
    } else if (newStatus === 'empty') {
        statusBadge.classList.remove('bg-green-100', 'text-green-800', 'bg-orange-100', 'text-orange-800');
        statusBadge.classList.add('bg-gray-100', 'text-gray-800');
        statusIcon.classList.remove('text-green-500', 'text-orange-500', 'fas fa-utensils', 'fas fa-calendar-alt');
        statusIcon.classList.add('text-gray-500', 'fas fa-chair');
        statusText.textContent = 'Trống';
        tableCard.querySelector('.h-48').classList.remove('table-active', 'table-booking');
        tableCard.querySelector('.h-48').classList.add('table-empty');
        if (ellipsisButton) ellipsisButton.closest('button').style.display = 'block';
        if (actionButtonContainer) {
            actionButtonContainer.innerHTML = `
                <button class="w-1/2 px-3 py-2.5 bg-indigo-600 text-white text-sm font-medium rounded-xl hover:bg-indigo-700 transition-all shadow-md flex items-center justify-center" onclick="openBookingModal('${tableNumber}')">
                    <i class="fas fa-calendar-plus mr-1.5"></i>
                    Đặt bàn
                </button>
                <button class="w-1/2 px-3 py-2.5 bg-green-500 text-white text-sm font-medium rounded-xl hover:bg-green-600 transition-all shadow-md flex items-center justify-center" onclick="startServing('${tableNumber}')">
                    <i class="fas fa-utensils mr-1.5"></i>
                    Phục vụ
                </button>
            `;
        }
    } else if (newStatus === 'booking') {
        statusBadge.classList.remove('bg-green-100', 'text-green-800', 'bg-gray-100', 'text-gray-800');
        statusBadge.classList.add('bg-orange-100', 'text-orange-800');
        statusIcon.classList.remove('text-green-500', 'text-gray-500', 'fas fa-utensils', 'fas fa-chair');
        statusIcon.classList.add('text-orange-500', 'fas fa-calendar-alt');
        statusText.textContent = 'Đã đặt';
        tableCard.querySelector('.h-48').classList.remove('table-active', 'table-empty');
        tableCard.querySelector('.h-48').classList.add('table-booking');
        if (ellipsisButton) ellipsisButton.closest('button').style.display = 'none';
        if (actionButtonContainer) {
            actionButtonContainer.innerHTML = `
                <button class="w-full px-4 py-2.5 bg-green-500 text-white text-sm font-medium rounded-xl hover:bg-green-600 transition-all shadow-md flex items-center justify-center confirm-btn">
                    <i class="fas fa-check mr-1.5"></i>
                    Xác nhận
                </button>
            `;
            actionButtonContainer.querySelector('.confirm-btn').onclick = () => {
                changeTableStatus(actionButtonContainer.querySelector('.confirm-btn'), 'active');
            };
        }
        showTableDetails(tableNumber, 'booking');
    }
}

// Function to handle booking modal
function openBookingModal(tableNumber) {
    document.getElementById('bookingModal').classList.remove('hidden');
    document.getElementById('bookingModal').classList.add('flex');
    document.getElementById('tableNumber').value = tableNumber;
    document.getElementById('bookingModal').dataset.currentTableId = `table-${tableNumber.replace('Bàn ', '').trim()}`;
}

function closeBookingModal() {
    document.getElementById('bookingModal').classList.add('hidden');
    document.getElementById('bookingModal').classList.remove('flex');
    document.getElementById('bookingForm').reset();
    document.getElementById('bookingModal').dataset.currentTableId = '';
}

// Initialize event handlers when document is ready
document.addEventListener('DOMContentLoaded', function() {
    // Tab switching functionality
    document.querySelectorAll('nav a[data-status]').forEach(tab => {
        tab.addEventListener('click', function(e) {
            e.preventDefault();
            const status = this.getAttribute('data-status');
            
            // Remove active state from all tabs
            document.querySelectorAll('nav a[data-status]').forEach(t => {
                t.classList.remove('bg-gradient-to-r', 'from-indigo-500', 'to-purple-600', 'text-white', 'shadow-md');
                t.classList.add('text-gray-600', 'hover:bg-gray-50');
            });
            
            // Add active state to clicked tab
            this.classList.remove('text-gray-600', 'hover:bg-gray-50');
            this.classList.add('bg-gradient-to-r', 'from-indigo-500', 'to-purple-600', 'text-white', 'shadow-md');
            
            // Filter tables
            document.querySelectorAll('.table-card').forEach(card => {
                if (status === card.getAttribute('data-status') || status === 'active') {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    });

    // Search functionality
    document.querySelector('input[placeholder*="Tìm tên bàn"]').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        document.querySelectorAll('.table-card').forEach(card => {
            const title = card.querySelector('h3').textContent.toLowerCase();
            card.style.display = title.includes(searchTerm) ? 'flex' : 'none';
        });
    });

    // Initialize view details buttons
    document.querySelectorAll('.table-card').forEach(card => {
        const viewDetailsBtn = card.querySelector('button:has(.fa-eye)');
        if (viewDetailsBtn) {
            viewDetailsBtn.onclick = () => {
                const tableNumber = card.querySelector('h3').textContent.trim();
                const status = card.getAttribute('data-status');
                showTableDetails(tableNumber, status);
            };
        }
        // For booking: add confirm button logic
        const confirmBtn = card.querySelector('button:has(.fa-check)');
        if (confirmBtn && card.getAttribute('data-status') === 'booking') {
            confirmBtn.onclick = () => {
                const tableNumber = card.querySelector('h3').textContent.trim();
                changeTableStatus(confirmBtn, 'active');
            };
        }
    });

    // Handle booking form submission
    document.getElementById('bookingForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const tableId = document.getElementById('bookingModal').dataset.currentTableId;
        const tableCard = document.getElementById(tableId);
        if (tableCard) {
            const tableNumber = tableCard.querySelector('h3').textContent.trim();
            changeTableStatus(tableCard.querySelector('.relative button i.fa-ellipsis-v'), 'booking');
            closeBookingModal();
            alert('Đặt bàn thành công!');
        } else {
            console.error('Could not find table card for status update.');
            closeBookingModal();
            alert('Đặt bàn thất bại!');
        }
    });

    // Close modal when clicking outside
    document.getElementById('bookingModal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeBookingModal();
        }
    });
}); 