# Kinect Tech

Plataforma de gestão de academia estruturada em microserviços, com contratos API First e organização hexagonal (Ports & Adapters).

## Serviços

| Módulo | Responsabilidade | Porta local | Schema PostgreSQL |
|---|---|---:|---|
| `kinect-orchestrator` | BFF; encaminha chamadas para os serviços | 8080 | — |
| `kinect-persons` | Cadastro, medidas e condições de saúde de alunos e profissionais | 8081 | `persons` |
| `kinect-payments` | Pagamentos, método, parcelas, vencimento e liquidação | 8082 | `payments` |
| `kinect-trainingprograms` | Treinos, exercícios, aluno e personal responsável | 8083 | `trainingprograms` |
| `kinect-api-contracts` | Especificações OpenAPI e interfaces/modelos Java gerados | — | — |

Cada microserviço é responsável por seu schema. Os nomes de tabela e campos estão definidos pelas entidades JPA. O perfil local cria/atualiza esses objetos durante a inicialização; os demais perfis validam o schema existente.

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

O BFF encaminha as rotas abaixo aos serviços correspondentes; as mesmas rotas também estão disponíveis diretamente nos serviços:

| Recurso | Rotas |
|---|---|
| Pessoas | `/api/v1/persons`, `/api/v1/persons/{personId}` |
| Pagamentos | `/api/v1/payments`, `/api/v1/payments/{paymentId}` |
| Treinos | `/api/v1/training-programs`, `/api/v1/training-programs/{trainingProgramId}` |

`POST` cria (`201`), `GET` lista ou consulta por id, `PUT` substitui os dados (`204`) e `DELETE` remove (`204`). Consultas ou alterações de ids inexistentes retornam `404`.

Os contratos OpenAPI em `kinect-api-contracts/src/main/java/kinect/api/contracts/` são a fonte das interfaces implementadas pelos controllers inbound.
