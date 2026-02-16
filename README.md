# Coupon API – Desafio Técnico (Create & Delete)

Este projeto implementa **apenas o que o desafio pede como foco**: 
- **Create** (`POST /coupon`)
- **Delete (soft delete)** (`DELETE /coupon/{id}`)

E inclui também o **GET** (`GET /coupon/{id}`) para facilitar validação manual do soft delete.

## ✅ Regras de negócio implementadas

- Campos obrigatórios na criação: `code`, `description`, `discountValue`, `expirationDate`.
- `code`:
  - deve ser **alfanumérico** e ter **6 caracteres**;
  - aceita caracteres especiais **na entrada**, mas eles são **removidos antes de salvar e retornar** (ex.: `ABC-123` -> `ABC123`).
- `discountValue`:
  - mínimo de **0.5**, sem máximo predeterminado.
- `expirationDate`:
  - **não pode estar no passado**.
- `published`:
  - pode ser criado como `true` ou `false`.

### Delete (soft delete)
- Pode deletar a qualquer momento.
- **Soft delete**: o registro permanece no banco, mudando `status` para `DELETED`.
- **Não pode deletar duas vezes** (segunda tentativa retorna **409 Conflict**).

## 🧱 Arquitetura (Clean Architecture + “intenções”)

Não existe `CouponService` genérico.

Cada ação do usuário é um caso de uso:
- `CreateCoupon`
- `DeleteCoupon`
- `GetCoupon`

As regras ficam no **domínio** (`domain/...`) e são testadas diretamente.

## ▶️ Como executar

### Rodar com H2 (padrão)
```bash
mvn test
mvn spring-boot:run
```

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- H2 Console: `http://localhost:8081/h2-console`

### Rodar com Docker Compose (Postgres)
```bash
docker compose up --build
```

## 📌 Exemplos

### Create
```bash
curl --location --request POST 'http://localhost:8081/coupon' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "code": "ABC-123",
    "description": "Promo",
    "discountValue": 0.8,
    "expirationDate": "2099-01-01T00:00:00Z",
    "published": false
  }'
```

### Delete
```bash
curl --location --request DELETE 'http://localhost:8081/coupon/{id}'
```

## ✅ Testes

- Testes de domínio cobrindo regras.
- Teste de integração (MockMvc) cobrindo comportamento de Create e Delete.

