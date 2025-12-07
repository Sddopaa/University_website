// adminPanel.js

// Текущий факультет пользователя
let currentFaculty = '';

// Получает факультет пользователя с сервера
async function getUserFaculty(userId) {
    try {
        const response = await fetch(`/adminPanel/getFaculty/${userId}`);
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return await response.text();
    } catch (error) {
        console.error('Ошибка при получении факультета:', error);
        return '';
    }
}

// Показывает поля в зависимости от текущей роли
async function showRoleFields() {
    const userSelect = document.getElementById('userSelect');
    const selectedOption = userSelect.options[userSelect.selectedIndex];

    // Скрываем все блоки
    document.getElementById('studentToTeacherFields').style.display = 'none';
    document.getElementById('teacherToStudentFields').style.display = 'none';
    document.getElementById('facultyInfo').style.display = 'none';
    document.getElementById('workExperienceError').textContent = '';

    // Если пользователь выбран
    if (selectedOption.value) {
        const userId = selectedOption.value;
        const currentRole = selectedOption.getAttribute('data-role');
        const userAge = selectedOption.getAttribute('data-age');
        const newRoleField = document.getElementById('newRole');
        const userAgeField = document.getElementById('userAge');

        // Сохраняем возраст
        userAgeField.value = userAge;

        // Получаем факультет пользователя с сервера
        currentFaculty = await getUserFaculty(userId);

        if (!currentFaculty) {
            alert('Не удалось получить информацию о факультете');
            return;
        }

        // Показываем информацию о факультете
        document.getElementById('facultyInfo').style.display = 'block';
        document.getElementById('facultyDisplay').textContent = getFacultyDisplayName(currentFaculty);

        // Определяем новую роль
        if (currentRole === 'student') {
            newRoleField.value = 'teacher';
            document.getElementById('studentToTeacherFields').style.display = 'block';
            loadTeacherDepartments();
        } else if (currentRole === 'teacher') {
            newRoleField.value = 'student';
            document.getElementById('teacherToStudentFields').style.display = 'block';
            loadStudentSpecializations();
        }
    } else {
        document.getElementById('newRole').value = '';
        document.getElementById('userAge').value = '';
        currentFaculty = '';
    }
}

// Получаем отображаемое название факультета
function getFacultyDisplayName(facultyCode) {
    if (!universityData || !universityData[facultyCode]) {
        return facultyCode;
    }
    return universityData[facultyCode].fullName || facultyCode;
}

// Загружает кафедры для преподавателя
function loadTeacherDepartments() {
    const deptSelect = document.getElementById('department');
    const subjectSelect = document.getElementById('subject');

    if (!deptSelect || !subjectSelect) return;

    // Очищаем
    deptSelect.innerHTML = '<option value="">Выберите кафедру</option>';
    subjectSelect.innerHTML = '<option value="">Сначала выберите кафедру</option>';
    subjectSelect.disabled = true;

    // Если есть данные для этого факультета
    if (currentFaculty && universityData && universityData[currentFaculty]) {
        const departments = Object.keys(universityData[currentFaculty].departments);

        if (departments.length > 0) {
            departments.forEach(dept => {
                const option = document.createElement('option');
                option.value = dept;
                option.textContent = dept;
                deptSelect.appendChild(option);
            });
        }
    }
}

// Обновление предметов при выборе кафедры
function updateTeacherSubjects() {
    const deptSelect = document.getElementById('department');
    const subjectSelect = document.getElementById('subject');

    if (!deptSelect || !subjectSelect) return;

    const department = deptSelect.value;

    // Очищаем
    subjectSelect.innerHTML = '<option value="">Выберите предмет</option>';
    subjectSelect.disabled = true;

    // Если выбрана кафедра
    if (currentFaculty && department && universityData && universityData[currentFaculty]) {
        const subjects = universityData[currentFaculty].departments[department];

        if (subjects && subjects.length > 0) {
            // Заполняем предметы
            subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject;
                option.textContent = subject;
                subjectSelect.appendChild(option);
            });

            // Включаем выбор предмета
            subjectSelect.disabled = false;
        }
    }
}

// Загрузка специализаций для студента
function loadStudentSpecializations() {
    const specSelect = document.getElementById('specialization');

    if (!specSelect) return;

    // Очищаем
    specSelect.innerHTML = '<option value="">Выберите специализацию</option>';

    // Если есть данные для этого факультета
    if (currentFaculty && universityData && universityData[currentFaculty]) {
        const specialties = universityData[currentFaculty].specialties;

        if (specialties && specialties.length > 0) {
            specialties.forEach(spec => {
                const option = document.createElement('option');
                option.value = spec;
                option.textContent = spec;
                specSelect.appendChild(option);
            });
        }
    }
}

// Проверка стажа работы
function validateWorkExperience() {
    const experience = document.getElementById('workExperience').value;
    const userAge = document.getElementById('userAge').value;
    const errorElement = document.getElementById('workExperienceError');

    errorElement.textContent = '';

    if (!experience) {
        errorElement.textContent = 'Введите стаж работы';
        return false;
    }

    const expNum = parseInt(experience);
    const ageNum = parseInt(userAge);

    if (isNaN(expNum)) {
        errorElement.textContent = 'Введите число';
        return false;
    }

    if (expNum < 0 || expNum > 50) {
        errorElement.textContent = 'Стаж должен быть от 0 до 50 лет';
        return false;
    }

    // Главная проверка: стаж не должен быть больше возраста минус 14
    if (ageNum - expNum < 14) {
        errorElement.textContent = 'Некорректный стаж для возраста ' + ageNum + ' лет';
        return false;
    }

    return true;
}

// Проверка полей преподавателя
function validateTeacherFields() {
    const department = document.getElementById('department').value;
    const subject = document.getElementById('subject').value;

    if (!department) {
        alert('Выберите кафедру');
        return false;
    }

    if (!subject || document.getElementById('subject').disabled) {
        alert('Выберите предмет');
        return false;
    }

    if (!validateWorkExperience()) {
        alert('Исправьте ошибку в стаже работы');
        return false;
    }

    return true;
}

// Проверка полей студента
function validateStudentFields() {
    const specialization = document.getElementById('specialization').value;
    const course = document.getElementById('course').value;

    if (!specialization) {
        alert('Выберите специализацию');
        return false;
    }

    if (!course) {
        alert('Выберите курс');
        return false;
    }

    return true;
}

// Основная отправка формы
function submitAdminForm() {
    const userSelect = document.getElementById('userSelect');
    const newRole = document.getElementById('newRole').value;

    // Проверка выбора пользователя
    if (!userSelect.value) {
        alert('Пожалуйста, выберите пользователя');
        return;
    }

    // Проверка определения новой роли
    if (!newRole) {
        alert('Невозможно определить новую роль');
        return;
    }

    // Проверка полей в зависимости от роли
    if (newRole === 'teacher') {
        if (!validateTeacherFields()) {
            return;
        }
    } else if (newRole === 'student') {
        if (!validateStudentFields()) {
            return;
        }
    }

    // Подтверждение действия
    if (!confirm('Вы уверены, что хотите изменить роль пользователя?')) {
        return;
    }

    // Отправляем форму
    document.getElementById('changeRoleForm').submit();
}

// Инициализация
document.addEventListener('DOMContentLoaded', function() {
    // Обработчик для стажа
    document.getElementById('workExperience')?.addEventListener('input', function() {
        validateWorkExperience();
    });

    // Обработчик для кафедры
    document.getElementById('department')?.addEventListener('change', function() {
        updateTeacherSubjects();
    });
});