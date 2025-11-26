// Данные университета
const universityData = {
    ФВТ: {
        name: "Факультет вычислительной техники",
        departments: {
            KT: "Кафедра \"Компьютерные технологии\"",
            MiSK: "Кафедра \"Математика и суперкомпьютерное моделирование\"",
            SAP: "Кафедра \"Системы автоматизированного проектирования\"",
            ViPM: "Кафедра \"Высшая и прикладная математика\"",
            IVS: "Кафедра \"Информационно-вычислительные системы\"",
            VT: "Кафедра \"Вычислительная техника\"",
            IOUiP: "Кафедра \"Информационное обеспечение управления и производства\"",
            MO_iP_EVM: "Кафедра \"Математическое обеспечение и применение ЭВМ\""
        },
        subjects: {
            JAVA: "Программирование на Java",
            OOP: "Объектно-ориентированное программирование",
            ALGORITHMS: "Алгоритмы и структуры данных",
            DATABASES: "Базы данных",
            WEB: "Веб-программирование",
            ARCHITECTURE: "Архитектура ЭВМ",
            OS: "Операционные системы",
            NETWORKS: "Компьютерные сети",
            SECURITY: "Информационная безопасность"
        },
        specialties: [
            '09.03.01 - Информатика и вычислительная техника',
            '09.03.02 - Информационные системы и технологии',
            '09.03.04 - Программная инженерия',
            '09.03.03 - Прикладная информатика',
            '02.03.03 - Математическое обеспечение и администрирование информационных систем',
            '01.03.04 - Прикладная математика',
            '01.03.02 - Прикладная математика и информатика'
        ]
    },
    ФИТЭ: {
        name: "Факультет информационных технологий и электроники",
        departments: {
            AIS: "Кафедра \"Автономные информационные и управляющие системы\"",
            RiRS: "Кафедра \"Радиотехника и радиоэлектронные системы\"",
            AiT: "Кафедра \"Автоматика и телемеханика\"",
            PHYSICS: "Кафедра \"Физика\"",
            IBSiT: "Кафедра \"Информационная безопасность систем и технологий\"",
            IIMiM: "Кафедра \"Информационно-измерительная техника и метрология\"",
            NANO: "Кафедра \"Нано- и микроэлектроника\"",
            KiPRA: "Кафедра \"Конструирование и производство радиоаппаратуры\""
        },
        subjects: {
            ELECTRONICS: "Электроника",
            NANOELECTRONICS: "Наноэлектроника",
            RADIOTECH: "Радиотехника",
            TELECOM: "Телекоммуникации",
            AUTOMATION: "Автоматизация и управление",
            MEASUREMENT: "Информационно-измерительная техника",
            METROLOGY: "Метрология",
            CRYPTO: "Криптография"
        },
        specialties: [
            '03.03.02 - Физика',
            '11.03.03 - Конструирование и технология электронных средств',
            '11.03.04 - Электроника и наноэлектроника',
            '12.03.01 - Приборостроение',
            '27.03.01 - Стандартизация и метрология',
            '27.03.04 - Управление в технических системах',
            '28.03.01 - Нанотехнологии и микросистемная техника'
        ]
    },
    ФПТЭТ: {
        name: "Факультет промышленных технологий, электроэнергетики и транспорта",
        departments: {
            TSB: "Кафедра \"Техносферная безопасность\"",
            SiLPM: "Кафедра \"Сварочное, литейное производство и материаловедение\"",
            PS: "Кафедра \"Приборостроение\"",
            TMiG: "Кафедра \"Теоретическая и прикладная механика и графика\"",
            TO_iOM: "Кафедра \"Технологии и оборудование машиностроения\"",
            CHEMISTRY: "Кафедра \"Химия\"",
            KiIM: "Кафедра \"Контроль и испытания материалов\"",
            EiE: "Кафедра \"Электроэнергетика и электротехника\"",
            TRANSPORT: "Кафедра \"Транспортные машины\""
        },
        subjects: {
            INDUSTRIAL_TECH: "Промышленные технологии",
            MACHINE_BUILDING: "Машиностроение",
            ENERGY: "Электроэнергетика",
            HEAT_ENERGY: "Теплоэнергетика",
            TRANSPORT_SYSTEMS: "Транспортные системы",
            LOGISTICS: "Логистика",
            TECH_SAFETY: "Техносферная безопасность",
            MATERIALS: "Материаловедение"
        },
        specialties: [
            '15.03.01 - Машиностроение',
            '15.03.05 - Конструкторско-технологическое обеспечение машиностроительных производств',
            '23.03.01 - Технология транспортных процессов',
            '20.03.01 - Техносферная безопасность',
            '13.03.01 - Теплоэнергетика и теплотехника',
            '13.03.02 - Электроэнергетика и электротехника'
        ]
    }
};

