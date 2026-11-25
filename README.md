# 🏢 Contador Presencial

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-SDK%2024%2B%20%7C%20Target%2037-brightgreen.svg?logo=android&logoColor=white)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.02.01-4285F4.svg?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-M3-purple.svg?logo=materialdesign&logoColor=white)](https://m3.material.io)
[![AGP](https://img.shields.io/badge/AGP-9.5.1-orange.svg)](https://developer.android.com/build)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-informational.svg)](#)

> Aplicativo Android open-source desenvolvido para rastrear e contabilizar dias de trabalho presencial em modelos híbridos, promovendo controle e total privacidade sem depender de serviços externos de rastreamento de localização como o Google Maps.

---

## 📌 Sobre o Projeto

Com a popularização do trabalho híbrido, muitos profissionais precisam comprovar ou acompanhar o cumprimento de metas de comparecimento presencial ao escritório. Frequentemente, a alternativa tem sido depender do histórico de linha do tempo do Google Maps ou de anotações dispersas — o que expõe dados sensíveis de geolocalização e consome bateria.

O **Contador Presencial** nasceu para resolver essa dor com simplicidade, foco e **privacidade em primeiro lugar**:
- **100% Offline e Privado:** Não coleta, armazena ou compartilha sua localização em servidores externos.
- **Controle Direto:** Registro claro e objetivo dos dias em que você esteve presencialmente no escritório.
- **Metas Flexíveis:** Configure sua meta mensal e monitore seu progresso facilmente.

---

## 📱 Screenshots / Demonstração

| Tela Principal (Contador) | Tela de Configurações | Modo Escuro (Opcional) |
| :---: | :---: | :---: |
| <img src="docs/screenshots/home.png" width="240" alt="Tela do Contador" /> <br> *(Placeholder: Adicione o print da Home)* | <img src="docs/screenshots/settings.png" width="240" alt="Tela de Configurações" /> <br> *(Placeholder: Adicione o print de Configurações)* | <img src="docs/screenshots/dark_mode.png" width="240" alt="Modo Escuro" /> <br> *(Placeholder: Adicione o print do Modo Escuro)* |

---

## ✨ Funcionalidades

- [x] **Contador de Presenças:** Visualização destacada da contagem de dias presenciais realizados no período.
- [x] **Configuração Persistida Localmente:** Tela de configurações integrada ao banco local Room para salvar e carregar dados de endereço e periodicidade (`SEMANAL` / `MENSAL`) com validação de campos obrigatórios.
- [x] **Navegação Type-Safe:** Fluxo de telas seguro e desacoplado através do Jetpack Navigation Compose tipado com Kotlinx Serialization.
- [x] **Interface Moderna com Material 3:** Componentes visuais com suporte a Dynamic Color (Material You no Android 12+), Dark/Light theme e tipografia padronizada.
- [x] **Edge-to-Edge Nativo:** Experiência imersiva aproveitando as áreas de corte e barras do sistema Android.
- [ ] **Histórico e Calendário Mensal:** *(Planejado)* Registro das datas exatas dos comparecimentos.
- [ ] **Relatórios e Exportação:** *(Planejado)* Exportação de relatórios em formato CSV/PDF para comprovação interna.

---

## 🏗️ Arquitetura e Tecnologias

O projeto adota padrões modernos da engenharia Android recomendados pelo Google, priorizando manutenibilidade, testabilidade e separação de responsabilidades com **MVVM** e **Clean Architecture**:

- **Unidirectional Data Flow (UDF):** Estado gerenciado de forma descendente (state down) e eventos propagados de forma ascendente (events up).
- **Stateful & Stateless Composables:** Separação clara entre a lógica de estado/navegação e componentes puramente visuais, facilitando testes e previews no Android Studio.

### Principais Bibliotecas e Ferramentas

| Tecnologia | Versão | Descrição & Link Oficial |
|---|---|---|
| **Kotlin** | `2.2.10` | Linguagem base moderna com concisão e segurança de nulos ([Kotlin](https://kotlinlang.org/)) |
| **Jetpack Compose** | BOM `2026.02.01` | Toolkit declarativo moderno para criação de interfaces nativas ([Jetpack Compose](https://developer.android.com/jetpack/compose)) |
| **Material 3** | BOM | Componentes de design system Material You ([Material 3](https://m3.material.io/)) |
| **Navigation Compose** | `2.10.2` | Navegação reativa e tipada entre composables ([Navigation Compose](https://developer.android.com/guide/navigation/design)) |
| **Kotlinx Serialization** | `1.7.3` | Serialização tipada de rotas e estruturas de dados ([Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)) |
| **Room & KSP** | `2.8.5` / `2.3.14` | Persistência local SQLite tipada com geração via KSP ([Room](https://developer.android.com/training/data-storage/room)) |
| **AndroidX Core KTX / Lifecycle** | `1.10.1` / `2.11.1` | Extensões Kotlin, `ViewModel` e integração de ciclo de vida com Compose ([AndroidX](https://developer.android.com/jetpack/androidx)) |
| **AGP & R8 Optimization** | `9.5.1` | Android Gradle Plugin 9 com otimização e minificação via R8 ([AGP Release Notes](https://developer.android.com/build/releases/gradle-plugin)) |

---

## 🔑 Requisitos de API e Chaves

> [!NOTE]
> **Não são necessárias chaves de API ou arquivos de credenciais.**  
> O aplicativo opera inteiramente local no dispositivo e não requer arquivos como `google-services.json` ou chaves da Google Maps API no `local.properties`. Basta clonar e compilar!

---

## 🚀 Pré-requisitos e Como Rodar

### Pré-requisitos
- **Android Studio** Ladybug (2024.2.1) ou superior instalado.
- **JDK 11** ou superior (configurado nas configurações do Android Studio ou via Gradle Toolchains Foojay).
- Dispositivo Android físico ou Emulador com **Android 7.0 (API 24)** ou superior.

### Passo a Passo

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/<seu-usuario>/contador_presencial.git
   cd contador_presencial
   ```

2. **Abrir no Android Studio:**
   - Inicie o Android Studio.
   - Selecione **Open** e navegue até a pasta clonada `contador_presencial`.
   - Aguarde o término da sincronização do Gradle (*Sync Project with Gradle Files*).

3. **Executar no Emulador ou Dispositivo Físico:**
   - Conecte seu dispositivo via USB (com depuração USB ativada) ou inicialize um AVD (Android Virtual Device).
   - Clique no botão verde de **Run** (`Shift + F10`) no Android Studio selecionando o módulo `app`.

4. **Compilar via Linha de Comando (Opcional):**
   ```bash
   # Linux / macOS
   ./gradlew :app:assembleDebug

   # Windows (PowerShell / CMD)
   .\gradlew.bat :app:assembleDebug
   ```

5. **Executar Testes Unitários:**
   ```bash
   ./gradlew :app:testDebugUnitTest
   ```

---

## 📁 Estrutura de Pastas

Organização simplificada dos módulos e pacotes do projeto:

```text
contador_presencial/
├── app/
│   ├── build.gradle.kts                # Configuração do módulo Android (Plugins, SDKs, Dependências)
│   ├── schemas/                        # Schemas exportados do Room para versionamento e migrações
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml     # Declaração de componentes e Application do aplicativo
│       │   ├── keepRules/              # Regras de R8/ProGuard para AGP 9
│       │   ├── java/br/com/contador_presencial/
│       │   │   ├── ContadorPresencialApplication.kt # Inicialização do AppContainer
│       │   │   ├── MainActivity.kt     # Entry point e Composable da Tela Principal (Contador)
│       │   │   ├── data/               # Camada de dados (Room, DAOs, Entidades, Mappers, Repositórios)
│       │   │   ├── di/                 # Contêiner manual de dependências (AppContainer)
│       │   │   ├── domain/             # Modelos de domínio e contratos de repositório
│       │   │   ├── navigation/         # Camada de navegação tipada
│       │   │   │   ├── Routes.kt       # Rotas tipadas serializáveis (@Serializable)
│       │   │   │   └── AppNavHost.kt   # Definição e mapeamento do NavHost
│       │   │   └── ui/                 # Camada de apresentação (Interface de Usuário)
│       │   │       ├── screens/        # Telas da aplicação (Stateful & Stateless)
│       │   │       │   └── configuration/
│       │   │       │       ├── ConfigurationScreen.kt
│       │   │       │       ├── ConfigurationUiState.kt
│       │   │       │       └── ConfigurationViewModel.kt
│       │   │       └── theme/          # Tema Material 3, Tipografia e Cores
│       │   │           ├── Color.kt
│       │   │           ├── Theme.kt
│       │   │           └── Type.kt
│       │   └── res/                    # Recursos de layout, ícones e strings (strings.xml)
│       ├── test/                       # Testes unitários de JVM (JUnit 4, Coroutines Test)
│       └── androidTest/                # Testes instrumentados na plataforma (Compose UI Test, Room)
├── gradle/
│   ├── libs.versions.toml              # Version Catalog centralizado de dependências
│   └── wrapper/                        # Gradle Wrapper binaries (9.8.2)
├── AGENTS.md                           # Padrões e diretrizes técnicas para desenvolvedores e agentes
└── README.md                           # Documentação principal do projeto
```

---

## 🤝 Contribuição

Contribuições da comunidade são muito bem-vindas! Siga os passos abaixo para contribuir:

1. Faça um **Fork** do projeto.
2. Crie uma branch para sua funcionalidade ou correção:
   ```bash
   git checkout -b feature/minha-nova-funcionalidade
   ```
3. Realize seus commits seguindo boas práticas de mensagens descritivas:
   ```bash
   git commit -m "feat: adiciona persistencia de meta mensal com DataStore"
   ```
4. Envie as alterações para a sua branch remota:
   ```bash
   git push origin feature/minha-nova-funcionalidade
   ```
5. Abra um **Pull Request (PR)** detalhando as mudanças realizadas e o motivo da alteração.

---

## 📄 Licença

Este projeto é distribuído sob a licença **MIT**. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<p align="center">
  Desenvolvido com foco em produtividade, privacidade e tecnologia Android moderna.
</p>
