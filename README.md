# 🌟 Spring Todo Auth

**Spring Boot Todo ilovasi** — foydalanuvchi autentifikatsiyasi, rollar va priority-based task boshqaruvi bilan.  
<div align="center">

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)

</div>

---

## 📖 Qisqacha

Spring Todo Auth foydalanuvchilarga ro‘yxatdan o‘tish, login qilish va todos yaratish, tahrirlash, o‘chirish imkonini beradi.  
Adminlar foydalanuvchilarni boshqaradi, rollarni o‘zgartiradi va bloklash/aktivlashtirish qiladi.

---

## ✨ Imkoniyatlar

- 👤 Foydalanuvchi boshqaruvi — Ro‘yxatdan o‘tish, login, profil ko‘rish  (USER, ADMIN)
- 📝 Todo boshqaruvi — CRUD operatsiyalar, foydalanuvchiga tegishli todos  
- ⚡ Priority — LOW, MEDIUM, HIGH  
- 🛠 Admin panel — Rollarni o‘zgartirish, foydalanuvchini bloklash/aktivlashtirish, Admin/User qilib tayinlash
- 🔒 Security — Login, logout, remember-me  
- ✅ Validatsiya — username va password shartlari (uzunlik, kuchli parol), shuningdek takroriy username’lar oldini olish.

---

## 🗂 Tuzilishi

```plaintext
controller: AuthUserController, TodoController
service: AuthUserService, TodoService
dao: AuthUserDao, TodoDao
model: AuthUser, Todo
dto: UserRegisterDTO
enums: Role, Priority, StringToPriorityConverter
security: SecurityConfigurer, CustomUserDetailsService, CustomAuthenticatedFailureHandler
templates: login.html, register.html, todos.html, todo_form.html, profile.html, admin.html
