// register.js
let selectedRole = '';
let userAge = 0;

// ========== ИНИЦИАЛИЗАЦИЯ ==========
document.addEventListener('DOMContentLoaded', function() {
    // Устанавливаем максимальную дату (сегодня)
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('birthDate').max = today;

    // Восстанавливаем выбранную роль если есть
    const roleInput = document.getElementById('role');
    if (roleInput && roleInput.value) {
        selectedRole = roleInput.value;
        showFieldsForRole(selectedRole);
        // Делаем активной кнопку с выбранной ролью
        updateRoleButtons();
    }

    // Биндим событие изменения кафедры для преподавателей
    const departmentSelect = document.getElementById('department');
    if (departmentSelect) {
        departmentSelect.addEventListener('change', function() {
            // Очищаем ошибку кафедры при выборе
            document.getElementById('departmentError').textContent = '';
            updateSubjects();
        });
    }

    // Биндим событие изменения предмета для преподавателей
    const subjectSelect = document.getElementById('subject');
    if (subjectSelect) {
        subjectSelect.addEventListener('change', function() {
            // Очищаем ошибку предмета при выборе
            document.getElementById('subjectError').textContent = '';
        });
    }

    // Биндим события для очистки ошибок при выборе в других select
    const facultySelect = document.getElementById('faculty');
    if (facultySelect) {
        facultySelect.addEventListener('change', function() {
            document.getElementById('facultyError').textContent = '';
            updateStudentFields();
        });
    }

    const teacherFacultySelect = document.getElementById('teacherFaculty');
    if (teacherFacultySelect) {
        teacherFacultySelect.addEventListener('change', function() {
            document.getElementById('teacherFacultyError').textContent = '';
            updateTeacherFields();
        });
    }

    const specialtySelect = document.getElementById('specialty');
    if (specialtySelect) {
        specialtySelect.addEventListener('change', function() {
            document.getElementById('specialtyError').textContent = '';
        });
    }

    const courseSelect = document.getElementById('course');
    if (courseSelect) {
        courseSelect.addEventListener('change', function() {
            document.getElementById('courseError').textContent = '';
        });
    }
});

// ========== ВЫБОР РОЛИ ==========
function selectRole(role) {
    selectedRole = role;
    document.getElementById('role').value = role;

    // Активная кнопка
    document.querySelectorAll('.role-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');

    // Очищаем ошибку
    document.getElementById('roleError').textContent = '';

    showFieldsForRole(role);
}

function updateRoleButtons() {
    document.querySelectorAll('.role-btn').forEach(btn => {
        if (btn.textContent.toLowerCase().includes(selectedRole)) {
            btn.classList.add('active');
        }
    });
}

function showFieldsForRole(role) {
    // Показываем общие поля
    document.getElementById('commonFields').style.display = 'block';

    // Скрываем все специфичные поля
    document.getElementById('studentFields').style.display = 'none';
    document.getElementById('teacherFields').style.display = 'none';

    if (role === 'student') {
        document.getElementById('studentFields').style.display = 'block';
    } else if (role === 'teacher') {
        document.getElementById('teacherFields').style.display = 'block';
    }
}

