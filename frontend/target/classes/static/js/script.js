const spinnerOverlay = document.createElement('div');
spinnerOverlay.id = 'globalSpinnerOverlay';
spinnerOverlay.className = 'fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[100] hidden';
spinnerOverlay.innerHTML = '<div class="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>';

function showGlobalSpinner() {
    spinnerOverlay.classList.remove('hidden');
}

function hideGlobalSpinner() {
    spinnerOverlay.classList.add('hidden');
}

document.addEventListener('DOMContentLoaded', function() {
    document.body.appendChild(spinnerOverlay);

    // Lấy các phần tử DOM
    const loginBtn = document.getElementById('login-btn');
    const userProfileMenu = document.getElementById('user-profile-menu');
    const avatarButton = document.getElementById('avatar-button');
    const userInitial = document.getElementById('user-initial');
    const dropdownMenu = document.getElementById('dropdown-menu');
    const logoutButton = document.getElementById('logout-button');

    // Để thử nghiệm, bạn có thể đặt một đối tượng giả trong localStorage
    // localStorage.setItem('loggedInUser', JSON.stringify({ name: 'User', email: 'user@example.com' }));
    
    // Kiểm tra xem thông tin người dùng có trong localStorage không
    const loggedInUser = localStorage.getItem('loggedInUser');

    if (loggedInUser) {
        // Phân tích cú pháp chuỗi JSON để lấy đối tượng người dùng
        const user = JSON.parse(loggedInUser);

        // Ẩn nút Login và hiển thị menu avatar
        if (loginBtn) loginBtn.classList.add('hidden');
        if (userProfileMenu) userProfileMenu.classList.remove('hidden');

        // Đặt ký tự đầu tiên của tên người dùng lên avatar
        // Giả sử đối tượng người dùng có thuộc tính 'name'
        if (user.name && userInitial) {
            userInitial.textContent = user.name.charAt(0).toUpperCase();
        } else {
            userInitial.textContent = 'U'; // Ký tự mặc định nếu không có tên
        }
    } else {
        // Nếu không có người dùng đăng nhập, đảm bảo nút Login hiển thị và menu avatar bị ẩn
        if (loginBtn) loginBtn.classList.remove('hidden');
        if (userProfileMenu) userProfileMenu.classList.add('hidden');
    }

    // Xử lý bật/tắt dropdown khi nhấp vào avatar
    if (avatarButton) {
        avatarButton.addEventListener('click', function() {
            const isHidden = dropdownMenu.classList.contains('hidden');
            if (isHidden) {
                dropdownMenu.classList.remove('hidden');
                // Thêm class để kích hoạt animation
                requestAnimationFrame(() => {
                    dropdownMenu.classList.add('dropdown-enter-active');
                    dropdownMenu.classList.remove('dropdown-enter');
                });
            } else {
                // Thêm class để kích hoạt animation thoát
                dropdownMenu.classList.add('dropdown-exit-active');
                dropdownMenu.classList.remove('dropdown-enter-active');
                dropdownMenu.addEventListener('transitionend', () => {
                    dropdownMenu.classList.add('hidden');
                    dropdownMenu.classList.remove('dropdown-exit-active');
                }, { once: true });
            }
        });
    }

    // Xử lý đăng xuất khi nhấp vào nút Logout
    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault(); // Ngăn hành vi mặc định của thẻ <a>

            // Xóa thông tin người dùng khỏi localStorage
            localStorage.removeItem('loggedInUser');

            // Tải lại trang để cập nhật giao diện
            window.location.href = '/'; // Hoặc window.location.reload();
        });
    }

    // Đóng dropdown nếu nhấp ra ngoài
    window.addEventListener('click', function(event) {
        if (!userProfileMenu.contains(event.target)) {
            if (!dropdownMenu.classList.contains('hidden')) {
                dropdownMenu.classList.add('hidden');
            }
        }
    });
});

function openModal(modalId) {
    document.getElementById(modalId).classList.remove('hidden');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
}

// Mobile Menu Toggle
const mobileMenuBtn = document.getElementById("mobile-menu-btn");
const mobileMenu = document.getElementById("mobileMenu");
const closeMobileMenu = document.getElementById("closeMobileMenu");

