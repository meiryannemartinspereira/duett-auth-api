# Duett Auth API

Sistema de autenticação completo com controle de acesso por perfil (USER / ADMIN), usando JWT, refresh tokens e gerenciamento administrativo.

---

## 🔹 Tecnologias

* **Backend:** Java Spring Boot (REST API + Spring Security + JWT)
* **Banco:** H2 In-Memory
* **Documentação:** Swagger / OpenAPI
* **Segurança:** BCrypt, JWT stateless, Bean Validation, Exception Handler global

---

## 🔹 Funcionalidades

### Cadastro de Usuário

* Nome, Email (único), CPF (único), Senha (criptografada), Perfil (USER / ADMIN)
* Validação de dados duplicados e formato do CPF

### Login

* Autenticação via JWT
* Access e Refresh tokens
* Bloqueio de tentativas após múltiplas falhas

### Rota protegida `/me`

* Retorna dados do usuário autenticado
* JWT obrigatório

### Troca de senha

* Confirma senha atual
* Valida nova senha
* Atualiza `tokenVersion`

### Refresh Token

* Rotação segura de tokens

### Logout

* Invalida tokens aumentando `tokenVersion`

### Área Administrativa

* Listagem e exclusão de usuários (ROLE_ADMIN)
* Rotas protegidas por perfil

### Documentação

* Swagger / OpenAPI disponível em `/swagger-ui.html`

---

## 🔹 Estrutura Backend

```
src/
├── controller/
├── service/
├── repository/
├── security/
├── config/
├── dto/
└── exception/
```

---

## 🔹 Executando a API

### Clonar repositório e compilar

```bash
git clone <repo-url>
cd duett-auth
mvn clean install
```

### Executar a API

```bash
mvn spring-boot:run
```

### H2 Database Console

* URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
* JDBC URL: `jdbc:h2:mem:authdb`
* Username: `sa`
* Password: (vazio)

### Swagger / OpenAPI

* URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔹 Testes com cURL

### Register

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"User Test","email":"user@test.com","password":"123456","cpf":"123.456.789-10"}'
```

### Authenticate

```bash
curl -X POST http://localhost:8080/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"123456"}'
```

### Get Current User

```bash
curl -X GET http://localhost:8080/auth/me \
  -H "Authorization: Bearer <access_token>"
```

### Refresh Token

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<refresh_token>"}'
```

### Logout

```bash
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer <access_token>"
```

### Admin - List Users

```bash
curl -X GET http://localhost:8080/admin/users \
  -H "Authorization: Bearer <admin_access_token>"
```

---

## 🔹 Diferenciais

* JWT Stateless Authentication
* Proteção por perfil
* Login attempt blocking
* Refresh token seguro
* Código limpo e organizado em camadas
* Projeto pronto para deploy
