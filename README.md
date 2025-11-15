# spring-todo-auth

A Spring Boot Todo application with user authentication, role management, and priority-based task management.

---

## 🌟 Features

- User registration and login with **role-based access** (`USER` and `ADMIN`)
- CRUD operations for todos, **each todo associated with a user**
- Priority-based task management (`LOW`, `MEDIUM`, `HIGH`)
- Admin panel for managing users:
  - Change user roles
  - Toggle user block status
- Spring Security configuration with login, logout, remember-me
- Validation for user registration to prevent duplicate usernames

---

## 🗂 Folder & Class Structure

src/main/java/uz/pdp/todo
├── controller
│ ├── AuthUserController.java
│ └── TodoController.java
├── service
│ ├── AuthUserService.java
│ ├── TodoService.java
│ └── TodoApplication.java
├── dao
│ ├── AuthUserDao.java
│ └── TodoDao.java
├── model
│ ├── AuthUser.java
│ └── Todo.java
├── dto
│ └── UserRegisterDTO.java
├── enums
│ ├── Role.java
│ ├── Priority.java
│ └── StringToPriorityConverter.java
├── security
│ ├── SecurityConfigurer.java
│ ├── CustomUserDetailsService.java
│ └── CustomAuthenticatedFailureHandler.java

Copy code
src/main/resources/templates
├── login.html
├── register.html
├── todos.html
├── todo_form.html
├── profile.html
├── admin.html
├── fragments.html
└── logout.html

yaml
Copy code

---

## 📊 Screenshots / UI Preview

> You can replace the placeholders with actual screenshots later.

| Login Page | Register Page | Todo List |
|------------|---------------|-----------|
| ![login](screenshots/login.png) | ![register](screenshots/register.png) | ![todos](screenshots/todos.png) |

| Todo Form | Profile Page | Admin Panel |
|-----------|--------------|-------------|
| ![todo_form](screenshots/todo_form.png) | ![profile](screenshots/profile.png) | ![admin](screenshots/admin.png) |

---

## ⚙️ Technologies Used

- Java 17+
- Spring Boot
- Spring Security
- JDBC Template
- Thymeleaf
- Lombok
- PostgreSQL (or any relational DB)

---

## 🚀 How to Run

1. Clone the repository:

```bash
git clone https://github.com/yourusername/spring-todo-auth.git
cd spring-todo-auth
Configure your database in application.properties

Build and run:

bash
Copy code
./mvnw spring-boot:run
Open in browser: http://localhost:8080/auth/login

👤 User Accounts
Register a new user via /auth/register

Admin panel: /auth/admin (manage users, roles, block/unblock)

📝 Notes
Each todo is associated with a user and has created_at and updated_at timestamps.

Registration prevents duplicate usernames.

Uses Lombok for boilerplate code reduction.