if (mobileMenuBtn && mobileMenu && closeMobileMenu) {
    mobileMenuBtn.addEventListener("click", () => {
        mobileMenu.classList.toggle("active");
    });

    closeMobileMenu.addEventListener("click", () => {
        mobileMenu.classList.remove("active");
    });
}

// Scroll to Top Button
const scrollToTopBtn = document.createElement("div");
scrollToTopBtn.className = "scroll-to-top";
scrollToTopBtn.innerHTML = "↑";
document.body.appendChild(scrollToTopBtn);

scrollToTopBtn.addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
});

// Floating Particles (simple, no GSAP)
function createParticle() {
    const particle = document.createElement("div");
    particle.className = "particle";
    const size = Math.random() * 10 + 5;
    particle.style.width = `${size}px`;
    particle.style.height = `${size}px`;
    particle.style.left = `${Math.random() * 100}vw`;
    particle.style.top = `${Math.random() * 100}vh`;
    document.getElementById("particles-container").appendChild(particle);

    // Simple animation
    particle.animate([
        { transform: "translateY(0)" },
        { transform: "translateY(-100vh)" }
    ], {
        duration: Math.random() * 10000 + 10000,
        iterations: 1,
        easing: "linear"
    }).onfinish = () => {
        particle.remove();
        createParticle();
    };
}
for (let i = 0; i < 10; i++) {
    setTimeout(createParticle, i * 1000);
}

// Loading Screen Animation
document.addEventListener('DOMContentLoaded', function() {
    const loadingScreen = document.getElementById('loadingScreen');
    const loadingParticles = document.getElementById('loadingParticles');
    
    // Create loading particles
    for (let i = 0; i < 30; i++) {
        const particle = document.createElement('div');
        particle.className = 'loading-particle';
        const size = Math.random() * 8 + 4;
        particle.style.width = size + 'px';
        particle.style.height = size + 'px';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.top = Math.random() * 100 + '%';
        particle.style.animationDelay = Math.random() * 3 + 's';
        particle.style.opacity = Math.random() * 0.5 + 0.3;
        loadingParticles.appendChild(particle);
    }

    // Hide loading screen after delay
    setTimeout(() => {
        if (loadingScreen) {
            loadingScreen.classList.add('hidden');
            setTimeout(() => {
                loadingScreen.style.display = 'none';
            }, 1200);
        }
    }, 5000);
});

