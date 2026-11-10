package com.betacom.fe.dto.input;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CertificatoReq {
	private Boolean tipo;  // false normale true agonistico
	private LocalDate dataCerticifato;
	private Integer socioID;
}
