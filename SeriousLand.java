/**
 * Esta classe contem a Fase 2 do Projeto de Introdução a Programacao
 * @author Luís Costa n 47082
 * @date Dezembro 2014
 */
import java.util.Scanner;
public class SeriousLand {
  public static void main (String [] args) {
        
    Scanner leitor = new Scanner(System.in);    	  
	  int h = avisoEntre(2, 50, "Quantas ruas horizontais? Valor entre 2 e 50");
	  int v = avisoEntre(2, 50, "\nQuantas ruas verticais? Valor entre 2 e 50"); 
	  imprimeIntro();
 	  char[][] matriz = lerMatriz(leitor, h, v);
	  int linhaR = avisoEntre(1, h , 
	                          "\nRua horizontal do agente de redondificação"); 
	  int colunaR = avisoEntre(1,v, "\nRua vertical do agente de redondificação");  	  
	  double seriousIniciais = countChar(matriz, '-');
	  double seriousRedondos;
	  
	  if (ehIgual(matriz, '-', linhaR - 1, colunaR - 1))  
	    seriousRedondos = 0;  // Caso o agente redondifique um '-'
	  else
	    seriousRedondos = -1; // Caso o agente redondifique um '.'
      
    matriz[linhaR - 1][colunaR - 1] = 'o';
    int opcao = -1; // Valor qualquer que permita a 1a iterecao do while   
    // Caso nao seja possivel redondificar nenhum Serious mesmo antes de jogar
    boolean fim = acabaJogo(matriz); 

    while(opcao != 0 && fim) {     
      printDiv();
	    printMatriz(matriz);
	    pontosCardeais();
	    printWind();      
	    opcao = avisoEntre(0, 4, ""); // Permite escolher os ventos 
	    contagio(matriz, opcao);  
	    fim = acabaJogo(matriz);  // Verifica se ha mais Serious por contagiar  
	  } 
	   
	   printDiv();
	   printMatriz(matriz);
	   System.out.println(fim ? "\n\nAinda podia redondificar mais!" :
	                      "\n\nJa não é possivel redondificar mais!");
	   seriousRedondos+= countChar(matriz, 'o');
	   double percent = seriousRedondos / seriousIniciais * 100;
	   System.out.println("Foram redondificados " + Math.round(percent) + 
	                      "% SeriousLanders\n");	                    
  }
  /**
	 * Le um valor introduzido e ve se ele e um numero inteiro
	 * @param leitor scanner onde estara o valor a testar
   * @requires leitor != null
	 * @return Caso seja um numero inteiro devolve esse valor, 
	 * caso nao seja envia mensagem de erro
	 */
	public static int verificaInt(Scanner leitor) {
		int valor = 0;
		boolean erro = false;

		do { 
			if(leitor.hasNextInt()) {
				valor = leitor.nextInt();
				erro = false; 
			}
			else {
				System.out.print("\nErro! ");
				System.out.print("O que introduziu nao é um numero!");
				System.out.println(" Volte a tentar");
				leitor.next();
				erro = true;
			}
		} while(erro);
		return valor;
	} 
	/**
	 * Envia uma instrucao ao utilizador quando esta e diferente de "",
	 * le um valor e devolve se estiver no intervalo definido, 
	 * se nao estiver, envia uma mensagem de aviso
	 * @param min menor valor do intervalo
	 * @param max maior valor do intervalo
	 * @param instrucao informacao enviada ao utilizador
	 * @requires max >= min
	 * @return Caso esteja entre o intervalo, devolve o valor, 
	 * caso nao esteja envia mensagem de erro
	 */
	public static int avisoEntre(int min, int max, String instrucao) {
		int num;
		Scanner leitor = new Scanner(System.in);
		do {
			if(instrucao != "")
				System.out.println(instrucao);
			num = verificaInt(leitor);
			
			if(!verificaEntre(min, max, num)) {
				System.out.println("Valor fora do intervalo (" + min + "," + max + ")");
				System.out.println("Volte a tentar."); 
			}
		} while (num < min || num > max);
		return num;
	}
	/**
	 * Verifica se um determinado numero inteiro esta entre dois valores
	 * @param min valor minimo do intervalo
	 * @param max valor maximo do intervalo
	 * @param num numero inteiro a testar	
	 * @requires max >= min
	 * @return Devolve true se o valor estiver dentro do intervalo, 
	 * caso contrario devolve false
	 */
	public static boolean verificaEntre(int min, int max, int num) {
		
		if(num < min || num > max)
			return false;
		else
			return true;
	}
	/**
	 * Imprime no ecran a introducao explicativa do mapa de posicoes
	 * dos SeriousLanders
	 */
  public static void imprimeIntro() {
    System.out.print("\nAgora vai indicar, linha a linha,");
	  System.out.print(" o mapa das posições dos SeriousLanders\n");
	  System.out.print("('-' que corresponde a um SeriousLander,");
	  System.out.println(" '.' a um local vazio)");
	  System.out.println("Os simbolos  não devem ter espaços intercalados");
  }    
  /**
   * Le os valores de uma matriz de caracteres vindos do standart input
   * @param leitor canal de leitura
   * @param h numero de linhas
   * @param v numero de colunas
   * @requires h >0 && v > 0 && leitor != null
   * @return Devolve matriz com os caracteres introduzidos
   */
  public static char[][] lerMatriz(Scanner leitor, int h, int v) {
    String linha;
    char[][] matriz = new char[h][v];
    for (int i = 0; i < h; i++) {
      System.out.println("\nIntroduza os " + v + " valores da rua " + (i+1));
      do {			
			  linha = leitor.next();
			    if (linha.length() != v || !verificaChar(linha,'-', '.')) {
				    System.out.print("Por favor volte a repetir"); 
				    System.out.println("\nErro nos valores!"); 
			    }
      } while (linha.length() != v || !verificaChar(linha,'-', '.'));
      for (int j = 0; j < v; j++)
        matriz[i][j] = linha.charAt(j);
    }
    return matriz;     
  }
  /**
	 * Verifica se os valores da string sao um dos dois caracteres definidos
	 * @param linha string onde se vai testar
	 * @param symbol1 primeiro caracter definido
	 * @param symbol2 segundo caracter definido
	 * @return Devolve true se todos os valores da String 
	 * forem um dos caracteres definidos, 
	 * caso contrario, devolve false
	 */
	public static boolean verificaChar(String linha, char symbol1, char symbol2) {
		int countChar = 0;
		for(int i = 0; i < linha.length(); i++) {
			if(linha.charAt(i) == symbol1 ||
			   linha.charAt(i) == symbol2)
				countChar++;			
		}
		if(countChar == linha.length())
			return true;
		else
			return false;
	}
  /**
   * Imprime uma matriz de caracteres no ecran
   * @param m matriz a imprimir no ecran
   * @requires m != null && todas as linhas de m tem de ter o mesmo comprimento
   */
  public static void printMatriz(char[][] m) {
    System.out.println("\n");
    for (int i = 0; i < m.length; i++) {
      for (int j = 0; j < m[0].length; j++)
        System.out.print(m[i][j] + " ");
      System.out.println();
    }    
   }
   /**
   * Imprime no ecran os pontos cardeais N, S, E e W
   */
  public static void pontosCardeais() {
    System.out.println("\n");
    char[][] v = {{' ',' ',' ','N',' ',' ',' '}, {' ',' ',' ','|',' ',' ',' '}, 
                  {'w','-','-','+','-','-','E'}, {' ',' ',' ','|',' ',' ',' '},
                  {' ',' ',' ','S',' ',' ',' '}};
    for (int i = 0; i < v.length; i++) {
      for (int j = 0; j < v[0].length; j++)
        System.out.print(v[i][j]);
      System.out.println();
    }
  }
  /**
   * Imprime no ecran as opcoes de escolha do vento
   */
  public static void printWind() {
    System.out.println("\n\nEscolha a origem do vento");
    System.out.println("1 - N     2 - E     3 - S     4 - W");
    System.out.println("0 - Terminar\n");
  }  
  /**
   * Imprime no ecran a divisoria
   */
  public static void printDiv() {
    System.out.println("\n\n\n\n=============================================");
  }
  /**
   * Altera os os elementos da matriz consoante o vento que for aplicado 
   * @param matriz onde vai se implementar as modificacoes
   * @param x opcao introduzida pelo utilizador para escolha do vento
   * @requires matriz != null e todas as linhas tem de ter o mesmo comprimento
   * @requires x < 5 && x > 0
   */
  public static void contagio(char[][] matriz, int x) {
    switch (x) {    
      case 1 : { //norte
        for (int j = 0; j < matriz[0].length; j++) {
          for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][j] != '.') 
              verificaVizinho(matriz, i + 1, j, 'n');              
          }  
        } 
        break;
      }        
      case 2 : { //este
        for (int i = 0; i < matriz.length; i++) {
          for (int j = matriz[0].length - 1; j > -1; j--) {
            if (matriz[i][j] != '.')
              verificaVizinho(matriz, i, j - 1, 'e');
          }
        }
        break;
      }      
      case 3 : { //sul
        for ( int j = 0; j < matriz[0].length; j++) {
          for (int i = matriz.length - 1; i > -1; i--) {
            if (matriz[i][j] != '.') 
              verificaVizinho(matriz, i - 1 , j, 's');                   
          }
        }
        break;
      }        
      case 4 : { //oeste
        for (int i = 0; i < matriz.length; i++) {
          for (int j = 0; j < matriz[0].length; j++) {
            if (matriz[i][j] != '.')
              verificaVizinho(matriz, i, j + 1, 'w');
           }
        }
        break;

      } 
    }
  }
  /**
   * Verifica na matriz o vizinho consoante a direccao do vento
   * @param m matriz do jogo
   * @param l linha da matriz onde e vai verificar a vizinhanca
   * @param c coluna da matriz onde se vai verificar a vizinhanca
   * @param c1 caracter que define a direccao do vento
   * @requires m != null && todas as linhas de m tem de ter o mesmo comprimento
   * @requires l >= 0 && c >= 0 
   */
  public static void verificaVizinho(char[][] m, int l, int c, char vento) {
    switch(vento) {
      case 'n' : {
        if ( l < m.length) {
          char c1 = m[l-1][c];
          char c2 = m[l][c];
          m[l][c] = substitui(c1, c2);
        }  
        break;
      }
      case 's' : {
        if ( l > -1) {
          char c1 = m[l+1][c];      
          char c2 = m[l][c];
          m[l][c] = substitui(c1, c2);
        }
        break;
      }
      case 'w' : {
        if ( c < m[0].length) {
          char c1 = m[l][c-1];
          char c2 = m[l][c];
          m[l][c] = substitui(c1, c2);
        }  
        break;
      }
      case 'e' : {
        if ( c > -1) {
          char c1 = m[l][c+1];      
          char c2 = m[l][c];
          m[l][c] = substitui(c1, c2);
        } 
        break; 
      }  
    } 
  }        
  /**
   * Altera o aspecto do SeriousLander consoante o nivel de
   * redondificacao do seu antecessor
   * @param c1 caracter antecessor
   * @param c2 caracter a avaliar
   * @return Devolve caracter do SeriousLander modificado ou nao, 
   * consoante o nivel de redondificacao do seu antecessor
   */
  public static char substitui(char c1, char c2) {
    if (c1 == '<')
      if (c2 == '-')
        c2 = '<';
    if (c1 == 'c') {
      if (c2 == '<')
        c2 = 'c';
      if (c2 == '-')
        c2 = '<';
    }
    if (c1 == 'o') {
      if (c2 == 'c')
        c2 = 'o';
      if ( c2 == '<')
        c2 = 'c';
      if (c2 == '-')
        c2 = '<';
    }
    return c2;
  }
  /**
   * Devolve o numero de caracteres existentes numa dada matriz
   * @param m matriz de caracteres   
   * @param c caracter a contabilizar
   * @requires m != null && todas as linhas de m tem de ter o mesmo comprimento
   * @return O numero de caracteres contabilizado
   */
  public static int countChar(char[][] m, char c) {
    int total = 0;
    for (int i = 0; i < m.length; i++)
      for (int j = 0; j < m[0].length; j++)
        if (m[i][j] == c)
          total++;
    return total;
  }
  /**
   * Verifica numa dada matriz de caracteres, consoante as suas coordenadas,
   * se o caracter eh igual ao caracter especificado como variavel
   * @param m matriz que contem os caracteres a testar
   * @param c1 caracter ao qual iremos comparar
   * @param x linha onde se encontra o caracter a testar
   * @param y coluna onde se encontra o caracter a testar
   * @requires m != null && todas as linhas de m tem de ter o mesmo comprimento
   * @requires x > -1 && y > -1
   * @requires x - 1 < m.length && y - 1 < m[0].length
   * @return Devolve true se for igual e false se nao for
   */
  public static boolean ehIgual(char[][] m, char c1, int x, int y) {
    if ( m[x][y] == c1)
      return true;
    else
      return false;
  }
  /**
   * Verifica se se ainda ha algum Serious que possa ser "redondificado"
   * @param m matriz onde se encontra a decorrer o jogo SeriousLand
   * @requires m != null && todas as linhas de m tem de ter o mesmo comprimento
   * @return true se ainda existem Serious que podem ser "redondificados"
   * e false quando ja nao ha
   */
  public static boolean acabaJogo(char[][] m) {      
    int count = 0;
    boolean continua = false;
    char[][] n = copiaMatriz(m);
    for(int i = 1; i <= 4 && !continua; i++) {
      contagio(n,i);
      for (int l = 0; l < m.length && !continua; l++) {
        for (int j = 0; j < m[0].length && !continua; j++) {
          if (n[l][j] == m[l][j])
            count++;
          else
          continua = true;  
        } 
      }  
    }     
    if (count == n.length * n[0].length * 4)
      return false;
    else
      return true;
  }
  /**
   * Faz uma copia de uma dada matriz de caracteres
   * @param m matriz a ser copiada
   * @requires m != null && todas as linhas de m tem de ter o mesmo comprimento
   * @return Devolve a copia da matriz
   */
  public static char[][] copiaMatriz(char[][] m) {
    char[][] copia = new char[m.length][m[0].length];
    for (int i = 0; i < m.length; i++)
      for (int j = 0; j < m[0].length; j++)
        copia[i][j] = m[i][j];
    return copia;
  }  
}




  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