// Reservation Table Map Logic
// Reservation Table Map Logic
// FILE SCRIPT.JS - PHIÊN BẢN ĐÃ SỬA
document.addEventListener('DOMContentLoaded', function() {
    const reservationForm = document.getElementById('reservationForm'); // Lấy cả form
    const checkAvailabilityBtn = document.querySelector('#reservationForm button[type="button"]');
    const tableMapGrid = document.getElementById('table-map-grid');
    const availabilityMessage = document.getElementById('availability-message');
    const resDateInput = document.getElementById('res-date');
    const resTimeInput = document.getElementById('res-time');
    const resGuestsInput = document.getElementById('res-guests');
    const availableGuestsSpan = document.getElementById('available-guests');
    const availableDateSpan = document.getElementById('available-date');
    const availableTimeSpan = document.getElementById('available-time');
    const hiddenSelectedTableId = document.getElementById('selected-table-id'); // Lấy trường input ẩn

    if (checkAvailabilityBtn) {
        checkAvailabilityBtn.addEventListener('click', async function() {
            const selectedDate = resDateInput.value;
            const selectedTime = resTimeInput.value;
            const selectedGuests = resGuestsInput.value;

            if (!selectedDate || !selectedTime || !selectedGuests) {
                alert('Vui lòng chọn ngày, giờ và số người.');
                return;
            }

            availabilityMessage.classList.remove('hidden');
            tableMapGrid.classList.remove('hidden');
            
            availableGuestsSpan.innerText = selectedGuests;
            availableDateSpan.innerText = selectedDate;
            availableTimeSpan.innerText = selectedTime;
            
            tableMapGrid.innerHTML = '<p class="col-span-full text-center">Đang tải danh sách bàn trống...</p>';

            try {
                const response = await fetch('http://localhost:8080/tables');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const allTables = await response.json();
                const emptyTables = allTables.filter(table => table.tabStatus === 'EMPTY' && !table.isdeleted);

                tableMapGrid.innerHTML = '';

                if (emptyTables.length > 0) {
                    emptyTables.forEach(table => {
                        const tableElement = document.createElement('button');
                        tableElement.type = 'button';
                        // Gỡ bỏ các lớp focus, chỉ giữ lại hover. Trạng thái chọn sẽ do lớp .table-selected quản lý
                        tableElement.className = 'table-item p-4 border border-gray-300 rounded-lg text-center cursor-pointer hover:bg-green-200 transition-colors';
                        tableElement.dataset.tableId = table.id; // Lưu id của bàn

                        // Sử dụng tabNum để hiển thị thay vì id, thân thiện hơn
                        const tableNumber = table.id || 'N/A'; 
                        tableElement.innerHTML = `
                            <span class="font-bold text-lg">Bàn ${tableNumber}</span>
                            <br>
                            <span class="text-sm text-green-600">Trống</span>
                        `;
                        
                        tableMapGrid.appendChild(tableElement);
                    });

                    // LOGIC QUẢN LÝ VIỆC CHỌN BÀN
                    const allTableButtons = tableMapGrid.querySelectorAll('.table-item');
                    allTableButtons.forEach(button => {
                        button.addEventListener('click', function(event) {
                            // 1. Bỏ chọn tất cả các bàn khác
                            allTableButtons.forEach(btn => btn.classList.remove('table-selected'));

                            // 2. Thêm lớp 'table-selected' cho bàn vừa được click
                            const clickedTable = event.currentTarget;
                            clickedTable.classList.add('table-selected');

                            // 3. Lưu ID của bàn đã chọn vào trường input ẩn
                            hiddenSelectedTableId.value = clickedTable.dataset.tableId;
                        });
                    });

                } else {
                    tableMapGrid.innerHTML = '<p class="col-span-full text-center text-red-500">Không tìm thấy bàn nào trống.</p>';
                }

            } catch (error) {
                console.error("Lỗi khi fetch bàn:", error);
                tableMapGrid.innerHTML = `<p class="col-span-full text-center text-red-500">Không thể tải danh sách bàn. Vui lòng thử lại.</p>`;
            }
        });
    }

    // Xử lý khi submit form
    if (reservationForm) {
        reservationForm.addEventListener('submit', async function(event) {
            // Ngăn form gửi đi ngay lập tức
            event.preventDefault(); 
            
            // Kiểm tra đăng nhập
            const loggedInUser = localStorage.getItem('loggedInUser');
            if (!loggedInUser) {
                alert('Bạn cần phải đăng nhập trước khi đặt bàn');
                window.location.href = '/login';
                return;
            }

            if (!hiddenSelectedTableId.value) {
                alert('Vui lòng chọn một bàn trước khi xác nhận.');
                return;
            }

            // Parse user data từ localStorage
            const userData = JSON.parse(loggedInUser);

            console.log('loggedInUser raw:', loggedInUser); // Debug raw localStorage string
            console.log('userData parsed:', userData); // Debug parsed object

            // Lấy email từ userData để tìm customerId
            const customerEmail = userData.accEmail;
            if (!customerEmail) {
                showNotification('Không tìm thấy email người dùng. Vui lòng thử đăng nhập lại.', 'error');
                hideGlobalSpinner();
                return;
            }

            let customerId = null;
            try {
                // Fetch customer data by email
                const customerResponse = await fetch(`http://localhost:8080/customers/email/${customerEmail}`);
                if (!customerResponse.ok) {
                    throw new Error('Không thể lấy thông tin khách hàng');
                }
                const customerData = await customerResponse.json();
                customerId = customerData.id;
            } catch (error) {
                console.error('Lỗi khi lấy customerId:', error);
                showNotification('Không thể lấy thông tin khách hàng. Vui lòng thử lại.', 'error');
                hideGlobalSpinner();
                return;
            }

            // Nếu không có customerId sau khi fetch, thì có lỗi. Thông báo cho người dùng.
            if (!customerId) {
                showNotification('Không tìm thấy ID khách hàng. Vui lòng đăng nhập lại.', 'error');
                hideGlobalSpinner();
                return;
            }

            // Lấy tất cả dữ liệu từ form
            const formData = new FormData(reservationForm);
            const reservationData = Object.fromEntries(formData.entries());

            console.log('Reservation Date:', reservationData['res-date']);
            console.log('Reservation Time:', reservationData['res-time']);

            const bookingData = {
                employeeId: 8,
                customerId: customerId,
                tableId: parseInt(hiddenSelectedTableId.value),
                startTime: new Date(`${reservationData['res-date']}T${reservationData['res-time']}:00Z`).toISOString(),
                endTime: new Date(new Date(`${reservationData['res-date']}T${reservationData['res-time']}:00Z`).getTime() + (2 * 60 * 60 * 1000)).toISOString(), // Cộng thêm 2 giờ
                status: 0
            };

            try {
                // Hiển thị spinner
                const spinner = document.getElementById('spinner');
                if (spinner) spinner.classList.remove('hidden');

                const response = await fetch('http://localhost:8080/bookings', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(bookingData)
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const result = await response.json();
                
                if (spinner) spinner.classList.add('hidden');

                const successModal = document.getElementById('successModal');
                if (successModal) {
                    successModal.classList.remove('hidden');
                    setTimeout(() => {
                        successModal.classList.add('hidden');
                    }, 3000);
                } else {
                    alert('Đặt bàn thành công! Vui lòng chờ xác nhận từ nhà hàng.');
                }
            } catch (error) {
                const spinner = document.getElementById('spinner');
                if (spinner) spinner.classList.add('hidden');

                console.error('Lỗi khi đặt bàn:', error);
                alert('Có lỗi xảy ra khi đặt bàn. Vui lòng thử lại sau.');
            }
        });
    }
});

// Counter Animation with adjusted duration
function animateCounter(element) {
    const target = parseInt(element.getAttribute('data-target'));
    const duration = 5000; // 5 seconds
    const step = target / (duration / 16); // 60fps
    let current = 0;

    const updateCounter = () => {
        current += step;
        if (current < target) {
            element.textContent = Math.floor(current).toLocaleString();
            requestAnimationFrame(updateCounter);
        } else {
            element.textContent = target.toLocaleString();
        }
    };

    updateCounter();
}

// Scroll Animation Observer
const scrollObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
            // If it's the stats container, start the counter animation
            if (entry.target.classList.contains('stats-container')) {
                const counters = entry.target.querySelectorAll('.counter');
                counters.forEach(counter => {
                    animateCounter(counter);
                });
            }
        }
    });
}, { 
    threshold: 0.2, // Trigger when 20% of the element is visible
    rootMargin: '-50px'
});

