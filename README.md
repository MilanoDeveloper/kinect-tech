# Kinect Tech

Plataforma de gestão de academia estruturada em microserviços, com contratos API First e organização hexagonal (Ports & Adapters).

## Serviços

| Módulo | Responsabilidade | Porta local | Schema PostgreSQL |
|---|---|---:|---|
| `kinect-orchestrator` | API BFF; implementa os casos de uso e coordena chamadas entre serviços | 8080 | — |
| `kinect-persons` | Cadastro, medidas e condições de saúde de alunos e profissionais | 8081 | `persons` |
| `kinect-payments` | Pagamentos, método, parcelas, vencimento e liquidação | 8082 | `payments` |
| `kinect-trainingprograms` | Treinos, exercícios, aluno e personal responsável | 8083 | `trainingprograms` |
| `kinect-api-contracts` | Especificações OpenAPI e interfaces/modelos Java gerados | — | — |

Cada microserviço é responsável por seu schema. Os nomes de tabela e campos estão definidos pelas entidades JPA. O perfil local cria/atualiza esses objetos durante a inicialização; os demais perfis validam o schema existente.

Datas de negócio e auditoria são representadas como `LocalDate`, persistidas como `DATE` e trafegadas pela API no formato `ddMMyyyy` (por exemplo, `03102026`).

## Massa de dados local

Para gerar um conjunto variado e repetível de dados de demonstração, inicie os serviços uma vez com o perfil local para criar os schemas e tabelas e depois execute `database/seed-demo-data.sql` no banco `kinect-tech` (por exemplo, com `psql -d kinect-tech -f database/seed-demo-data.sql`). O script cria 300 pessoas, 720 pagamentos e 240 programas de treino com exercícios. Os registros têm identificadores `demo.*` ou prefixo `DEMO:`; ao executá-lo novamente, os registros de demonstração são atualizados/recriados sem remover dados que não sejam de demonstração. O script converte colunas de auditoria/pagamento preexistentes de timestamp para `DATE`, descartando a parte de hora conforme o padrão `LocalDate`.

## Requisitos e banco local

- Java 25
- Maven 3.9+
- PostgreSQL local na porta `5432`
- Um banco de dados chamado `kinect-tech`

Os arquivos `application.yml` usam variáveis de ambiente obrigatórias para conexão e configuração de produção. Os arquivos `application-local.yml` fornecem defaults apenas para desenvolvimento local (`localhost`, usuário `postgres` e senha `postgres`); altere-os ou defina `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` conforme seu ambiente.

## Compilação e execução

Gere e instale primeiro os contratos compartilhados:

```powershell
mvn -f kinect-api-contracts\pom.xml -DskipTests install
```

Em terminais separados, inicie os serviços usando o perfil local:

```powershell
mvn -f kinect-persons\pom.xml spring-boot:run "-Dspring-boot.run.profiles=local"
mvn -f kinect-payments\pom.xml spring-boot:run "-Dspring-boot.run.profiles=local"
mvn -f kinect-trainingprograms\pom.xml spring-boot:run "-Dspring-boot.run.profiles=local"
mvn -f kinect-orchestrator\pom.xml spring-boot:run "-Dspring-boot.run.profiles=local"
```

O BFF expõe as rotas abaixo e implementa o CRUD coordenando os serviços responsáveis. Ele não persiste cópias locais: cadastros de pessoas vão para `kinect-persons`, pagamentos para `kinect-payments` e treinos para `kinect-trainingprograms`.

| Recurso | Rotas |
|---|---|
| Pessoas | `/api/v1/persons`, `/api/v1/persons/{personId}` |
| Pagamentos | `/api/v1/payments`, `/api/v1/payments/{paymentId}` |
| Treinos | `/api/v1/training-programs`, `/api/v1/training-programs/{trainingProgramId}` |

`POST` cria (`201`), `GET` lista ou consulta por id, `PUT` substitui os dados (`204`) e `DELETE` remove (`204`). Consultas ou alterações de ids inexistentes retornam `404`.

Ao criar/atualizar um pagamento ou treino pelo BFF, informe `personId` ou `studentId` no corpo. O orquestrador consulta `kinect-persons` primeiro; se a pessoa não existir, responde `404` e não encaminha a gravação. Se existir, encaminha os dados ao serviço dono do recurso. Assim, a associação entre pessoa e pagamento/treino usa o id, sem compartilhar tabelas entre schemas.

Os contratos OpenAPI em `kinect-api-contracts/src/main/java/kinect/api/contracts/` são a fonte das interfaces implementadas pelos controllers inbound.

## Documentação Swagger

O Swagger UI está disponível em cada aplicação:

| Aplicação | Swagger UI | Especificação OpenAPI |
|---|---|---|
| Orchestrator | `http://localhost:8080/swagger-ui.html` | `http://localhost:8080/v3/api-docs` |
| Persons | `http://localhost:8081/swagger-ui.html` | `http://localhost:8081/v3/api-docs` |
| Payments | `http://localhost:8082/swagger-ui.html` | `http://localhost:8082/v3/api-docs` |
| Training Programs | `http://localhost:8083/swagger-ui.html` | `http://localhost:8083/v3/api-docs` |
