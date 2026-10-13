# API de Autenticação com JWT

API REST desenvolvida com **Spring Boot** para cadastro e autenticação de usuários. As senhas são protegidas com **BCrypt** e as rotas protegidas são acessadas por meio de tokens **JWT**.

## Sumário

- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Como funciona a autenticação](#como-funciona-a-autenticação)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Endpoints](#endpoints)
- [Exemplos de uso](#exemplos-de-uso)
- [Segurança](#segurança)
- [Banco de dados](#banco-de-dados)
- [Status HTTP](#status-http)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Autor](#autor)

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem |
| Spring Boot | Framework da API |
| Spring Security | Autenticação e autorização |
| Spring Data JPA | Persistência de dados |
| H2 Database | Banco de dados local (modo arquivo) |
| BCrypt | Hash de senhas |
| JWT (JJWT) | Geração e validação de tokens |
| Maven | Gerenciamento de dependências |

## Funcionalidades

- Cadastro de usuários
- Senhas armazenadas como hash BCrypt (com salt)
- Login por e-mail e senha
- Geração de token JWT assinado, com expiração
- Validação do token JWT por filtro de autenticação
- Rota protegida por autenticação
- Tratamento global de erros (e-mail já cadastrado, credenciais inválidas)

## Como funciona a autenticação

```text
1. Cliente  ──POST /usuarios/register──▶  API   (senha vira hash BCrypt e é salva)
2. Cliente  ──POST /usuarios/login─────▶  API   (valida credenciais e gera o JWT)
3. Cliente  ◀──────── { "token": "..." } ─ API
4. Cliente  ──GET /usuarios/teste──────▶  API   (header Authorization: Bearer <token>)
5. JwtAuthenticationFilter valida assinatura e expiração e libera o acesso
```

## Pré-requisitos

- [JDK 21](https://adoptium.net/) ou superior
- [Maven 3.9+](https://maven.apache.org/)
- OpenSSL (opcional, apenas para gerar a chave JWT)

Verifique as instalações:

```bash
java -version
mvn -version
```

## Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/0rphis/java-cryptography.git
cd java-cryptography
```

### 2. Crie o arquivo de configuração

Copie o arquivo de exemplo:

```bash
# Linux / macOS
cp src/main/resources/application-example.properties src/main/resources/application.properties

# Windows (PowerShell)
Copy-Item src/main/resources/application-example.properties src/main/resources/application.properties
```

### 3. Gere e configure a chave JWT

```bash
openssl rand -base64 32
```

Cole o resultado em `src/main/resources/application.properties`:

```properties
jwt.secret=SUA_CHAVE_BASE64
jwt.expiration-ms=3600000
```

> O `application.properties` contém a chave real e é ignorado pelo Git. Use o `application-example.properties` como modelo.

### 4. Inicie a aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## Endpoints

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| POST | `/usuarios/register` | Não | Cadastra um usuário |
| POST | `/usuarios/login` | Não | Realiza login e retorna um JWT |
| GET | `/usuarios/teste` | **Sim (JWT)** | Rota protegida de teste |

## Exemplos de uso

### Cadastro

```http
POST /usuarios/register
Content-Type: application/json
```

```json
{
  "nome": "Ana Silva",
  "email": "ana.silva@email.com",
  "senha": "Senha@123"
}
```

Resposta esperada:

```text
201 Created
Usuário cadastrado com sucesso!
```

Com `curl`:

```bash
curl -X POST http://localhost:8080/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana Silva","email":"ana.silva@email.com","senha":"Senha@123"}'
```

### Login

```http
POST /usuarios/login
Content-Type: application/json
```

```json
{
  "email": "ana.silva@email.com",
  "senha": "Senha@123"
}
```

Resposta esperada:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Com `curl`:

```bash
curl -X POST http://localhost:8080/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana.silva@email.com","senha":"Senha@123"}'
```

### Rota protegida

```http
GET /usuarios/teste
Authorization: Bearer SEU_TOKEN_JWT
```

Resposta esperada:

```text
Token válido. Usuário autenticado: ana.silva@email.com
```

Com `curl`:

```bash
curl http://localhost:8080/usuarios/teste \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

## Segurança

- Senhas **nunca** são salvas em texto puro.
- O BCrypt gera hashes com **salt** aleatório, então senhas iguais resultam em hashes diferentes.
- O JWT é **assinado** e possui **tempo de expiração** configurável.
- Rotas protegidas exigem um token JWT válido.
- A chave JWT e os arquivos do banco (`data/`) não são versionados.
- Em produção, recomenda-se HTTPS e um banco de dados como PostgreSQL ou MySQL.

## Banco de dados

O projeto usa o **H2 em modo arquivo**. Os dados ficam salvos na pasta `data/` e **persistem entre reinicializações**. Para zerar o banco, pare a aplicação e apague essa pasta.

## Status HTTP

| Status | Significado |
|---|---|
| `200 OK` | Login realizado ou token válido |
| `201 Created` | Usuário cadastrado |
| `401 Unauthorized` | Credenciais ou token inválidos |
| `403 Forbidden` | Rota protegida sem autenticação |
| `409 Conflict` | E-mail já cadastrado |

## Estrutura do projeto

```text
java-cryptography/
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── controller/
│   │   │   └── AuthController.java
│   │   ├── dto/
│   │   │   ├── AuthResponse.java
│   │   │   ├── LoginRequest.java
│   │   │   └── RegistroRequest.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── model/
│   │   │   └── Usuario.java
│   │   ├── repository/
│   │   │   └── UsuarioRepository.java
│   │   ├── security/
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtService.java
│   │   │   └── SecurityConfig.java
│   │   ├── service/
│   │   │   └── UsuarioService.java
│   │   └── AuthApplication.java
│   └── resources/
│       ├── application-example.properties
│       └── application.properties
├── .gitignore
└── pom.xml
```

| Pacote | Responsabilidade |
|---|---|
| `controller` | Endpoints REST |
| `dto` | Objetos de entrada e saída da API |
| `exception` | Tratamento global de erros |
| `model` | Entidade `Usuario` |
| `repository` | Acesso ao banco (Spring Data JPA) |
| `security` | Filtro JWT, geração do token e configuração do Spring Security |
| `service` | Regras de negócio do cadastro e login |

## Autor

**Mateus Daniel**

[GitHub](https://github.com/0rphis)
