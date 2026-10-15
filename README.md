# awrds

API REST que calcula os intervalos de prêmios consecutivos dos produtores vencedores
do Golden Raspberry Awards. Na inicialização a aplicação lê um CSV, persiste os filmes
em um banco H2 em memória e expõe os intervalos mínimo e máximo entre vitórias.

## Requisitos

- **JDK 17** (foi desenvolvido com java 17)
- Nenhuma instalação de Maven é necessária
- Nenhum banco externo é necessário — o H2 roda em memória

## Rodando a aplicação

Poweshell
```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

Para gerar e executar o jar:

```powershell
.\mvnw.cmd clean package
java -jar target\awrds-0.0.1-SNAPSHOT.jar
```

## Endpoints

### `GET /api/v1/movies/intervals`

Retorna o produtor com o menor intervalo entre duas vitórias consecutivas e o
produtor com o maior intervalo. Quando há empate, todos os registros empatados
aparecem na lista.

```bash
curl http://localhost:8080/api/v1/movies/intervals
```

```json
{
  "min": [
    { "producer": "Joel Silver", "interval": 1, "previousWin": 1990, "followingWin": 1991 }
  ],
  "max": [
    { "producer": "Matthew Vaughn", "interval": 13, "previousWin": 2002, "followingWin": 2015 }
  ]
}
```

### `GET /api/v1/movies`

Lista todos os filmes vencedores persistidos, ordenados por ano.

```bash
curl http://localhost:8080/api/v1/movies
```

```json
[
  { "producer": "Allan Carr", "title": "Can't Stop the Music", "year": 1980, "winner": true, "id": null }
]
```

## Console do H2

Com a aplicação rodando, acesse `http://localhost:8080/h2-console` e use:

| Campo    | Valor                  |
| -------- | ---------------------- |
| JDBC URL | `jdbc:h2:mem:moviesdb` |
| Usuário  | `sa`                   |
| Senha    | *(vazia)*              |

Os filmes ficam na tabela `awards`. O schema é recriado a cada inicialização
(`spring.jpa.hibernate.ddl-auto=create`), então os dados não persistem entre execuções.

## Carga de dados (CSV)

`DataService` lê o CSV do **classpath**, em `src/main/resources/data/`. O arquivo é
delimitado por `;` e precisa conter as colunas `year`, `title`, `producers` e `winner`
(a ordem não importa — o header é mapeado por nome). Linhas inválidas são registradas
em log e ignoradas, sem interromper a carga:

- ano ausente, em branco ou não numérico
- `title` ou `producers` em branco
- linhas com menos colunas que o header

O campo `producers` é desmembrado por vírgula e pela conjunção `and`, de modo que cada
produtor é persistido como um registro próprio.

Para trocar o arquivo carregado, altere o nome passado em
`DataInitializer.run()` (`src/main/java/com/example/awrds/config/DataInitializer.java`).

## Testes

As classes de teste seguem o sufixo `Test`/`Tests`, reconhecido pelos padrões padrão do
Surefire, então um único comando executa toda a suíte — incluindo os testes de integração:

```powershell
.\mvnw.cmd clean test
```

Linux / macOS:

```bash
./mvnw clean test
```

Saída esperada:

```
Running com.example.awrds.AwrdsApplicationTests
Running com.example.awrds.controller.MovieConsistencyTest
Running com.example.awrds.repository.MovieRepositoryTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Para rodar apenas os testes de integração:

```powershell
.\mvnw.cmd clean test "-Dtest=MovieConsistencyTest,MovieRepositoryTest"
```

Para rodar uma única classe:

```powershell
.\mvnw.cmd clean test "-Dtest=MovieConsistencyTest"
```

Os relatórios ficam em `target/surefire-reports/`.

### O que cada teste de integração cobre

- **`MovieConsistencyTest`** — sobe o contexto completo da aplicação com `MockMvc` e
  valida que `GET /api/v1/movies/intervals` retorna `200` e que os objetos em `min` e
  `max` contêm `producer`, `interval`, `previousWin` e `followingWin`.
- **`MovieRepositoryTest`** — `@DataJpaTest` contra o H2, verificando que
  `findByWinnerTrueOrderByYearAsc()` retorna apenas vencedores, em ordem crescente de ano.
- **`AwrdsApplicationTests`** — verifica que o contexto Spring inicializa, o que exercita
  a carga do CSV pelo `DataInitializer`.

Todos usam o H2 em memória, sem necessidade de infraestrutura externa.

## Stack

- Java 17
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation, DevTools)
- H2 (em memória) + console
- JUnit 5, Spring Boot Test, MockMvc, AssertJ
