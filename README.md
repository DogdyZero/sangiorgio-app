# sangiorgio-app
## Objetivo

## Como funciona
Com o intuito de explorar a um pouco os principios arquiteturais e um pouco de cloud, foi feito um pequeno projeto, 
- Aplicação Java 21 com Springboot 3.2
- contando com 5 endpoints
- Banco de dados NoSql mongodb
- Versão resumida da arquitetura clean arch (explicação das modificações abaixo)
- AWS SQS como sistema para mensageria
- JUnit e mockito com uma alta cobertura de testes

## Sobre Clean Arch
A Arquitetura Limpa (Clean Architecture), tem uma divisão bem especifca de camadas com o intuito de organizar o projeto, separar mais as responsabilidades de cada camada, aplicação do principio de inversão de dependência, dentro de outras vantagens e também com as suas desvantagens.
- Em suma, são dividido em 3 camadas, com cada subdivisão de camadas, sendo a mais externa, a camada de infraestrutura, e a mais interna a camada de dominio
- Na camada de dominio com o principio do DDD, que é falar com a linguagem de negócio, sendo essa responsável pelas regras de negócio e sem ter conhecimento de nenhum framework.
- Na camada de aplicação, essa fica responsável pela comunicação com a camada de negocio, onde deve conter os casos de uso
- Na camada de infraestrutura, deve ficar somente as informações externas, e que não dizem respeito ao negocio, mas sim a tecnologica, ou seja, aqui ficam as informações do banco de dados, de conexões com apis externas, entre outros
- Neste projeto, foi feito uma redução das comunicações entre objetos, para tentar deixa-lo mais simples, já que se trata de poucos endpoints, dentre elas, foi removido a controller, presenters na camada de aplicação, e a comunicação entre os casos de uso e a camada de infraestrutura foram feitos diretamente com as implementações dos gateways, na qual a interface está na camada de aplicação, e a implementação na camada de infraestrutura, sendo que o correto seria a implementação ficar dentro da própria camada de aplicação

## Configuração
Criar um arquivo .env, no local onde está o docker-compose, nele deve conter as variáveis
```
MONGO_INITDB_ROOT_USERNAME=seu username do banco
MONGO_INITDB_ROOT_PASSWORD=sua senha do banco
SPRING_DATA_MONGODB_USERNAME=seu username do banco
SPRING_DATA_MONGODB_PASSWORD=sua senha do banco
SPRING_DATA_MONGODB_DATABASE=admin
SPRING_DATA_MONGODB_HOST=db
SPRING_DATA_MONGODB_PORT=27017
AWS_ACCESS_KEY_ID=sua access key
AWS_SECRET_ACCESS_KEY=sua secret key
AWS_REGION=sua region
CLOUD_SQS_NAME=sua fila sqs
```

após configurar é só rodar `docker-compose up -d`
## Endpoints
1. Vendedores
   1. Get All Vendedores
      - Método: GET
      - URL: http://localhost:8080/vendedores
      - Descrição: Obtém a lista de todos os vendedores.
      - Parâmetros: Nenhum
      - Headers: Nenhum
      ``` json
      [
        {
            "id": "670698d7569c9721e39a391f",
            "codigo": "BV6SMY",
            "nome": "Douglas"
        }
      ]
      ``` 
   2. Post Vendedor
      - Método: POST
      - URL: http://localhost:8080/vendedores
      - Descrição: Adiciona um novo vendedor.
      - Parâmetros: Nenhum
      - Headers: Nenhum
      - Body (JSON):
      ``` json
        {
          "nome": "Douglas"
        }
      ```
      - Resposta: Sucesso 201
      - Retorno semelhante ao Get Vendedores
      
     
2. Cobrança
   1. Get All Cobrança
      - Método: GET
      - URL: http://localhost:8080/cobrancas
      - Descrição: Obtém a lista de todas as cobranças.
      - Parâmetros: Nenhum
      - Headers: Nenhum
      ``` json
        [
            {
                 "id": "6706addd2881541e56d80ca9",
                "codigo": "JKPMBH",
                "status": "EXCEDENTE",
                "valor": 25.0,
                "pago": null,
                "cliente": "João"
            }
        ]
      ```

   2. Criar Cobrança
      - Método: POST
      - URL: http://localhost:8080/cobrancas
      - Descrição: Cria uma nova cobrança.
      - Parâmetros: Nenhum
      - Headers: Nenhum
      - Body (JSON):
      ``` json 
   
      {
          "valor": 25.0,
          "cliente": "Douglas"
      }
      ```
      - Resposta semelhante ao endpoint de vendedores
3. Pagamento
   1. Novo Pagamento
      - Método: POST
      - URL: http://localhost:8080/pagamentos
      - Descrição: Realiza um novo pagamento para um vendedor.
      - Parâmetros: Nenhum
      - Headers: Nenhum
      - Body (JSON):
      ``` json
   
      {
        "codigoVendedor": "BV6SMY",
        "listaPagamentos": [
            {
                "codigo": "JKPMBH",
                "valorPago": 26
            }
        ]
      }
      ```
    - Status code: 200
    - Resposta: Sem mensagem