// Связи кафедр с предметами
const departmentSubjects = {
    // ФВТ кафедры
    "Кафедра \"Компьютерные технологии\"": ["Программирование на Java", "Объектно-ориентированное программирование", "Веб-программирование"],
    "Кафедра \"Математика и суперкомпьютерное моделирование\"": ["Алгоритмы и структуры данных", "Математическое моделирование"],
    "Кафедра \"Системы автоматизированного проектирования\"": ["Архитектура ЭВМ", "Системы автоматизированного проектирования"],
    "Кафедра \"Высшая и прикладная математика\"": ["Высшая математика", "Прикладная математика"],
    "Кафедра \"Информационно-вычислительные системы\"": ["Базы данных", "Информационные системы"],
    "Кафедра \"Вычислительная техника\"": ["Компьютерные сети", "Архитектура ЭВМ"],
    "Кафедра \"Информационное обеспечение управления и производства\"": ["Информационная безопасность", "Управление данными"],
    "Кафедра \"Математическое обеспечение и применение ЭВМ\"": ["Операционные системы", "Математическое обеспечение"],

    // ФИТЭ кафедры
    "Кафедра \"Автономные информационные и управляющие системы\"": ["Автоматизация и управление", "Системы управления"],
    "Кафедра \"Радиотехника и радиоэлектронные системы\"": ["Радиотехника", "Телекоммуникации"],
    "Кафедра \"Автоматика и телемеханика\"": ["Автоматизация и управление", "Телемеханика"],
    "Кафедра \"Физика\"": ["Физика", "Электроника"],
    "Кафедра \"Информационная безопасность систем и технологий\"": ["Криптография", "Информационная безопасность"],
    "Кафедра \"Информационно-измерительная техника и метрология\"": ["Информационно-измерительная техника", "Метрология"],
    "Кафедра \"Нано- и микроэлектроника\"": ["Наноэлектроника", "Микроэлектроника"],
    "Кафедра \"Конструирование и производство радиоаппаратуры\"": ["Электроника", "Радиотехника"],

    // ФПТЭТ кафедры
    "Кафедра \"Техносферная безопасность\"": ["Техносферная безопасность", "Экология"],
    "Кафедра \"Сварочное, литейное производство и материаловедение\"": ["Материаловедение", "Технологии производства"],
    "Кафедра \"Приборостроение\"": ["Приборостроение", "Измерительная техника"],
    "Кафедра \"Теоретическая и прикладная механика и графика\"": ["Механика", "Графика"],
    "Кафедра \"Технологии и оборудование машиностроения\"": ["Машиностроение", "Промышленные технологии"],
    "Кафедра \"Химия\"": ["Химия", "Материаловедение"],
    "Кафедра \"Контроль и испытания материалов\"": ["Контроль качества", "Испытания материалов"],
    "Кафедра \"Электроэнергетика и электротехника\"": ["Электроэнергетика", "Электротехника"],
    "Кафедра \"Транспортные машины\"": ["Транспортные системы", "Логистика"]
};

let selectedRole = '';

