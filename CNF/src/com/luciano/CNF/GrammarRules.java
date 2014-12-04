package com.luciano.CNF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarRules {
	public ArrayList<String> grammarRules = new ArrayList<String>();

	public boolean importFile(String fileName) throws IOException {
		try {
			File fileIn = new File(fileName);
			Reader IN = new FileReader(fileIn);
			LineNumberReader reader = new LineNumberReader(IN);

			while (reader.ready()) {
				this.grammarRules.add(reader.readLine().concat(" "));
				// this.grammarRules.addRule(reader.readLine());
			}
			reader.close();
			IN.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("File not Found !!!: " + e.getMessage());
			return false;
		}
	}

	public boolean verifyInitialRecursion() {
		
		if (this.grammarRules.get(0).indexOf("S", 2) > 1) {
			return true;
		} else
			return false;
	}

	public void removeInitialRecusion() {
		String strAux = this.grammarRules.get(0).replaceFirst("S ->", "Q ->");
		grammarRules.add(0, strAux);
	}

	public boolean verifyLambdaRule() {
		for (String i : grammarRules) {
			if (i.contains(".")) {
				return true;
			}

		}
		return false;
	}

	public void removeLambdaRule() {
		ArrayList<String> whos_have = new ArrayList<String>();
		ArrayList<String> whos_have_aux = new ArrayList<String>();

		// Verifica quais regras tem lambda
		for (String i : this.grammarRules) {
			if (i.contains(" . ")) {
				whos_have.add(i.substring(0, 1));
			}
		}

		// Verifica quais regras possuiem conex√£o com regras lambda
		for (String i : this.grammarRules) {
			for (String j : whos_have) {
				if (i.contains(" " + j + " ")) {
					whos_have_aux.add(i.substring(0, 1));
				}
			}
		}

		// Adiciona regras n√£o existes ao Array whos_have
		for (String x : whos_have_aux) {
			if (!whos_have.contains(x))
				whos_have.add(x);
		}

		// Separa as regras
		for (String i : this.grammarRules) {
			for (String j : whos_have) {
				if (i.substring(0, 1).compareTo(j) == 0) {
					String[] parses;
					parses = i.split("\\|");
					String regra_atual = "";
					for (int k = 0; k < parses.length; k++) {

						if (parses[k].substring(0, 1).compareTo(" ") == 0) {
							whos_have_aux.add(regra_atual.concat(parses[k]));
						} else {
							regra_atual = regra_atual.concat(parses[k]
									.substring(0, 4));
							whos_have_aux.add(parses[k]);
						}
					}

				}
			}
		}

		// Verificar se alguma das regras separadas possui relaÔøΩÔøΩo a regra
		// lambda
		ArrayList<String> criadas = new ArrayList<String>();
		for (String regras_separadas : whos_have_aux) {
			ArrayList<Integer> quantidade = new ArrayList<Integer>();
			// verificar se ela aparece 1 ou mais vezes
			// Caso uma vez, ÔøΩ somente retirar a ocorrencia e adicionar a regra
			// caso mais vezes, necessario uma permutacao
			for (String regras_lambda : whos_have) {
				for (int i = 0; i < regras_separadas.length(); i++) {
					if (regras_separadas.charAt(i) == regras_lambda.charAt(0)) {
						quantidade.add(i); // marca as posi√ß√µes, por isso o for
											// e n√£o StringUtils
					}

				}

				if (quantidade.size() == 1) {
					criadas.add(regras_separadas.replace(regras_lambda, ""));
				} else if (quantidade.size() > 1) {
					// concatenadas.add(e)
				}
			}
			for (String string_i : criadas) {
				System.out.println("criada: " + string_i);
			}
		}

		for (String string : whos_have) {
			System.out.println(string);
		}

		for (String string : whos_have_aux) {
			System.out.println(string);
		}

	}

	public boolean verifyChainRule() {
		for (String i : grammarRules) {
			if (i.matches(".*[|] [A-Z] [|].*")) {
				return true;
			}
		}
		return false;
	}

	public static int indexOf(Pattern pattern, String s) {
		Matcher matcher = pattern.matcher(s);
		return matcher.find() ? matcher.start() : -1;
	}

	private boolean searchTerm(String term) {
		for (String string : this.grammarRules) {
			if (string.contains(term)) {
				return true;
			}
		}
		return false;
	}

	public void removeChainRule() {

		ArrayList<String> grammarRules_aux = new ArrayList<String>();

		Pattern p = Pattern.compile(" [A-Z] ");
		Matcher m;
		// Percorro todas as regras da gramatica
		int index = 0;
		String string_i_aux = "";
		for (String string_i : grammarRules) {

			// Verifico se alguma regra pattern pertence a regra string_i
			m = p.matcher(string_i);

			// Se sim, ira percorrer a linha, substituir a regra da cadeia e
			// verificar novamente se ainda possui regra de cadeia
			// ate nÔøΩo encontrar mais nenhuma, passando assim para a proxima
			// regra
			string_i_aux = "";
			// Verifica se realmente ela possui regras da cadeia, caso seja um
			// falso positivo
//			if (searchTerm(m.toString().trim())) {
				while (m.find()) {
					string_i_aux = string_i;
					// Procura o indice que combine com o pattern
					int idx_start = indexOf(Pattern.compile(" [A-Z] "),
							string_i_aux);
					// System.out.println("Index " + idx_start +
					// " - Real index: " +
					// (idx_start + 2));

					// Verifica a regra que possui cadeia
					char rule = string_i_aux.charAt(idx_start + 1);
					// System.out.println("Rule: " + rule);

					// Remove a regra que possuia cadeia
					string_i_aux = string_i_aux.replaceAll(" \\| " + rule, "");
					string_i_aux = string_i_aux.replaceAll(rule + " \\| ", "");
					// System.out.println("Blank rule: " + string_i_aux);

					// Insere novas regras para a "regra da cadeia eliminada"
					for (String string_concat : grammarRules) {
						if (string_concat.substring(0, 1).startsWith("" + rule,
								0)) {
							string_i_aux = string_i_aux.concat("|"
									+ string_concat.substring(4,
											string_concat.length()));
						}
					}
					// System.out.println("String concat: " + string_i_aux);

					string_i = string_i_aux;
					m = p.matcher(string_i);
				}
				if (string_i_aux != null) {
					grammarRules_aux.add(index, string_i_aux);
				}
//			}
			index++;
		}
		index = 0;
		for (String idx : grammarRules_aux) {
			if (!idx.isEmpty()) {
				this.grammarRules.remove(index);
				this.grammarRules.add(index, idx);
			}
			index++;
		}
	}

	public void removeTermRule() {
		ArrayList<String> terms = new ArrayList<String>();
		ArrayList<String> prevs = new ArrayList<String>();
		LinkedHashSet<String> remove = new LinkedHashSet<String>();

		Pattern p = Pattern.compile(" [a-z]* ");
		Matcher m;

		// Preenche os primeiros terminais para saber quais regras os possuems
		for (String rules : this.grammarRules) {
			m = p.matcher(rules);
			if (m.find()) {
				terms.add(rules.substring(0, 1)); // add terminal rule
			}
		}

		System.out.println("Primeiro Term: " + terms.toString());

		// Faz la√ßo para verificar quais dessas regras est√£o presentes em toda a
		// gramatica
		do {
			// Clear ArrayList Prevs, to avoid duplicates
			prevs.clear();
			prevs.addAll(terms);

			for (String grammar_rule : this.grammarRules) {
				for (String rule_term : prevs) {
					if (rule_term.compareTo(grammar_rule.substring(0, 1)) != 0) {
						p = Pattern.compile(" [a-z]*".concat(rule_term
								.concat(" ")));
						m = p.matcher(grammar_rule);
						if (m.find())
							terms.add(grammar_rule.substring(0, 1));
						p = Pattern.compile(" ".concat(rule_term).concat(
								"[a-z]* "));
						m = p.matcher(grammar_rule);
						if (m.find())
							terms.add(grammar_rule.substring(0, 1));
					}
				}
			}

			// remove repeat elements of ArrayList term
			remove.clear();
			remove.addAll(terms);
			terms.clear();
			terms.addAll(remove);

		} while (!prevs.equals(terms));

		// Remove the useless ( no terminal conections ) rules
		prevs.clear(); // used to new grammar ( aux )
		for (String rule_terms : terms) {
			for (String grammar : this.grammarRules) {
				if (grammar.substring(0, 1).compareTo(rule_terms) == 0) {
					prevs.add(grammar);
					System.out.println("Grammar add in final term: " + grammar);
				}
			}
		}

		//Clear e add all rules, except the rules with no conections terminals
		this.grammarRules.clear();
		this.grammarRules.addAll(prevs);			
	}

	public void removeReachRule(){
		ArrayList<String> reach = new ArrayList<String>();
		ArrayList<String> new_ = new ArrayList<String>();
		ArrayList<String> prev = new ArrayList<String>();
		
		//Cause grammar starts with Q
		for (String grammar : grammarRules) {
			if ((grammar.substring(1,0).compareTo("Q") == 0)){
				
			}
		}
		//if not start Q, then start S
		if (reach.size() == 0){
			for (String grammar : grammarRules) {
				if ((grammar.substring(1,0).compareTo("S") == 0)){
					
				}
			}			
		}
		
		do{
			// clear new_ and add Reach-Prev
			new_.clear();
			new_.addAll(reach);
			new_.removeAll(prev);
			
			// clear preva, and Prev = Reach
			prev.clear();
			prev.addAll(reach);
			
			for (String A : new_) {
//				reach.add(e)
			}
			
		}while(!reach.equals(prev));
	}
	
	
	// retorna a variavel de um determinado indice da regra
	public String retornaVariavel(int indice) {
		String str = new String();
		StringTokenizer st = new StringTokenizer(grammarRules.get(indice), "-");
		str += st.nextToken().trim();
		return str;
	}
	
	public Integer temLambda() {

		for (String s : this.grammarRules) {
			if (s.contains(".")) {
				// TODO tirar syso
			//	System.out.println("Regra: " + s + "Indice: " + this.regras.indexOf(s));
				return this.grammarRules.indexOf(s);
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
				System.out.println("O NULL È: " + NULL.toString());
				System.out.println("O PREV È: " + PREV.toString());

				// para cada variavel A que pertence ao conjunto de variaveis
				for (String a : this.grammarRules) {
					// Se A deriva w e w pertence a PREV faÁa
					
					StringTokenizer st = new StringTokenizer(a, "->");
					st.nextToken();
					String aux = st.nextToken().trim();
					
					if(a.contains(".")){
						varQueGeramLambda += retornaVariavel(grammarRules.indexOf(a)) + "#";
					}
					
					for (Integer x : PREV) {
						if (aux.contains(retornaVariavel(x))) {
							if (!NULL.contains(grammarRules.indexOf(a))) {
								NULL.add(grammarRules.indexOf(a));
							}
						}
					}
				}
				System.out.println("O NULL È: " + NULL.toString());
				System.out.println("O PREV È: " + PREV.toString());

			} while (!PREV.equals(NULL));

			// ComeÁa a remover os Lambdas

			// Se o simbolo inicial estiver em NULL ent„o S -> .
			if (NULL.contains(0)) {
				String str = grammarRules.get(0);
				ArrayList<String> producoes = new ArrayList<>();
				StringTokenizer st = new StringTokenizer(str, "->|");
				st.nextToken();
				while(st.hasMoreTokens()){
					producoes.add(st.nextToken().trim());
				}
				for (String s : producoes){
					if(s.equals(s.toUpperCase())){
						for(Integer i : NULL){
							if(s.contains(retornaVariavel(i))){
								for(int x=0;x<s.length();x++){
									//POG += POG + POG.POG();
									String y = new String();
									y+= s.charAt(x);
									if(varQueGeramLambda.contains(y)){
										inicialLambda = true;
									}
								}
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
				str += grammarRules.get(i);

				// Pega a primeira posicao do NULL que È o que j· tem lambda
				if (NULL.indexOf(i) == 0) {
					StringTokenizer st = new StringTokenizer(str, "->|");
					variavel += st.nextToken().trim();
					while (st.hasMoreTokens()) {
						String aux = st.nextToken().trim();
						// Aqui j· tirou o lambda
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
									if (!projecoes.contains(projNova)
											&& projNova.length() != 0) {
										projecoes.add(projNova);
									}
									projNova = aux;

								} else {
									projNova += proj.charAt(z);
								}

								if (!projecoes.contains(projNova)
										&& projNova.length() != 0) {
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
					grammarRules.set(i, strBff.toString());
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
									if (!projecoes.contains(projNova)
											&& projNova.length() != 0) {
										projecoes.add(projNova);
									}
									projNova = aux;

								} else {
									projNova += proj.charAt(z);
								}

								if (!projecoes.contains(projNova)
										&& projNova.length() != 0) {
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
					grammarRules.set(i, strBff.toString());
					projecoes.clear();

				}
			}
		//	System.out.println(projecoes);
		}
		if (inicialLambda == true) {
			String str = new String();
			str = grammarRules.get(0);
			str += "| . ";
			grammarRules.set(0, str);
		}
	}

	public void removeCagada(){
		ArrayList<String> aux_grammar = grammarRules;
		for (String string : aux_grammar) {
			for (int i = 4; i < string.length(); i++) {
				if ((string.charAt(i) == '|' ) && (string.charAt(i) != ' ')){
					String antes = string.substring(0, i);
				    String depois = string.substring(i+1, string.length());		
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "GrammarRules [grammarRules=" + grammarRules + "]";
	}

	public void printGrammar() {
		System.out.println("\n============================================\n");
		for (String i : grammarRules) {
			System.out.println(i);
		}
		System.out.println("\n============================================\n");
	}

}
