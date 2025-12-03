// register.js
let selectedRole = '';
let userAge = 0; // Будем хранить возраст глобально

// ========== ИНИЦИАЛИЗАЦИЯ ==========
document.addEventListener('DOMContentLoaded', function() {
    // Устанавливаем максимальную дату (сегодня)
    const today = new Date().toISOString().split('T')[0]; // Получаем сегодняшнюю дату в формате YYYY-MM-DD
    document.getElementById('birthDate').max = today; // Устанавливаем максимальную дату в поле ввода

    // Восстанавливаем выбранную роль если есть
    const roleInput = document.getElementById('role'); // Находим поле с ролью
    if (roleInput && roleInput.value) { // Если поле существует и имеет значение
        selectedRole = roleInput.value; // Сохраняем выбранную роль
        showFieldsForRole(selectedRole); // Показываем соответствующие поля
    }

    // Биндим событие изменения кафедры для преподавателей
    const departmentSelect = document.getElementById('department'); // Находим select кафедры
    if (departmentSelect) { // Если он существует
        departmentSelect.addEventListener('change', updateSubjects); // Добавляем обработчик изменения
    }
});

// ========== ВЫБОР РОЛИ ==========
function selectRole(role) {
    selectedRole = role; // Сохраняем выбранную роль
    document.getElementById('role').value = role; // Устанавливаем значение в скрытое поле

    // Активная кнопка
    document.querySelectorAll('.role-btn').forEach(btn => btn.classList.remove('active')); // Убираем активный класс со всех кнопок
    event.target.classList.add('active'); // Добавляем активный класс нажатой кнопке

    // Очищаем ошибку
    document.getElementById('roleError').textContent = ''; // Очищаем сообщение об ошибке

    showFieldsForRole(role); // Показываем соответствующие поля
}

function showFieldsForRole(role) {
    // Показываем общие поля
    document.getElementById('commonFields').style.display = 'block'; // Показываем блок общих полей

    // Скрываем все специфичные поля
    document.getElementById('studentFields').style.display = 'none'; // Скрываем поля студента
    document.getElementById('teacherFields').style.display = 'none'; // Скрываем поля преподавателя

    if (role === 'student') { // Если выбрана роль студента
        document.getElementById('studentFields').style.display = 'block'; // Показываем поля студента
    } else if (role === 'teacher') { // Если выбрана роль преподавателя
        document.getElementById('teacherFields').style.display = 'block'; // Показываем поля преподавателя
    }
}