// ========== ФУНКЦИЯ ПРОВЕРКИ ДАТЫ ==========
function calculateAge() {
    const birthDateInput = document.getElementById('birthDate').value;
    const ageDisplay = document.getElementById('ageDisplay');
    const ageHidden = document.getElementById('age');
    const errorElement = document.getElementById('birthDateError');

    // Очищаем предыдущие значения
    errorElement.textContent = '';
    ageHidden.value = '';
    ageDisplay.textContent = '';
    userAge = 0;

    // 1. Проверяем что поле не пустое
    if (!birthDateInput || birthDateInput.trim() === '') {
        errorElement.textContent = 'Некорректная дата рождения';
        return false;
    }

    // 2. Проверяем формат даты YYYY-MM-DD
    const dateRegex = /^(\d{4})-(\d{2})-(\d{2})$/;
    const match = birthDateInput.match(dateRegex);

    if (!match) {
        errorElement.textContent = 'Неверный формат даты. Используйте YYYY-MM-DD';
        return false;
    }

    // 3. Извлекаем и проверяем компоненты даты
    const year = parseInt(match[1], 10);
    const month = parseInt(match[2], 10);
    const day = parseInt(match[3], 10);

    // 4. Проверяем год (от 1900 до текущего года)
    const currentYear = new Date().getFullYear();
    if (year < 1900) {
        errorElement.textContent = 'Год не может быть меньше 1900';
        return false;
    }
    if (year > currentYear) {
        errorElement.textContent = 'Год не может быть больше текущего';
        return false;
    }

    // 5. Проверяем месяц (1-12)
    if (month < 1 || month > 12) {
        errorElement.textContent = 'Месяц должен быть от 01 до 12';
        return false;
    }

    // 6. Проверяем день в зависимости от месяца
    const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
    let maxDays = daysInMonth[month - 1];

    if (month === 2 && isLeapYear) {
        maxDays = 29;
    }

    if (day < 1 || day > maxDays) {
        const monthNames = [
            'январе', 'феврале', 'марте', 'апреле', 'мае', 'июне',
            'июле', 'августе', 'сентябре', 'октябре', 'ноябре', 'декабре'
        ];
        const monthName = monthNames[month - 1];

        if (month === 2) {
            if (isLeapYear) {
                errorElement.textContent = `В ${monthName} ${year} года может быть только 29 дней (високосный год)`;
            } else {
                errorElement.textContent = `В ${monthName} ${year} года может быть только 28 дней`;
            }
        } else {
            errorElement.textContent = `В ${monthName} может быть только ${maxDays} дней`;
        }
        return false;
    }

    // 7. Создаем объект Date и проверяем что дата не в будущем
    const birthDate = new Date(year, month - 1, day);
    const today = new Date();

    if (birthDate.getFullYear() !== year ||
        birthDate.getMonth() !== month - 1 ||
        birthDate.getDate() !== day) {
        errorElement.textContent = 'Некорректная дата';
        return false;
    }

    if (birthDate > today) {
        errorElement.textContent = 'Дата рождения не может быть в будущем';
        return false;
    }

    // 8. Рассчитываем возраст
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    // 9. Проверяем возрастные ограничения
    if (age < 17) {
        errorElement.textContent = 'Минимальный возраст: 17 лет';
        return false;
    } else if (age > 70) {
        errorElement.textContent = 'Максимальный возраст: 70 лет';
        return false;
    }

    // Все ок, сохраняем возраст
    ageHidden.value = age;
    userAge = age;
    ageDisplay.textContent = `Возраст: ${age} лет`;

    // Проверяем стаж если он введен
    const experienceInput = document.getElementById('workExperience');
    if (experienceInput && experienceInput.value) {
        validateWorkExperience();
    }

    return true;
}

// ========== ПРОВЕРКА СТАЖА РАБОТЫ ==========
function validateWorkExperience() {
    const experienceInput = document.getElementById('workExperience');
    const experience = experienceInput.value.trim();
    const errorElement = document.getElementById('workExperienceError');

    errorElement.textContent = '';

    // Если поле пустое, ничего не проверяем
    if (!experience) {
        return true;
    }

    // Проверяем что это число
    const expNum = parseInt(experience);
    if (isNaN(expNum)) {
        errorElement.textContent = 'Введите число';
        return false;
    }

    // Проверяем диапазон
    if (expNum < 0 || expNum > 50) {
        errorElement.textContent = 'Стаж должен быть от 0 до 50 лет';
        return false;
    }

    // Проверяем что стаж не больше возраста минус 14 лет
    if (userAge > 0) {
        const minAgeForExperience = 14;
        if (expNum > userAge - minAgeForExperience) {
            errorElement.textContent = `Стаж слишком большой. При возрасте ${userAge} максимальный стаж: ${Math.max(0, userAge - minAgeForExperience)}`;
            return false;
        }
    }

    return true;
}

// ========== ВАЛИДАЦИЯ ПОЛЕЙ ==========
function validateUsername() {
    const username = document.getElementById('username').value.trim();
    const errorElement = document.getElementById('usernameError');

    if (!username) {
        errorElement.textContent = 'Логин не может быть пустым';
        return false;
    }

    // Только английские буквы и цифры
    if (!/^[a-zA-Z0-9]+$/.test(username)) {
        errorElement.textContent = 'Только английские буквы и цифры';
        return false;
    }

    if (username.length < 3 || username.length > 50) {
        errorElement.textContent = 'От 3 до 50 символов';
        return false;
    }

    errorElement.textContent = '';
    return true;
}

