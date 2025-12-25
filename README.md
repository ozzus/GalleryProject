# GalleryProject

Android-приложение для просмотра галереи и базовой авторизации. Проект построен на MVVM и использует Hilt для DI.

## Установка

1. Установите Android Studio и Android SDK.
2. Откройте проект в Android Studio.
3. Дождитесь синхронизации Gradle.

## Запуск

- Запуск из Android Studio: `Run` -> выбрать устройство/эмулятор.
- Или через Gradle:
  - Windows: `.\gradlew :app:installDebug`
  - macOS/Linux: `./gradlew :app:installDebug`

## Архитектура

- **UI**: `Fragment` + `ViewModel` (MVVM).
- **Domain/Data**: репозитории для работы с данными.
- **DI**: Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, модули в `di/`).
- **Навигация**: Navigation Component + Safe Args.

Основные пакеты:

- `auth/` — экраны и ViewModel авторизации.
- `data/` — репозитории (auth, photos).
- `home/` — экран ленты фото.
- `profile/` — профиль и настройки.

## API

Базовый URL: `https://gallery.prod2.webant.ru/`

### Авторизация

**Login**

```
POST /token
Content-Type: application/x-www-form-urlencoded

grant_type=password
username=...
password=...
client_id=...
client_secret=...
```

**Refresh**

```
POST /token
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token
refresh_token=...
client_id=...
client_secret=...
```

### Регистрация

```
POST /users
Content-Type: application/ld+json

{
  "email": "user@example.com",
  "birthday": "2024-07-12T13:02:08.213Z",
  "displayName": "UserName",
  "phone": "+70000000000",
  "plainPassword": "password"
}
```

### Загрузка изображения

```
GET /get_files/{path}
```

Пример:

```
/get_files/2024/07/05/0c1ab51a/0fd023fa8a501209fcfd4c2a0578dd51.jpg
```

### Флоу работы с фото

1. Создать объект `Files` (`POST /files`, content-type `multipart/form-data`).
2. Получить `iri` созданной модели (например, `/files/2`).
3. Создать `Photo` с полем `file: "/files/2"`.

## Примечания

- Для `POST /token` требуются `client_id` и `client_secret`. Если их нет, сервер вернет ошибку.
- Для теста без авторизации можно использовать режим гостя в экране логина.
