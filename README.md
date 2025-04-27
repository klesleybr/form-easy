# **FormEasy**

![Licença](https://img.shields.io/badge/Licen%C3%A7a-MIT-green) ![Versão](https://img.shields.io/badge/Vers%C3%A3o-1.0-blue)

**Facilitando a coleta e análise de dados para pesquisas acadêmicas.**  
O FormEasy é uma solução eficiente para enviar formulários por e-mail, armazenar respostas de forma organizada e gerar estatísticas detalhadas.

---

## 📖 **Sobre o Projeto**

### **Objetivo**  
O FormEasy foi projetado para otimizar a coleta de dados em pesquisas acadêmicas, eliminando o uso de formulários físicos e simplificando a análise de dados.

### **Descrição do Problema**  
Pesquisadores frequentemente enfrentam desafios para gerenciar dados de formulários físicos. O FormEasy resolve isso ao permitir o envio em massa de e-mails que redirecionam os destinatários para um formulário, com respostas armazenadas de maneira centralizada.

---

## 🛠️ **Funcionalidades**

- **Login via Google**: Acesso rápido e seguro utilizando a conta Google.  
- **Envio de formulários por e-mail**: Redirecionamento para preenchimento online.  
- **Armazenamento centralizado**: Acesso às respostas organizadas por usuário ou ID.  
- **Estatísticas detalhadas**: Visualização gráfica e quantitativa dos dados coletados.  

---

## 💻 **Tecnologias Utilizadas**

- **Back-end**: Spring Boot  
- **Front-end**: JavaFX  
- **Ferramentas de design**: Figma, Scene Builder  
- **Integrações**: APIs Google OAuth e Google Sheets  
- **Comunicação e gestão**: Discord, modelo Kanban  

---

## 📥 **Instalação**

1. **Clone o repositório**:  
   ```bash
   git clone https://github.com/seu-usuario/FormEasy.git
   ```
2. **Entre no diretório do projeto**:  
   ```bash
   cd FormEasy
   ```
3. **Configure o ambiente**:  
   Defina variáveis no arquivo `.env` para acessar as APIs do Google.

4. **Compile e execute o projeto**:  
   - **Back-end (Spring Boot)**:  
     ```bash
     mvn spring-boot:run
     ```
   - **Front-end (JavaFX)**:  
     Execute o arquivo principal na IDE (Eclipse).  

---

## 🚀 **Como Usar**

Com essas interfaces simples e intuitivas, o FormEasy garante que você colete, gerencie e analise dados de forma eficiente.

### **1. Faça login com sua conta Google**
   
- Ao abrir o sistema, a primeira tela exibida será a de login. Clique no botão "Login com Google" para acessar a aplicação de forma segura. Caso já tenha cadastro, insira seu e-mail e senha.

### **2. Tela de Menu**

- Após o login, você será direcionado ao menu principal. Nesta tela, é possível:
   - Enviar um novo formulário
   - Analisar as respostas de formulários já respondidos

### **3. Tela de Envio de E-mails**

- Para criar e enviar um novo formulário, siga os passos:
   - Adicione um assunto ao e-mail.
   - Preencha a descrição do envio com o link do formulário ou um lembrete.
   - Anexe uma planilha contendo os destinatários do e-mail.
   - Clique no botão Enviar para disparar os e-mails em massa para os destinatários listados.

### **4. Tela de Acessar Respostas e Exibir Estatísticas**

- Caso escolha analisar as respostas, siga as etapas:
   - Clique em Procurar Formulários para localizar todos os formulários com planilhas no Google Sheets.
   - Selecione um formulário da lista e clique em Mostrar Respostas para exibir o que cada participante respondeu.
   - Opcionalmente, marque o checkbox Exibir Estatísticas para visualizar os dados em formato percentual, como a porcentagem de escolhas de uma opção específica.

### **5. Resumo do Fluxo**

- Faça login com sua conta Google.
- Crie e envie um formulário para os destinatários.
- Visualize as respostas e analise os dados diretamente na plataforma.

---

## 🤝 **Contribuições**

1. Faça um fork do projeto.  
2. Crie uma nova branch:  
   ```bash
   git checkout -b minha-feature
   ```
3. Faça suas alterações e commit:  
   ```bash
   git commit -m "Minha nova feature"
   ```
4. Envie suas alterações:  
   ```bash
   git push origin minha-feature
   ```
5. Abra um Pull Request.

---

## 📝 **Licença**

Este projeto está licenciado sob os termos da licença MIT. Consulte o arquivo [LICENSE](./LICENSE) para mais informações.

---

## 📬 **Contato**

Equipe FormEasy:  
- **Anthonio Henrique Carvalho Calazans** - Desenvolvimento  
- **Davi Cavalcanti Lima** - Documentação  
- **Guilherme Augusto Feitosa da Silva** - Desenvolvimento  
- **José Klesley Pereira Feitosa** - Revisão de Código e Desenvolvimento 
- **Thiago Ferreira Batista dos Santos** - Prototipagem  
- **Vinícius Alves Ferreira dos Santos** - Desenvolvimento  

Professor(a): **Maria Renay Barbosa da Silva**  
Centro Universitário do Rio São Francisco, 2024.

---
