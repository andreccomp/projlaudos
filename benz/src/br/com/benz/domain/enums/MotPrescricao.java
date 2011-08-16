package br.com.benz.domain.enums;
/**
 * @author padua
 *
 */
public enum MotPrescricao {
	/**
	 *
	 */
	
	ALIVIOQUEIXASSINTOMAS(1),
	
	CRITERIOSDIAG(2),
	
	MEDICACAOADJUVANTE(3),
	
	MEDICACAOPRIMARIA(4),
	
	MANUTENCAOANTERIOR(5),
	
	NAOUSOMEDICACAO(6);
	
	
	private int value;
	
	
	MotPrescricao(int value){
		this.value = value;
	}
	
	public static MotPrescricao fromInt(int value) {
		switch (value) {
		case 1:
			return ALIVIOQUEIXASSINTOMAS;
		case 2:
			return CRITERIOSDIAG;
		case 3:
			return MEDICACAOADJUVANTE;
		case 4:
			return MEDICACAOPRIMARIA;
		case 5:
			return MANUTENCAOANTERIOR;
		case 6:
			return NAOUSOMEDICACAO;
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		switch (this) {
		case ALIVIOQUEIXASSINTOMAS:
			return "Para al�vio de queixas ou sintomas";
		case CRITERIOSDIAG:
			return "Com base em crit�rios diagn�sticos (CID 10)";
		case MEDICACAOADJUVANTE:
			return "Como medica��o adjuvante";
		case MEDICACAOPRIMARIA:
			return "Como medica��o prim�ria";
		case MANUTENCAOANTERIOR:
			return "Como manuten��o de uso anterior";
		case NAOUSOMEDICACAO:
			return "N�o costumo usar essas medica��es";
		}
		return null;
	}
	
	public String getToString() {
		return toString();
	}
	
	/**
	 * @return retorna o inteiro correspondete.
	 */
	public int getToInt() {
		return toInt();
	}

	public void setToInt(int tempo) {
		fromInt(tempo);
	}
	
	public int toInt() {
		return value;
	}
}
