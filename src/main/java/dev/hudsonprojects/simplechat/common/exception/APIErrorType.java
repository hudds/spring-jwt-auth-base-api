package dev.hudsonprojects.simplechat.common.exception;

public enum APIErrorType {
	/**
	 * Problemas na requisição que não são relacionados a regras de negócio
	 * Exemplos: Método não suportado pela rota, Rota não encontrada, Tipo errado no body ... 
	 */
	HTTP_REQUEST_ERROR,
	
	/**
	 * Usuário não autorizado
	 */
	UNAUTHORIZED,
	
	/**
	 * Usuário está autorizado mas não possui permissão
	 */
	FORBIDDEN,
	
	/**
	 * Erros de validação relacionados a regra de negócio
	 */
	VALIDATION_ERROR,
	/**
	 * Erros internos da a API
	 */
	INTERNAL_ERROR,
	
	/**
	 * Erros que não se encaixam em nenhuma das outras categorias 
	 */
	OTHER,
	
	NOT_FOUND
}
