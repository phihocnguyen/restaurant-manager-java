document.addEventListener('DOMContentLoaded', function() {
    /* 1. KHỞI TẠO TÍNH NĂNG KÉO-THẢ CHO DANH SÁCH NHÂN VIÊN
    -----------------------------------------------------------------*/
    const containerEl = document.getElementById('external-events');
    if (containerEl) {
        new FullCalendar.Draggable(containerEl, {
            itemSelector: '.employee-item',
            eventData: function(eventEl) {
                const fullName = eventEl.getAttribute('data-fullname');
                const jobTitle = eventEl.getAttribute('data-jobtitle');

                // --- Lấy màu từ class Tailwind --- //
                let bgColor = '';
                const classList = eventEl.classList;
                for (let i = 0; i < classList.length; i++) {
                    const className = classList[i];
                    if (className.startsWith('bg-')) {
                        // Đây là một class Tailwind setting background color
                        // Chúng ta cần mapping class này sang mã màu hex
                        // Lưu ý: Mapping này cần phải đầy đủ các màu bạn sử dụng
                        const colorMapping = {
                            'bg-blue-500/80': '#3b82f6', // Ví dụ: blue-500
                            'bg-green-500/80': '#22c55e', // Ví dụ: green-500
                            'bg-purple-500/80': '#a855f7', // Ví dụ: purple-500
                            'bg-yellow-500/80': '#f59e0b', // Ví dụ: yellow-500
                            // Thêm các màu khác nếu cần
                        };
                        bgColor = colorMapping[className] || ''; // Lấy màu từ mapping, fallback về rỗng nếu không tìm thấy
                        break; // Thoát vòng lặp sau khi tìm thấy màu
                    }
                }
                // --- Kết thúc lấy màu --- //

                // Fallback về màu mặc định nếu không lấy được màu từ Tailwind
                if (!bgColor) {
                     bgColor = eventEl.style.backgroundColor || ''; // Lấy màu từ inline style nếu có
                }

                return {
                    title: fullName + ' - ' + jobTitle,
                    backgroundColor: bgColor || '#3788d8', // Sử dụng màu lấy được, fallback về màu mặc định FullCalendar nếu rỗng
                    borderColor: bgColor || '#3788d8',
                    allDay: false, // Explicitly set allDay to false for dragged events
                    extendedProps: {
                        fullName: fullName,
                        jobTitle: jobTitle
                    }
                };
            }
        });
    } else {
        console.error("Container element with id 'external-events' not found.");
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
                // alert('Ca làm việc đã được cập nhật: ' + info.event.title + ' vào lúc ' + info.event.start.toLocaleString()); // Removed alert
                console.log('Event dropped:', info.event.title, 'New start:', info.event.start, 'New end:', info.event.end);
            },
            eventReceive: function(info) {
                // alert('Ca làm việc mới được thêm: ' + info.event.title + ' vào lúc ' + info.event.start.toLocaleString()); // Removed alert
                console.log('Event received:', info.event.title, 'Start:', info.event.start, 'End:', info.event.end);
            },
            select: function(info) {
                const title = prompt('Nhập tên ca làm việc:');
                if (title) {
                    calendar.addEvent({
                        title: title,
                        start: info.start,
                        end: info.end,
                        // Removed allDay: info.allDay
                    });
                }
                calendar.unselect();
            },
            eventClick: function(info) {
                // alert('Click vào ca làm việc: ' + info.event.title); // Removed alert
                console.log('Event clicked:', info.event.title, 'Start:', info.event.start, 'End:', info.event.end);
            }
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
                    'Chức vụ': event.extendedProps.jobTitle || '',
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