// ========== ОБЪЕДИНЕННАЯ ФУНКЦИЯ ПРОВЕРКИ ДАТЫ И РАСЧЕТА ВОЗРАСТА ==========
function calculateAge() {
    // Получаем элементы DOM
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
        return;
    }

    // 2. Проверяем формат даты YYYY-MM-DD
    const dateRegex = /^(\d{4})-(\d{2})-(\d{2})$/;
    const match = birthDateInput.match(dateRegex);

    // ВАЖНО: проверяем что match не null
    if (!match) {
        errorElement.textContent = 'Неверный формат даты. Используйте YYYY-MM-DD';
        return;
    }

    // 3. Извлекаем и проверяем компоненты даты
    const year = parseInt(match[1], 10);
    const month = parseInt(match[2], 10);
    const day = parseInt(match[3], 10);

    // 4. Проверяем год (от 1900 до текущего года)
    const currentYear = new Date().getFullYear();
    if (year < 1900) {
        errorElement.textContent = 'Год не может быть меньше 1900';
        return;
    }
    if (year > currentYear) {
        errorElement.textContent = 'Год не может быть больше текущего';
        return;
    }

    // 5. Проверяем месяц (1-12) - ЭТУ ПРОВЕРКУ НУЖНО ДОБАВИТЬ!
    if (month < 1 || month > 12) {
        errorElement.textContent = 'Месяц должен быть от 01 до 12';
        return;
    }

    // 6. Проверяем день в зависимости от месяца
    // Массив с количеством дней в месяцах для НЕВИСОКОСНОГО года
    const daysInMonth = [
        31, // январь
        28, // февраль
        31, // март
        30, // апрель
        31, // май
        30, // июнь
        31, // июль
        31, // август
        30, // сентябрь
        31, // октябрь
        30, // ноябрь
        31  // декабрь
    ];

    // Проверяем високосный ли год
    const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);

    // Получаем максимальное количество дней для данного месяца
    let maxDays = daysInMonth[month - 1];

    // Если февраль и високосный год - 29 дней
    if (month === 2 && isLeapYear) {
        maxDays = 29;
    }

    // Проверяем день
    if (day < 1 || day > maxDays) {
        let monthNames = [
            'январе', 'феврале', 'марте', 'апреле', 'мае', 'июне',
            'июле', 'августе', 'сентябре', 'октябре', 'ноябре', 'декабре'
        ];
        let monthName = monthNames[month - 1];

        if (month === 2) {
            if (isLeapYear) {
                errorElement.textContent = `В ${monthName} ${year} года может быть только 29 дней (високосный год)`;
            } else {
                errorElement.textContent = `В ${monthName} ${year} года может быть только 28 дней`;
            }
        } else {
            errorElement.textContent = `В ${monthName} может быть только ${maxDays} дней`;
        }
        return;
    }

    // 7. Создаем объект Date и проверяем что дата не в будущем
    const birthDate = new Date(year, month - 1, day);
    const today = new Date();

    // Проверяем что Date создал корректную дату
    if (birthDate.getFullYear() !== year ||
        birthDate.getMonth() !== month - 1 ||
        birthDate.getDate() !== day) {
        errorElement.textContent = 'Некорректная дата';
        return;
    }

    // Проверяем что дата не в будущем
    if (birthDate > today) {
        errorElement.textContent = 'Дата рождения не может быть в будущем';
        return;
    }

    // 8. Рассчитываем возраст
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    // Корректируем если день рождения еще не наступил в этом году
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    // 9. Проверяем возрастные ограничения
    if (age < 17) {
        errorElement.textContent = 'Минимальный возраст: 17 лет';
        ageHidden.value = '';
        ageDisplay.textContent = '';
    } else if (age > 70) {
        errorElement.textContent = 'Максимальный возраст: 70 лет';
        ageHidden.value = '';
        ageDisplay.textContent = '';
    } else {
        // Все ок, сохраняем возраст
        errorElement.textContent = '';
        ageHidden.value = age;
        userAge = age;
        ageDisplay.textContent = `Возраст: ${age} лет`;

        // Проверяем стаж если он введен
        const experienceInput = document.getElementById('workExperience');
        if (experienceInput && experienceInput.value) {
            validateWorkExperience();
        }
    }
}

 // ========== ПРОВЕРКА СТАЖА РАБОТЫ ==========
 function validateWorkExperience() {
     const experienceInput = document.getElementById('workExperience'); // Находим поле стажа
     const experience = parseInt(experienceInput.value); // Преобразуем значение в число
     const errorElement = document.getElementById('workExperienceError'); // Находим элемент для ошибки

     // Очищаем ошибку
     errorElement.textContent = ''; // Очищаем предыдущее сообщение об ошибке

     // Если поле пустое, ничего не проверяем
     if (!experienceInput.value) { // Если поле пустое
         return true; // Возвращаем true (поле необязательное)
     }

     // Проверяем что это число
     if (isNaN(experience)) { // Если значение не является числом
         errorElement.textContent = 'Введите число'; // Показываем ошибку
         return false; // Возвращаем false
     }

     // Проверяем диапазон
     if (experience < 0 || experience > 50) { // Если стаж меньше 0 или больше 50
         errorElement.textContent = 'Стаж должен быть от 0 до 50 лет'; // Показываем ошибку
         return false; // Возвращаем false
     }

     // Проверяем что стаж не больше возраста минус 14 лет
     if (userAge > 0) { // Если возраст известен
         const minAgeForExperience = 14; // Минимальный возраст для начала работы
         if (experience > userAge - minAgeForExperience) { // Если стаж больше чем (возраст - 14)
             errorElement.textContent = `Стаж слишком большой. При возрасте ${userAge} максимальный стаж: ${Math.max(0, userAge - minAgeForExperience)}`; // Показываем ошибку
             return false; // Возвращаем false
         }
     }

     return true; // Если все проверки пройдены, возвращаем true
 }

// ========== ВАЛИДАЦИЯ ПОЛЕЙ ==========
function validateUsername() {
    const username = document.getElementById('username').value.trim(); // Получаем значение логина и убираем пробелы
    const errorElement = document.getElementById('usernameError'); // Находим элемент для ошибки

    if (!username) { // Если логин пустой
        errorElement.textContent = 'Логин не может быть пустым'; // Показываем ошибку
        return false; // Возвращаем false
    }

    // Только английские буквы и цифры
    if (!/^[a-zA-Z0-9]+$/.test(username)) { // Если логин содержит другие символы
        errorElement.textContent = 'Только английские буквы и цифры'; // Показываем ошибку
        return false; // Возвращаем false
    }

    if (username.length < 3 || username.length > 50) { // Если логин слишком короткий или длинный
        errorElement.textContent = 'От 3 до 50 символов'; // Показываем ошибку
        return false; // Возвращаем false
    }

    errorElement.textContent = ''; // Очищаем ошибку
    return true; // Возвращаем true
}

