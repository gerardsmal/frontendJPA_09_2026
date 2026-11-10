package com.betacom.fe.dto.output;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AbbonamentoDTO {
	private Integer id;
	private LocalDate dataAbbonamento;
	private Integer durataAbbonamento;
	private List<AttivitaDTO> attivita;

}
