
---
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
- Uses Lombok for boilerplate code reduction

---

## 🗂 Folder & Class Structure

```plaintext
src/main/java/uz/pdp/todo
├── controller
│   ├── AuthUserController.java      # User authentication & profile
│   └── TodoController.java          # Todo CRUD and UI mapping
├── service
│   ├── AuthUserService.java         # User service (CRUD, login, role)
│   ├── TodoService.java             # Todo service (CRUD)
│   └── TodoApplication.java         # Spring Boot application entry point
├── dao
│   ├── AuthUserDao.java             # DB interaction for users
│   └── TodoDao.java                 # DB interaction for todos
├── model
│   ├── AuthUser.java                # User entity/model
│   └── Todo.java                    # Todo entity/model
├── dto
│   └── UserRegisterDTO.java         # User registration DTO
├── enums
│   ├── Role.java                    # User roles
│   ├── Priority.java                # Todo priorities
│   └── StringToPriorityConverter.java # Converter for request params
└── security
    ├── SecurityConfigurer.java           # Spring Security config
    ├── CustomUserDetailsService.java     # UserDetailsService implementation
    └── CustomAuthenticatedFailureHandler.java # Login failure handler
````

```plaintext
src/main/resources/templates
├── login.html
├── register.html
├── todos.html
├── todo_form.html
├── profile.html
├── admin.html
├── fragments.html
└── logout.html
```

---

## ⚙️ Technologies Used

* Java 17+
* Spring Boot
* Spring Security
* JDBC Template
* Thymeleaf
* Lombok
* PostgreSQL (or any relational DB)

---

## 🚀 How to Run

1. Clone the repository:

```bash
git clone https://github.com/yourusername/spring-todo-auth.git
cd spring-todo-auth
```

2. Configure your database in `application.properties`.

3. Build and run:

```bash
./mvnw spring-boot:run
```

4. Open in browser: [http://localhost:8080/auth/login](http://localhost:8080/auth/login)

---

## 👤 User Accounts

* Register a new user via `/auth/register`
* Admin panel: `/auth/admin` (manage users, roles, block/unblock)

---

<div align="center">

**vv**

</div>
