package com.betacom.fe.dto.input;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificatoReq {
	private Boolean tipo;  // false normale true agonistico
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dataCerticifato;
	private Integer socioID;
}
