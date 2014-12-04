package com.luciano.CNF;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		GrammarRules grammar = new GrammarRules();
		System.out.println("Iniciando");

		try {
			// Importa arquivo
			if (grammar.importFile("grammar.txt")) {
				System.out.println(grammar.toString());
			}

			grammar.printGrammar();

			// Verifica Simbolo inicial recursivo
			if (grammar.verifyInitialRecursion()) {
				grammar.removeInitialRecusion();
			} else
				System.out.println("N�o possui regra inicial recursiva");

			// Verifica Simbolos Labda
			if (grammar.verifyLambdaRule()) {
				grammar.removeLambda();
			} else
				System.out.println("Não possui regra Lambda");

			// Verifica regra da cadeira
			if (grammar.verifyChainRule()) {
				grammar.removeChainRule();
			} else
				System.out.println("N�o possui regra da Cadeia");

			//Remove não terminais ( TERM )
//			grammar.removeTermRule();
			
			grammar.printGrammar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Erro");
		}

	}

}
