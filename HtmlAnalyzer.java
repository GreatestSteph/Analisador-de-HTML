import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HtmlAnalyzer {
    // Define um atributo que armazena a URL
    private String url;

    // Define um construtor que recebe a URL como argumento
    public HtmlAnalyzer(String url) {
        this.url = url;
    }

    // Método que mostra a estrutura HTML
    public void displayHTMLStructure() {
        try {
            // Converte a string URL em URI e depois em URL
            URI uri = new URI(url);
            URL urlObject = uri.toURL();

            // Lê a página
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlObject.openStream()));
            
            // Define uma linha
            String line; 
            
            // Armazena o nível máximo de profundidade
            int maxIndentation = 0; 

            // Armazena o conteúdo da linha mais profunda
            String maxIndentationLine = ""; 

            // Variavel para guardar o numero de tags total <>
            int tagCount = 0; 

            // Lê cada linha da URL
            while ((line = reader.readLine()) != null) {
                // Conta o numero de tags a cada linha
                tagCount += countTags(line); 

                // Atualiza qual é a linha mais profunda
                int indentation = countIndentation(line); 
                if (indentation > maxIndentation) {
                    maxIndentation = indentation;
                    maxIndentationLine = line;
                }
            }
            reader.close();

            // Print da linha mais profunda
            System.out.println(maxIndentationLine); 

            if (tagCount % 2 == 0) {
                // Se os números de tags com pares resultarem em par, então o HTML está ok
                System.out.println("");
            } else {
                // Se os números de tags com pares resultarem em ímpar, então o HTML é malformado
                System.out.println("malformed HTML");
            }

        } catch (URISyntaxException | IllegalArgumentException e) {
            // Mensagem avisando que houve um erro na sintax ou na URI não absoluta
            System.err.println("URL connection error");
        } catch (IOException e) {
            // Mensagem avisando que houve um erro de conexão
            System.err.println("URL connection error");
        }
    }
    
    // Método que conta o número de tags
    private int countTags(String line) {
        // Conta o numero de tags a cada linha
        int count = 0; 
        
        // Indica qual linha está, sua posição
        int pos = 0; 
        
        // Lê cada caractere da linha
        while ((pos = line.indexOf('<', pos)) != -1) {
            // Aqui são ignoradas as tags que nao necessitam de um par
            if (!line.startsWith("<!DOCTYPE", pos) &&
                !line.startsWith("<br/", pos) &&
                !line.startsWith("<img", pos) &&
                !line.startsWith("<input", pos) &&
                !line.startsWith("<meta", pos) &&
                !line.startsWith("<link", pos) &&
                !line.startsWith("<hr", pos) &&
                !line.startsWith("<base", pos)) {
                // Conta o número de tags encontradas em uma linha 
                count++; 
            }
            // Se '>' for encontrado, a posição de pos irá para o próximo '>' 
            pos = line.indexOf('>', pos);
            // Se o '>' não for encontrado, indexOf() retorna -1, indicando que a tag não foi fechada corretamente
            if (pos == -1) break;
        }
        return count;
    }

    // Método que conta o número de espaços/profundidade
    private int countIndentation(String line) {
        // Conta o número de espaços
        int count = 0; 
        
        // Converte a linha em uma array de caracteres usando o método toCharArray()
        // Então percorre por cada caractere desse array 
        for (char c : line.toCharArray()) { 
            if (Character.isWhitespace(c)) { 
                // Se o caractere atual for um espaço em branco, incrementa o contador
                count++; 
            } else {
                break;
            }
        }
        return count;
    }

    // Método que inicializa o programa corretamente
    public static void main(String[] args) {
        String url = args[0];
        HtmlAnalyzer htmlAnalyzer = new HtmlAnalyzer(url);
        htmlAnalyzer.displayHTMLStructure();
    }
}
