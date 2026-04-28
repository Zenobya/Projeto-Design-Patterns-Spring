# 🧩 Explorando Padrões de Projetos na Prática com Java

> 🎓 Projeto desenvolvido durante o **Bootcamp NTT DATA — Backend Java com Spring AI** na plataforma [DIO](https://www.dio.me)
>
> 📌 Desafio: **Design Patterns com Java: Dos Clássicos (GoF) ao Spring Framework**

---

## 📖 Sobre o projeto

API REST de gerenciamento de clientes que consome a API do **ViaCEP** para preenchimento automático de endereços. O projeto demonstra na prática o uso de **Design Patterns** com **Spring Boot**.

---

## 🎯 Design Patterns utilizados

| Pattern | Onde é aplicado | O que faz |
|---------|----------------|-----------|
| **Singleton** | `@Service`, `@Autowired` | O Spring garante uma única instância de cada componente |
| **Strategy** | `ClienteService` (interface) | Permite trocar a implementação sem alterar o controller |
| **Facade** | `ClienteRestController` | Esconde a complexidade do banco e do ViaCEP atrás de endpoints simples |

---

## ✨ Melhorias implementadas

### 🛡️ Tratamento de erros global
Adicionado `GlobalExceptionHandler` para capturar erros e retornar respostas HTTP adequadas:

| Situação | Resposta anterior | Resposta atual |
|----------|------------------|----------------|
| Cliente não encontrado | 500 Internal Server Error | 404 Not Found + mensagem clara |
| CEP inválido ou ausente | 500 Internal Server Error | 400 Bad Request + mensagem clara |

### ✅ Validação de CEP
Antes de consultar o ViaCEP, o sistema agora valida se o CEP foi informado e se possui 8 dígitos numéricos.

### 🔁 Correção no atualizar
O método `atualizar` agora verifica se o cliente existe antes de salvar e seta o ID corretamente.

### 📬 Status HTTP corretos
- `POST` retorna **201 Created** (antes retornava 200)
- `DELETE` retorna **204 No Content** (antes retornava 200)

---

## 🛠️ Tecnologias utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Web**
- **H2 Database** (banco em memória)
- **OpenFeign** (consumo da API ViaCEP)

---

## 📁 Estrutura do projeto

```
src/
├── main/
│   └── java/
│       └── one/digitalinnovation/gof/
│           ├── controller/
│           │   ├── ClienteRestController.java   # Facade — endpoints REST
│           │   └── GlobalExceptionHandler.java  # Tratamento global de erros (melhoria)
│           ├── model/
│           │   ├── Cliente.java
│           │   ├── ClienteRepository.java       # interface JPA
│           │   ├── Endereco.java
│           │   └── EnderecoRepository.java      # interface JPA
│           ├── service/
│           │   ├── impl/
│           │   │   └── ClienteServiceImpl.java  # Singleton + Facade
│           │   ├── ClienteService.java          # Strategy — interface
│           │   └── ViaCepService.java           # Client HTTP via OpenFeign
│           └── Application.java
└── test/
    └── java/
        └── one/digitalinnovation/gof/
            └── LabPadroesProjetoSpringApplicationTests.java
```

---

## ⚙️ Como executar

### 📌 Pré-requisitos
- Java 17+
- Maven
- IntelliJ IDEA (recomendado)

### ▶️ Passo a passo

**1. Clone o repositório**
```bash
git clone https://github.com/Zenobya/Projeto-Design-Patterns-Spring.git
```

**2. Abra na IDE e aguarde o Maven baixar as dependências**

**3. Execute a classe `Application.java`**

A aplicação estará disponível em:
```
http://localhost:8080
```

---

## 🧪 Testando a API

Recomendo usar o **Postman** ou **Insomnia**.

### Inserir cliente
```http
POST http://localhost:8080/clientes
Content-Type: application/json

{
    "nome": "Zenobya",
    "endereco": {
        "cep": "01310100"
    }
}
```

### Buscar todos
```http
GET http://localhost:8080/clientes
```

### Buscar por ID
```http
GET http://localhost:8080/clientes/1
```

### Atualizar
```http
PUT http://localhost:8080/clientes/1
Content-Type: application/json

{
    "nome": "Zenobya Atualizada",
    "endereco": {
        "cep": "01310100"
    }
}
```

### Deletar
```http
DELETE http://localhost:8080/clientes/1
```

### Testando os erros (melhorias)
```http
GET http://localhost:8080/clientes/999
# Retorna: 404 - "Cliente não encontrado com id: 999"

POST http://localhost:8080/clientes
{ "nome": "Teste", "endereco": { "cep": "123" } }
# Retorna: 400 - "CEP inválido: 123"
```

---

## 📄 Licença

Este projeto está sob a licença **MIT**.

---

## 👩‍💻 Autora

Desenvolvido por **Camila Machado - Zenóbya** ✨

[![GitHub](https://img.shields.io/badge/GitHub-Zenobya-181717?style=flat&logo=github&logoColor=white)](https://github.com/Zenobya)
[![DIO](https://img.shields.io/badge/DIO-Bootcamp-F97316?style=flat&logo=rocket&logoColor=white)](https://www.dio.me)
[![NTT DATA](https://img.shields.io/badge/NTT_DATA-Backend_Java_com_Spring_AI-00ADEF?style=flat)](https://www.dio.me/bootcamp/ntt-data-backend-java-com-spring-ai)