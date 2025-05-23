# Mind Compass - Aplicativo de Bem-estar Mental

## Sobre o Projeto

Mind Compass é um aplicativo mobile Android desenvolvido para a Softtek, focado no monitoramento anônimo do bem-estar psicossocial dos colaboradores. O aplicativo permite que os usuários registrem seu humor diário, preencham questionários de autoavaliação e recebam dicas de bem-estar.

## Arquitetura

O projeto segue os princípios de Clean Architecture e o padrão MVVM (Model-View-ViewModel):

```
+---------------------------+
|       Presentation        |
| +---------------------+   |
| |     Components      |   |
| +---------------------+   |
| |        UI           |   |
| +---------------------+   |
| |     ViewModels      |   |
| +---------------------+   |
+------------+------------+-+
             |
+------------v--------------+
|         Domain            |
| +---------------------+   |
| |       Models        |   |
| +---------------------+   |
| |      Use Cases      |   |
| +---------------------+   |
| |     Repositories    |   |
| |     (Interfaces)    |   |
| +---------------------+   |
+------------+------------+-+
             |
+------------v--------------+
|          Data             |
| +---------------------+   |
| |  Repository Impls   |   |
| +---------------------+   |
| |       Local         |   |
| |   (Room Database)   |   |
| +---------------------+   |
| |       Remote        |   |
| |   (API Interfaces)  |   |
| +---------------------+   |
+---------------------------+
```

### Camadas

1. **Presentation**: Contém os componentes de UI, ViewModels e lógica de apresentação.
2. **Domain**: Contém a lógica de negócio, modelos de domínio e interfaces de repositórios.
3. **Data**: Implementações concretas de repositórios, acesso a dados locais e remotos.

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **UI Framework**: Jetpack Compose
- **Persistência Local**: Room Database
- **Injeção de Dependência**: Hilt
- **Navegação**: Navigation Compose
- **Coroutines & Flow**: Para operações assíncronas
- **Lifecycle**: ViewModel e LiveData

## Decisões Técnicas e Justificativas

### Jetpack Compose vs XML
Optamos pelo Jetpack Compose devido à sua natureza declarativa e reativa, que permite uma implementação mais rápida e manutenível da UI. A separação clara entre UI e lógica facilita os testes e a manutenção.

### Clean Architecture
A implementação de Clean Architecture permite uma separação clara entre as diferentes camadas do aplicativo, tornando-o mais modular, testável e resiliente a mudanças. Essa abordagem facilita futuras expansões, como a integração com um backend real na segunda sprint.

### MVVM
O padrão MVVM combinado com Jetpack Compose oferece um fluxo de dados unidirecional e facilita o gerenciamento de estado da aplicação. Os ViewModels são responsáveis pela lógica de apresentação, deixando os componentes de UI mais limpos e focados apenas na renderização.

### Room Database
Escolhemos o Room para persistência local por ser uma solução robusta e recomendada pelo Android Jetpack, oferecendo uma camada de abstração sobre o SQLite e integrando-se perfeitamente com Coroutines e Flow para operações assíncronas.

### Injeção de Dependência com Hilt
O Hilt simplifica a injeção de dependência no Android, reduzindo a quantidade de código boilerplate e facilitando a gestão do ciclo de vida dos componentes.

### Privacidade e Anonimidade
O aplicativo coleta apenas o e-mail corporativo (@softtek.com) para identificação, sem solicitar outros dados pessoais, garantindo o anonimato dos usuários nas avaliações de bem-estar.

## Como Executar o Projeto

### Requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 11 ou superior
- API Level 24 (Android 7.0) ou superior

### Passos para Execução

1. Clone o repositório:
   ```
   git clone https://github.com/gu-guedes/softekhealth.git
   ```

2. Abra o projeto no Android Studio

3. Sincronize o projeto com os arquivos Gradle

4. Execute o aplicativo em um dispositivo ou emulador (API Level 24+)

## Funcionalidades Implementadas (Sprint 1 - Frontend)

### LoginScreen
- Autenticação anônima com e-mail corporativo (@softtek.com)
- Validação de formato de e-mail
- Armazenamento seguro do token de sessão em SharedPreferences

### HomeScreen
- Dashboard principal com widgets interativos
- Seleção de humor diário (grid de emojis 3x3)
- Cards com métricas resumidas de bem-estar
- Gráfico de tendências de humor nas últimas 2 semanas
- Seção de dicas de bem-estar

### FormsScreen
- Questionário de autoavaliação psicossocial em múltiplas páginas
- Sliders para níveis de estresse (0-10)
- Botões de opção para frequência de sintomas
- Campos de texto para notas opcionais
- Checkboxes para seleção de múltiplos sintomas
- Opção para salvar rascunho localmente

### Funcionalidades Adicionais
- Notificações locais para lembretes diários
- Suporte a modo escuro/claro
- Estrutura preparada para integração com backend na próxima sprint

## Fluxo de Dados

1. **Autenticação**: O usuário fornece seu e-mail corporativo, que é validado e armazenado localmente.

2. **Registro de Humor**:
   - Usuário seleciona seu humor na tela inicial
   - O ViewModel processa a seleção
   - O UseCase valida os dados
   - O Repository armazena no banco de dados Room
   - A UI é atualizada para refletir o novo estado

3. **Questionário de Autoavaliação**:
   - Usuário preenche o questionário em etapas
   - Os dados são coletados pelo ViewModel
   - Ao finalizar, o UseCase processa e valida os dados
   - O Repository salva no banco de dados Room
   - Feedback visual é fornecido ao usuário

4. **Visualização de Dados**:
   - Os Repositories recuperam dados do banco local
   - Os dados são transformados e processados pelos UseCases
   - Os ViewModels preparam os dados para apresentação
   - A UI exibe gráficos, métricas e histórico

## Próximos Passos (Sprint 2)

- Implementação de backend para sincronização de dados
- Dashboard administrativo para visualização de métricas agregadas
- Análise de tendências e alertas para problemas coletivos
- Ampliação do questionário com novas dimensões de avaliação

## Contribuidores

- Equipe Softtek / FIAP

## Licença

Uso interno exclusivo para Softtek
