package br.edu.unicesumar.aep_parte2.mapper;

import br.edu.unicesumar.aep_parte2.domain.dto.HistoricoStatusResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.HistoricoStatusModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoricoStatusMapper {

    @Mapping(source = "atendenteModel.nome", target = "nomeAtendente")
    HistoricoStatusResponse toResponse(HistoricoStatusModel model);

    List<HistoricoStatusResponse> toResponseList(List<HistoricoStatusModel> models);
}