function validatePassword() {
    const password = document.getElementById('password').value; // Получаем значение пароля
    const errorElement = document.getElementById('passwordError'); // Находим элемент для ошибки

    if (!password) { // Если пароль пустой
        errorElement.textContent = 'Пароль не может быть пустым'; // Показываем ошибку
        return false; // Возвращаем false
    }

    if (password.length < 3) { // Если пароль слишком короткий
        errorElement.textContent = 'Минимум 3 символа'; // Показываем ошибку
        return false; // Возвращаем false
    }

    errorElement.textContent = ''; // Очищаем ошибку
    return true; // Возвращаем true
}

// ========== ВАЛИДАЦИЯ ИМЯ И ФАМИЛИИ ==========
function validateName(fieldId, fieldName) {
    const value = document.getElementById(fieldId).value.trim(); // Получаем значение поля и убираем пробелы
    const errorElement = document.getElementById(fieldId + 'Error'); // Находим элемент для ошибки

    if (!value) { // Если поле пустое
        errorElement.textContent = `Обязательное поле`; // Показываем ошибку
        return false; // Возвращаем false
    }

    // Только русские буквы, пробел, дефис
    if (!/^[а-яА-ЯёЁ\s\-]+$/.test(value)) { // Если поле содержит другие символы
        errorElement.textContent = 'Только русские буквы'; // Показываем ошибку
        return false; // Возвращаем false
    }

    if (value.length < 2) { // Если значение слишком короткое
        errorElement.textContent = 'Минимум 2 символа'; // Показываем ошибку
        return false; // Возвращаем false
    }

    errorElement.textContent = ''; // Очищаем ошибку
    return true; // Возвращаем true
}

// ========== ВАЛИДАЦИЯ ОТЧЕСТВА (НЕОБЯЗАТЕЛЬНОЕ ПОЛЕ) ==========
function validateOptionalName(fieldId) {
    const value = document.getElementById(fieldId).value.trim(); // Получаем значение поля и убираем пробелы
    const errorElement = document.getElementById(fieldId + 'Error'); // Находим элемент для ошибки

    // Если поле пустое - ошибок нет (отчество необязательное)
    if (!value) { // Если поле пустое
        errorElement.textContent = ''; // Очищаем ошибку
        return true; // Возвращаем true
    }

    // Только русские буквы, пробел, дефис
    if (!/^[а-яА-ЯёЁ\s\-]+$/.test(value)) { // Если поле содержит другие символы
        errorElement.textContent = 'Только русские буквы'; // Показываем ошибку
        return false; // Возвращаем false
    }

    if (value.length < 2) { // Если значение слишком короткое
        errorElement.textContent = 'Минимум 2 символа'; // Показываем ошибку
        return false; // Возвращаем false
    }

    errorElement.textContent = ''; // Очищаем ошибку
    return true; // Возвращаем true
}

// ========== ФУНКЦИИ ДЛЯ СТУДЕНТОВ/ПРЕПОДАВАТЕЛЕЙ ==========
function updateStudentFields() {
    const faculty = document.getElementById('faculty').value; // Получаем выбранный факультет
    const specialtySelect = document.getElementById('specialty'); // Находим select специальностей

    specialtySelect.innerHTML = '<option value="">Выберите специальность</option>'; // Очищаем и добавляем пустой вариант
    specialtySelect.disabled = true; // Блокируем select

    if (faculty && universityData[faculty]) { // Если факультет выбран и существует в данных
        universityData[faculty].specialties.forEach(spec => { // Для каждой специальности
            const option = document.createElement('option'); // Создаем элемент option
            option.value = spec; // Устанавливаем значение
            option.textContent = spec; // Устанавливаем текст
            specialtySelect.appendChild(option); // Добавляем option в select
        });
        specialtySelect.disabled = false; // Разблокируем select
    }
}

function updateTeacherFields() {
    const faculty = document.getElementById('teacherFaculty').value; // Получаем выбранный факультет
    const departmentSelect = document.getElementById('department'); // Находим select кафедр

    departmentSelect.innerHTML = '<option value="">Выберите кафедру</option>'; // Очищаем и добавляем пустой вариант
    departmentSelect.disabled = true; // Блокируем select
    document.getElementById('subject').innerHTML = '<option value="">Сначала выберите кафедру</option>'; // Очищаем предметы
    document.getElementById('subject').disabled = true; // Блокируем select предметов

    if (faculty && universityData[faculty]) { // Если факультет выбран и существует в данных
        Object.keys(universityData[faculty].departments).forEach(dept => { // Для каждой кафедры
            const option = document.createElement('option'); // Создаем элемент option
            option.value = dept; // Устанавливаем значение
            option.textContent = dept; // Устанавливаем текст
            departmentSelect.appendChild(option); // Добавляем option в select
        });
        departmentSelect.disabled = false; // Разблокируем select
    }
}

