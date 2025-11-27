// Данные университета
const universityData = {
    ФВТ: {
        specialties: [
            '09.03.01 - Информатика и вычислительная техника',
            '09.03.02 - Информационные системы и технологии',
            '09.03.04 - Программная инженерия',
            '09.03.03 - Прикладная информатика'
        ],
        departments: {
            KT: "Кафедра \"Компьютерные технологии\"",
            MiSK: "Кафедра \"Математика и суперкомпьютерное моделирование\"",
            SAP: "Кафедра \"Системы автоматизированного проектирования\""
        }
    },
    ФИТЭ: {
        specialties: [
            '03.03.02 - Физика',
            '11.03.03 - Конструирование и технология электронных средств',
            '11.03.04 - Электроника и наноэлектроника'
        ],
        departments: {
            AIS: "Кафедра \"Автономные информационные и управляющие системы\"",
            RiRS: "Кафедра \"Радиотехника и радиоэлектронные системы\"",
            AiT: "Кафедра \"Автоматика и телемеханика\""
        }
    },
    ФПТЭТ: {
        specialties: [
            '15.03.01 - Машиностроение',
            '15.03.05 - Конструкторско-технологическое обеспечение машиностроительных производств',
            '23.03.01 - Технология транспортных процессов'
        ],
        departments: {
            TSB: "Кафедра \"Техносферная безопасность\"",
            SiLPM: "Кафедра \"Сварочное, литейное производство и материаловедение\"",
            PS: "Кафедра \"Приборостроение\""
        }
    }
};

// Связи кафедр с предметами
const departmentSubjects = {
    "Кафедра \"Компьютерные технологии\"": ["Программирование на Java", "Объектно-ориентированное программирование", "Веб-программирование"],
    "Кафедра \"Математика и суперкомпьютерное моделирование\"": ["Алгоритмы и структуры данных", "Математическое моделирование"],
    "Кафедра \"Системы автоматизированного проектирования\"": ["Архитектура ЭВМ", "Системы автоматизированного проектирования"],
    "Кафедра \"Автономные информационные и управляющие системы\"": ["Автоматизация и управление", "Системы управления"],
    "Кафедра \"Радиотехника и радиоэлектронные системы\"": ["Радиотехника", "Телекоммуникации"],
    "Кафедра \"Автоматика и телемеханика\"": ["Автоматизация и управление", "Телемеханика"],
    "Кафедра \"Техносферная безопасность\"": ["Техносферная безопасность", "Экология"],
    "Кафедра \"Сварочное, литейное производство и материаловедение\"": ["Материаловедение", "Технологии производства"]
};

let selectedRole = '';

// Функция 1: Проверка високосного года
function isLeapYear(year) {
    return (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
}

// Функция 2: Проверка корректности даты
function isValidDate(day, month, year) {
    if (year < 1900 || year > new Date().getFullYear()) return false;
    if (month < 1 || month > 12) return false;
    if (day < 1 || day > 31) return false;

    const monthsWith30Days = [4, 6, 9, 11];
    if (monthsWith30Days.includes(month) && day > 30) return false;

    if (month === 2) {
        if (isLeapYear(year)) {
            return day <= 29;
        } else {
            return day <= 28;
        }
    }

    return true;
}

// Функция 3: Расчет возраста из даты рождения
function calculateAge() {
    const birthDateInput = document.getElementById('birthDate');
    const ageHidden = document.getElementById('age');

    if (!birthDateInput.value) {
        ageHidden.value = '';
        return null;
    }

    const birthDate = new Date(birthDateInput.value);
    const today = new Date();

    const [year, month, day] = birthDateInput.value.split('-').map(Number);
    if (!isValidDate(day, month, year)) {
        return 'Некорректная дата';
    }

    if (birthDate > today) {
        return 'Дата рождения не может быть в будущем';
    }

    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    if (age < 0) {
        return 'Некорректная дата (отрицательный возраст)';
    }

    ageHidden.value = age;
    return age;
}

// Функция 4: Валидация имени и фамилии
function validateName() {
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();

    if (!firstName || !lastName) {
        return 'Имя и фамилия обязательны для заполнения';
    }

    if (firstName.length < 2 || lastName.length < 2) {
        return 'Имя и фамилия должны содержать минимум 2 символа';
    }

    return true;
}

// Функция 5: Валидация выбора роли и полей
function validateRoleFields() {
    if (!selectedRole) {
        return 'Пожалуйста, выберите тип пользователя';
    }

    if (selectedRole === 'student') {
        const faculty = document.getElementById('faculty').value;
        const course = document.getElementById('course').value;
        const specialization = document.getElementById('specialty').value;

        if (!faculty || !course || !specialization) {
            return 'Для студента необходимо заполнить факультет, курс и специальность';
        }
    }

    if (selectedRole === 'teacher') {
        const faculty = document.getElementById('teacherFaculty').value;
        const department = document.getElementById('department').value;
        const subject = document.getElementById('subject').value;
        const workExperience = document.getElementById('workExperience').value;

        if (!faculty || !department || !subject || !workExperience) {
            return 'Для преподавателя необходимо заполнить факультет, кафедру, предмет и стаж работы';
        }
    }

    return true;
}

// Функция 6: Основная валидация и отправка
function validateAndSubmit() {
    // Проверка имени и фамилии
    const nameValidation = validateName();
    if (nameValidation !== true) {
        alert(nameValidation);
        return false;
    }

    // Расчет и проверка возраста
    const ageResult = calculateAge();
    if (typeof ageResult === 'string') {
        alert(ageResult);
        return false;
    }

    // Проверка роли и полей
    const roleValidation = validateRoleFields();
    if (roleValidation !== true) {
        alert(roleValidation);
        return false;
    }

    // Все проверки пройдены - форма отправляется
    return true;
}

// Функции для работы с интерфейсом
function selectRole(role) {
    selectedRole = role;
    document.getElementById('role').value = role;

    document.querySelectorAll('.role-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    document.getElementById('commonFields').style.display = 'none';
    document.getElementById('studentFields').style.display = 'none';
    document.getElementById('teacherFields').style.display = 'none';

    document.getElementById('commonFields').style.display = 'block';

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