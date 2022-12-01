# analisadorLexicoSintatico

## Resumo
Este programa realiza a análise léxica e sintática dos programas escritos na linguagem `ldm`. 
Realiza a identificação de:

* Números inteiros
* Constantes lógicas
* Contantes literais
* Atribuicao
* Identificadores
* Ponto e virgula
* Chaves
* Estrutura condição
* Estrutura repetição

### Exemplo gramática da liguagem ldm
```
classe teste {
    var boateAberta = verdadeiro;

    enquanto boateAberta == verdadeiro {
         se idade >= 18 {
            permissao = verdadeiro;
        } senao {
            permissao = falso; 
        }
    }
}
```

### Arquivo ldm
É disponibilizado um arquivo lexicamente e sintaticamente correto `program_sample.txt`

### Exemplo compilação da linguagem ldm

Para facilitar a compilação do projeto foi utilizado a ferramenta `make` para automatizar as tarefas/comandos. Abra o terminal no diretório do projeto e execute:

```sh
$ make compile
```

### Exemplo de execução da análise léxica e sintática

Para executar a análise léxica e sintática podemos fazer de duas formas:

Não havendo o make instalado, é possível rodar diretamente o pacote `.jar`
```sh
$ java -jar jar/projeto.jar program_sample.txt
```

No caso do make instalado podemos utilizá-lo

```sh
$ make run FILE=program_sample.txt
```

### Requisitos
* JDK 1.8
* [GNU make](https://www.gnu.org/software/make/)
