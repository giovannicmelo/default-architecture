# default-architecture

Projeto Android de referência que demonstra uma arquitetura em camadas (Clean Architecture), apresentação em **MVI**, UI em **Jetpack Compose** organizada com **Atomic Design**, injeção de dependência com **Hilt** e testes em todos os níveis.

A feature de exemplo (`feature/sample`) busca uma lista de itens em uma API, salva em um banco local e exibe numa tela — funcionando offline com o que já estiver em cache.

## Diagrama da arquitetura

As setas sólidas indicam "usa / depende de"; as tracejadas indicam implementação de interface e fornecimento via DI.

```mermaid
flowchart TB
    subgraph APP["app (host)"]
        Activity["MainActivity<br/>NavHost"]
    end

    subgraph PRESENTATION["feature/sample/presentation"]
        Nav["navigation<br/>SampleListRoute + sampleListScreen()"]
        Route["screen<br/>SampleRoute (stateful)"]
        Screen["screen<br/>SampleScreen (stateless)"]
        VM["viewmodel<br/>SampleViewModel"]
        Contract["state / intent / effect<br/>SampleUiState · SampleUiIntent · SampleUiEffect"]
    end

    subgraph DESIGN["core/ui (Design System)"]
        Theme["theme<br/>DefaultArchitectureTheme · Spacing"]
        Template["template<br/>ScreenTemplate"]
        Organism["organism<br/>AppTopBar · ItemList"]
        Molecule["molecule<br/>ListItemCard"]
        Atom["atom<br/>TitleText · BodyText · LoadingIndicator"]
    end

    subgraph DOMAIN["feature/sample/domain (Kotlin puro)"]
        UseCase["usecase<br/>GetSamplesUseCase"]
        RepoContract["repository<br/>SampleRepository (interface)"]
        DomainModel["model<br/>Sample"]
    end

    subgraph DATA["feature/sample/data"]
        RepoImpl["repository<br/>SampleRepositoryImpl"]
        Mapper["mapper<br/>toEntity() · toDomain()"]
        Remote["source/remote<br/>SampleRemoteDataSource(Impl)"]
        Local["source/local<br/>SampleLocalDataSource(Impl)"]
        Service["framework/service<br/>SampleService (Retrofit)"]
        Db["framework/db<br/>SampleDao · SampleDatabase (Room)"]
        DataModel["model<br/>SampleResponse · SampleEntity"]
    end

    subgraph DI["feature/sample/di (Hilt)"]
        DataModule["SampleDataModule"]
        DomainModule["SampleDomainModule"]
    end

    API[("API REST")]
    SQLite[("SQLite")]

    Activity --> Nav --> Route
    Route --> VM
    Route --> Screen
    VM --> Contract
    Screen --> Contract
    Screen --> Template
    Template --> Organism --> Molecule --> Atom
    Theme -.- Template

    VM --> UseCase --> RepoContract
    RepoContract --> DomainModel

    RepoImpl -. implementa .-> RepoContract
    RepoImpl --> Remote --> Service --> API
    RepoImpl --> Local --> Db --> SQLite
    RepoImpl --> Mapper
    Mapper --> DataModel
    Mapper --> DomainModel

    DataModule -. fornece .-> RepoImpl
    DomainModule -. fornece .-> UseCase
```

## Estrutura de pastas

Pacote base: `com.giovannicampos.defaultarchitecture`

```
├── MainActivity.kt                  # host de navegação (NavHost)
├── DefaultArchitectureApplication.kt # @HiltAndroidApp
├── core/
│   ├── common/                      # utilitários compartilhados (vazio por enquanto)
│   └── ui/
│       ├── theme/                   # tema e tokens (cores, espaçamento)
│       ├── navigation/              # navegação compartilhada (vazio por enquanto)
│       └── design/
│           ├── atom/
│           ├── molecule/
│           ├── organism/
│           └── template/
└── feature/                         # tudo o que não é feature é core; as features são irmãs aqui
    └── sample/                      # a feature de exemplo: copie-a para feature/<nome>
        ├── data/
        │   ├── framework/
        │   │   ├── db/                  # Room: DAO e Database
        │   │   └── service/             # Retrofit: interface da API
        │   ├── source/
        │   │   ├── local/               # data source local (usa o DAO)
        │   │   └── remote/              # data source remoto (usa o service)
        │   ├── repository/              # implementação do repository do domínio
        │   ├── mapper/                  # conversões entre modelos
        │   └── model/                   # SampleResponse (API) e SampleEntity (banco)
        ├── di/                          # módulos Hilt
        ├── domain/
        │   ├── model/                   # modelos puros
        │   ├── repository/              # contratos (interfaces)
        │   └── usecase/                 # regras de negócio
        └── presentation/
            ├── viewmodel/
            ├── model/                   # modelos específicos de UI (vazio por enquanto)
            └── ui/
                ├── screen/              # Route (stateful) + Screen (stateless)
                ├── component/           # componentes exclusivos da feature (vazio por enquanto)
                ├── navigation/          # rota e registro no NavGraph
                ├── state/
                ├── intent/
                └── effect/
```

