package com.leonardo.escuela.dto.grupos;
import com.leonardo.escuela.dto.horarios.HorarioResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
@Schema(description="Información del grupo y sus horarios")
public record GrupoResponse(Long idGrupo, Long idCurso, Long idMaestro, Long idAula,
                            String periodo, List<HorarioResponse> horarios) {}
