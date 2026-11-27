# Padrões de Projeto na Prática com Spring Boot (GoF)

API REST desenvolvida em **Java 17** e **Spring Boot 4.0.3** para demonstrar a aplicação prática dos padrões de projeto **Gang of Four (GoF)** — **Singleton**, **Strategy** e **Facade** — no ecossistema Spring, integrando persistência em memória com **H2 Database**, documentação interativa com **OpenAPI/Swagger UI** e consulta automática de endereços via **ViaCEP** com **Spring Cloud OpenFeign**.

---

## Padrões de Projeto Aplicados

| Padrão GoF | Como é aplicado no projeto | Classes envolvidas |
| :--- | :--- | :--- |
| **Singleton** | O container IoC do Spring instancia e gerencia componentes anotados com `@Service` e `@Repository` como instâncias únicas compartilhadas na aplicação. | `ClientServiceImpl`, `ClientRepository`, `AddressRepository` |
| **Strategy** | Define uma interface de contrato para as regras de negócio de clientes, permitindo múltiplas implementações intercambiáveis sem alterar o controller. | `ClientService` (interface) e `ClientServiceImpl` (implementação) |
| **Facade** | Expõe uma interface simplificada para operações complexas, abstraindo a orquestração entre a consulta externa na API do ViaCEP, o cache/persistência do endereço e o vínculo com o cliente. | `ClientServiceImpl` (`saveCostumerWithCep`) e `ClientRestController` |

---

## Arquitetura e Fluxo de Funcionamento

```text
src/main/java/br/com/gof
├── controller
│   └── ClientRestController.java       # Endpoints REST (/clients)
├── model
│   ├── Address.java                    # Entidade JPA mapeada a partir do retorno do ViaCEP (CEP como @Id)
│   ├── AddressRepository.java          # CrudRepository<Address, String>
│   ├── Client.java                     # Entidade JPA com relacionamento @ManyToOne para Address
│   └── ClientRepository.java           # CrudRepository<Client, Long>
├── service
│   ├── ClientService.java              # Contrato Strategy para operações de cliente
│   ├── ViaCepService.java              # Cliente HTTP declarativo (@FeignClient) para viacep.com.br
│   └── impl
│       └── ClientServiceImpl.java      # Implementação Singleton/Facade com injeção via construtor
└── PadroesProjetoSpringApplication.java # Classe principal (@EnableFeignClients)
```

### Fluxo de Cadastro e Atualização (`POST /clients` e `PUT /clients/{id}`)

1. O cliente envia apenas o `name` e o `cep` dentro do objeto `address`.
2. O `ClientServiceImpl` verifica no `AddressRepository` (H2) se o CEP já está cadastrado localmente.
3. Caso não exista, aciona o `ViaCepService` (`GET https://viacep.com.br/ws/{cep}/json`), persiste o `Address` retornado no H2 e o vincula ao `Client`.
4. Caso já exista, reaproveita o registro salvo no banco sem realizar uma nova chamada externa.

---

## Stack Tecnológica

- **Linguagem:** Java 17
- **Framework:** Spring Boot `4.0.3` (`spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`)
- **Integração HTTP:** Spring Cloud `2025.1.1` (`spring-cloud-starter-openfeign`)
- **Documentação API:** Springdoc OpenAPI UI `3.2.1` (`springdoc-openapi-starter-webmvc-ui`)
- **Banco de Dados:** H2 Database em memória + `spring-boot-h2console`
- **Build Tool:** Maven (`mvnw`)

---

## Como Executar o Projeto

### Pré-requisitos

- **JDK 17** (ou superior) instalado e configurado na variável `JAVA_HOME`
- Conexão com a internet para consulta à API pública do [ViaCEP](https://viacep.com.br/)

### Executando via Maven Wrapper

No Windows (PowerShell/CMD):
```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:
```bash
./mvnw spring-boot:run
```

### Acessos Rápidos (Aplicação em Execução)

- **API Base URL:** `http://localhost:8080`
- **Swagger UI (Documentação Interativa):** `http://localhost:8080/swagger-ui.html`
- **H2 Console:** `http://localhost:8080/h2-console`

---

## Endpoints da API (`/clients`)

| Método | Rota | Descrição | Corpo da Requisição (JSON) |
| :--- | :--- | :--- | :--- |
| `GET` | `/clients` | Lista todos os clientes cadastrados com seus endereços | — |
| `GET` | `/clients/{id}` | Busca um cliente específico pelo `id` | — |
| `POST` | `/clients` | Cadastra um cliente e preenche o endereço automaticamente pelo CEP | `{"name": "Diego", "address": {"cep": "01001000"}}` |
| `PUT` | `/clients/{id}` | Atualiza os dados e o endereço de um cliente existente | `{"name": "Diego Silva", "address": {"cep": "20040002"}}` |
| `DELETE` | `/clients/{id}` | Remove um cliente pelo `id` | — |

### Exemplo de Requisição e Resposta (`POST /clients`)

**Request:**
```json
{
  "name": "Diego Silva",
  "address": {
    "cep": "01001000"
  }
}
```

**Response (`200 OK`):**
```json
{
  "id": 1,
  "name": "Diego Silva",
  "address": {
    "cep": "01001-000",
    "logradouro": "Praça da Sé",
    "complemento": "lado ímpar",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP",
    "estado": "São Paulo",
    "ibge": "3550308",
    "gia": "1004",
    "ddd": "11",
    "siafi": "7107"
  }
}
```