// Функция для проверки високосного года
function isLeapYear(year) {
    return (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
}

// Функция для проверки корректности даты
function isValidDate(day, month, year) {
    // Проверяем базовые диапазоны
    if (year < 1900 || year > new Date().getFullYear()) return false;
    if (month < 1 || month > 12) return false;
    if (day < 1 || day > 31) return false;

    // Проверяем месяцы с 30 днями
    const monthsWith30Days = [4, 6, 9, 11];
    if (monthsWith30Days.includes(month) && day > 30) return false;

    // Проверяем февраль
    if (month === 2) {
        if (isLeapYear(year)) {
            return day <= 29;
        } else {
            return day <= 28;
        }
    }

    return true;
}

// Функция для расчета возраста из даты рождения
function calculateAge() {
    const birthDateInput = document.getElementById('birthDate');
    const ageDisplay = document.getElementById('ageDisplay');
    const ageHidden = document.getElementById('age');

    if (!birthDateInput.value) {
        ageDisplay.value = '';
        ageHidden.value = '';
        return;
    }

    const birthDate = new Date(birthDateInput.value);
    const today = new Date();

    // Проверяем корректность даты
    const [year, month, day] = birthDateInput.value.split('-').map(Number);
    if (!isValidDate(day, month, year)) {
        ageDisplay.value = 'Некорректная дата';
        ageDisplay.style.color = 'red';
        ageHidden.value = '';
        return;
    }

    // Проверяем что дата не в будущем
    if (birthDate > today) {
        ageDisplay.value = 'Дата рождения не может быть в будущем';
        ageDisplay.style.color = 'red';
        ageHidden.value = '';
        return;
    }

    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    // Проверяем, был ли уже день рождения в этом году
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    // Проверяем что возраст не отрицательный
    if (age < 0) {
        ageDisplay.value = 'Некорректная дата (отрицательный возраст)';
        ageDisplay.style.color = 'red';
        ageHidden.value = '';
        return;
    }

    // Валидация возраста
    if (age < 17) {
        ageDisplay.value = `${age} лет - Слишком молодой (мин. 17 лет)`;
        ageDisplay.style.color = 'red';
        ageHidden.value = '';
    } else if (age > 70) {
        ageDisplay.value = `${age} лет - Слишком взрослый (макс. 70 лет)`;
        ageDisplay.style.color = 'red';
        ageHidden.value = '';
    } else {
        ageDisplay.value = `${age} лет`;
        ageDisplay.style.color = 'green';
        ageHidden.value = age;
    }
}

function selectRole(role) {
    selectedRole = role;
    document.getElementById('role').value = role;

    // Сбрасываем все кнопки
    document.querySelectorAll('.role-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // Активируем выбранную кнопку
    event.target.classList.add('active');

    // Скрываем все дополнительные поля
    document.getElementById('commonFields').style.display = 'none';
    document.getElementById('studentFields').style.display = 'none';
    document.getElementById('teacherFields').style.display = 'none';

    // Показываем общие поля для всех ролей
    document.getElementById('commonFields').style.display = 'block';

    // Показываем специфичные поля в зависимости от роли
    if (role === 'student') {
        document.getElementById('studentFields').style.display = 'block';
    } else if (role === 'teacher') {
        document.getElementById('teacherFields').style.display = 'block';
    }
}

function updateStudentFields() {
    const faculty = document.getElementById('faculty').value;
    const specialtySelect = document.getElementById('specialty');

    specialtySelect.innerHTML = '<option value="">Выберите специальность</option>';
    specialtySelect.disabled = !faculty;

    if (faculty && universityData[faculty]) {
        universityData[faculty].specialties.forEach(spec => {
            const option = document.createElement('option');
            option.value = spec;
            option.textContent = spec;
            specialtySelect.appendChild(option);
        });
    }
}

function updateTeacherFields() {
    const faculty = document.getElementById('teacherFaculty').value;
    const departmentSelect = document.getElementById('department');

    departmentSelect.innerHTML = '<option value="">Выберите кафедру</option>';
    document.getElementById('subject').innerHTML = '<option value="">Сначала выберите кафедру</option>';

    departmentSelect.disabled = !faculty;
    document.getElementById('subject').disabled = true;

    if (faculty && universityData[faculty]) {
        Object.values(universityData[faculty].departments).forEach(dept => {
            const option = document.createElement('option');
            option.value = dept;
            option.textContent = dept;
            departmentSelect.appendChild(option);
        });
    }
}

function updateSubjects() {
    const department = document.getElementById('department').value;
    const subjectSelect = document.getElementById('subject');

    subjectSelect.innerHTML = '<option value="">Выберите предмет</option>';
    subjectSelect.disabled = !department;

    if (department && departmentSubjects[department]) {
        departmentSubjects[department].forEach(subject => {
            const option = document.createElement('option');
            option.value = subject;
            option.textContent = subject;
            subjectSelect.appendChild(option);
        });
    }
}

// Валидация формы перед отправкой
function validateForm() {
    const age = document.getElementById('age').value;

    if (!age) {
        alert('Пожалуйста, введите дату рождения для расчета возраста');
        return false;
    }

    if (age < 17 || age > 70) {
        alert('Возраст должен быть от 17 до 70 лет');
        return false;
    }

    if (!selectedRole) {
        alert('Пожалуйста, выберите тип пользователя');
        return false;
    }

    // Проверяем обязательные поля для каждой роли
    if (selectedRole === 'student') {
        const faculty = document.getElementById('faculty').value;
        const course = document.getElementById('course').value;
        const specialization = document.getElementById('specialty').value;

        if (!faculty || !course || !specialization) {
            alert('Пожалуйста, заполните все обязательные поля для студента');
            return false;
        }
    }

    if (selectedRole === 'teacher') {
        const faculty = document.getElementById('teacherFaculty').value;
        const department = document.getElementById('department').value;
        const subject = document.getElementById('subject').value;
        const workExperience = document.getElementById('workExperience').value;

        if (!faculty || !department || !subject || !workExperience) {
            alert('Пожалуйста, заполните все обязательные поля для преподавателя');
            return false;
        }
    }

    return true;
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault(); // Останавливаем отправку только если валидация не пройдена
            }
            // Если валидация пройдена - форма отправится нормально
        });
    }
});

function resetForm() {
    document.getElementById('registrationForm').reset();
    selectedRole = '';
    document.querySelectorAll('.role-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById('commonFields').style.display = 'none';
    document.getElementById('studentFields').style.display = 'none';
    document.getElementById('teacherFields').style.display = 'none';

    // Сбрасываем disabled состояния
    document.getElementById('specialty').disabled = true;
    document.getElementById('department').disabled = true;
    document.getElementById('subject').disabled = true;
}