## Conceitos

### Clean Architecture e regra de dependência

O código é dividido em três camadas, e as dependências apontam sempre para dentro, em direção ao domínio:

- **domain** — o núcleo. Contém o modelo `Sample`, o contrato `SampleRepository` e o `GetSamplesUseCase`. É **Kotlin puro**: não conhece Android, Room, Retrofit, Hilt nem a camada `data`.
- **data** — implementa os contratos do domínio. Conhece frameworks (Room, Retrofit, kotlinx-serialization) e converte seus modelos para os do domínio.
- **presentation** — depende apenas do domínio (use cases e modelos). Não sabe de onde os dados vêm.

A **inversão de dependência** acontece no repository: o domínio define a interface `SampleRepository` e a camada `data` a implementa (`SampleRepositoryImpl`). O Hilt liga uma à outra.

### Modelos por camada e mappers

Cada camada tem o seu modelo, para que mudanças em uma não vazem para as outras:

| Modelo | Camada | Papel |
|---|---|---|
| `SampleResponse` | data | formato da API (`@Serializable`) |
| `SampleEntity` | data | formato do banco (`@Entity`) |
| `Sample` | domain | conceito de negócio, sem anotações |

As conversões ficam em `data/mapper` (`toEntity()` e `toDomain()`), porque é a camada `data` que conhece os dois lados.

### Repository offline-first

`SampleRepositoryImpl` coordena as duas fontes de dados:

1. busca na API (`SampleRemoteDataSource`) e salva no banco (`SampleLocalDataSource`);
2. se não houver rede (`IOException`), mantém o que já está salvo;
3. responde **sempre a partir do banco**, que é a fonte única de verdade.

Os detalhes de framework ficam em `data/framework` (DAO do Room e service do Retrofit); os data sources em `data/source` os encapsulam atrás de interfaces próprias.

### MVI na camada de apresentação

O fluxo é unidirecional:

```
Usuário → SampleUiIntent → SampleViewModel → SampleUiState  → Screen
                                             ↘ SampleUiEffect → Route (navegação, snackbar)
```

- **`SampleUiState`** — tudo o que a tela mostra num instante (`isLoading`, `samples`). Exposto como `StateFlow`.
- **`SampleUiIntent`** — ações do usuário (`LoadSamples`, `SampleClicked`). Entram por um único ponto: `onIntent()`.
- **`SampleUiEffect`** — eventos de disparo único (`NavigateToSampleDetail`, `ShowLoadError`). Expostos por um `Channel`, para não se repetirem em recomposições ou rotação.

A tela é dividida em duas partes:

- **`SampleRoute`** (stateful) — obtém o ViewModel via `hiltViewModel()`, coleta o estado com `collectAsStateWithLifecycle` e os efeitos com `repeatOnLifecycle`.
- **`SampleScreen`** (stateless) — recebe `uiState` e `onIntent`; fácil de pré-visualizar (`@Preview`) e testar.

### Atomic Design no Design System

Os componentes reutilizáveis ficam em `core/ui/design`, organizados por nível de composição:

| Nível | Componentes | Descrição |
|---|---|---|
| Átomo | `TitleText`, `BodyText`, `LoadingIndicator` | elementos indivisíveis |
| Molécula | `ListItemCard` | combinação de átomos |
| Organismo | `AppTopBar`, `ItemList` | seções completas da interface |
| Template | `ScreenTemplate` | esqueleto da tela com slots (top bar, conteúdo, loading, snackbar) |
| Página | `SampleScreen` | template preenchido com dados reais — fica na feature |

O Design System **não conhece as features**: `ItemList` recebe seu próprio modelo (`ItemListEntry`), e é a `SampleScreen` que converte `Sample` para ele. Cores e espaçamentos vêm de `core/ui/theme` (`DefaultArchitectureTheme` e `Spacing`).

### Navegação

A `MainActivity` é o único host e declara o `NavHost`. Cada feature expõe:

- uma rota tipada com `@Serializable` (`SampleListRoute`);
- uma extensão de `NavGraphBuilder` (`sampleListScreen()`) que registra o destino e recebe os callbacks de navegação.