// Observe all sections with fade-in classes
document.querySelectorAll('.fade-in, .fade-in-left, .fade-in-right, .stats-container').forEach(element => {
    scrollObserver.observe(element);
});

// Function to check login status and update UI
function updateAuthUI() {
    const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));
    const loginButton = document.getElementById('loginButton');
    const avatarDropdown = document.getElementById('avatarDropdown');
    const avatarButton = document.getElementById('avatarButton');
    
    if (loggedInUser) {
        // User is logged in
        loginButton.classList.add('hidden');
        avatarDropdown.classList.remove('hidden');
        
        // Set avatar initial if available
        if (loggedInUser.username) {
            avatarButton.innerHTML = loggedInUser.username.charAt(0).toUpperCase();
        }
    } else {
        // User is not logged in
        loginButton.classList.remove('hidden');
        avatarDropdown.classList.add('hidden');
    }
}

// Toggle dropdown menu
document.getElementById('avatarButton')?.addEventListener('click', function(e) {
    e.stopPropagation();
    const dropdownMenu = document.getElementById('dropdownMenu');
    dropdownMenu.classList.toggle('hidden');
});

// Close dropdown when clicking outside
document.addEventListener('click', function(e) {
    const dropdownMenu = document.getElementById('dropdownMenu');
    const avatarButton = document.getElementById('avatarButton');
    if (dropdownMenu && !dropdownMenu.contains(e.target) && !avatarButton?.contains(e.target)) {
        dropdownMenu.classList.add('hidden');
    }
});

// Handle logout
document.getElementById('logoutButton')?.addEventListener('click', function() {
    localStorage.removeItem('loggedInUser');
    window.location.href = '/';
});

// Check login status on page load
document.addEventListener('DOMContentLoaded', updateAuthUI);

function showNotification(message, type) {
    // For now, use a simple alert. In a real application, this would be a custom UI notification.
    alert(message);
}