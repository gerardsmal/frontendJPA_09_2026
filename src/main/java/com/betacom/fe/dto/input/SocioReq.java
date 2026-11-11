package com.betacom.fe.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocioReq {
	private Integer id;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String email;
	private Integer attivita;
	

}
