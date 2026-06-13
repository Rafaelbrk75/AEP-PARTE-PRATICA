# ObservaAcao API

API REST desenvolvida em Java com Spring Boot para abertura, acompanhamento e atendimento de solicitacoes urbanas. O sistema permite que cidadaos registrem problemas da cidade, acompanhem o status da solicitacao e adicionem comentarios ou imagens. Atendentes podem consultar a fila, assumir solicitacoes e atualizar o andamento.

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- H2 Database
- Swagger / OpenAPI
- Lombok
- MapStruct
- Maven

## Funcionalidades

- Login com geracao de token JWT.
- Perfis de usuario: cidadao e atendente.
- Abertura de solicitacoes urbanas.
- Geracao automatica de protocolo.
- Consulta publica por protocolo, categoria e bairro/localizacao.
- Controle de status da solicitacao.
- Validacao de transicoes de status.
- Fila de atendimento com filtros.
- Registro de historico das movimentacoes.
- Comentarios em solicitacoes.
- Upload e download de imagens anexadas.
- Controle de prazo por prioridade.
- Marcacao automatica de solicitacoes atrasadas.
- Documentacao interativa com Swagger.

## Regras principais

As solicitacoes podem ter as seguintes categorias:

- `ILUMINACAO_PUBLICA`
- `SANEAMENTO`
- `VIAS_PUBLICAS`
- `SEGURANCA`
- `MEIO_AMBIENTE`
- `OUTROS`

As prioridades possuem prazos diferentes:

- `URGENTE`: 24 horas
- `ALTA`: 72 horas
- `MEDIA`: 168 horas
- `BAIXA`: 360 horas

Fluxo de status permitido:

```text
ABERTA -> EM_ANALISE -> EM_ANDAMENTO -> RESOLVIDA -> ENCERRADA
```

A solicitacao tambem pode ser cancelada durante o fluxo, conforme as regras definidas no enum de status.

## Como executar o projeto

Clone o repositorio e acesse a pasta do projeto:

```bash
cd aep_parte2
```

Execute a aplicacao com Maven Wrapper:

```bash
.\mvnw.cmd spring-boot:run
```

A API ficara disponivel em:

```text
http://localhost:8080
```

## Banco de dados

O projeto utiliza banco H2 em memoria para desenvolvimento.

Console do H2:

```text
http://localhost:8080/h2-console
```

Dados de conexao:

```text
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: deixe em branco
```

## Usuarios de teste

Ao iniciar a aplicacao, o sistema cria automaticamente dois usuarios de teste:

```text
Cidadao:
email: cidadao@teste.com
senha: 123456

Atendente:
email: atendente@teste.com
senha: 123456
```

## Documentacao da API

A documentacao Swagger pode ser acessada em:

```text
http://localhost:8080/swagger-ui.html
```

Depois de fazer login, copie o token JWT retornado e utilize no Swagger no formato:

```text
Bearer seu_token_aqui
```

## Endpoints principais

### Autenticacao

```http
POST /api/auth/login
```

Realiza login e retorna o token JWT.

### Cidadao

```http
POST /api/cidadao/solicitacoes
GET /api/cidadao/solicitacoes
GET /api/cidadao/solicitacoes/{id}
POST /api/cidadao/solicitacoes/{id}/comentarios
POST /api/cidadao/solicitacoes/{id}/anexos
```

Permite abrir solicitacoes, listar solicitacoes do cidadao autenticado, comentar e anexar imagens.

### Atendente

```http
GET /api/atendente/solicitacoes
PATCH /api/atendente/solicitacoes/{id}/atribuir
PATCH /api/atendente/solicitacoes/{id}/status
```

Permite consultar a fila de atendimento, assumir solicitacoes e atualizar status.

### Consultas publicas

```http
GET /api/solicitacoes/protocolo/{protocolo}
GET /api/solicitacoes/categoria/{categoria}
GET /api/solicitacoes/bairro?q=nome-do-bairro
```

Permite consultar solicitacoes sem autenticacao.

### Historico e anexos

```http
GET /api/historico/solicitacoes/{id}
GET /api/anexos/{id}/arquivo
```

Permite visualizar o historico de movimentacoes e baixar arquivos anexados, respeitando as permissoes do usuario autenticado.

## Estrutura do projeto

```text
src/main/java/br/edu/unicesumar/aep_parte2
├── config       # Configuracoes de seguranca, Swagger e dados iniciais
├── controller   # Endpoints REST
├── domain       # DTOs, entidades e enums
├── exception    # Tratamento de erros
├── mapper       # Conversao entre entidades e DTOs
├── repository   # Interfaces JPA
├── security     # JWT e autenticacao
└── service      # Regras de negocio
```

## Observacoes

O projeto usa H2 com `ddl-auto=create-drop`, entao os dados sao recriados a cada inicializacao. Os arquivos enviados como anexos sao salvos por padrao em `uploads/anexos`.
