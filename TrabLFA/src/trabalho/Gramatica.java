package trabalho;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import sun.security.provider.certpath.OCSP.RevocationStatus;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Gramatica {

	private ArrayList<String> regras;

	public Gramatica() {
		regras = new ArrayList<>();
	}

	public ArrayList<String> getRegras() {
		return regras;
	}

	public void setRegras(ArrayList<String> regras) {
		this.regras = regras;
	}

	public void adicionaRegra(String regra) {
		this.regras.add(regra);
	}

	public void mostrarGramatica() {
		for (String g : this.regras) {
			System.out.println(g);
		}
	}

	public boolean ehRecursiva(String regra) {
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < regra.length(); i++) {
			if (regra.charAt(i) == 'S') {
				strBuffer.append("S");
			}
		}
		if (strBuffer.length() > 1) {
			System.out.println("O simbolo inicial é recursivo");
			return true;
		} else {
			System.out.println("O simbolo inicial não é recursivo");
			return false;
		}
	}

	public boolean removeRecusaoInicial() {

		if (ehRecursiva(this.regras.get(0))) {
			ArrayList<String> regrasAux = new ArrayList<>();
			for (String s : this.regras) {
				regrasAux.add(s);
			}
			this.regras.clear();
			String inicial = new String();
			inicial = regrasAux.get(0).replaceFirst("S", "S1");

			this.regras.add(inicial);
			for (String s : regrasAux) {
				this.regras.add(s);
			}
			return true;
		} else {

			return false;
		}
	}

	public Integer temLambda() {

		for (String s : this.regras) {
			if (s.contains(".")) {
				// TODO tirar syso
				// System.out.println("Regra: " + s + "Indice: " +
				// this.regras.indexOf(s));
				return this.regras.indexOf(s);
			}
		}
		System.out.println("Sem regras Lambda");
		return null;
	}

	// talvez mudar o nome para variaveis anulaveis
	public void removeLambda() {
		boolean inicialLambda = false;
		ArrayList<Integer> PREV = new ArrayList<>();
		ArrayList<Integer> NULL = new ArrayList<>();

		// para a gramatica inteira
		while (this.temLambda() != null) {
			PREV.clear();
			NULL.clear();
			NULL.add(this.temLambda());
			String varQueGeramLambda = new String();
			// para cada lambda
			do {
				// PREV = NULL
				for (Integer i : NULL) {
					if (!PREV.contains(i)) {
						PREV.add(i);
					}
				}
				System.out.println("O NULL é: " + NULL.toString());
				System.out.println("O PREV é: " + PREV.toString());

				// para cada variavel A que pertence ao conjunto de variaveis
				for (String a : this.regras) {
					// Se A deriva w e w pertence a PREV faça

					StringTokenizer st = new StringTokenizer(a, "->");
					st.nextToken();
					String aux = st.nextToken().trim();

					if (a.contains(".")) {
						varQueGeramLambda += retornaVariavel(regras.indexOf(a)) + "#";
					} else {

					}

					for (Integer x : PREV) {
						if (aux.contains(retornaVariavel(x))) {
							if (!NULL.contains(regras.indexOf(a))) {
								NULL.add(regras.indexOf(a));
							}
						}
					}
				}
				System.out.println("O NULL é: " + NULL.toString());
				System.out.println("O PREV é: " + PREV.toString());

			} while (!PREV.equals(NULL));

			// Começa a remover os Lambdas

			// Se o simbolo inicial estiver em NULL então S -> .
			if (NULL.contains(0)) {
				String str = regras.get(0);
				ArrayList<String> producoes = new ArrayList<>();
				StringTokenizer st = new StringTokenizer(str, "->|");
				st.nextToken();
				while (st.hasMoreTokens()) {
					producoes.add(st.nextToken().trim());
				}
				for (String s : producoes) {
					if (s.equals(s.toUpperCase())) {
						for (Integer i : NULL) {
							if (s.contains(retornaVariavel(i))) {
								for (int x = 0; x < s.length(); x++) {

									String y = new String();
									y += s.charAt(x);
									if (varQueGeramLambda.contains(y)) {
										inicialLambda = true;
									}

								}
								inicialLambda = true;
							}
						}

					}
				}
			}
			System.out.println("Simbolo inicial com lambda: " + inicialLambda);
			String variavel = new String();
			ArrayList<String> projecoes = new ArrayList<>();
			for (Integer i : NULL) {

				String str = new String();
				str += regras.get(i);

				// Pega a primeira posicao do NULL que é o que já tem lambda
				if (NULL.indexOf(i) == 0) {
					StringTokenizer st = new StringTokenizer(str, "->|");
					variavel += st.nextToken().trim();
					while (st.hasMoreTokens()) {
						String aux = st.nextToken().trim();
						// Aqui já tirou o lambda
						if (!aux.contains(".")) {
							projecoes.add(aux);
						}
					}
					for (int x = 0; x < projecoes.size(); x++) {
						String proj = projecoes.get(x);
						String projNova = new String();
						if (proj.contains(variavel)) {
							for (int z = 0; z < proj.length(); z++) {
								if (proj.charAt(z) == variavel.charAt(0)) {
									String aux = new String();
									aux += proj.substring(0, z);
									projNova += proj.substring(z + 1);
									if (!projecoes.contains(projNova) && projNova.length() != 0) {
										projecoes.add(projNova);
									}
									projNova = aux;

								} else {
									projNova += proj.charAt(z);
								}

								if (!projecoes.contains(projNova) && projNova.length() != 0) {
									projecoes.add(projNova);
								}

							}
						}
					}
					// Recriando a regra inteira
					// Gravando a regra
					StringBuffer strBff = new StringBuffer();
					strBff.append(variavel);
					strBff.append(" -> ");
					int x = 1;
					for (String s : projecoes) {
						strBff.append(s);
						if (x < projecoes.size()) {
							strBff.append(" | ");
						}
						x++;
					}
					regras.set(i, strBff.toString());
					projecoes.clear();

					// Demais regras
				} else {
					String v = new String();
					StringTokenizer st = new StringTokenizer(str, "->|");
					v += st.nextToken().trim();
					while (st.hasMoreTokens()) {
						String aux = st.nextToken().trim();
						projecoes.add(aux);
					}
					for (int x = 0; x < projecoes.size(); x++) {
						String proj = projecoes.get(x);
						String projNova = new String();
						if (proj.contains(variavel)) {
							for (int z = 0; z < proj.length(); z++) {
								if (proj.charAt(z) == variavel.charAt(0)) {
									String aux = new String();
									aux += proj.substring(0, z);
									projNova += proj.substring(z + 1);
									if (!projecoes.contains(projNova) && projNova.length() != 0) {
										projecoes.add(projNova);
									}
									projNova = aux;

								} else {
									projNova += proj.charAt(z);
								}

								if (!projecoes.contains(projNova) && projNova.length() != 0) {
									projecoes.add(projNova);
								}

							}
						}
					}
					// Recriando a regra inteira
					// Gravando a regra
					StringBuffer strBff = new StringBuffer();
					strBff.append(v);
					strBff.append(" -> ");
					int x = 1;
					for (String s : projecoes) {
						strBff.append(s);
						if (x < projecoes.size()) {
							strBff.append(" | ");
						}
						x++;
					}
					regras.set(i, strBff.toString());
					projecoes.clear();

				}
			}
			// System.out.println(projecoes);
		}
		if (inicialLambda == true) {
			String str = new String();
			str = regras.get(0);
			str += "| .";
			regras.set(0, str);
		}
	}

	// retorna a variavel de um determinado indice da regra
	public String retornaVariavel(int indice) {
		String str = new String();
		StringTokenizer st = new StringTokenizer(regras.get(indice), "-");
		str += st.nextToken().trim();
		return str;
	}

	public void regraCadeia() {
		ArrayList<String> producoes = new ArrayList<>();

		while (temRegraCadeia()) {
			for (int i = 0; i < regras.size(); i++) {
				// pegando regra inteira
				String variavel = new String();
				String regra = regras.get(i);
				producoes.clear();
				StringTokenizer st = new StringTokenizer(regra, "->|");
				// variavel
				variavel = st.nextToken();
				// pega cada produção e armazena no arraylist
				while (st.hasMoreTokens()) {
					producoes.add(st.nextToken().trim());
				}
				// para cada producao loop
				for (String s : producoes) {

					if (s.length() == 1 && s.equals(s.toUpperCase()) && !s.contains(".")) {
						System.out.println("o s é: " + s);
						String producao = retornaProducao(s.trim());
						System.out.println("Producao: " + producao);
						// regra é a regra atual
						producoes.set(producoes.indexOf(s), producao);
					}

					System.out.println("producoes: " + producoes.toString());
				}
				StringBuffer novaRegra = new StringBuffer();
				novaRegra.append(variavel.trim() + " -> ");
				for (String s : producoes) {
					novaRegra.append(s + " | ");
				}
				novaRegra.delete(novaRegra.length() - 3, novaRegra.length());

				regras.set(i, novaRegra.toString());
			}

		}
	}

	public String retornaProducao(String variavel) {
		String retorno = new String();
		for (String str : this.regras) {
			String varAtual = new String();
			StringTokenizer st = new StringTokenizer(str, "->");
			varAtual = st.nextToken().trim();
			if (varAtual.equals(variavel)) {
				String aux = st.nextToken().trim();
				if (aux.length() >= 1) {
					retorno = aux;
				}

			}
		}
		return retorno;
	}

	public boolean temRegraCadeia() {
		ArrayList<String> producoes = new ArrayList<>();

		for (String r : this.regras) {
			StringTokenizer st = new StringTokenizer(r, "->|");
			st.nextToken();
			while (st.hasMoreTokens()) {
				producoes.add(st.nextToken().trim());
			}
			for (String s : producoes) {
				if (s.length() == 1 && s.equals(s.toUpperCase()) && !s.contains(".")) {
					return true;
				}
			}

		}
		return false;
	}

	// Algoritmo TERM invertido
	public void removeNaoTerminais() {
		ArrayList<String> projecoes = new ArrayList<>();
		String terminais = new String();
		boolean ehTerminal = false;

		for (int i = 0; i < regras.size(); i++) {

			String str = regras.get(i);
			projecoes.clear();
			StringTokenizer st = new StringTokenizer(str, "->|");
			String variavel = st.nextToken().trim();

			while (st.hasMoreTokens()) {
				projecoes.add(st.nextToken().trim());
			}

			for (String s : projecoes) {
				if (s.equals(s.toUpperCase()) || s.length() == 0) {
					ehTerminal = true;
				} else {
					ehTerminal = false;
					break;
				}
			}

			if (ehTerminal) {
				this.regras.remove(regras.indexOf(str));
				terminais += variavel + "#";
			}

		}
		System.out.println("Terminais: " + terminais);
	}

	// TODO Algoritmo do Reach quase pronto
	public void removeInuteis() {
		ArrayList<Integer> indices = new ArrayList<>();
		// Adiciona a regra inicial
		indices.add(0);

		for (String str : this.regras) {
			StringTokenizer st = new StringTokenizer(str, "->|");
			String var = st.nextToken().trim();
			while (st.hasMoreTokens() && indices.contains(retornaIndice(var))) {
				String regra = st.nextToken().trim();
				for (int i = 0; i < regra.length(); i++) {
					String aux = new String();
					aux += regra.charAt(i);
					if (aux.equals(aux.toUpperCase()) && !indices.contains(retornaIndice(aux))) {
						// add todos que são alcançaveis
						indices.add(retornaIndice(aux));
					}
				}
			}
		}
		Collections.sort(indices);
		System.out.println("Reach: " + indices.toString() + "\n\n");

		ArrayList<String> regrasAux = new ArrayList<>();

		for (int i : indices) {
			regrasAux.add(regras.get(i));
		}

		regras.clear();
		for (String s : regrasAux) {
			regras.add(regrasAux.get(regrasAux.indexOf(s)));
		}
	}

	public int retornaIndice(String variavel) {
		for (String str : this.regras) {
			StringTokenizer st = new StringTokenizer(str, "->|");
			if (st.nextToken().trim().equals(variavel)) {
				return regras.indexOf(str);
			}
		}
		return 0;
	}

	// aplica chomsky depois de todas as alterações
	public void chomsky() {

		ArrayList<String> realCNF = new ArrayList<String>();

		int proximaRegra = 0;
		int contador = 0;
		for (String strRegra : this.regras) {
			StringTokenizer st = new StringTokenizer(strRegra, "->|");
			String variavel = st.nextToken().trim();
			while (st.hasMoreTokens()) {
				String regraMudada = strRegra.substring(0, 5);
				String producao = st.nextToken().trim();
				if (producao.length() == 1) {
					// Já é terminal
					realCNF.add(regraMudada.concat(" ".concat(producao)));
				} else if (producao.length() == 2) {
					// verifica para o primeiro caracter
					proximaRegra = 0;
					if (producao.charAt(0) == producao.toLowerCase().charAt(0)) {
						// concatena a nova produção
						realCNF.add(producao.toUpperCase().substring(0, 1).concat("^ -> ".concat(producao.substring(0, 1))));
						producao = producao.replaceFirst(producao.substring(0, 1), producao.substring(0, 1).toUpperCase().concat("^"));
						proximaRegra += 1;
					}
					// verifica para o segundo caracter
					if (producao.charAt(1 + proximaRegra) == producao.toLowerCase().charAt(1)) {
						// concatena a nova produção
						realCNF.add(producao.toUpperCase().substring(1 + proximaRegra, 2 + proximaRegra).concat("^ -> ".concat(producao.substring(1 + proximaRegra, 2 + proximaRegra))));
						producao = producao.replaceFirst(producao.substring(1 + proximaRegra, 2 + proximaRegra), producao.substring(1 + proximaRegra, 2 + proximaRegra).toUpperCase().concat("^"));
					}
					realCNF.add(regraMudada.concat(" ".concat(producao)));
				} else if (producao.length() > 2) {
					String nova;
					while (producao.length() > 0) {
						// Verifica o primeiro caracter da produção
						proximaRegra = 0;
						if (producao.length() != 1) {
							if (producao.charAt(0) == producao.toLowerCase().charAt(0)) {
								// concatena a nova produção
								realCNF.add(producao.toUpperCase().substring(0, 1).concat("^ -> ".concat(producao.substring(0, 1))));
								producao = producao.replaceFirst(producao.substring(0, 1), producao.substring(0, 1).toUpperCase().concat("^"));
								proximaRegra = 1;
							}
						}

						// Salva para a nova string
						nova = producao.substring(1 + proximaRegra, producao.length());

						System.out.println(nova);
						System.out.println(nova.length());
						// Adiciona strings ao array

						if (nova.length() != 0) {
							realCNF.add(regraMudada.concat(" ".concat(producao.substring(0, 1 + proximaRegra).concat("T_" + contador + "^"))));
						} else
							realCNF.add(regraMudada.concat(" ".concat(producao.substring(0, 1 + proximaRegra))));
						// realCNF.add("T_" + contador +
						// "^ -> ".concat(nova));
						regraMudada = "T_" + contador + "^ -> ";
						contador++;

						producao = nova;
						System.out.println(producao);

					}
				}

			}

		}

		LinkedHashSet<String> limpar = new LinkedHashSet<String>();
		limpar.addAll(realCNF);
		this.regras.clear();
		this.regras.addAll(limpar);
		for (String string : this.regras) {
			System.out.println(string);
		}
		this.formatarGramatica();

	}
	
	
	//LUCIANO

	private void formatarGramatica() {
		ArrayList<String> regras_aux = this.regras;
		ArrayList<String> cfinal = new ArrayList<String>();
		String regra = "";
		String linha_gramatica = "";
		for (String string : this.regras) {
			regra = "";
			linha_gramatica = "";
			regra = regra.concat(string.substring(0, 4));
			linha_gramatica = linha_gramatica.concat(regra.replaceAll("-", "").replaceAll(">", "").trim().concat(" -> "));
			for (String string_inter : regras_aux) {
				if (string_inter.substring(0, 4).compareTo(regra) == 0) {
					linha_gramatica = linha_gramatica.concat(string_inter.substring(4, string_inter.length()).replaceAll(">", "").trim().concat(" | "));
				}				
			}
			cfinal.add(linha_gramatica.substring(0, linha_gramatica.length()-2).replaceAll(" -> - ", " ->"));
		}
		
		LinkedHashSet<String> limpa = new LinkedHashSet<String>();
		limpa.addAll(cfinal);
		this.regras.clear();
		this.regras.addAll(limpa);
		

	}

	public boolean existeVariavel(String variavel) {

		for (String str : this.regras) {
			String varAtual = new String();
			StringTokenizer st = new StringTokenizer(str, "->");
			varAtual = st.nextToken().trim();
			if (varAtual.equals(variavel)) {
				return true;
			}
		}
		return false;
	}
}
