# E-commerce API

E-commerce API é uma aplicação RESTful desenvolvida em Java 17 com Spring Boot, permitindo gerenciamento de produtos, pedidos e autenticação de usuários com JWT.

## Tecnologias Utilizadas

* Java 17
* Spring Boot 3+
* Spring Security (JWT)
* Spring Data JPA (MySQL)
* Lombok
* Maven
* MySQL

## Requisitos

* Java 17

* MySQL 8.0.41

## Como Executar

### 1. Preparar o Ambiente

1. Clone este repositório:

```
git clone https://github.com/wallace-rocha/ecommerce-system.git
```
```
cd <path-do-seu-projeto>
```

### 2. Configurar o Banco de Dados (MySQL)

1. Faça o dump do banco de dados com o arquivo disponibilizado no link abaixo:

* https://drive.google.com/file/d/1nail1rSpuq6SJh64aIU7-ZBgLDKNKTc7/view?usp=sharing
2. Compilar e Rodar a Aplicação
```
 mvn clean install
```

## 3. Autenticação & Segurança

O sistema usa JWT (JSON Web Token) para autenticação.

1. Registrar um Usuário (ADMIN ou USER).
* Só tem permissão para criação usuários com role ADMIN.
```
POST http://localhost:8080/api/v1/auth/register
```
* Body (JSON)
```
{
    "name": "Wallace Rocha"
    "username": "wallace",
    "password": "123456",
    "cpf": "09798467060"
    "role": "USER"
}
```
2. Fazer login
```
POST http://localhost:8080/api/v1/auth/login
```
* Body (JSON)
```
{
    "username": "wallace",
    "password": "123456"
}
```
* Resposta (Token JWT)
```
{
    "token": "eyJhbGciOiJIUzI1..."
}
```

###  Importante
* Para acessar os endpoints protegidos, inclua o token JWT no cabeçalho Authorization.
* O usuário admin já existe no dump do banco. A senha é: 123456.