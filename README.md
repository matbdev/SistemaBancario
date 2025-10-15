# 💳 Sistema Bancário Simples

Este projeto é uma aplicação de console com interface gráfica (Swing) em **Java** que simula as operações básicas de um sistema bancário. Foi desenvolvido como exercício prático para aplicar conceitos de arquitetura de software, persistência de dados e Programação Orientada a Objetos (POO).

---

## 📝 Sobre o Projeto

O **Sistema Bancário** permite o gerenciamento de correntistas e suas respectivas contas bancárias. Os usuários podem realizar operações de **CRUD** (Criar, Ler, Atualizar, Deletar) para correntistas, criar contas bancárias (normais e especiais), realizar depósitos, saques e consultar extratos de transações. Todos os dados são persistidos em arquivos de texto (`.dat`), simulando uma camada de persistência simples.

---

## ⚙️ Principais Funcionalidades

### 👤 Gerenciamento de Correntistas
- Cadastro, edição e exclusão de correntistas
- Validação de CPF e prevenção de duplicados

### 🏦 Gerenciamento de Contas Bancárias
- Criação de contas normais e contas especiais (com limite de cheque especial)
- Geração automática de números de conta únicos ou definição manual
- Associação de cada conta a um correntista

### 💰 Operações Financeiras
- Realização de depósitos e saques
- Validação de saldo e limites

### 🗂️ Persistência de Dados
- Todas as informações de correntistas, contas e transações são salvas em arquivos de texto locais

### 🖥️ Interface com o Usuário
- Menus de console interativos para navegação
- Janelas de diálogo (Swing) para entrada de dados em cadastros e edições

---

## 🏗️ Estrutura do Projeto

O projeto é modularizado em duas partes principais para promover a reutilização e a separação de responsabilidades:

### 1️⃣ Alexandria (Biblioteca Core)
Atua como biblioteca de utilitários compartilhados, com código genérico reutilizável em outros projetos.

- **exceptions**: Classes de exceção genéricas (`CpfInvalidoException`, `NullInputException`)
- **models**: Modelos de dados fundamentais (`Pessoa`, `CPF`)
- **repository**: Interface `BaseDAO` como contrato para operações de persistência
- **util**: Ferramentas úteis para manipulação de arquivos, formatação, entrada de dados, exibição de mensagens e validações
- **view**: Classe base `Menu.java` para criação de menus de console

### 2️⃣ SistemaBancario (Aplicação Principal)
Implementa a lógica de negócio específica do sistema bancário, utilizando a biblioteca Alexandria.

- **exceptions**: Exceções do domínio bancário (`ContaJaExisteException`, `SaldoInvalidoException`)
- **repository/persist**: Classes DAO responsáveis pela persistência:
  - `CorrentistaDAO.java` (arquivo `correntista.dat`)
  - `ContaBancariaDAO.java` (arquivo `conta_bancaria.dat`)
  - `TransacaoDAO.java` (arquivo `transacao.dat`)
- **service/business**: Camada de lógica de negócio
  - `ContaBancaria.java` e `ContaBancariaEspecial.java`: Tipos de contas e regras
  - `Numero.java`: Modela o número da conta
  - `Transacao.java`: Movimentação financeira
- **view**: Menus (`MenuContasCorrentistas`, `MenuBanco`, etc.) e telas Swing (`TelaCadastroConta`, `TelaEditarUsuario`, etc.)

---

## 🏛️ Arquitetura e Decisões de Design

### 🏢 Arquitetura em Camadas
- **View (UI)**: Interação com o usuário (menus e telas)
- **Service/Business**: Regras e comportamento do sistema
- **Repository/Persistence**: Abstração do acesso a dados

### 📦 Padrão DAO (Data Access Object)
Isola a lógica de negócio da persistência de dados, permitindo futura alteração da forma de armazenamento (ex: banco de dados).

### 🔌 Injeção de Dependência (DI)
Instâncias de DAOs são criadas uma única vez na classe `TelaPrincipal` e "injetadas" via construtor nas classes que delas necessitam.

**Benefícios:**
- Fonte Única de Verdade
- Testabilidade (facilidade para mocks)
- Baixo Acoplamento

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java Development Kit (JDK) 11 ou superior
- Apache Maven

### Passos

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repositorio>
   ```

2. **Compile a biblioteca Alexandria:**
   ```bash
   cd Alexandria
   mvn clean install
   ```

3. **Compile e execute a aplicação SistemaBancario:**
   ```bash
   cd ../SistemaBancario
   mvn clean package
   java -jar target/SistemaBancario-1.0-SNAPSHOT.jar
   ```

A aplicação iniciará, exibindo o menu principal no console.

---

## 📈 Evoluções e Melhorias Futuras

- **Modernizar a Persistência:** Substituir sistema de arquivos por:
  - Serialização JSON (`Gson`, `Jackson`)
  - Banco de dados embutido (`SQLite` com JDBC)
- **Introduzir Camada de Serviço:** Formalizar entre View e DAOs para melhor separação
- **Utilizar Enums:** Converter constantes `char` (como `MOV_DEBITO`, `MOV_CREDITO`) para Enums

---

## 🧑‍💻 Contribuição

Sinta-se à vontade para contribuir com sugestões, melhorias ou relatando bugs! 

---

## 📄 Licença

Este projeto está sob licença MIT.

---

**Projeto desenvolvido para fins educacionais.**