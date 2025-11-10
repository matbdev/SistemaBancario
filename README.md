# 💳 Sistema Bancário (Desktop Swing)

Uma aplicação desktop em Java (Swing) que simula um sistema bancário completo — gerencia correntistas, contas (normais e especiais), transações e extratos. Foi desenvolvida como projeto prático para aplicar POO, arquitetura em camadas (MVC adaptado) e persistência com SQLite.

---

Índice

- [🚀 Visão geral](#visao-geral)
- [✨ Principais funcionalidades](#principais-funcionalidades)
- [🏗️ Arquitetura e organização do código](#arquitetura-e-organizacao-do-codigo)
- [🛠️ Como executar (build & run)](#como-executar-build--run)
- [📁 Estrutura do repositório (resumo)](#estrutura-do-repositorio-resumo)
- [✅ Boas práticas e decisões de design](#boas-praticas-e-decisoes-de-design)
- [♻️ Evoluções](#evolucoes)
- [🤝 Como contribuir](#como-contribuir)
- [📝 Licença](#licenca)

---

<a name="visao-geral"></a>

## 🚀 Visão geral

O Sistema Bancário oferece uma interface gráfica completa construída com Java Swing e navegação baseada em CardLayout. Todas as operações sobre contas e correntistas são persistidas em um banco de dados SQLite (`sistemabancario.db`) e o acesso a dados é encapsulado por DAOs, facilitando manutenção e testes.

Público-alvo: estudantes e desenvolvedores que querem estudar:

- arquitetura em camadas (View / Controller / Model),
- padrões DAO e Factory,
- gerenciamento transacional com SQLite,
- desenvolvimento de interfaces Swing com modelos de tabela customizados.

---

<a name="principais-funcionalidades"></a>

## ✨ Principais funcionalidades

- Gerenciamento de correntistas
  - Cadastro, edição e exclusão
  - Validação de CPF e prevenção de duplicatas
- Gerenciamento de contas
  - Criar contas normais e contas especiais (com limite de cheque)
  - Associação conta ↔ correntista
  - Geração automática de números de conta formatados
- Operações financeiras
  - Depósito, saque e transferência (operacionalmente atômicas)
  - Registro de transações e visualização de extrato (ordenado por data)
- Interface
  - Aplicação totalmente Swing (painéis, formulários `.form` e JTable customizadas)
  - Navegação por `FramePrincipal` + `CardLayout`
- Persistência
  - Banco SQLite (`sistemabancario.db`)
  - Padrão DAO para isolar a camada de persistência
  - Gerenciamento explícito de transações (BEGIN / COMMIT / ROLLBACK)

---

<a name="arquitetura-e-organizacao-do-codigo"></a>

## 🏗️ Arquitetura e organização do código

Projeto modularizado em duas partes:

1. Alexandria (biblioteca core)

   - Utilitários e modelos reutilizáveis
   - Gerenciamento de conexão JDBC
   - Exceções e interfaces genéricas (IDao, IFilter)
   - Classes: CPF, Pessoa, FormatadorTexto, Verificador, DataBaseConnectionManager, etc.

2. SistemaBancario (aplicação principal)
   - Controllers: gerenciam interações entre View e serviço/DAO
   - Views: painéis Swing e formulários (`.form` e `.java`)
   - Repositórios/DAOs: `ContaBancariaDAO`, `TransacaoDAO`, `CorrentistaDAO`, `DAOFactory`
   - Models/Service: `ContaBancaria`, `ContaBancariaEspecial`, `Transacao`, `Numero`

Padrões adotados:

- MVC (Model / View / Controller) adaptado
- DAO + Factory
- Injeção de dependências via construtores (controllers recebem DAOs/serviços)
- Gerenciamento explícito de transações para operações que alteram múltiplas tabelas

---

<a name="como-executar-build--run"></a>

## 🛠️ Como executar (build & run)

Pré-requisitos

- Java 11+ (JDK)
- Apache Maven
- (Opcional) IDE: NetBeans / IntelliJ / Eclipse (suporta arquivos `.form`)

Passos rápidos

1. Clone o repositório:

```bash
git clone <url-do-repositorio>
```

2. Build da biblioteca Alexandria:

```bash
cd Alexandria
mvn clean install
```

3. Build e execução da aplicação:

```bash
cd ../SistemaBancario
mvn clean package
java -jar target/SistemaBancario-1.0-Sigma.jar
```

Observações

- O arquivo de banco `sistemabancario.db` encontra-se no diretório `SistemaBancario` para conveniência; o path também pode ser configurado em `src/main/resources/config.properties`.
- Para desenvolvimento com IDE (NetBeans): abra o diretório raiz ou cada módulo separadamente. Os arquivos `.form` fornecem integração visual com o NetBeans GUI Builder.

---

<a name="estrutura-do-repositorio-resumo"></a>

## 📁 Estrutura do repositório (resumo)

Raiz contendo dois módulos principais:

- Alexandria/ (biblioteca core)
  - src/main/java/.../alexandria/{exceptions, interfaces, models, repository, util, view}
- SistemaBancario/ (aplicação Swing)
  - src/main/java/.../sistemabancario/{controller, repository, service, view, exceptions}
  - src/main/resources/{config.properties, images}
  - sistemabancario.db (SQLite)

(Árvore completa e detalhada foi enviada junto ao projeto.)

---

<a name="boas-praticas-e-decisoes-de-design"></a>

## ✅ Boas práticas e decisões de design

- Injeção de dependências via construtor facilita testabilidade e garante única instância de DAOs (evita conflitos de acesso ao arquivo/DB).
- DAOs isolam SQL e o mapeamento para modelos, permitindo trocar a persistência sem afetar a lógica de negócio.
- Uso de transações para garantir atomicidade em operações que alteram várias entidades (ex.: transferência).
- Modelos de tabela customizados (TableModel) para desacoplar camada de UI da lógica de dados.

---

<a name="evolucoes"></a>

## ♻️ Evoluções

Prioridade alta

- Substituir strings/constantes por Enums (ex.: tipos de movimento DEBITO / CREDITO)

Melhorias avançadas

- Migrar para ORM leve (ex.: JDBI) ou usar um banco mais robusto dependendo do escopo
- Melhorias de UX no Swing (temas, validações inline, feedbacks em tempo real)

---

<a name="como-contribuir"></a>

## 🤝 Como contribuir

- Abra issues descrevendo bugs, melhorias ou recursos desejados.
- Para mudanças de código:
  1. Fork o repositório
  2. Crie uma branch com a feature/bugfix
  3. Faça commits claros e pequenos
  4. Abra um Pull Request descrevendo mudanças e motivação
- Sugestões de testes e casos de borda são muito bem-vindos (ex.: validação de CPF, limites de saque, concorrência nas operações).

---

<a name="licenca"></a>

## 📝 Licença

MIT — consulte o arquivo LICENSE para detalhes.

---

Desenvolvido para fins educacionais.
