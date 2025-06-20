function getRoleColorHex(role) {
    if (role === 'cashier') return '#3b82f6'; // blue
    if (role === 'kitchen') return '#22c55e'; // green
    if (role === 'delivery man') return '#f59e0b'; // yellow
    return '#9ca3af'; // gray
}

document.addEventListener('DOMContentLoaded', function() {
    /* 0. FETCH AND RENDER EMPLOYEES
    -----------------------------------------------------------------*/
    async function fetchEmployees() {
        try {
            const response = await fetch('http://localhost:8080/employees');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const employees = await response.json();
            return employees;
        } catch (error) {
            console.error('Error fetching employees:', error);
            return [];
        }
    }

    function getRandomColor() {
        const colors = [
            'bg-blue-500/80',
            'bg-green-500/80',
            'bg-purple-500/80',
            'bg-yellow-500/80',
            'bg-red-500/80',
            'bg-indigo-500/80',
            'bg-pink-500/80',
            'bg-teal-500/80'
        ];
        return colors[Math.floor(Math.random() * colors.length)];
    }

    function getRoleNameVi(role) {
        if (role === 'cashier') return 'Thu ngân';
        if (role === 'kitchen') return 'Bếp';
        if (role === 'delivery man') return 'Giao hàng';
        return role;
    }

    function getRoleColorClass(role) {
        if (role === 'cashier') return 'bg-blue-500/80';
        if (role === 'kitchen') return 'bg-green-500/80';
        if (role === 'delivery man') return 'bg-yellow-500/80';
        return 'bg-gray-400/80';
    }

    function getRoleTextColorClass(role) {
        if (role === 'cashier') return 'text-blue-500-100';
        if (role === 'kitchen') return 'text-green-500-100';
        if (role === 'delivery man') return 'text-yellow-500-100';
        return 'text-gray-500-100';
    }

    function renderEmployees(employees) {
        const employeeList = document.getElementById('employee-list');
        if (!employeeList) return;

        employeeList.innerHTML = employees.map(employee => {
            const colorClass = getRoleColorClass(employee.role);
            const textColorClass = getRoleTextColorClass(employee.role);
            return `
                <div class='fc-event employee-item ${colorClass} hover:shadow-lg hover:scale-105 transition-all duration-150 cursor-pointer' 
                     data-fullname="${employee.name}" 
                     data-jobtitle="${getRoleNameVi(employee.role) || 'Nhân viên'}">
                    <p class="font-bold">${employee.name}</p>
                    <p class="text-sm ${textColorClass}">${getRoleNameVi(employee.role) || 'Nhân viên'}</p>
                </div>
            `;
        }).join('');
    }

    // Fetch and render employees
    fetchEmployees().then(employees => {
        renderEmployees(employees);
        initializeDraggable();
    });

    function getRoleFromVi(jobTitle) {
        if (jobTitle === 'Thu ngân') return 'cashier';
        if (jobTitle === 'Bếp') return 'kitchen';
        if (jobTitle === 'Giao hàng') return 'delivery man';
        return '';
    }

    function initializeDraggable() {
        const containerEl = document.getElementById('external-events');
        if (containerEl) {
            new FullCalendar.Draggable(containerEl, {
                itemSelector: '.employee-item',
                eventData: function(eventEl) {
                    const fullName = eventEl.getAttribute('data-fullname');
                    const jobTitle = eventEl.getAttribute('data-jobtitle');
                    const role = getRoleFromVi(jobTitle);
                    const color = getRoleColorHex(role);
                    return {
                        title: fullName + ' - ' + jobTitle,
                        backgroundColor: color,
                        borderColor: color,
                        allDay: false,
                        extendedProps: {
                            fullName: fullName,
                            jobTitle: jobTitle,
                            role: role
                        }
                    };
                }
            });
        } else {
            console.error("Container element with id 'external-events' not found.");
        }
    }

    /* 2. KHỞI TẠO VÀ CẤU HÌNH LỊCH FULLCALENDAR
    -----------------------------------------------------------------*/
    const calendarEl = document.getElementById('calendar');
    if (calendarEl) {
        const calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'timeGridWeek',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            slotMinTime: '06:00:00',
            slotMaxTime: '22:00:00',
            editable: true,
            droppable: true,
            selectable: true,
            selectMirror: true,
            dayMaxEvents: true,
            allDaySlot: false,
            locale: 'vi',
            buttonText: {
                today: 'Hôm nay',
                month: 'Tháng',
                week: 'Tuần',
                day: 'Ngày'
            },
            eventDrop: function(info) {
                saveCalendarEvents(calendar);
                console.log('Event dropped:', info.event.title, 'New start:', info.event.start, 'New end:', info.event.end);
            },
            eventReceive: function(info) {
                // Gán role vào extendedProps nếu có
                if (info.draggedEl) {
                    const jobTitle = info.draggedEl.getAttribute('data-jobtitle');
                    const role = getRoleFromVi(jobTitle);
                    info.event.setExtendedProp('role', role);
                    info.event.setProp('backgroundColor', getRoleColorHex(role));
                    info.event.setProp('borderColor', getRoleColorHex(role));
                }
                saveCalendarEvents(calendar);
                console.log('Event received:', info.event.title, 'Start:', info.event.start, 'End:', info.event.end);
            },
            select: function(info) {
                const title = prompt('Nhập tên ca làm việc:');
                if (title) {
                    // Mặc định không có role, sẽ là xám
                    const event = calendar.addEvent({
                        title: title,
                        start: info.start,
                        end: info.end,
                        backgroundColor: getRoleColorHex(''),
                        borderColor: getRoleColorHex(''),
                        extendedProps: { role: '' }
                    });
                    saveCalendarEvents(calendar);
                }
                calendar.unselect();
            },
            eventClick: function(info) {
                console.log('Event clicked:', info.event.title, 'Start:', info.event.start, 'End:', info.event.end);
            },
            eventDidMount: function(arg) {
                // Set màu theo role
                const role = arg.event.extendedProps.role;
                const color = getRoleColorHex(role);
                arg.el.style.backgroundColor = color;
                arg.el.style.borderColor = color;
            },
            eventResize: function(info) {
                saveCalendarEvents(calendar);
                console.log('Event resized:', info.event.title, 'New start:', info.event.start, 'New end:', info.event.end);
            }
        });

        // Load events from localStorage
        loadCalendarEvents(calendar);
        // Sau khi load, set lại màu cho các event
        calendar.getEvents().forEach(ev => {
            const color = getRoleColorHex(ev.extendedProps.role);
            ev.setProp('backgroundColor', color);
            ev.setProp('borderColor', color);
        });
        calendar.render();

        /* 3. XUẤT LỊCH RA FILE EXCEL
        -----------------------------------------------------------------*/
        const exportButton = document.getElementById('exportExcel');
        if (exportButton) {
            exportButton.addEventListener('click', function() {
                // Lấy tất cả sự kiện từ calendar
                const events = calendar.getEvents();
                
                // Chuyển đổi sự kiện thành dữ liệu cho Excel
                const excelData = events.map(event => ({
                    'Nhân viên': event.extendedProps.fullName || event.title,
                    'Vị trí': getRoleNameVi(event.extendedProps.role) || '',
                    'Ngày bắt đầu': event.start ? event.start.toLocaleDateString('vi-VN') : '',
                    'Giờ bắt đầu': event.start ? event.start.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) : '',
                    'Ngày kết thúc': event.end ? event.end.toLocaleDateString('vi-VN') : '',
                    'Giờ kết thúc': event.end ? event.end.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) : '',
                }));

                // Tạo worksheet
                const ws = XLSX.utils.json_to_sheet(excelData);

                // Tạo workbook
                const wb = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(wb, ws, "Lịch làm việc");

                // Xuất file Excel
                const fileName = `Lich_lam_viec_${new Date().toLocaleDateString('vi-VN').replace(/\//g, '-')}.xlsx`;
                XLSX.writeFile(wb, fileName);
            });
        }
    } else {
        console.error("Element with id 'calendar' not found.");
    }
});

// Persist calendar events to localStorage
function saveCalendarEvents(calendar) {
    const events = calendar.getEvents().map(e => ({
        title: e.title,
        start: e.start,
        end: e.end,
        extendedProps: e.extendedProps,
    }));
    localStorage.setItem('calendarEvents', JSON.stringify(events));
}

function loadCalendarEvents(calendar) {
    const events = JSON.parse(localStorage.getItem('calendarEvents') || '[]');
    events.forEach(e => {
        calendar.addEvent({
            title: e.title,
            start: e.start,
            end: e.end,
            extendedProps: e.extendedProps,
            backgroundColor: getRoleColorHex(e.extendedProps?.role),
            borderColor: getRoleColorHex(e.extendedProps?.role)
        });
    });
}