function validatePassword() {
    const password = document.getElementById('password').value;
    const errorElement = document.getElementById('passwordError');

    if (!password) {
        errorElement.textContent = 'Пароль не может быть пустым';
        return false;
    }

    if (password.length < 3) {
        errorElement.textContent = 'Минимум 3 символа';
        return false;
    }

    errorElement.textContent = '';
    return true;
}

// ========== ВАЛИДАЦИЯ ИМЯ И ФАМИЛИИ ==========
function validateName(fieldId, fieldName) {
    const value = document.getElementById(fieldId).value.trim();
    const errorElement = document.getElementById(fieldId + 'Error');

    if (!value) {
        errorElement.textContent = `Обязательное поле`;
        return false;
    }

    // Только русские буквы, пробел, дефис
    if (!/^[а-яА-ЯёЁ\s\-]+$/.test(value)) {
        errorElement.textContent = 'Только русские буквы';
        return false;
    }

    if (value.length < 2) {
        errorElement.textContent = 'Минимум 2 символа';
        return false;
    }

    errorElement.textContent = '';
    return true;
}

// ========== ВАЛИДАЦИЯ ОТЧЕСТВА (НЕОБЯЗАТЕЛЬНОЕ ПОЛЕ) ==========
function validateOptionalName(fieldId) {
    const value = document.getElementById(fieldId).value.trim();
    const errorElement = document.getElementById(fieldId + 'Error');

    // Если поле пустое - ошибок нет (отчество необязательное)
    if (!value) {
        errorElement.textContent = '';
        return true;
    }

    // Только русские буквы, пробел, дефис
    if (!/^[а-яА-ЯёЁ\s\-]+$/.test(value)) {
        errorElement.textContent = 'Только русские буквы';
        return false;
    }

    if (value.length < 2) {
        errorElement.textContent = 'Минимум 2 символа';
        return false;
    }

    errorElement.textContent = '';
    return true;
}

// ========== ФУНКЦИИ ДЛЯ СТУДЕНТОВ/ПРЕПОДАВАТЕЛЕЙ ==========
function updateStudentFields() {
    const faculty = document.getElementById('faculty').value;
    const specialtySelect = document.getElementById('specialty');

    specialtySelect.innerHTML = '<option value="">Выберите специальность</option>';
    specialtySelect.disabled = true;

    if (faculty && universityData[faculty]) {
        universityData[faculty].specialties.forEach(spec => {
            const option = document.createElement('option');
            option.value = spec;
            option.textContent = spec;
            specialtySelect.appendChild(option);
        });
        specialtySelect.disabled = false;
    }
}

function updateTeacherFields() {
    const faculty = document.getElementById('teacherFaculty').value;
    const departmentSelect = document.getElementById('department');

    departmentSelect.innerHTML = '<option value="">Выберите кафедру</option>';
    departmentSelect.disabled = true;
    document.getElementById('subject').innerHTML = '<option value="">Сначала выберите кафедру</option>';
    document.getElementById('subject').disabled = true;

    if (faculty && universityData[faculty]) {
        Object.keys(universityData[faculty].departments).forEach(dept => {
            const option = document.createElement('option');
            option.value = dept;
            option.textContent = dept;
            departmentSelect.appendChild(option);
        });
        departmentSelect.disabled = false;
    }
}

function updateSubjects() {
    const faculty = document.getElementById('teacherFaculty').value;
    const department = document.getElementById('department').value;
    const subjectSelect = document.getElementById('subject');

    subjectSelect.innerHTML = '<option value="">Выберите предмет</option>';
    subjectSelect.disabled = true;

    if (faculty && department && universityData[faculty]) {
        const subjects = universityData[faculty].departments[department];
        if (subjects) {
            subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject;
                option.textContent = subject;
                subjectSelect.appendChild(option);
            });
            subjectSelect.disabled = false;
        }
    }
}