Assim a feature não conhece o `NavController` nem outras features — quem decide para onde ir é o host.

### Injeção de dependência (Hilt)

- `DefaultArchitectureApplication` com `@HiltAndroidApp` e `MainActivity` com `@AndroidEntryPoint`.
- `SampleDataModule` — fornece Retrofit, `SampleService`, Room e DAO, e liga as interfaces (data sources e repository) às implementações.
- `SampleDomainModule` — fornece o `GetSamplesUseCase`. O use case é criado no módulo (sem `@Inject`) para manter o domínio livre de anotações de framework.
- `SampleViewModel` usa `@HiltViewModel`.

A separação entre os dois módulos permite que os testes substituam **apenas a camada de dados**.

## Testes

Todos os testes seguem **Given / When / Then**, com o corpo separado por comentários `// Given`, `// When`, `// Then`.

### Testes na JVM (`app/src/test`)

Nomes com backticks descrevendo o cenário, por exemplo:
`` `given network is unavailable, when getSamples, then returns cached samples without saving` ``

| Alvo | Ferramentas |
|---|---|
| Mappers | JUnit |
| Repository | **MockWebServer** (API simulada com Retrofit real) + **mockK** (data source local) |
| Use case | mockK |
| ViewModel | mockK + **Turbine** (Flow) + `MainDispatcherRule` |
| Screen | **Robolectric** + Compose UI Test + **Robot Pattern** (`SampleScreenRobot`) |
| Arquitetura (`architecture/ArchitectureTest.kt`) | **Konsist** — camadas, pacotes, MVI, design system e nomes Given/When/Then |

### Testes instrumentados (`app/src/androidTest`)

Rodam em dispositivo ou emulador. Os nomes seguem `givenX_whenY_thenZ`, pois o formato DEX só aceita espaços em nomes de método a partir da API 30 (o `minSdk` é 24).

| Alvo | Ferramentas |
|---|---|
| `SampleDaoTest` | Room em memória sobre o SQLite real |
| `SampleFlowTest` (ponta a ponta) | Hilt (`HiltTestRunner`, `@TestInstallIn`) + Compose UI Test + Robot Pattern (`SampleFlowRobot`) |

No teste de ponta a ponta, `FakeSampleDataModule` substitui o `SampleDataModule` por um `FakeSampleRepository`; todo o resto (Activity, navegação, tela, ViewModel e use case) é real.

### O padrão Robot

Os Robots concentram *como* interagir com a tela (encontrar nós, clicar, verificar), deixando o teste focado em *o quê* está sendo verificado:

```kotlin
robot.showScreen(SampleUiState(samples = samples))
robot.clickSample("Segunda")
robot.assertIntentsSent(SampleUiIntent.SampleClicked(sampleId = "2"))
```

## Stack

| Tecnologia | Versão |
|---|---|
| Android Gradle Plugin | 9.4.1 |
| Kotlin | 2.2.10 |
| compileSdk / targetSdk / minSdk | 37 / 37 / 24 |
| Jetpack Compose (BOM) + Material 3 | 2026.09.00 |
| Navigation Compose | 2.10.2 |
| Lifecycle (ViewModel, runtime-compose) | 2.11.0 |
| Kotlin Coroutines | 1.11.0 |
| Hilt | 2.60.1 |
| Room (com KSP) | 2.8.5 |
| Retrofit + kotlinx-serialization | 3.0.0 / 1.9.0 |
| mockK · Turbine · MockWebServer | 1.14.11 · 1.2.1 · 4.12.0 |
| Robolectric | 4.17 |
| Konsist | 0.17.3 |

As versões ficam centralizadas em `gradle/libs.versions.toml`.

## Comandos

```bash
./gradlew :app:assembleDebug               # compila o app
./gradlew :app:testDebugUnitTest           # testes na JVM (inclui Robolectric)
./gradlew :app:connectedDebugAndroidTest   # testes instrumentados (precisa de emulador/dispositivo)
```

## Pendências conhecidas

- `BASE_URL` em `SampleDataModule` é um placeholder (`https://example.com/api/`); ao executar o app, a lista fica vazia e o snackbar de erro aparece.
- A tela de detalhe ainda não existe: o clique emite `NavigateToSampleDetail`, mas o host não navega para lugar nenhum.
- Erros HTTP (ex.: 500) são propagados como `HttpException` em vez de cair no cache.
- O schema do Room não é exportado (`exportSchema = false`); vale habilitar antes de precisar de migrações.
