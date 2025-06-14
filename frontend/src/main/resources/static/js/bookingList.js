// Create spinner overlay
const spinnerOverlay = document.createElement('div');
spinnerOverlay.id = 'globalSpinnerOverlay';
spinnerOverlay.className = 'fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[100] hidden';
spinnerOverlay.innerHTML = '<div class="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>';

// Add spinner to body when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    document.body.appendChild(spinnerOverlay);
});

// Spinner helper functions
function showGlobalSpinner() {
    console.log("Showing global spinner...");
    spinnerOverlay.classList.remove('hidden');
}

function hideGlobalSpinner() {
    console.log("Hiding global spinner...");
    spinnerOverlay.classList.add('hidden');
}

function openModal(modalId) {
    document.getElementById(modalId).classList.remove('hidden');
    document.body.classList.add('modal-open'); // Prevent scrolling
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
    document.body.classList.remove('modal-open'); // Re-enable scrolling
}

// Function to open booking list modal
function openBookingListModal() {
    // Create modal if it doesn't exist
    let modal = document.getElementById('bookingListModal');
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'bookingListModal';
        modal.className = 'fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden';
        modal.innerHTML = `
            <div class="relative top-20 mx-auto p-5 border w-3/4 shadow-lg rounded-md bg-white">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-xl font-bold text-gray-900">Danh sách đặt bàn</h3>
                    <button onclick="closeBookingListModal()" class="text-gray-400 hover:text-gray-500">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">STT</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Khách hàng</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Bàn</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Thời gian</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Trạng thái</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody id="bookingList" class="bg-white divide-y divide-gray-200">
                            <!-- Booking list will be loaded here -->
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
    }

    // Show modal
    openModal('bookingListModal');

    // Load booking list
    loadBookingList();
}

function closeBookingListModal() {
    const modal = document.getElementById('bookingListModal');
    if (modal) {
        modal.classList.add('hidden');
        document.body.classList.remove('modal-open');
    }
}

// Function to load booking list
async function loadBookingList() {
    try {
        showGlobalSpinner();
        const response = await fetch('http://localhost:8080/bookings');
        if (!response.ok) {
            throw new Error('Failed to fetch bookings');
        }
        const bookings = await response.json();

        // Filter only pending bookings (status = 0)
        const pendingBookings = bookings.filter(booking => booking.status === 0);

        const bookingListBody = document.getElementById('bookingList');
        bookingListBody.innerHTML = pendingBookings.map((booking, index) => {
            const date = new Date(booking.startTime);
            const options = {
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit', second: '2-digit',
                hour12: false, // 24-hour format
                timeZone: 'UTC' // Explicitly set timezone to UTC
            };
            const formattedTime = date.toLocaleString('en-US', options); // Using en-US for standard format

            return `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${index + 1}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${booking.customer ? booking.customer.name : 'N/A'}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Bàn ${booking.table.id}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${formattedTime}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                        Chờ xác nhận
                    </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button onclick="confirmBooking(${booking.id})" class="text-green-600 hover:text-green-900 mr-3">
                        <i class="fas fa-check"></i>
                    </button>
                    <button onclick="cancelBooking(${booking.id})" class="text-red-600 hover:text-red-900">
                        <i class="fas fa-times"></i>
                    </button>
                </td>
            </tr>
        `;
        }).join('');
    } catch (error) {
        console.error('Error loading booking list:', error);
        alert('Không thể tải danh sách đặt bàn');
    } finally {
        hideGlobalSpinner();
    }
}

// Function to confirm booking
async function confirmBooking(bookingId) {
    try {
        showGlobalSpinner();
        const response = await fetch(`http://localhost:8080/bookings/${bookingId}/status?status=1`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to confirm booking');
        }

        // Get booking details to save customer info
        const bookingDetailsResponse = await fetch(`http://localhost:8080/bookings/${bookingId}`);
        if (!bookingDetailsResponse.ok) {
            throw new Error('Failed to get booking details');
        }
        const bookingDetails = await bookingDetailsResponse.json();
        
        // Save customer info and start time to localStorage
        const tableId = bookingDetails.table.id;
        const customerInfoAndBookingTime = {
            id: bookingDetails.customer.id,
            name: bookingDetails.customer.name,
            startTime: bookingDetails.startTime // Add start time here
        };
        localStorage.setItem(`table_${tableId}_customer`, JSON.stringify(customerInfoAndBookingTime));

        // Reload booking list
        loadBookingList();
        // Reload page to update table status
        window.location.reload();
    } catch (error) {
        console.error('Error confirming booking:', error);
        alert('Không thể xác nhận đặt bàn');
    } finally {
        hideGlobalSpinner();
    }
}

// Function to load customer info for all tables
function loadCustomerInfoForTables() {
    document.querySelectorAll('.table-card').forEach(tableCard => {
        const tableId = tableCard.getAttribute('data-table');
        const customerInfoAndBookingTime = localStorage.getItem(`table_${tableId}_customer`);
        
        if (customerInfoAndBookingTime) {
            const { name, startTime } = JSON.parse(customerInfoAndBookingTime); // Destructure startTime
            const customerNameElement = tableCard.querySelector('.customer-name-reserved'); // Use the new class
            const reservationTimeElement = tableCard.querySelector('.reservation-time-reserved'); // Use the new class

            if (customerNameElement) {
                customerNameElement.textContent = name;
            }
            if (reservationTimeElement && startTime) {
                const date = new Date(startTime);
                const options = {
                    year: 'numeric', month: '2-digit', day: '2-digit',
                    hour: '2-digit', minute: '2-digit', second: '2-digit',
                    hour12: false,
                    timeZone: 'UTC' // Explicitly set timezone to UTC
                };
                reservationTimeElement.textContent = date.toLocaleString('en-US', options);
            }
        } else {
            // If no customer info in localStorage, reset to default (Khách vãng lai, N/A)
            const customerNameElement = tableCard.querySelector('.customer-name-reserved');
            const reservationTimeElement = tableCard.querySelector('.reservation-time-reserved');
            if (customerNameElement) {
                customerNameElement.textContent = 'Khách vãng lai';
            }
            if (reservationTimeElement) {
                reservationTimeElement.textContent = 'N/A';
            }
        }
    });
}

// Call loadCustomerInfoForTables when page loads
document.addEventListener('DOMContentLoaded', loadCustomerInfoForTables);

// Function to cancel booking
async function cancelBooking(bookingId) {
    if (!confirm('Bạn có chắc chắn muốn hủy đặt bàn này?')) {
        return;
    }

    try {
        showGlobalSpinner();
        const response = await fetch(`http://localhost:8080/bookings/${bookingId}/status?status=2`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to cancel booking');
        }

        // Get booking details to remove customer info
        const bookingDetailsResponse = await fetch(`http://localhost:8080/bookings/${bookingId}`);
        if (!bookingDetailsResponse.ok) {
            throw new Error('Failed to get booking details');
        }
        const bookingDetails = await bookingDetailsResponse.json();
        
        // Remove customer info from localStorage
        const tableId = bookingDetails.table.id;
        localStorage.removeItem(`table_${tableId}_customer`);

        // Reload booking list
        loadBookingList();
        // Reload page to update table status
        window.location.reload();
    } catch (error) {
        console.error('Error canceling booking:', error);
        alert('Không thể hủy đặt bàn');
    } finally {
        hideGlobalSpinner();
    }
} 