// ========== ГЛАВНАЯ ВАЛИДАЦИЯ ФОРМЫ ==========
function validateForm() {
    let isValid = true;

    // 1. Логин
    if (!validateUsername()) {
        isValid = false;
    }

    // 2. Пароль
    if (!validatePassword()) {
        isValid = false;
    }

    // 3. Роль
    if (!selectedRole) {
        document.getElementById('roleError').textContent = 'Выберите тип пользователя';
        isValid = false;
    }

    // 4. Общие поля (только если роль выбрана)
    if (selectedRole) {
        if (!validateName('firstName', 'Имя')) {
            isValid = false;
        }

        if (!validateName('lastName', 'Фамилия')) {
            isValid = false;
        }

        // Отчество - необязательное поле, но если заполнено - проверяем
        if (!validateOptionalName('patronymic')) {
            isValid = false;
        }

        // Дата рождения - проверяем что поле заполнено
        const birthDate = document.getElementById('birthDate').value;
        if (!birthDate) {
            document.getElementById('birthDateError').textContent = 'Выберите дату рождения';
            isValid = false;
        } else {
            // Вызываем calculateAge и проверяем результат
            if (!calculateAge()) {
                isValid = false;
            }
        }

        // Дополнительная проверка возраста
        if (!userAge || userAge < 17 || userAge > 70) {
            document.getElementById('birthDateError').textContent = 'Возраст должен быть от 17 до 70 лет';
            isValid = false;
        }
    }

    // 5. Специфичные поля для студента
    if (selectedRole === 'student') {
        const faculty = document.getElementById('faculty').value;
        const specialty = document.getElementById('specialty').value;
        const course = document.getElementById('course').value;

        if (!faculty) {
            document.getElementById('facultyError').textContent = 'Выберите факультет';
            isValid = false;
        }

        if (!specialty || document.getElementById('specialty').disabled) {
            document.getElementById('specialtyError').textContent = 'Выберите специальность';
            isValid = false;
        }

        if (!course) {
            document.getElementById('courseError').textContent = 'Выберите курс';
            isValid = false;
        }
    }

    // 6. Специфичные поля для преподавателя
    if (selectedRole === 'teacher') {
        const faculty = document.getElementById('teacherFaculty').value;
        const department = document.getElementById('department').value;
        const subject = document.getElementById('subject').value;

        if (!faculty) {
            document.getElementById('teacherFacultyError').textContent = 'Выберите факультет';
            isValid = false;
        }

        if (!department || document.getElementById('department').disabled) {
            document.getElementById('departmentError').textContent = 'Выберите кафедру';
            isValid = false;
        }

        if (!subject || document.getElementById('subject').disabled) {
            document.getElementById('subjectError').textContent = 'Выберите предмет';
            isValid = false;
        }

        // Проверка стажа работы
        if (!validateWorkExperience()) {
            isValid = false;
        }
    }

    // Если есть ошибки - показываем алерт и не даем отправить форму
    if (!isValid) {
        alert('Пожалуйста, исправьте ошибки в форме перед отправкой!');
        // Прокручиваем к первой ошибке
        const firstError = document.querySelector('.field-error:not(:empty)');
        if (firstError) {
            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        return false; // Важно: возвращаем false чтобы форма НЕ отправилась
    }

    // Для поля firstName - убирает ВСЕ пробелы (внутри тоже)
    document.getElementById('firstName').value =
        document.getElementById('firstName').value.replace(/\s+/g, '').charAt(0).toUpperCase() +
        document.getElementById('firstName').value.replace(/\s+/g, '').slice(1).toLowerCase();

    // Для поля lastName - убирает ВСЕ пробелы (внутри тоже)
    document.getElementById('lastName').value =
        document.getElementById('lastName').value.replace(/\s+/g, '').charAt(0).toUpperCase() +
        document.getElementById('lastName').value.replace(/\s+/g, '').slice(1).toLowerCase();

    // Для поля patronymic - убирает ВСЕ пробелы (внутри тоже), если поле не пустое
    const patronymicField = document.getElementById('patronymic');
    if (patronymicField.value.trim()) {
        patronymicField.value =
            patronymicField.value.replace(/\s+/g, '').charAt(0).toUpperCase() +
            patronymicField.value.replace(/\s+/g, '').slice(1).toLowerCase();
    }
    return true;
}