function updateSubjects() {
    const faculty = document.getElementById('teacherFaculty').value; // Получаем выбранный факультет
    const department = document.getElementById('department').value; // Получаем выбранную кафедру
    const subjectSelect = document.getElementById('subject'); // Находим select предметов

    subjectSelect.innerHTML = '<option value="">Выберите предмет</option>'; // Очищаем и добавляем пустой вариант
    subjectSelect.disabled = true; // Блокируем select

    if (faculty && department && universityData[faculty]) { // Если факультет и кафедра выбраны
        const subjects = universityData[faculty].departments[department]; // Получаем предметы для кафедры
        if (subjects) { // Если предметы существуют
            subjects.forEach(subject => { // Для каждого предмета
                const option = document.createElement('option'); // Создаем элемент option
                option.value = subject; // Устанавливаем значение
                option.textContent = subject; // Устанавливаем текст
                subjectSelect.appendChild(option); // Добавляем option в select
            });
            subjectSelect.disabled = false; // Разблокируем select
        }
    }
}

// ========== ВАЛИДАЦИЯ ФОРМЫ ==========
function validateForm() {
    let isValid = true; // Флаг валидности формы

    // 1. Логин
    if (!validateUsername()) isValid = false; // Проверяем логин

    // 2. Пароль
    if (!validatePassword()) isValid = false; // Проверяем пароль

    // 3. Роль
    if (!selectedRole) { // Если роль не выбрана
        document.getElementById('roleError').textContent = 'Выберите тип пользователя'; // Показываем ошибку
        isValid = false; // Устанавливаем флаг в false
    }

    // 4. Общие поля
    if (selectedRole) { // Если роль выбрана
        if (!validateName('firstName', 'Имя')) isValid = false; // Проверяем имя
        if (!validateName('lastName', 'Фамилия')) isValid = false; // Проверяем фамилию
        // Отчество - необязательное поле
        if (!validateOptionalName('patronymic')) isValid = false; // Проверяем отчество

        // Дата рождения
        const birthDate = document.getElementById('birthDate').value; // Получаем дату рождения
        if (!birthDate) { // Если дата не выбрана
            document.getElementById('birthDateError').textContent = 'Выберите дату рождения'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        } else if (!isValidDate(birthDate)) { // Если дата некорректна
            document.getElementById('birthDateError').textContent = 'Некорректная дата'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }

        // Возраст должен быть рассчитан
        if (!userAge || userAge < 17 || userAge > 70) { // Если возраст некорректен
            document.getElementById('birthDateError').textContent = 'Некорректный возраст. Выберите дату рождения'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }
    }

    // 5. Специфичные поля
    if (selectedRole === 'student') { // Если выбрана роль студента
        if (!document.getElementById('faculty').value) { // Если факультет не выбран
            document.getElementById('facultyError').textContent = 'Выберите факультет'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }
        if (!document.getElementById('specialty').value || document.getElementById('specialty').disabled) { // Если специальность не выбрана или select заблокирован
            document.getElementById('specialtyError').textContent = 'Выберите специальность'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }
        if (!document.getElementById('course').value) { // Если курс не выбран
            document.getElementById('courseError').textContent = 'Выберите курс'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }
    }

    if (selectedRole === 'teacher') { // Если выбрана роль преподавателя
        if (!document.getElementById('teacherFaculty').value) { // Если факультет не выбран
            document.getElementById('teacherFacultyError').textContent = 'Выберите факультет'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }
        if (!document.getElementById('department').value || document.getElementById('department').disabled) { // Если кафедра не выбрана или select заблокирован
            document.getElementById('departmentError').textContent = 'Выберите кафедру'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }
        if (!document.getElementById('subject').value || document.getElementById('subject').disabled) { // Если предмет не выбран или select заблокирован
            document.getElementById('subjectError').textContent = 'Выберите предмет'; // Показываем ошибку
            isValid = false; // Устанавливаем флаг в false
        }

        // Проверка стажа работы
        if (!validateWorkExperience()) { // Если стаж некорректен
            isValid = false; // Устанавливаем флаг в false
        }

        // Дополнительная проверка: стаж должен быть числом
//        const experienceInput = document.getElementById('workExperience'); // Находим поле стажа
//        if (experienceInput && experienceInput.value) { // Если поле существует и имеет значение
//            const experience = parseInt(experienceInput.value); // Преобразуем значение в число
//            if (isNaN(experience)) { // Если значение не число
//                document.getElementById('workExperienceError').textContent = 'Введите число'; // Показываем ошибку
//                isValid = false; // Устанавливаем флаг в false
//            }
//        }
    }

    return isValid; // Возвращаем результат валидации
}